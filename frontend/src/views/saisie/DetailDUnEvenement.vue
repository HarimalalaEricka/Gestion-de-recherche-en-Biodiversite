<script setup>
import { usePage } from '../../composables/usePage'
import { onMounted, ref, onBeforeUnmount, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getItemById } from '../../services/api'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

// Correction des icônes de marqueur avec Vite
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'

usePage()

const route = useRoute()
const idEvent = route.params.idEvent

const event = ref(null)

async function fetchEvent() {
  try {
    const response = await getItemById('events', idEvent)
    event.value = response
  } catch (error) {
    console.error('Erreur lors de la récupération de l\'événement :', error)
  }
}

function formatDate(dateString) {
  if (!dateString) return '—'
  const date = new Date(dateString)
  return date.toLocaleDateString('fr-FR', { year: 'numeric', month: 'long', day: 'numeric' })
}

delete L.Icon.Default.prototype._getIconUrl
L.Icon.Default.mergeOptions({
  iconUrl: markerIcon,
  iconRetinaUrl: markerIcon2x,
  shadowUrl: markerShadow,
})

const mapEl = ref(null)
let map = null
let marker = null

onMounted(() => {
  map = L.map(mapEl.value).setView([-18.9, 47.5], 6) // Madagascar par défaut
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
  }).addTo(map)

  fetchEvent()
})

// Quand l'événement arrive de l'API, on place le marqueur
watch(event, (ev) => {
  const lat = ev?.location?.decimalLatitude
  const lng = ev?.location?.decimalLongitude
  if (!map || lat == null || lng == null) return

  if (marker) marker.remove()
  marker = L.marker([lat, lng]).addTo(map).bindPopup(ev.location?.locality ?? '')
  map.setView([lat, lng], 13)
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
        <li class="breadcrumb-item"><router-link to="/saisie/saisie-contexte">Saisie</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">Détail d'un événement</li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">Détail de l'événement</h1>

    <div class="card">
      <div class="card-header fw-semibold">Détails</div>
      <div class="card-body d-grid gap-3">
        <ul class="list-unstyled mb-0 d-grid gap-1">
          <li><span class="text-body-secondary">Recherche :</span> {{ event?.dataset?.recherche?.sujet ?? '—' }}</li>
          <li><span class="text-body-secondary">Dataset :</span> {{ event?.dataset?.datasetName ?? '—' }}</li>
          <li>
            <span class="text-body-secondary">Lieu :</span>
            {{ [event?.location?.municipality, event?.location?.locality].filter(Boolean).join(', ') || '—' }}
          </li>
          <li><span class="text-body-secondary">Protocole :</span> {{ event?.protocol?.protocolName ?? '—' }}</li>
          <li><span class="text-body-secondary">Date :</span> {{ formatDate(event?.eventDate) }}</li>
        </ul>

        <div ref="mapEl" class="event-map"></div>
      </div>
    </div>

    <div class="card">
      <div class="card-header fw-semibold">Observations</div>
      <div class="card-body d-grid gap-3">
        <div class="card">
          <div class="card-header">
            <input type="search" form="nf" class="form-control form-control-sm table-filter"
              placeholder="Filtrer..." aria-label="Filtrer">
          </div>
          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0" data-table>
              <thead>
                <tr>
                  <th scope="col">Taxon</th>
                  <th scope="col">Statut</th>
                  <th scope="col">Nombre</th>
                  <th scope="col">Identifié par</th>
                  <th scope="col"></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>Lemur catta</td>
                  <td><span class="badge text-bg-success">Publiée</span></td>
                  <td>Rakoto Andry</td>
                  <td>Rakoto Andry</td>
                  <td class="text-end">
                    <router-link class="btn btn-sm btn-outline-primary" to="/partage/detail-d-une-observation">Ouvrir</router-link>
                  </td>
                </tr>
                <tr>
                  <td>Propithecus verreauxi</td>
                  <td><span class="badge text-bg-secondary">Privée</span></td>
                  <td>Rasoa Voahangy</td>
                  <td>Rasoa Voahangy</td>
                  <td class="text-end">
                    <router-link class="btn btn-sm btn-outline-primary" to="/partage/detail-d-une-observation">Ouvrir</router-link>
                  </td>
                </tr>
                <tr>
                  <td>Furcifer pardalis</td>
                  <td><span class="badge text-bg-secondary">Brouillon</span></td>
                  <td>Randria Tojo</td>
                  <td>Randria Tojo</td>
                  <td class="text-end">
                    <router-link class="btn btn-sm btn-outline-primary" to="/partage/detail-d-une-observation">Ouvrir</router-link>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div class="d-flex flex-wrap gap-2">
      <a class="btn btn-outline-danger" href="/saisie/fiche-d-un-dataset"
        data-confirm="Voulez-vous vraiment effectuer cette action ?">Supprimer</a>
      <router-link class="btn btn-primary" to="/recherches/modifier-une-recherche">Modifier</router-link>
    </div>
  </main>
</template>

<style scoped>
.event-map {
  height: 320px;
  width: 100%;
  border-radius: .375rem;
}
</style>