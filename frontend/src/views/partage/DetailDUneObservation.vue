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

const page = ref(1);
const perPage = ref(10);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / perPage.value)));

const formatDate = (d) => (d ? new Date(d).toLocaleDateString("fr-FR") : "-");
const isAccepted = (identification) => identification?.isAcceptedIdentification === true;
async function fetchOccurrence() {
    try {
        occurrence.value = await getItemById("occurrences", idOccurrence);
    } catch(e) {
        console.error("Erreur lors du chargement de l'occurrence: ", e);
    }
}

const total = ref(0)

async function fetchIdentifications() {
    try {
        const { items, total: totalCount } = await getItems(
            "identifications",
            { 'occurrence.occurrenceID': idOccurrence },
            { page: page.value, perPage: perPage.value, sort: "dateIdentified,desc" }
        )
        identifications.value = items
        total.value = totalCount
    } catch (e) {
        console.error("Erreur lors du chargement des identifications: ", e)
    }
}

onMounted(() => {
    fetchOccurrence()
    fetchIdentifications()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/partage/detail-d-une-observation">D&#195;&#169;tail d&#39;une observation</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Propithecus verreauxi</h1>
        <p class="text-body-secondary mb-0">Observation n° 1042</p>
        <div class="row g-4">
            <div class="col-lg-4 d-grid gap-3 align-content-start">
                <div class="card">
                    <div class="card-header fw-semibold">Événement</div>
                    <div class="card-body d-grid gap-3">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li><span class="text-body-secondary">Date :</span> {{formatDate(occurrence?.event?.eventDate)}} </li>
                            <li><span class="text-body-secondary">Dataset :</span> {{occurrence?.event?.dataset?.datasetName}} </li>
                            <li><span class="text-body-secondary">Protocole :</span> {{occurrence?.event?.protocol?.protocolName}} </li>
                        </ul>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header fw-semibold">Lieu</div>
                    <div class="card-body d-grid gap-3">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li><span class="text-body-secondary">Pays :</span> {{occurrence?.event?.location?.province?.pays?.pays}} </li>
                            <li><span class="text-body-secondary">Province :</span> {{occurrence?.event?.location?.province?.province}} </li>
                            <li><span class="text-body-secondary">Commune :</span> {{occurrence?.event?.location?.municipality}} </li>
                            <li><span class="text-body-secondary">Coordonnées :</span> {{occurrence?.event?.location?.decimalLatitude}} ; {{occurrence?.event?.location?.decimalLongitude}}</li>
                        </ul>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header fw-semibold">Organisme et observation</div>
                    <div class="card-body d-grid gap-3">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li><span class="text-body-secondary">Portée :</span> {{occurrence?.organism?.organismScope}} </li>
                            <li><span class="text-body-secondary">Détection :</span> {{occurrence?.occurrenceStatus}} </li>
                            <li><span class="text-body-secondary">Type de donnée :</span> {{occurrence?.basisOfRecord}}</li>
                            <li><span class="text-body-secondary">Nombre :</span> {{occurrence?.individualCount}} </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-lg-8 d-grid gap-3 align-content-start">
                <div class="card">
                    <div class="card-header fw-semibold">Identifications</div>
                    <div class="card-body d-grid gap-3">
                        <div class="card">
                            <div class="card-header"><input type="search" form="nf"
                                    class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                                    aria-label="Filtrer"></div>
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
                                            <td colspan="5" class="text-center text-body-secondary">Aucune identification trouvée</td>
                                        </tr>
                                        <template v-else>
                                            <tr v-for="i in identifications" :key="i.identificationID">
                                                <td> {{i.taxon.scientificName}} </td>
                                                <td> {{formatDate(i.dateIdentified)}} </td>
                                                <td> {{i.agent.utilisateurId}} </td>
                                                <td> {{i.identificationQualifier}} </td>
                                                <td> {{isAccepted(i) ? 'Oui' : 'Non'}} </td>
                                                <td> <router-link
                                                    class="btn btn-sm btn-outline-primary"
                                                    :to="`/partage/fiche-taxon/${i.taxon.taxonID}`"
                                                    >Fiche Taxon</router-link> 
                                                </td>
                                            </tr>
                                        </template>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header fw-semibold">Mesures</div>
                    <div class="card-body d-grid gap-3">
                        <div class="card">
                            <div class="card-header"><input type="search" form="nf"
                                    class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                                    aria-label="Filtrer"></div>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0" data-table>
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
                <div class="card">
                    <div class="card-header fw-semibold">Médias</div>
                    <div class="card-body">
                        <div class="row g-2">
                            <div class="col-4">
                                <div class="ratio ratio-1x1 bg-body-secondary rounded">
                                    <div class="d-flex align-items-center justify-content-center text-body-secondary"><i
                                            class="bi bi-image fs-3"></i></div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="ratio ratio-1x1 bg-body-secondary rounded">
                                    <div class="d-flex align-items-center justify-content-center text-body-secondary"><i
                                            class="bi bi-image fs-3"></i></div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="ratio ratio-1x1 bg-body-secondary rounded">
                                    <div class="d-flex align-items-center justify-content-center text-body-secondary"><i
                                            class="bi bi-image fs-3"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header fw-semibold">Matériel et ADN</div>
                    <div class="card-body d-grid gap-3">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li><span class="text-body-secondary">Échantillon :</span> MNHN, collection MAM, n° 2024-118
                            </li>
                            <li><span class="text-body-secondary">Analyse ADN :</span> COI, 658 pb, 1 240 lectures</li>
                        </ul>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header fw-semibold">Références</div>
                    <div class="card-body d-grid gap-3">
                        <ul class="list-unstyled mb-0 d-grid gap-1">
                            <li>Auteur, 2019. Titre de la référence.</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><a class="btn btn-primary" href="#" data-demo>Exporter</a></div>
    
  </main>
</template>
