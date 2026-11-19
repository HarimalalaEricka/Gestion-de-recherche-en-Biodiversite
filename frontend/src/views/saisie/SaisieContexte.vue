<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { insertItem, getItems } from '../../services/api'

usePage()

const route = useRoute()
const idDataset = Number(route.params.idDataset)
const datasetValide = Number.isInteger(idDataset) && idDataset > 0

/* ---------- Utilitaires ---------- */

const formatDate = (d) =>
  d ? new Date(d).toLocaleDateString('fr-FR') : '-'

function debounce(fn, delay = 300) {
  let timer
  return (...args) => {
    clearTimeout(timer)
    timer = setTimeout(() => fn(...args), delay)
  }
}

function parseCoord(v) {
  const s = String(v ?? '').trim().replace(',', '.')
  return s === '' ? null : Number(s)
}

/* ------------------------------------------------------------------ */
/* Liste des événements (pagination côté backend)                      */
/* ------------------------------------------------------------------ */

const evenements = ref([])
const evTotal = ref(0)
const evLoading = ref(false)
const evPage = ref(1)
const evPerPage = ref(10)

const evTotalPages = computed(() =>
  Math.max(1, Math.ceil(evTotal.value / evPerPage.value))
)

let evRequestId = 0

async function fetchEvenements() {
  const requestId = ++evRequestId
  evLoading.value = true

  try {
    const { items, total } = await getItems(
      'events',
      { 'dataset.datasetID': idDataset },
      { page: evPage.value, perPage: evPerPage.value, sort: 'eventID,desc' }
    )

    if (requestId !== evRequestId) return

    // Page devenue vide : reculer d'une page
    if (items.length === 0 && evPage.value > 1) {
      evPage.value--
      return
    }

    evenements.value = items
    evTotal.value = total ?? 0
  } catch (e) {
    if (requestId !== evRequestId) return
    console.error('Erreur chargement des événements :', e)
    evenements.value = []
    evTotal.value = 0
  } finally {
    if (requestId === evRequestId) evLoading.value = false
  }
}

watch(evPerPage, () => {
  if (evPage.value !== 1) {
    evPage.value = 1
  } else {
    fetchEvenements()
  }
})

watch(evPage, fetchEvenements)

/* ------------------------------------------------------------------ */
/* Formulaire d'ajout d'un événement                                   */
/* ------------------------------------------------------------------ */

const showForm = ref(false)
const formRef = ref(null)
const saving = ref(false)
const error = ref('')
const success = ref('')

const pays = ref([])
const provinces = ref([])

const dateEvenement = ref('')
const paysSelected = ref(null)
const provinceSelected = ref(null)
const commune = ref('')
const localite = ref('')
const latitude = ref('')
const longitude = ref('')

/* ---------- Protocole (recherche avec suggestions) ---------- */

const protocolInput = ref('')
const protocolSelected = ref(null)
const protocolResults = ref([])
const protocolOpen = ref(false)
const protocolLoading = ref(false)
const protocolActive = ref(-1)

let protocolRequestId = 0

async function searchProtocols() {
  const requestId = ++protocolRequestId
  protocolLoading.value = true

  try {
    const filters = {}
    const q = protocolInput.value.trim()
    if (q) filters.protocolName = q

    const { items } = await getItems('protocols', filters, {
      page: 1,
      perPage: 4,
      sort: 'protocolName,asc'
    })

    if (requestId !== protocolRequestId) return

    protocolResults.value = items
    protocolActive.value = items.length ? 0 : -1
  } catch (e) {
    if (requestId !== protocolRequestId) return
    console.error('Erreur recherche des protocoles :', e)
    protocolResults.value = []
    protocolActive.value = -1
  } finally {
    if (requestId === protocolRequestId) protocolLoading.value = false
  }
}

const searchProtocolsDebounced = debounce(searchProtocols)

function onProtocolInput() {
  protocolSelected.value = null
  protocolOpen.value = true
  searchProtocolsDebounced()
}

function onProtocolFocus() {
  protocolOpen.value = true
  searchProtocols()
}

function closeProtocols() {
  protocolOpen.value = false
}

function chooseProtocol(p) {
  protocolSelected.value = p.protocolID
  protocolInput.value = p.protocolName
  protocolOpen.value = false
}

function clearProtocol() {
  protocolSelected.value = null
  protocolInput.value = ''
  protocolResults.value = []
  protocolOpen.value = false
}

function onProtocolKeydown(e) {
  if (!protocolOpen.value && e.key === 'ArrowDown') {
    protocolOpen.value = true
    searchProtocols()
    return
  }

  if (!protocolOpen.value) return

  if (e.key === 'ArrowDown') {
    e.preventDefault()
    protocolActive.value =
      (protocolActive.value + 1) % Math.max(protocolResults.value.length, 1)
  } else if (e.key === 'ArrowUp') {
    e.preventDefault()
    protocolActive.value =
      (protocolActive.value - 1 + protocolResults.value.length) %
      Math.max(protocolResults.value.length, 1)
  } else if (e.key === 'Enter') {
    e.preventDefault()
    const p = protocolResults.value[protocolActive.value]
    if (p) chooseProtocol(p)
  } else if (e.key === 'Escape') {
    protocolOpen.value = false
  }
}

/* ---------- Pays / provinces ---------- */

const provincesFiltrees = computed(() =>
  provinces.value.filter(
    (pr) => !paysSelected.value || pr.pays?.paysID === paysSelected.value
  )
)

watch(paysSelected, () => {
  provinceSelected.value = null
})

/* ---------- Lieu existant ---------- */

const locationSelected = ref(null) // locationID du lieu pris comme base
const locationOriginal = ref(null) // valeurs du lieu au moment du choix
const locationInput = ref('')
const locationResults = ref([])
const locationOpen = ref(false)
const locationLoading = ref(false)
const showLocationSearch = ref(false)

let locationRequestId = 0

const provinceById = computed(
  () => new Map(provinces.value.map((p) => [p.provinceID, p]))
)

// "Province, Pays" d'un lieu
function locationPlace(l) {
  const prov = provinceById.value.get(l.province?.provinceID) ?? l.province
  const paysId = prov?.pays?.paysID
  const paysName = pays.value.find((x) => x.paysID === paysId)?.pays ?? prov?.pays?.pays
  return [prov?.province, paysName].filter(Boolean).join(', ')
}

function locationCoords(l) {
  const lat = l.decimalLatitude
  const lng = l.decimalLongitude
  return lat != null && lng != null ? `${lat}, ${lng}` : 'Sans coordonnées'
}

async function searchLocations() {
  const requestId = ++locationRequestId
  locationLoading.value = true

  try {
    const q = locationInput.value.trim()
    const opts = { page: 1, perPage: 8, sort: 'locality,asc' }

    const [byLocality, byMunicipality] = await Promise.all([
      getItems('locations', q ? { locality: q } : {}, opts),
      q ? getItems('locations', { municipality: q }, opts) : { items: [] }
    ])

    if (requestId !== locationRequestId) return

    const unique = new Map()
    ;[...byLocality.items, ...byMunicipality.items].forEach((l) =>
      unique.set(l.locationID, l)
    )
    locationResults.value = [...unique.values()].slice(0, 10)
  } catch (e) {
    if (requestId !== locationRequestId) return
    console.error('Erreur recherche des lieux :', e)
    locationResults.value = []
  } finally {
    if (requestId === locationRequestId) locationLoading.value = false
  }
}

const searchLocationsDebounced = debounce(searchLocations)

function openLocationSearch() {
  showLocationSearch.value = true
  locationOpen.value = true
  searchLocations()
}

// Annulation possible à tout moment : ferme la recherche sans rien changer
function cancelLocationSearch() {
  showLocationSearch.value = false
  locationOpen.value = false
  locationInput.value = ''
  locationResults.value = []
  locationRequestId++ // ignore les réponses en cours
  locationLoading.value = false
}

function onLocationInput() {
  locationOpen.value = true
  searchLocationsDebounced()
}

async function chooseLocation(l) {
  cancelLocationSearch()

  const prov = provinceById.value.get(l.province?.provinceID)
  const provinceID = prov?.provinceID ?? l.province?.provinceID ?? null

  locationSelected.value = l.locationID
  locationOriginal.value = {
    commune: l.municipality ?? '',
    localite: l.locality ?? '',
    lat: l.decimalLatitude ?? null,
    lng: l.decimalLongitude ?? null,
    provinceID
  }

  commune.value = l.municipality ?? ''
  localite.value = l.locality ?? ''
  latitude.value = l.decimalLatitude ?? ''
  longitude.value = l.decimalLongitude ?? ''

  paysSelected.value = prov?.pays?.paysID ?? null
  await nextTick() // le watch(paysSelected) remet la province à null
  provinceSelected.value = provinceID
}

// On garde les valeurs des champs, on arrête juste de réutiliser l'ancien lieu
function detachLocation() {
  locationSelected.value = null
  locationOriginal.value = null
}

// Vrai si l'utilisateur a changé au moins un champ du lieu choisi
const locationModified = computed(() => {
  const o = locationOriginal.value
  if (!locationSelected.value || !o) return false

  const t = (s) => String(s ?? '').trim()
  return (
    t(commune.value) !== t(o.commune) ||
    t(localite.value) !== t(o.localite) ||
    parseCoord(latitude.value) !== o.lat ||
    parseCoord(longitude.value) !== o.lng ||
    (provinceSelected.value ?? null) !== o.provinceID
  )
})

/* ---------- Ma position ---------- */

const geoLoading = ref(false)
const geoError = ref('')
let geoErrorTimer = null

function useMyPosition() {
  clearTimeout(geoErrorTimer)
  geoError.value = ''

  if (!navigator.geolocation) {
    geoError.value = 'Géolocalisation non supportée'
    return
  }

  geoLoading.value = true

  const onSuccess = ({ coords }) => {
    clearTimeout(geoErrorTimer)
    geoError.value = ''
    latitude.value = coords.latitude.toFixed(6)
    longitude.value = coords.longitude.toFixed(6)
    geoLoading.value = false
  }

  const onFail = (e) => {
    console.warn('Géolocalisation :', e.code, e.message)
    clearTimeout(geoErrorTimer)
    geoErrorTimer = setTimeout(() => {
      geoError.value =
        e.code === 1 ? 'Autorisation de localisation refusée'
        : e.code === 2 ? 'Position indisponible'
        : 'Délai dépassé, réessayez'
      geoLoading.value = false
    }, 1500)
  }

  navigator.geolocation.getCurrentPosition(
    onSuccess,
    (e) => {
      if (e.code === 1) return onFail(e)

      navigator.geolocation.getCurrentPosition(onSuccess, onFail, {
        enableHighAccuracy: false,
        timeout: 10000,
        maximumAge: 60000
      })
    },
    { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
  )
}

onBeforeUnmount(() => clearTimeout(geoErrorTimer))

/* ---------- Ouverture / fermeture / reset ---------- */

function ouvrirFormulaire() {
  error.value = ''
  success.value = ''
  showForm.value = true
}

function resetForm() {
  dateEvenement.value = ''
  commune.value = ''
  localite.value = ''
  latitude.value = ''
  longitude.value = ''
  geoError.value = ''
  clearProtocol()
  detachLocation()
  cancelLocationSearch()
  formRef.value?.classList.remove('was-validated')
  // pays et province conservés : on saisit souvent plusieurs lignes de suite
}

function fermerFormulaire() {
  showForm.value = false
  resetForm()
}

/* ---------- Soumission ---------- */

async function enregistrer() {
  error.value = ''
  success.value = ''

  if (!formRef.value.checkValidity()) {
    formRef.value.classList.add('was-validated')
    return
  }

  if (protocolInput.value.trim() && !protocolSelected.value) {
    error.value = 'Choisissez un protocole dans la liste, ou videz le champ.'
    return
  }

  const lat = parseCoord(latitude.value)
  const lng = parseCoord(longitude.value)

  if (Number.isNaN(lat) || Number.isNaN(lng)) {
    error.value = 'Latitude ou longitude invalide'
    return
  }

  saving.value = true
  try {
    // Lieu choisi et inchangé : on réutilise son ID. Sinon, nouveau lieu.
    const reuse = locationSelected.value && !locationModified.value

    const locationID = reuse
      ? locationSelected.value
      : (
          await insertItem('locations', {
            municipality: commune.value,
            locality: localite.value,
            decimalLatitude: lat,
            decimalLongitude: lng,
            province: provinceSelected.value
              ? { provinceID: provinceSelected.value }
              : null
          })
        ).locationID

    await insertItem('events', {
      eventDate: `${dateEvenement.value}T00:00:00`,
      dataset: { datasetID: idDataset },
      location: { locationID },
      protocol: protocolSelected.value
        ? { protocolID: protocolSelected.value }
        : null
    })

    success.value = 'Événement enregistré.'
    resetForm()

    // La nouvelle ligne apparaît en tête de liste
    if (evPage.value !== 1) {
      evPage.value = 1
    } else {
      fetchEvenements()
    }
  } catch (e) {
    console.error('Erreur enregistrement du contexte :', e)
    error.value = "Erreur lors de l'enregistrement de l'événement"
  } finally {
    saving.value = false
  }
}

/* ---------- Chargement initial ---------- */

const TOUT = { page: 1, perPage: 1000 }

async function loadLocation() {
  try {
    const [paysRes, provincesRes] = await Promise.all([
      getItems('pays', {}, TOUT),
      getItems('provinces', {}, TOUT)
    ])
    pays.value = paysRes.items
    provinces.value = provincesRes.items
  } catch (e) {
    console.error('Erreur chargement des lieux :', e)
  }
}

onMounted(() => {
  loadLocation()
  if (datasetValide) fetchEvenements()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link to="/saisie/saisie-contexte">Saisie</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">Saisie : contexte</li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">Nouvelle observation</h1>

    <ul class="nav nav-tabs">
      <li class="nav-item">
        <router-link class="nav-link active" :to="`/saisie/saisie-contexte/${idDataset}`">
          1 Contexte
        </router-link>
      </li>
      <li class="nav-item">
        <span class="nav-link disabled" aria-disabled="true">2 Observations</span>
      </li>
      <li class="nav-item">
        <span class="nav-link disabled" aria-disabled="true">3 Identification</span>
      </li>
      <li class="nav-item">
        <span class="nav-link disabled" aria-disabled="true">4 Compléments</span>
      </li>
    </ul>

    <!-- Dataset absent ou invalide -->
    <div v-if="!datasetValide" class="alert alert-warning mb-0" role="alert">
      Aucun dataset sélectionné. Choisissez d'abord un dataset pour saisir des événements.
    </div>

    <template v-else>

      <!-- Liste des événements -->
      <div class="card">
        <div class="card-header fw-semibold">
          Une ligne = un événement enregistré
        </div>

        <div class="card-body d-grid gap-3">

          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col">Date</th>
                  <th scope="col">Commune</th>
                  <th scope="col">Localité</th>
                  <th scope="col">Protocole</th>
                  <th scope="col"></th>
                </tr>
              </thead>

              <tbody>

                <tr v-if="evLoading">
                  <td colspan="5" class="text-center text-body-secondary">Chargement...</td>
                </tr>

                <tr v-else-if="evenements.length === 0">
                  <td colspan="5" class="text-center text-body-secondary">
                    Aucun événement pour ce dataset.
                  </td>
                </tr>

                <template v-else>
                  <tr v-for="ev in evenements" :key="ev.eventID">
                    <td>{{ formatDate(ev.eventDate) }}</td>
                    <td>{{ ev.location?.municipality || '-' }}</td>
                    <td>{{ ev.location?.locality || '-' }}</td>
                    <td>{{ ev.protocol?.protocolName || '-' }}</td>
                    <td class="text-nowrap">
                      <router-link
                        class="btn btn-sm btn-outline-primary"
                        :to="`/saisie/saisie-observations/${ev.eventID}`"
                      >
                        Observations
                      </router-link>
                    </td>
                  </tr>
                </template>

              </tbody>
            </table>
          </div>

          <!-- Pagination -->
          <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
            <small class="text-body-secondary">{{ evTotal }} événement(s)</small>

            <div class="d-flex align-items-center gap-2">
              <select
                class="form-select form-select-sm w-auto"
                v-model.number="evPerPage"
                aria-label="Événements par page"
              >
                <option :value="5">5</option>
                <option :value="10">10</option>
                <option :value="20">20</option>
              </select>

              <button
                type="button"
                class="btn btn-sm btn-outline-secondary"
                :disabled="evPage <= 1 || evLoading"
                @click="evPage--"
              >
                Précédent
              </button>

              <span class="small">Page {{ evPage }} / {{ evTotalPages }}</span>

              <button
                type="button"
                class="btn btn-sm btn-outline-secondary"
                :disabled="evPage >= evTotalPages || evLoading"
                @click="evPage++"
              >
                Suivant
              </button>
            </div>
          </div>

          <div v-if="success" class="alert alert-success mb-0" role="status">{{ success }}</div>

          <div v-if="!showForm" class="d-flex flex-wrap gap-2">
            <button type="button" class="btn btn-outline-secondary" @click="ouvrirFormulaire">
              + Ajouter un événement
            </button>
          </div>

        </div>
      </div>

      <!-- Formulaire d'ajout -->
      <div v-if="showForm" class="card">
        <div class="card-header fw-semibold">Nouvel événement</div>

        <form
          ref="formRef"
          class="card-body d-grid gap-4 needs-validation"
          novalidate
          @submit.prevent="enregistrer"
        >

          <!-- Événement -->
          <div class="d-grid gap-3">
            <div class="fw-semibold">Événement</div>

            <div class="row g-3">
              <div class="col-md">
                <label class="form-label" for="f2">
                  Date de l'événement<span class="text-danger"> *</span>
                </label>
                <input
                  class="form-control"
                  type="date"
                  id="f2"
                  name="date-de-l-evenement"
                  v-model="dateEvenement"
                  required
                />
              </div>

              <!-- Protocole -->
              <div class="col-md">
                <label class="form-label" for="f3">Protocole</label>

                <div class="position-relative">
                  <div class="input-group">
                    <input
                      id="f3"
                      name="protocole"
                      type="text"
                      class="form-control"
                      :class="{ 'is-valid': protocolSelected }"
                      placeholder="Rechercher un protocole..."
                      autocomplete="off"
                      role="combobox"
                      aria-autocomplete="list"
                      aria-controls="protocol-list"
                      :aria-expanded="protocolOpen"
                      v-model="protocolInput"
                      @input="onProtocolInput"
                      @focus="onProtocolFocus"
                      @blur="closeProtocols"
                      @keydown="onProtocolKeydown"
                    />
                    <button
                      v-if="protocolInput"
                      type="button"
                      class="btn btn-outline-secondary"
                      aria-label="Effacer le protocole"
                      @mousedown.prevent
                      @click="clearProtocol"
                    >
                      &times;
                    </button>
                  </div>

                  <ul
                    v-if="protocolOpen"
                    id="protocol-list"
                    class="list-group position-absolute w-100 shadow"
                    style="z-index: 1050; max-height: 240px; overflow-y: auto"
                    role="listbox"
                  >
                    <li v-if="protocolLoading" class="list-group-item text-body-secondary">
                      Recherche...
                    </li>

                    <li
                      v-else-if="protocolResults.length === 0"
                      class="list-group-item text-body-secondary"
                    >
                      Aucun protocole trouvé
                    </li>

                    <template v-else>
                      <li
                        v-for="(p, i) in protocolResults"
                        :key="p.protocolID"
                        class="list-group-item list-group-item-action"
                        :class="{ active: i === protocolActive }"
                        role="option"
                        :aria-selected="i === protocolActive"
                        style="cursor: pointer"
                        @mousedown.prevent
                        @click="chooseProtocol(p)"
                        @mouseenter="protocolActive = i"
                      >
                        {{ p.protocolName }}
                      </li>
                    </template>
                  </ul>
                </div>
              </div>
            </div>
          </div>

          <hr class="my-0" />

          <!-- Lieu -->
          <div class="d-grid gap-3">
            <div class="fw-semibold">Lieu</div>

            <div class="d-grid gap-2">
              <div class="d-flex flex-wrap gap-2">
                <button
                  v-if="!showLocationSearch"
                  type="button"
                  class="btn btn-outline-secondary"
                  @click="openLocationSearch"
                >
                  {{ locationSelected ? 'Choisir un autre lieu' : 'Choisir un lieu existant' }}
                </button>

                <button
                  v-else
                  type="button"
                  class="btn btn-outline-danger"
                  @click="cancelLocationSearch"
                >
                  Annuler
                </button>

                <button
                  v-if="locationSelected && !showLocationSearch && !locationModified"
                  type="button"
                  class="btn btn-outline-secondary"
                  @click="detachLocation"
                >
                  Ne plus réutiliser ce lieu
                </button>
              </div>

              <div v-if="showLocationSearch" class="position-relative">
                <input
                  type="text"
                  class="form-control"
                  placeholder="Rechercher par localité ou commune..."
                  autocomplete="off"
                  v-model="locationInput"
                  @input="onLocationInput"
                  @keydown.esc="cancelLocationSearch"
                  @keydown.enter.prevent
                />

                <ul
                  v-if="locationOpen"
                  class="list-group position-absolute w-100 shadow"
                  style="z-index: 1050; max-height: 300px; overflow-y: auto"
                >
                  <li v-if="locationLoading" class="list-group-item text-body-secondary">
                    Recherche...
                  </li>

                  <li
                    v-else-if="!locationResults.length"
                    class="list-group-item text-body-secondary"
                  >
                    Aucun lieu trouvé
                  </li>

                  <template v-else>
                    <li
                      v-for="l in locationResults"
                      :key="l.locationID"
                      class="list-group-item list-group-item-action"
                      style="cursor: pointer"
                      @click="chooseLocation(l)"
                    >
                      <div class="fw-semibold">
                        {{ [l.locality, l.municipality].filter(Boolean).join(', ') || 'Lieu #' + l.locationID }}
                      </div>
                      <small class="text-body-secondary d-block">
                        {{ locationPlace(l) || 'Province inconnue' }}
                      </small>
                      <small class="text-body-secondary d-block">
                        {{ locationCoords(l) }}
                      </small>
                    </li>
                  </template>
                </ul>
              </div>

              <div
                v-if="locationSelected"
                class="alert py-2 mb-0"
                :class="locationModified ? 'alert-warning' : 'alert-info'"
              >
                <template v-if="locationModified">
                  Vous avez modifié le lieu choisi : un nouveau lieu sera créé à l'enregistrement.
                </template>
                <template v-else>
                  Lieu existant sélectionné : il sera réutilisé tel quel. Modifiez un champ pour créer un nouveau lieu.
                </template>
              </div>
            </div>

            <div class="row g-3">
              <div class="col-md">
                <label class="form-label" for="f5">Pays</label>
                <select class="form-select" id="f5" name="pays" v-model="paysSelected">
                  <option :value="null">-- Choisir --</option>
                  <option v-for="p in pays" :key="p.paysID" :value="p.paysID">
                    {{ p.pays }}
                  </option>
                </select>
              </div>

              <div class="col-md">
                <label class="form-label" for="f6">
                  Province<span class="text-danger"> *</span>
                </label>
                <select
                  class="form-select"
                  id="f6"
                  name="province"
                  v-model="provinceSelected"
                  required
                >
                  <option :value="null" disabled>-- Choisir --</option>
                  <option
                    v-for="pr in provincesFiltrees"
                    :key="pr.provinceID"
                    :value="pr.provinceID"
                  >
                    {{ pr.province }}
                  </option>
                </select>
              </div>

              <div class="col-md">
                <label class="form-label" for="f7">
                  Commune<span class="text-danger"> *</span>
                </label>
                <input
                  class="form-control"
                  type="text"
                  id="f7"
                  name="commune"
                  v-model="commune"
                  required
                />
              </div>
            </div>

            <div class="row g-3">
              <div class="col-md">
                <label class="form-label" for="f8">Localité</label>
                <input
                  class="form-control"
                  type="text"
                  id="f8"
                  name="localite"
                  v-model="localite"
                />
              </div>

              <div class="col-md">
                <label class="form-label" for="f9">Latitude</label>
                <input
                  class="form-control"
                  type="text"
                  inputmode="decimal"
                  id="f9"
                  name="latitude"
                  v-model="latitude"
                  placeholder="-18.9"
                />
              </div>

              <div class="col-md">
                <label class="form-label" for="f10">Longitude</label>
                <input
                  class="form-control"
                  type="text"
                  inputmode="decimal"
                  id="f10"
                  name="longitude"
                  v-model="longitude"
                  placeholder="48.4"
                />
              </div>
            </div>

            <div class="d-flex flex-wrap align-items-center gap-2">
              <button
                type="button"
                class="btn btn-outline-secondary"
                :disabled="geoLoading"
                @click="useMyPosition"
              >
                {{ geoLoading ? 'Localisation...' : 'Ma position' }}
              </button>
              <small v-if="geoError" class="text-danger">{{ geoError }}</small>
            </div>
          </div>

          <div v-if="error" class="alert alert-danger mb-0" role="alert">{{ error }}</div>

          <div class="d-flex flex-wrap gap-2">
            <button type="submit" class="btn btn-primary" :disabled="saving">
              {{ saving ? 'Enregistrement...' : 'Enregistrer la ligne' }}
            </button>
            <button
              type="button"
              class="btn btn-outline-secondary"
              :disabled="saving"
              @click="fermerFormulaire"
            >
              Fermer
            </button>
          </div>

        </form>
      </div>

    </template>

  </main>
</template>