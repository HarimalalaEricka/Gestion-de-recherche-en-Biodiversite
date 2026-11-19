<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getItems, getItemById } from '../../services/api'
import { listerMediasTaxon, importerMediasTaxon } from '../../services/gbifMedia'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

usePage()

const route = useRoute()
const router = useRouter()
const idTaxon = Number(route.params.idTaxon)

/* ---------- Réglages médias / mesures ---------- */

const MEDIA_ENDPOINT = 'medias'
const MEDIA_FILTER = 'occurrence.occurrenceID'
const mediaUrl = (m) => m?.accessURI ?? null

const MESURES_ENDPOINT = 'measurement-or-facts' 
const MESURES_FILTER = 'occurrence.occurrenceID'

const FILTRE_CLIENT = true

const appartient = (item, occId) =>
  !FILTRE_CLIENT || item.occurrence?.occurrenceID === occId

const STATUT_ABSENT = 'NOT_DETECTED'

const STATUTS = [
  { value: 'DETECTED', label: 'Présent' },
  { value: STATUT_ABSENT, label: 'Absent' }
]

const TYPES = [
  { value: 'FOSSIL_SPECIMEN', label: 'Fossile' },
  { value: 'HUMAN_OBSERVATION', label: 'Observation humaine' },
  { value: 'MACHINE_OBSERVATION', label: 'Observation machine' },
  { value: 'PRESERVED_SPECIMEN', label: 'Spécimen conservé' },
  { value: 'MATERIAL_SPECIMEN', label: 'Spécimen matériel' },
  { value: 'LIVING_SPECIMEN', label: 'Spécimen vivant' },
  { value: 'MATERIAL_SAMPLE', label: 'Échantillon matériel' }
]

const labelDe = (liste, v) =>
  liste.find((x) => x.value === v)?.label ?? v ?? '-'

const formatDate = (d) =>
  d ? new Date(d).toLocaleDateString('fr-FR') : '-'

const nomOrganisme = (o) =>
  o?.organismName || (o?.organismID ? `Organisme #${o.organismID}` : '-')

const taxon = ref(null)

/* ---------- Identifications → une ligne par occurrence ---------- */

const loading = ref(false)
const toutesIdentifications = ref([])
const page = ref(1)
const perPage = ref(10)

let requestId = 0

async function fetchIdentifications() {
  const id = ++requestId
  loading.value = true

  try {
    const { items } = await getItems(
      'identifications',
      { 'taxon.taxonID': idTaxon },
      { page: 1, perPage: 1000, sort: 'dateIdentified,desc' }
    )

    if (id !== requestId) return
    toutesIdentifications.value = items
  } catch (e) {
    if (id !== requestId) return
    console.error('Erreur lors du chargement des identifications :', e)
    toutesIdentifications.value = []
  } finally {
    if (id === requestId) loading.value = false
  }
}

const meilleure = (a, b) => {
  if (!!a.isAcceptedIdentification !== !!b.isAcceptedIdentification) {
    return !!a.isAcceptedIdentification
  }
  return new Date(a.dateIdentified) > new Date(b.dateIdentified)
}

const observations = computed(() => {
  const parOccurrence = new Map()

  for (const i of toutesIdentifications.value) {
    const key = i.occurrence?.occurrenceID
    if (key == null) continue

    const courante = parOccurrence.get(key)
    if (!courante || meilleure(i, courante)) parOccurrence.set(key, i)
  }

  return [...parOccurrence.values()].sort(
    (a, b) => new Date(b.dateIdentified) - new Date(a.dateIdentified)
  )
})

const total = computed(() => observations.value.length)

const totalPages = computed(() =>
  Math.max(1, Math.ceil(total.value / perPage.value))
)

const observationsPage = computed(() => {
  const debut = (page.value - 1) * perPage.value
  return observations.value.slice(debut, debut + perPage.value)
})

watch(perPage, () => { page.value = 1 })
watch(totalPages, (n) => {
  if (page.value > n) page.value = n
})

/* ---------- Statistiques ---------- */

const nbDatasets = computed(
  () =>
    new Set(
      observations.value
        .map((i) => i.occurrence?.event?.dataset?.datasetID)
        .filter((x) => x != null)
    ).size
)

const periode = computed(() => {
  const annees = observations.value
    .map((i) => i.occurrence?.event?.eventDate)
    .filter(Boolean)
    .map((d) => new Date(d).getFullYear())
    .filter((y) => !Number.isNaN(y))

  if (!annees.length) return '-'

  const min = Math.min(...annees)
  const max = Math.max(...annees)
  return min === max ? String(min) : `${min} – ${max}`
})

/* ---------- Photos des observations ---------- */

// occurrenceID -> liste d'URLs
const photos = ref(new Map())

let photosRequestId = 0

async function fetchPhotos() {
  const id = ++photosRequestId
  const ids = observations.value.map((i) => i.occurrence.occurrenceID)
  const resultat = new Map()

  await Promise.all(
    ids.map(async (occId) => {
      try {
        const { items } = await getItems(
          MEDIA_ENDPOINT,
          { [MEDIA_FILTER]: occId },
          { page: 1, perPage: 50 }
        )

        console.log(
          `Médias demandés : ${occId} → reçus :`,
          items.map((m) => m.occurrence?.occurrenceID)
        )

        const urls = items
          .filter((m) => appartient(m, occId))
          .map(mediaUrl)
          .filter(Boolean)

        if (urls.length) resultat.set(occId, urls)
      } catch (e) {
        console.error(`Médias de l'occurrence ${occId} :`, e)
      }
    })
  )

  if (id !== photosRequestId) return
  photos.value = resultat
}

// Quelques photos du taxon pour le bloc « Médias »
const photosTaxonOccurrence = computed(() =>
  [...photos.value.values()].flat().slice(0, 6)
)

const photosTaxon = computed(() =>
  [
    ...mediasTaxon.value.map((m) => ({
      url: mediaUrl(m),
      legende: m.comments || m.caption || ''
    }))
  ]
    .filter((p) => p.url)
    .slice(0, 6)
)
/* ---------- Médias du taxon (GBIF) ---------- */

const mediasTaxon = ref([])

async function chargerMediasTaxon() {
  const t = taxon.value
  if (!t?.taxonID) return

  try {
    let medias = await listerMediasTaxon(t.taxonID)

    if (medias.length === 0 && t.gbifKey) {
      medias = await importerMediasTaxon(t.taxonID, t.gbifKey)
    }

    mediasTaxon.value = medias
  } catch (e) {
    console.error('Médias du taxon :', e)
  }
}

async function fetchTaxon() {
  try {
    taxon.value = await getItemById('taxons', idTaxon)
    chargerMediasTaxon()
  } catch (e) {
    console.error('Erreur lors du chargement du taxon :', e)
  }
}
/* ---------- Mesures ---------- */

const mesures = ref([])
let mesuresRequestId = 0

async function fetchMesures() {
  const id = ++mesuresRequestId
  const ids = [...new Set(observations.value.map((i) => i.occurrence.occurrenceID))]
  const resultat = []

  await Promise.all(
    ids.map(async (occId) => {
      try {
        const { items } = await getItems(
          MESURES_ENDPOINT,
          { [MESURES_FILTER]: occId },
          { page: 1, perPage: 200 }
        )

        console.log(
          `Mesures demandées : ${occId} → reçues :`,
          items.map((m) => m.occurrence?.occurrenceID)
        )

        for (const m of items) {
          if (appartient(m, occId)) resultat.push(m)
        }
      } catch (e) {
        console.error(`Mesures de l'occurrence ${occId} :`, e)
      }
    })
  )

  if (id !== mesuresRequestId) return
  mesures.value = resultat
}

const toNombre = (v) => {
  const s = String(v ?? '').trim().replace(',', '.')
  if (s === '') return null
  const n = Number(s)
  return Number.isFinite(n) ? n : null
}

const formatNombre = (n) =>
  n.toLocaleString('fr-FR', { maximumFractionDigits: 3 })

// Une ligne par (type, unité) : min, max, nombre de mesures
const mesuresResume = computed(() => {
  const groupes = new Map()

  for (const m of mesures.value) {
    const n = toNombre(m.measurementValue)
    if (n === null) continue

    const type = m.measurementType?.measurementType ?? '-'
    const unite = m.measurementUnit?.measurementUnit ?? ''
    const key = `${type}|${unite}`

    const g = groupes.get(key) ?? { key, type, unite, min: n, max: n, nb: 0 }
    g.min = Math.min(g.min, n)
    g.max = Math.max(g.max, n)
    g.nb++
    groupes.set(key, g)
  }

  return [...groupes.values()].sort((a, b) => a.type.localeCompare(b.type, 'fr'))
})

/* ---------- Carte des observations ---------- */

const mapEl = ref(null)
let map = null
let markersLayer = null

// Un point (mêmes coordonnées) = un marqueur
const lieux = computed(() => {
  const parPoint = new Map()

  for (const i of observations.value) {
    const loc = i.occurrence?.event?.location
    if (loc?.decimalLatitude == null || loc?.decimalLongitude == null) continue

    const key = `${loc.decimalLatitude},${loc.decimalLongitude}`
    const groupe = parPoint.get(key) ?? { loc, ids: [] }
    groupe.ids.push(i.occurrence.occurrenceID)
    parPoint.set(key, groupe)
  }

  return [...parPoint.values()]
})

const sansCoordonnees = computed(
  () => total.value - lieux.value.reduce((n, l) => n + l.ids.length, 0)
)

function creerPopup({ loc, ids }) {
  const box = document.createElement('div')
  box.style.minWidth = '180px'

  const titre = document.createElement('div')
  titre.className = 'fw-semibold'
  titre.textContent =
    [loc.locality?.trim(), loc.municipality].filter(Boolean).join(', ') ||
    `Lieu #${loc.locationID}`
  box.appendChild(titre)

  const coords = document.createElement('div')
  coords.className = 'text-body-secondary small'
  coords.textContent = `${loc.decimalLatitude}, ${loc.decimalLongitude}`
  box.appendChild(coords)

  const nb = document.createElement('div')
  nb.className = 'text-body-secondary mb-2'
  nb.textContent = `${ids.length} observation(s)`
  box.appendChild(nb)

  for (const id of ids) {
    const bloc = document.createElement('div')
    bloc.className = 'mb-2'

    const a = document.createElement('a')
    a.href = '#'
    a.className = 'd-block'
    a.textContent = `Observation n° ${id}`
    a.addEventListener('click', (e) => {
      e.preventDefault()
      router.push(`/partage/detail-d-une-observation/${id}`)
    })
    bloc.appendChild(a)

    const urls = photos.value.get(id) ?? []
    if (urls.length) {
      const rangee = document.createElement('div')
      rangee.className = 'd-flex flex-wrap gap-1 mt-1'

      for (const url of urls.slice(0, 3)) {
        const lien = document.createElement('a')
        lien.href = url
        lien.target = '_blank'
        lien.rel = 'noopener'

        const img = document.createElement('img')
        img.src = url
        img.alt = `Photo de l'observation ${id}`
        img.loading = 'lazy'
        img.style.cssText =
          'width:64px;height:64px;object-fit:cover;border-radius:4px'

        lien.appendChild(img)
        rangee.appendChild(lien)
      }
      bloc.appendChild(rangee)
    }

    box.appendChild(bloc)
  }

  return box
}

// Marqueur sans image externe : icône Bootstrap + nombre d'observations
function creerIcone(nb) {
  return L.divIcon({
    className: '',
    html: `
      <div style="position:relative;width:32px;height:40px">
        <i class="bi bi-geo-alt-fill"
           style="font-size:36px;line-height:1;color:#dc3545;
                  filter:drop-shadow(0 1px 2px rgba(0,0,0,.5))"></i>
        <span style="position:absolute;top:6px;left:0;width:100%;
                     text-align:center;font:700 11px/1 sans-serif;color:#fff">
          ${nb}
        </span>
      </div>`,
    iconSize: [32, 40],
    iconAnchor: [16, 38],
    popupAnchor: [0, -34]
  })
}

function dessinerMarqueurs() {
  if (!map) return

  markersLayer.clearLayers()

  const points = []
  for (const groupe of lieux.value) {
    const { decimalLatitude: lat, decimalLongitude: lng } = groupe.loc

    L.marker([lat, lng], { icon: creerIcone(groupe.ids.length) })
      .bindPopup(() => creerPopup(groupe), { maxWidth: 280 })
      .addTo(markersLayer)

    points.push([lat, lng])
  }

  if (points.length === 1) {
    map.setView(points[0], 10)
  } else if (points.length > 1) {
    map.fitBounds(points, { padding: [30, 30], maxZoom: 12 })
  }
}

function initCarte() {
  map = L.map(mapEl.value).setView([-18.9, 47.5], 5)

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution:
      '&copy; contributeurs <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(map)

  markersLayer = L.layerGroup().addTo(map)
  dessinerMarqueurs()
}

watch(lieux, dessinerMarqueurs)

// Dès que la liste des observations change, on charge photos et mesures
watch(observations, () => {
  fetchPhotos()
  fetchMesures()
})

onMounted(() => {
  initCarte()
  fetchTaxon()
  fetchIdentifications()
})

onBeforeUnmount(() => {
  map?.remove()
  map = null
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">
          <router-link to="/partage/fiche-taxon">Fiche taxon</router-link>
        </li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">{{ taxon?.scientificName }}</h1>

    <ul class="list-unstyled mb-0 d-grid gap-1">
      <li><span class="text-body-secondary">Règne :</span> {{ taxon?.kingdom || '-' }}</li>
      <li><span class="text-body-secondary">Embranchement :</span> {{ taxon?.phylum || '-' }}</li>
      <li><span class="text-body-secondary">Classe :</span> {{ taxon?.taxonomicClass || '-' }}</li>
      <li><span class="text-body-secondary">Ordre :</span> {{ taxon?.order || '-' }}</li>
      <li><span class="text-body-secondary">Famille :</span> {{ taxon?.family || '-' }}</li>
      <li><span class="text-body-secondary">Genre :</span> {{ taxon?.genus || '-' }}</li>
      <li><span class="text-body-secondary">Nom scientifique :</span> {{ taxon?.scientificName || '-' }}</li>
    </ul>

    <!-- Statistiques -->
    <div class="row row-cols-2 row-cols-lg-4 g-3">
      <div class="col">
        <div class="card"><div class="card-body">
          <div class="fs-3 fw-semibold">{{ total }}</div>
          <div class="text-body-secondary">observations</div>
        </div></div>
      </div>
      <div class="col">
        <div class="card"><div class="card-body">
          <div class="fs-3 fw-semibold">{{ nbDatasets }}</div>
          <div class="text-body-secondary">datasets</div>
        </div></div>
      </div>
      <div class="col">
        <div class="card"><div class="card-body">
          <div class="fs-3 fw-semibold">{{ lieux.length }}</div>
          <div class="text-body-secondary">lieux</div>
        </div></div>
      </div>
      <div class="col">
        <div class="card"><div class="card-body">
          <div class="fs-3 fw-semibold">{{ periode }}</div>
          <div class="text-body-secondary">période</div>
        </div></div>
      </div>
    </div>

    <div class="row g-4">

      <div class="col-lg-8 d-grid gap-3 align-content-start">

        <!-- Carte -->
        <div class="card overflow-hidden">
          <div class="card-header fw-semibold">Carte des observations</div>

          <div
            ref="mapEl"
            style="height: 400px; isolation: isolate"
            role="region"
            aria-label="Carte des observations du taxon"
          ></div>

          <div v-if="!loading && sansCoordonnees > 0" class="card-footer small text-body-secondary">
            {{ sansCoordonnees }} observation(s) sans coordonnées ne sont pas affichées.
          </div>
        </div>

        <!-- Observations (une ligne par occurrence) -->
        <div class="card">
          <div class="card-header fw-semibold">Observations</div>
          <div class="card-body d-grid gap-3">

            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead>
                  <tr>
                    <th scope="col">Organisme</th>
                    <th scope="col">Détection</th>
                    <th scope="col">Type d'enregistrement</th>
                    <th scope="col">Nombre d'individus</th>
                    <th scope="col">Date de l'événement</th>
                    <th scope="col">Lieu</th>
                    <th scope="col"></th>
                  </tr>
                </thead>

                <tbody>
                  <tr v-if="loading">
                    <td colspan="7" class="text-center text-body-secondary py-4">
                      <span class="spinner-border spinner-border-sm me-2"></span>
                      Chargement des observations...
                    </td>
                  </tr>

                  <tr v-else-if="observationsPage.length === 0">
                    <td colspan="7" class="text-center text-body-secondary py-4">
                      Aucune observation pour ce taxon.
                    </td>
                  </tr>

                  <template v-else>
                    <tr v-for="i in observationsPage" :key="i.occurrence.occurrenceID">
                      <td>{{ nomOrganisme(i.occurrence?.organism) }}</td>
                      <td>
                        <span
                          class="badge"
                          :class="i.occurrence?.occurrenceStatus === STATUT_ABSENT
                            ? 'text-bg-secondary'
                            : 'text-bg-success'"
                        >
                          {{ labelDe(STATUTS, i.occurrence?.occurrenceStatus) }}
                        </span>
                      </td>
                      <td>{{ labelDe(TYPES, i.occurrence?.basisOfRecord) }}</td>
                      <td>{{ i.occurrence?.individualCount ?? '—' }}</td>
                      <td>{{ formatDate(i.occurrence?.event?.eventDate) }}</td>
                      <td>{{ i.occurrence?.event?.location?.municipality ?? '-' }}</td>
                      <td class="text-nowrap">
                        <div class="d-flex flex-wrap gap-1">
                          <router-link
                            class="btn btn-sm btn-outline-secondary"
                            :to="`/partage/detail-d-une-observation/${i.occurrence.occurrenceID}`"
                          >
                            Détails
                          </router-link>
                          <router-link
                            class="btn btn-sm btn-outline-primary"
                            :to="`/saisie/saisie-identification/${i.occurrence.occurrenceID}`"
                          >
                            Identification
                          </router-link>
                        </div>
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>

            <!-- Pagination -->
            <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
              <small class="text-body-secondary">{{ total }} observation(s)</small>

              <div class="d-flex align-items-center gap-2">
                <select
                  class="form-select form-select-sm w-auto"
                  v-model.number="perPage"
                  aria-label="Observations par page"
                >
                  <option :value="5">5</option>
                  <option :value="10">10</option>
                  <option :value="20">20</option>
                </select>

                <button
                  type="button"
                  class="btn btn-sm btn-outline-secondary"
                  :disabled="page <= 1 || loading"
                  @click="page--"
                >
                  Précédent
                </button>

                <span class="small">Page {{ page }} / {{ totalPages }}</span>

                <button
                  type="button"
                  class="btn btn-sm btn-outline-secondary"
                  :disabled="page >= totalPages || loading"
                  @click="page++"
                >
                  Suivant
                </button>
              </div>
            </div>

          </div>
        </div>
      </div>

      <div class="col-lg-4 d-grid gap-3 align-content-start">

        <!-- Médias : vraies photos, sinon vignettes vides -->
        <div class="card">
          <div class="card-header fw-semibold">Médias des occurrences</div>
          <div class="card-body">
            <div class="row g-2">
              <template v-if="photosTaxonOccurrence.length">
                <div v-for="(url, n) in photosTaxonOccurrence" :key="url + n" class="col-4">
                  <a :href="url" target="_blank" rel="noopener">
                    <div class="ratio ratio-1x1 rounded overflow-hidden">
                      <img
                        :src="url"
                        :alt="`Photo ${n + 1} de ${taxon?.scientificName ?? 'ce taxon'}`"
                        loading="lazy"
                        style="object-fit: cover"
                      />
                    </div>
                  </a>
                </div>
              </template>
              <template v-else>
                <div v-for="n in 3" :key="n" class="col-4">
                  <div class="ratio ratio-1x1 bg-body-secondary rounded">
                    <div class="d-flex align-items-center justify-content-center text-body-secondary">
                      <i class="bi bi-image fs-3"></i>
                    </div>
                  </div>
                </div>
              </template>
            </div>
          </div>
        </div>
         <div class="card">
          <div class="card-header fw-semibold">Médias du taxon</div>
          <div class="card-body">
            <div class="row g-2">
              <template v-if="photosTaxon.length">
                <div v-for="(p, n) in photosTaxon" :key="p.url + n" class="col-4">
                    <a :href="p.url" target="_blank" rel="noopener" :title="p.legende">
                    <div class="ratio ratio-1x1 rounded overflow-hidden">
                        <img
                        :src="p.url"
                        :alt="`Photo ${n + 1} de ${taxon?.scientificName ?? 'ce taxon'}`"
                        :title="p.legende"
                        loading="lazy"
                        style="object-fit: cover"
                        />
                    </div>
                    </a>
                </div>
                </template>

              <template v-else>
                <div v-for="n in 3" :key="n" class="col-4">
                  <div class="ratio ratio-1x1 bg-body-secondary rounded">
                    <div class="d-flex align-items-center justify-content-center text-body-secondary">
                      <i class="bi bi-image fs-3"></i>
                    </div>
                  </div>
                </div>
              </template>
            </div>
          </div>
        </div>

        <!-- Mesures -->
        <div class="card">
          <div class="card-header fw-semibold">Mesures</div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead>
                  <tr>
                    <th scope="col">Type</th>
                    <th scope="col">Min</th>
                    <th scope="col">Max</th>
                    <th scope="col">Unité</th>
                    <th scope="col">Nb</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="mesuresResume.length === 0">
                    <td colspan="5" class="text-center text-body-secondary py-3">
                      Aucune mesure pour ce taxon.
                    </td>
                  </tr>

                  <tr v-for="m in mesuresResume" :key="m.key">
                    <td>{{ m.type }}</td>
                    <td>{{ formatNombre(m.min) }}</td>
                    <td>{{ formatNombre(m.max) }}</td>
                    <td>{{ m.unite || '-' }}</td>
                    <td>{{ m.nb }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

      </div>
    </div>

    <div class="d-flex flex-wrap gap-2">
      <a class="btn btn-outline-secondary" href="#" data-demo>
        Modifier la fiche (chercheur principal)
      </a>
    </div>

  </main>
</template>