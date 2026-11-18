
<script setup>
import { usePage } from '../../composables/usePage'
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getItemById, getItems } from '../../services/api'

usePage()

const route = useRoute()

const idOccurrence = Number(route.params.idOccurrence)

const occurrence = ref(null)
const identifications = ref([])
const medias = ref([])

const page = ref(1)
const perPage = ref(10)
const total = ref(0)

const totalPages = computed(() =>
    Math.max(1, Math.ceil(total.value / perPage.value))
)

// Navigation de la galerie
const mediaPosition = ref(0)

const nextMedias = () => {
    if (mediaPosition.value < medias.value.length - 3) {
        mediaPosition.value++
    }
}

const previousMedias = () => {
    if (mediaPosition.value > 0) {
        mediaPosition.value--
    }
}

const formatDate = (d) =>
    d ? new Date(d).toLocaleDateString('fr-FR') : '-'

const isAccepted = (identification) =>
    identification?.isAcceptedIdentification === true

// Chargement de l'occurrence
async function fetchOccurrence() {
    try {
        occurrence.value = await getItemById(
            'occurrences',
            idOccurrence
        )
    } catch (e) {
        console.error(
            "Erreur lors du chargement de l'occurrence : ",
            e
        )
    }
}

// Chargement des identifications
async function fetchIdentifications() {
    try {
        const { items, total: totalCount } = await getItems(
            'identifications',
            { 'occurrence.occurrenceID': idOccurrence },
            {
                page: page.value,
                perPage: perPage.value,
                sort: 'dateIdentified,desc'
            }
        )

        identifications.value = items
        total.value = totalCount

    } catch (e) {
        console.error(
            'Erreur lors du chargement des identifications : ',
            e
        )
    }
}

// Chargement des médias
async function fetchMedias() {
    try {
        const { items } = await getItems(
            'medias',
            { 'occurrence.occurrenceID': idOccurrence },
            {
                page: 1,
                perPage: 100,
                sort: 'mediaID,asc'
            }
        )

        medias.value = items
        mediaPosition.value = 0

    } catch (e) {
        console.error(
            'Erreur lors du chargement des médias : ',
            e
        )
    }
}

onMounted(() => {
    fetchOccurrence()
    fetchIdentifications()
    fetchMedias()
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

                <li class="breadcrumb-item active" aria-current="page">
                    <router-link to="/partage/detail-d-une-observation">
                        Détail d'une observation
                    </router-link>
                </li>
            </ol>
        </nav>

        <!-- EN-TÊTE -->
        <div>
            <h1 class="h3 mb-0">
                Propithecus verreauxi
            </h1>

            <p class="text-body-secondary mb-0">
                Observation n° {{ idOccurrence }}
            </p>
        </div>

        <div class="row g-4">

            <!-- COLONNE GAUCHE -->
            <div class="col-lg-4 d-grid gap-3 align-content-start">

                <!-- ÉVÉNEMENT -->
                <div class="card">
                    <div class="card-header fw-semibold">
                        Événement
                    </div>

                    <div class="card-body">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li>
                                <span class="text-body-secondary">Date :</span>
                                {{ formatDate(occurrence?.event?.eventDate) }}
                            </li>

                            <li>
                                <span class="text-body-secondary">Dataset :</span>
                                {{ occurrence?.event?.dataset?.datasetName || '-' }}
                            </li>

                            <li>
                                <span class="text-body-secondary">Protocole :</span>
                                {{ occurrence?.event?.protocol?.protocolName || '-' }}
                            </li>
                        </ul>
                    </div>
                </div>

                <!-- LIEU -->
                <div class="card">
                    <div class="card-header fw-semibold">
                        Lieu
                    </div>

                    <div class="card-body">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li>
                                <span class="text-body-secondary">Pays :</span>
                                {{ occurrence?.event?.location?.province?.pays?.pays || '-' }}
                            </li>

                            <li>
                                <span class="text-body-secondary">Province :</span>
                                {{ occurrence?.event?.location?.province?.province || '-' }}
                            </li>

                            <li>
                                <span class="text-body-secondary">Commune :</span>
                                {{ occurrence?.event?.location?.municipality || '-' }}
                            </li>

                            <li>
                                <span class="text-body-secondary">Coordonnées :</span>
                                {{ occurrence?.event?.location?.decimalLatitude ?? '-' }}
                                ;
                                {{ occurrence?.event?.location?.decimalLongitude ?? '-' }}
                            </li>
                        </ul>
                    </div>
                </div>

                <!-- ORGANISME ET OBSERVATION -->
                <div class="card">
                    <div class="card-header fw-semibold">
                        Organisme et observation
                    </div>

                    <div class="card-body">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li>
                                <span class="text-body-secondary">Portée :</span>
                                {{ occurrence?.organism?.organismScope || '-' }}
                            </li>

                            <li>
                                <span class="text-body-secondary">Détection :</span>
                                {{ occurrence?.occurrenceStatus || '-' }}
                            </li>

                            <li>
                                <span class="text-body-secondary">Type de donnée :</span>
                                {{ occurrence?.basisOfRecord || '-' }}
                            </li>

                            <li>
                                <span class="text-body-secondary">Nombre :</span>
                                {{ occurrence?.individualCount ?? '-' }}
                            </li>
                        </ul>
                    </div>
                </div>

            </div>

            <!-- COLONNE DROITE -->
            <div class="col-lg-8 d-grid gap-3 align-content-start">

                <!-- IDENTIFICATIONS -->
                <div class="card">
                    <div class="card-header fw-semibold">
                        Identifications
                    </div>

                    <div class="card-body d-grid gap-3">
                        <div class="card">

                            <div class="card-header">
                                <input
                                    type="search"
                                    form="nf"
                                    class="form-control form-control-sm table-filter"
                                    placeholder="Filtrer..."
                                    aria-label="Filtrer"
                                >
                            </div>

                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0" data-table>

                                    <thead>
                                        <tr>
                                            <th scope="col">Taxon</th>
                                            <th scope="col">Date</th>
                                            <th scope="col">Identifié par</th>
                                            <th scope="col">Qualificatif</th>
                                            <th scope="col">Acceptée</th>
                                            <th scope="col"></th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr v-if="identifications.length === 0">
                                            <td colspan="6"
                                                class="text-center text-body-secondary">
                                                Aucune identification trouvée
                                            </td>
                                        </tr>

                                        <template v-else>
                                            <tr
                                                v-for="i in identifications"
                                                :key="i.identificationID"
                                            >
                                                <td>
                                                    {{ i.taxon?.scientificName || '-' }}
                                                </td>

                                                <td>
                                                    {{ formatDate(i.dateIdentified) }}
                                                </td>

                                                <td>
                                                    {{ i.agent?.utilisateurId || '-' }}
                                                </td>

                                                <td>
                                                    {{ i.identificationQualifier || '-' }}
                                                </td>

                                                <td>
                                                    {{ isAccepted(i) ? 'Oui' : 'Non' }}
                                                </td>

                                                <td>
                                                    <router-link
                                                        v-if="i.taxon?.taxonID"
                                                        class="btn btn-sm btn-outline-primary"
                                                        :to="`/partage/fiche-taxon/${i.taxon.taxonID}`"
                                                    >
                                                        Fiche Taxon
                                                    </router-link>
                                                </td>
                                            </tr>
                                        </template>
                                    </tbody>

                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- MESURES -->
                <div class="card">
                    <div class="card-header fw-semibold">
                        Mesures
                    </div>

                    <div class="card-body d-grid gap-3">
                        <div class="card">

                            <div class="card-header">
                                <input
                                    type="search"
                                    form="nf"
                                    class="form-control form-control-sm table-filter"
                                    placeholder="Filtrer..."
                                    aria-label="Filtrer"
                                >
                            </div>

                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">

                                    <thead>
                                        <tr>
                                            <th scope="col">Type</th>
                                            <th scope="col">Valeur</th>
                                            <th scope="col">Unité</th>
                                            <th scope="col">Méthode</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td>Poids</td>
                                            <td>2,4</td>
                                            <td>kg</td>
                                            <td>Transect linéaire</td>
                                        </tr>

                                        <tr>
                                            <td>Longueur du corps</td>
                                            <td>15,8</td>
                                            <td>cm</td>
                                            <td>Pesée</td>
                                        </tr>
                                    </tbody>

                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- MÉDIAS -->
                <div class="card">

                    <div class="card-header fw-semibold d-flex justify-content-between align-items-center">
                        <span>Médias</span>

                        <span class="badge bg-secondary">
                            {{ medias.length }}
                        </span>
                    </div>

                    <div class="card-body">

                        <!-- Aucun média -->
                        <div
                            v-if="medias.length === 0"
                            class="text-center text-body-secondary py-4"
                        >
                            <i class="bi bi-images fs-2 d-block mb-2"></i>
                            Aucun média associé à cette observation.
                        </div>

                        <!-- Galerie -->
                        <div v-else class="position-relative">

                            <div class="media-gallery">

                                <div
                                    v-for="media in medias.slice(mediaPosition, mediaPosition + 3)"
                                    :key="media.mediaID"
                                    class="media-item"
                                >

                                    <!-- IMAGE -->
                                    <a
                                        v-if="media.typeMedia === 'IMAGE'"
                                        :href="media.accessURI"
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        class="media-preview"
                                        :title="media.caption || 'Voir le média en taille réelle'"
                                    >
                                        <img
                                            :src="media.accessURI"
                                            :alt="media.caption || 'Média de l’observation'"
                                            loading="lazy"
                                        >
                                    </a>

                                    <!-- VIDÉO -->
                                    <video
                                        v-else-if="media.typeMedia === 'VIDEO'"
                                        :src="media.accessURI"
                                        controls
                                        class="media-preview"
                                    ></video>

                                    <!-- AUDIO -->
                                    <div
                                        v-else-if="media.typeMedia === 'AUDIO'"
                                        class="media-preview media-file"
                                    >
                                        <i class="bi bi-music-note-beamed fs-1"></i>

                                        <audio
                                            :src="media.accessURI"
                                            controls
                                        ></audio>
                                    </div>

                                    <!-- DOCUMENT -->
                                    <a
                                        v-else
                                        :href="media.accessURI"
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        class="media-preview media-file text-decoration-none"
                                    >
                                        <i class="bi bi-file-earmark-text fs-1"></i>

                                        <span>Consulter le document</span>
                                    </a>

                                    <!-- LÉGENDES -->
                                    <div class="media-caption">

                                        <div
                                            v-if="media.caption"
                                            class="fw-semibold"
                                        >
                                            {{ media.caption }}
                                        </div>

                                        <!-- <div
                                            v-if="media.comments"
                                            class="text-body-secondary small"
                                        >
                                            {{ media.comments }}
                                        </div>

                                        <div
                                            v-if="media.captureDevice"
                                            class="text-body-secondary small mt-1"
                                        >
                                            <i class="bi bi-camera me-1"></i>
                                            {{ media.captureDevice }}
                                        </div> -->

                                    </div>

                                </div>

                            </div>

                            <!-- FLÈCHE GAUCHE -->
                            <button
                                v-if="medias.length > 3"
                                type="button"
                                class="btn btn-light btn-sm media-arrow media-arrow-left shadow-sm"
                                @click="previousMedias"
                                :disabled="mediaPosition === 0"
                                aria-label="Médias précédents"
                            >
                                <i class="bi bi-chevron-left"></i>
                            </button>

                            <!-- FLÈCHE DROITE -->
                            <button
                                v-if="medias.length > 3"
                                type="button"
                                class="btn btn-light btn-sm media-arrow media-arrow-right shadow-sm"
                                @click="nextMedias"
                                :disabled="mediaPosition >= medias.length - 3"
                                aria-label="Médias suivants"
                            >
                                <i class="bi bi-chevron-right"></i>
                            </button>

                        </div>

                        <!-- INDICATEUR -->
                        <div
                            v-if="medias.length > 3"
                            class="text-center text-body-secondary small mt-3"
                        >
                            {{ mediaPosition + 1 }}
                            –
                            {{ Math.min(mediaPosition + 3, medias.length) }}
                            sur {{ medias.length }} médias
                        </div>

                    </div>
                </div>

                <!-- MATÉRIEL ET ADN -->
                <div class="card">
                    <div class="card-header fw-semibold">
                        Matériel et ADN
                    </div>

                    <div class="card-body d-grid gap-3">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li>
                                <span class="text-body-secondary">Échantillon :</span>
                                MNHN, collection MAM, n° 2024-118
                            </li>

                            <li>
                                <span class="text-body-secondary">Analyse ADN :</span>
                                COI, 658 pb, 1 240 lectures
                            </li>
                        </ul>
                    </div>
                </div>

                <!-- RÉFÉRENCES -->
                <div class="card">
                    <div class="card-header fw-semibold">
                        Références
                    </div>

                    <div class="card-body d-grid gap-3">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li>
                                Auteur, 2019. Titre de la référence.
                            </li>
                        </ul>
                    </div>
                </div>

            </div>
        </div>

        <!-- EXPORTER -->
        <div class="d-flex flex-wrap gap-2">
            <a class="btn btn-primary" href="#" data-demo>
                Exporter
            </a>
        </div>

    </main>
</template>

<style scoped>

/* GALERIE */
.media-gallery {
    display: flex;
    gap: 12px;
    overflow: hidden;
}

/* Chaque média */
.media-item {
    flex: 0 0 calc((100% - 24px) / 3);
    min-width: 0;
}

/* Aperçu */
.media-preview {
    display: block;
    width: 100%;
    aspect-ratio: 1 / 1;
    border-radius: 8px;
    overflow: hidden;
    background: var(--bs-tertiary-bg);
}

/* Images */
.media-preview img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform .2s ease;
}

.media-preview:hover img {
    transform: scale(1.05);
}

/* Audio et documents */
.media-file {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 12px;
    color: var(--bs-secondary-color);
    padding: 12px;
}

.media-file audio {
    width: 100%;
    max-width: 100%;
}

/* Légendes */
.media-caption {
    padding: 8px 2px;
    font-size: .875rem;
    overflow-wrap: anywhere;
}

/* FLÈCHES */
.media-arrow {
    position: absolute;
    top: 35%;
    z-index: 2;
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0;
}

.media-arrow-left {
    left: -12px;
}

.media-arrow-right {
    right: -12px;
}

.media-arrow:disabled {
    opacity: .35;
}

/* MOBILE */
@media (max-width: 576px) {
    .media-item {
        flex: 0 0 calc((100% - 12px) / 2);
    }
}

</style>
