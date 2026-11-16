<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getItemById, getItems } from '../../services/api'
import { useAccessError } from '../../composables/useAccessError'

const { gererAcces } = useAccessError()

const route = useRoute()

usePage()

const idDataset = route.params.idDataset
const dataset = ref(null)
const recherche = ref(null)
const createur = ref(null)

/* ------------------------------------------------------------------ */
/* Fiche du dataset                                                    */
/* ------------------------------------------------------------------ */

async function loadDataset() {
    try {
        const data = await getItemById(`datasets/${idDataset}`)
        dataset.value = data

        const rechercheData = await getItemById(
            `recherches/${data.recherche.rechercheID}`
        )
        recherche.value = rechercheData
        await fetchUtilisateurByAgentId()

    } catch (e) {
        if (gererAcces(e)) return      
        console.error('Erreur chargement du dataset :', e)
    }
}

async function fetchUtilisateurByAgentId() {
    try {
        const userId = dataset.value?.agent?.utilisateurId

        if (!userId) {
            console.warn('Aucun utilisateur associé à cet agent.')
            return
        }
        const utilisateur = await getItemById(`utilisateurs/${userId}`)
        createur.value = utilisateur

    } catch (e) {
        console.error('Erreur chargement de l’utilisateur :', e)
    }
}

/* ------------------------------------------------------------------ */
/* Statistique : total d'événements du dataset (indépendant des filtres) */
/* ------------------------------------------------------------------ */

const totalEventsDataset = ref(0)

async function fetchTotalEvents() {
    try {
        const { total } = await getItems(
            'events',
            { 'dataset.datasetID': idDataset },
            { page: 1, perPage: 1 }
        )
        totalEventsDataset.value = total ?? 0
    } catch (e) {
        console.error('Erreur chargement du total des événements :', e)
        totalEventsDataset.value = 0
    }
}

/* ------------------------------------------------------------------ */
/* Liste des événements (pagination + filtres lieu / protocole)        */
/* ------------------------------------------------------------------ */

const events = ref([])
const eventsTotal = ref(0)
const eventsLoading = ref(false)

const eventPage = ref(1)
const eventPerPage = ref(10)

const eventTotalPages = computed(() =>
    Math.max(1, Math.ceil(eventsTotal.value / eventPerPage.value))
)

// Évite d'envoyer une requête au backend à chaque frappe
function debounce(fn, delay = 300) {
    let timer

    return (...args) => {
        clearTimeout(timer)
        timer = setTimeout(() => fn(...args), delay)
    }
}

// Filtre par lieu : valeur saisie / valeur envoyée au backend
const lieuSearch = ref('')
const lieuQuery = ref('')

const applyLieuQuery = debounce((v) => {
    lieuQuery.value = v.trim()
})

watch(lieuSearch, applyLieuQuery)

// Filtre par protocole : valeur saisie / valeur envoyée au backend
const protocoleSearch = ref('')
const protocoleQuery = ref('')

const applyProtocoleQuery = debounce((v) => {
    protocoleQuery.value = v.trim()
})

watch(protocoleSearch, applyProtocoleQuery)

const formatDate = (d) =>
    d ? new Date(d).toLocaleDateString('fr-FR') : '-'

let eventsRequestId = 0

async function fetchEvents() {
    const requestId = ++eventsRequestId

    eventsLoading.value = true

    try {
        const filters = { 'dataset.datasetID': idDataset }

        if (lieuQuery.value) {
            filters['location.municipality'] = lieuQuery.value
        }

        if (protocoleQuery.value) {
            filters['protocol.protocolName'] = protocoleQuery.value
        }

        const { items, total } = await getItems(
            'events',
            filters,
            {
                page: eventPage.value,
                perPage: eventPerPage.value,
                sort: 'eventDate,desc'
            }
        )

        if (requestId !== eventsRequestId) return

        // Page devenue vide : reculer d'une page
        if (items.length === 0 && eventPage.value > 1) {
            eventPage.value--
            return
        }

        events.value = items
        eventsTotal.value = total ?? 0

    } catch (e) {
        if (requestId !== eventsRequestId) return

        console.error('Erreur chargement des événements :', e)

        events.value = []
        eventsTotal.value = 0

    } finally {
        if (requestId === eventsRequestId) {
            eventsLoading.value = false
        }
    }
}

// Nouveau filtre ou nouvelle taille de page : retour page 1
watch([lieuQuery, protocoleQuery, eventPerPage], () => {
    if (eventPage.value !== 1) {
        eventPage.value = 1 // le watcher de eventPage fera le fetch
    } else {
        fetchEvents()
    }
})

watch(eventPage, fetchEvents)

/* ------------------------------------------------------------------ */

onMounted(() => {
    loadDataset()
    fetchTotalEvents()
    fetchEvents()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link :to="`/saisie/saisie-contexte/${idDataset}`">Saisie</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">
          <router-link :to="`/saisie/fiche-d-un-dataset/${idDataset}`">Fiche d&#39;un dataset</router-link>
        </li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">{{ dataset?.datasetName || 'Chargement...' }}</h1>

    <ul class="list-unstyled mb-0 d-grid gap-1">
      <li><span class="text-body-secondary">Recherche :</span> {{ recherche?.sujet || 'Chargement...' }}</li>
      <li>
        <span class="text-body-secondary">Créateur :</span>
        {{ createur?.nom || 'Chargement...' }} {{ createur?.prenom || '' }}
      </li>
      <li><span class="text-body-secondary">Licence :</span> {{ dataset?.licence?.licenceCode || 'Chargement...' }}</li>
      <li><span class="text-body-secondary">Détenteur des droits :</span> {{ dataset?.rightsHolder || 'Chargement...' }}</li>
      <li><span class="text-body-secondary">Protocoles :</span> Transect linéaire, Point d&#x27;écoute</li>
    </ul>

    <div class="row row-cols-2 row-cols-lg-3 g-3">
      <div class="col">
        <div class="card">
          <div class="card-body">
            <div class="fs-3 fw-semibold">{{ totalEventsDataset }}</div>
            <div class="text-body-secondary">événements</div>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <div class="card-body">
            <div class="fs-3 fw-semibold">12</div>
            <div class="text-body-secondary">observations</div>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <div class="card-body">
            <div class="fs-3 fw-semibold">48</div>
            <div class="text-body-secondary">taxons</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Événements -->
    <div class="card">
      <div class="card-header fw-semibold">Événements</div>

      <div class="card-body d-grid gap-3">

        <!-- Filtres -->
        <div class="row g-2">
          <div class="col-md">
            <input
              v-model="lieuSearch"
              type="search"
              class="form-control form-control-sm"
              placeholder="Filtrer par lieu..."
              aria-label="Filtrer par lieu"
            />
          </div>
          <div class="col-md">
            <input
              v-model="protocoleSearch"
              type="search"
              class="form-control form-control-sm"
              placeholder="Filtrer par protocole..."
              aria-label="Filtrer par protocole"
            />
          </div>
        </div>

        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead>
              <tr>
                <th scope="col">Date</th>
                <th scope="col">Lieu</th>
                <th scope="col">Protocole</th>
                <th scope="col">Observations</th>
                <th scope="col"></th>
              </tr>
            </thead>

            <tbody>

              <tr v-if="eventsLoading">
                <td colspan="5" class="text-center text-body-secondary">
                  Chargement...
                </td>
              </tr>

              <tr v-else-if="events.length === 0">
                <td colspan="5" class="text-center text-body-secondary">
                  Aucun événement trouvé.
                </td>
              </tr>

              <template v-else>
                <tr v-for="event in events" :key="event.eventID">
                  <td>{{ formatDate(event.eventDate) }}</td>
                  <td>{{ event.location?.municipality || 'N/A' }}</td>
                  <td>{{ event.protocol?.protocolName || 'N/A' }}</td>
                  <td>{{ event.observations?.length || 0 }}</td>
                  <td class="text-end">
                    <router-link
                      class="btn btn-sm btn-outline-primary"
                      :to="`/saisie/detail-d-un-evenement/${event.eventID}`"
                    >
                      Ouvrir
                    </router-link>
                  </td>
                </tr>
              </template>

            </tbody>
          </table>
        </div>

        <!-- Pagination des événements -->
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
          <small class="text-body-secondary">
            {{ eventsTotal }} événement(s)
          </small>

          <div class="d-flex align-items-center gap-2">

            <select
              class="form-select form-select-sm w-auto"
              v-model.number="eventPerPage"
              aria-label="Événements par page"
            >
              <option :value="5">5</option>
              <option :value="10">10</option>
              <option :value="20">20</option>
            </select>

            <button
              type="button"
              class="btn btn-sm btn-outline-secondary"
              :disabled="eventPage <= 1 || eventsLoading"
              @click="eventPage--"
            >
              Précédent
            </button>

            <span class="small">
              Page {{ eventPage }} / {{ eventTotalPages }}
            </span>

            <button
              type="button"
              class="btn btn-sm btn-outline-secondary"
              :disabled="eventPage >= eventTotalPages || eventsLoading"
              @click="eventPage++"
            >
              Suivant
            </button>

          </div>
        </div>

      </div>
    </div>

    <div class="d-flex flex-wrap gap-2">
      <router-link class="btn btn-primary" :to="`/saisie/saisie-contexte/${idDataset}`">Saisir</router-link>
      <router-link class="btn btn-outline-secondary" to="/saisie/import-excel-csv">Importer Excel / CSV</router-link>
      <a class="btn btn-outline-secondary" href="#" data-demo>Exporter CSV</a>
      <a class="btn btn-outline-secondary" href="#" data-demo>Exporter Excel</a>
    </div>

  </main>
</template>