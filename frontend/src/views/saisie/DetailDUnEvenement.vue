
<script setup>
import { usePage } from '../../composables/usePage'
import { onMounted, ref, onBeforeUnmount, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getItemById, getItems } from '../../services/api'
import { useAccessError } from '../../composables/useAccessError'

import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

// Icônes Leaflet avec Vite
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'

usePage()

const { gererAcces } = useAccessError()
const route = useRoute()

const idEvent = route.params.idEvent

const event = ref(null)
const eventLoading = ref(false)

async function fetchEvent() {
    eventLoading.value = true

    try {
        const response = await getItemById('events', idEvent)
        event.value = response
    } catch (error) {
        if (gererAcces(error)) return

        console.error(
            "Erreur lors de la récupération de l'événement :",
            error
        )
    } finally {
        eventLoading.value = false
    }
}

function formatDate(dateString) {
    if (!dateString) return '—'

    const date = new Date(dateString)

    if (Number.isNaN(date.getTime())) return '—'

    return date.toLocaleDateString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    })
}

function nomOrganisme(organism) {
    return organism?.organismName ?? 'Organisme non renseigné'
}

// Libellés des statuts
const STATUTS = {
    PRESENT: 'Présent',
    present: 'Présent',
    ABSENT: 'Absent',
    absent: 'Absent'
}

// Libellés des types d'enregistrement
const TYPES = {
    HumanObservation: 'Observation humaine',
    MachineObservation: 'Observation automatique',
    PreservedSpecimen: 'Spécimen conservé',
    FossilSpecimen: 'Spécimen fossile',
    MaterialSample: 'Échantillon matériel',
    LivingSpecimen: 'Spécimen vivant'
}

function labelDe(liste, valeur) {
    return liste[valeur] ?? valeur ?? '—'
}

// ==========================================
// CARTE LEAFLET
// ==========================================

delete L.Icon.Default.prototype._getIconUrl

L.Icon.Default.mergeOptions({
    iconUrl: markerIcon,
    iconRetinaUrl: markerIcon2x,
    shadowUrl: markerShadow
})

const mapEl = ref(null)

let map = null
let marker = null

function initialiserCarte() {
    if (!mapEl.value) return

    map = L.map(mapEl.value).setView([-18.9, 47.5], 6)

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map)
}

function afficherLocalisation(ev) {
    const lat = Number(ev?.location?.decimalLatitude)
    const lng = Number(ev?.location?.decimalLongitude)

    if (!map || !Number.isFinite(lat) || !Number.isFinite(lng)) return

    if (marker) {
        marker.remove()
    }

    marker = L.marker([lat, lng])
        .addTo(map)
        .bindPopup(ev.location?.locality ?? 'Lieu de l’événement')

    map.setView([lat, lng], 13)
}

watch(event, (ev) => {
    if (ev) {
        afficherLocalisation(ev)
    }
})

const occurrences = ref([])
const occTotal = ref(0)
const occLoading = ref(false)

const occPage = ref(1)
const occPerPage = ref(10)

const occTotalPages = computed(() =>
    Math.max(1, Math.ceil(occTotal.value / occPerPage.value))
)

let occRequestId = 0

async function fetchOccurrences() {
    const requestId = ++occRequestId

    occLoading.value = true

    try {
        const { items, total } = await getItems(
            'occurrences',
            {
                'event.eventID': idEvent
            },
            {
                page: occPage.value,
                perPage: occPerPage.value,
                sort: 'occurrenceID,desc'
            }
        )

        // Ignorer les anciennes requêtes
        if (requestId !== occRequestId) return

        // Si la page est devenue vide, revenir à la précédente
        if (items.length === 0 && occPage.value > 1) {
            occPage.value--
            return
        }

        occurrences.value = items ?? []
        occTotal.value = total ?? 0

    } catch (error) {
        if (requestId !== occRequestId) return

        if (gererAcces(error)) return

        console.error(
            'Erreur lors du chargement des occurrences :',
            error
        )

        occurrences.value = []
        occTotal.value = 0

    } finally {
        if (requestId === occRequestId) {
            occLoading.value = false
        }
    }
}

// Changement du nombre d'occurrences par page
watch(occPerPage, () => {
    if (occPage.value !== 1) {
        occPage.value = 1
    } else {
        fetchOccurrences()
    }
})

// Changement de page
watch(occPage, fetchOccurrences)

onMounted(async () => {
    initialiserCarte()

    await Promise.all([
        fetchEvent(),
        fetchOccurrences()
    ])
})

onBeforeUnmount(() => {
    // Invalider les requêtes en cours
    occRequestId++

    if (map) {
        map.remove()
        map = null
        marker = null
    }
})
</script>

<template>
    <main id="content" class="p-4 d-grid gap-4 align-content-start">

        <!-- FIL D'ARIANE -->
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item">
                    <router-link to="/accueil">
                        Accueil
                    </router-link>
                </li>

                <li class="breadcrumb-item">
                    <router-link to="/saisie/saisie-contexte">
                        Saisie
                    </router-link>
                </li>

                <li class="breadcrumb-item active" aria-current="page">
                    Détail d'un événement
                </li>
            </ol>
        </nav>

        <h1 class="h3 mb-0">Détail de l'événement</h1>

        <div class="card">

            <div class="card-header fw-semibold">
                Détails de l'événement
            </div>

            <div class="card-body d-grid gap-3">

                <div v-if="eventLoading" class="text-center text-body-secondary">
                    Chargement des informations...
                </div>

                <ul v-else class="list-unstyled mb-0 d-grid gap-2">

                    <li>
                        <span class="text-body-secondary">Recherche :</span>
                        {{ event?.dataset?.recherche?.sujet ?? '—' }}
                    </li>

                    <li>
                        <span class="text-body-secondary">Dataset :</span>
                        {{ event?.dataset?.datasetName ?? '—' }}
                    </li>

                    <li>
                        <span class="text-body-secondary">Lieu :</span>
                        {{
                            [
                                event?.location?.municipality,
                                event?.location?.locality
                            ].filter(Boolean).join(', ') || '—'
                        }}
                    </li>

                    <li>
                        <span class="text-body-secondary">Protocole :</span>
                        {{ event?.protocol?.protocolName ?? '—' }}
                    </li>

                    <li>
                        <span class="text-body-secondary">Date :</span>
                        {{ formatDate(event?.eventDate) }}
                    </li>

                </ul>

                <!-- CARTE -->
                <div ref="mapEl" class="event-map"></div>

            </div>
        </div>

        <div class="card">

            <div class="card-header fw-semibold d-flex justify-content-between align-items-center">
                <span>Occurrences</span>
            </div>

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

                            <!-- CHARGEMENT -->
                            <tr v-if="occLoading">
                                <td colspan="6" class="text-center text-body-secondary py-4">
                                    <span class="spinner-border spinner-border-sm me-2"></span>
                                    Chargement des occurrences...
                                </td>
                            </tr>

                            <!-- AUCUNE OCCURRENCE -->
                            <tr v-else-if="occurrences.length === 0">
                                <td colspan="6" class="text-center text-body-secondary py-4">
                                    Aucune occurrence pour cet événement.
                                </td>
                            </tr>

                            <!-- OCCURRENCES -->
                            <template v-else>

                                <tr
                                    v-for="o in occurrences"
                                    :key="o.occurrenceID"
                                >

                                    <td>
                                        {{ nomOrganisme(o.organism) }}
                                    </td>

                                    <td>
                                        <span
                                            class="badge"
                                            :class="
                                                ['ABSENT', 'absent'].includes(o.occurrenceStatus)
                                                    ? 'text-bg-secondary'
                                                    : 'text-bg-success'
                                            "
                                        >
                                            {{ labelDe(STATUTS, o.occurrenceStatus) }}
                                        </span>
                                    </td>

                                    <td>
                                        {{ labelDe(TYPES, o.basisOfRecord) }}
                                    </td>

                                    <td>
                                        {{ o.individualCount ?? '—' }}
                                    </td>

                                    <td>
                                        {{
                                            formatDate(
                                                o.event?.eventDate ?? event?.eventDate
                                            )
                                        }}
                                    </td>

                                    <td>
                                        {{
                                            o.event?.location?.municipality ??
                                            event?.location?.municipality ??
                                            '—'
                                        }}
                                    </td>
                                    <router-link
                                        class="btn btn-sm btn-outline-secondary"
                                        :to="`/partage/detail-d-une-observation/${o.occurrenceID}`"
                                    >
                                        Détails
                                    </router-link>
                                    <router-link
                                        class="btn btn-sm btn-outline-primary"
                                        :to="`/saisie/saisie-identification/${o.occurrenceID}`"
                                        >
                                        Identification
                                    </router-link>

                                </tr>

                            </template>

                        </tbody>
                    </table>
                </div>

                <!-- ================================== -->
                <!-- PAGINATION -->
                <!-- ================================== -->

                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">

                    <small class="text-body-secondary">
                        {{ occTotal }} occurrence(s) au total
                    </small>

                    <div class="d-flex align-items-center gap-2">

                        <!-- Nombre par page -->
                        <select
                            class="form-select form-select-sm w-auto"
                            v-model.number="occPerPage"
                            aria-label="Occurrences par page"
                        >
                            <option :value="5">5</option>
                            <option :value="10">10</option>
                            <option :value="20">20</option>
                        </select>

                        <!-- Page précédente -->
                        <button
                            type="button"
                            class="btn btn-sm btn-outline-secondary"
                            :disabled="occPage <= 1 || occLoading"
                            @click="occPage--"
                        >
                            Précédent
                        </button>

                        <!-- Numéro de page -->
                        <span class="small text-nowrap">
                            Page {{ occPage }} / {{ occTotalPages }}
                        </span>

                        <!-- Page suivante -->
                        <button
                            type="button"
                            class="btn btn-sm btn-outline-secondary"
                            :disabled="occPage >= occTotalPages || occLoading"
                            @click="occPage++"
                        >
                            Suivant
                        </button>

                    </div>
                </div>

            </div>
        </div>

        <!-- ================================== -->
        <!-- ACTIONS -->
        <!-- ================================== -->

        <div class="d-flex flex-wrap gap-2">
            <router-link class="btn btn-primary" :to="`/saisie/saisie-observations/${idEvent}`">Saisir</router-link>

            <a
                class="btn btn-outline-danger"
                href="/saisie/fiche-d-un-dataset"
                data-confirm="Voulez-vous vraiment effectuer cette action ?"
            >
                Supprimer
            </a>

            <router-link
                class="btn btn-primary"
                to="/recherches/modifier-une-recherche"
            >
                Modifier
            </router-link>

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
