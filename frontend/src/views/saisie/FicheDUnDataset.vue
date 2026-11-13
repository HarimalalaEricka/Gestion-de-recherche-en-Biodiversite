<script setup>
import { usePage } from '../../composables/usePage'
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getItemById, getItems } from '../../services/api'

const route = useRoute()
const router = useRouter()

usePage()

const idDataset = route.params.idDataset
const dataset = ref(null)
const recherche = ref(null)
const createur = ref(null)

const totalevents = ref(0)
const events = ref([])
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
        console.error('Erreur chargement du dataset :', e)
    }
}

async function loadEvents() {
    try {
        const { items, total } = await getItems(`events/datasets/${idDataset}`)
        events.value = items
        totalevents.value = total
        console.log('Événements chargés :', events.value)
        console.log('Total des événements :', totalevents.value)
    } catch (e) {
        console.error('Erreur chargement des événements :', e)
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

onMounted(() => {
    loadDataset(),
    loadEvents()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/saisie/saisie-contexte">Saisie</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/fiche-d-un-dataset">Fiche d&#39;un dataset</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0"> {{ dataset?.datasetName || 'Chargement...' }} </h1>
        <ul class="list-unstyled mb-0 d-grid gap-1">
            <li><span class="text-body-secondary">Recherche :</span> {{ recherche?.sujet || 'Chargement...' }}</li>
            <li><span class="text-body-secondary">Créateur :</span> {{ createur?.nom || 'Chargement...' }} {{ createur?.prenom || 'Chargement...' }}</li>
            <li><span class="text-body-secondary">Licence :</span> {{ dataset?.licence?.licenceCode || 'Chargement...' }} </li>
            <li><span class="text-body-secondary">Détenteur des droits :</span> {{ dataset?.rightsHolder || 'Chargement...' }}</li>
            <li><span class="text-body-secondary">Protocoles :</span> Transect linéaire, Point d&#x27;écoute</li>
        </ul>
        <div class="row row-cols-2 row-cols-lg-3 g-3">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold"> {{ totalevents}} </div>
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
        <div class="card">
            <div class="card-header fw-semibold">Événements</div>
            <div class="card-body d-grid gap-3">
                <div class="card">
                    <div class="card-header"><input type="search" form="nf"
                            class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                            aria-label="Filtrer"></div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0" data-table>
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
                                <tr v-if="events.length === 0">
                                    <td colspan="5" class="text-center text-body-secondary">Aucun événement trouvé.</td>
                                </tr>
                                <tr v-for="event in events" :key="event.eventID">
                                    <td>{{ event.eventDate }}</td>
                                    <td>{{ event.location.municipality || 'N/A' }}</td>
                                    <td>{{ event.protocol?.protocolName || 'N/A' }}</td>
                                    <td>{{ event.observations?.length || 0 }}</td>
                                    <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                            :to="`/saisie/detail-d-un-evenement/${event.eventID}`">Ouvrir</router-link></td>
                                </tr>
                                <!-- <tr>
                                    <td>12/09/2026</td>
                                    <td>Parc national d&#x27;Andasibe</td>
                                    <td>Transect linéaire</td>
                                    <td>12</td>
                                    <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                            to="/saisie/detail-d-un-evenement">Ouvrir</router-link></td>
                                </tr>
                                <tr>
                                    <td>03/09/2026</td>
                                    <td>Réserve de Berenty</td>
                                    <td>Point d&#x27;écoute</td>
                                    <td>48</td>
                                    <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                            to="/saisie/detail-d-un-evenement">Ouvrir</router-link></td>
                                </tr>
                                <tr>
                                    <td>28/08/2026</td>
                                    <td>Forêt d&#x27;Ankarafantsika</td>
                                    <td>Piège photographique</td>
                                    <td>236</td>
                                    <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                            to="/saisie/detail-d-un-evenement">Ouvrir</router-link></td>
                                </tr>
                                <tr>
                                    <td>15/08/2026</td>
                                    <td>Parc national de Ranomafana</td>
                                    <td>Transect linéaire</td>
                                    <td>5</td>
                                    <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                            to="/saisie/detail-d-un-evenement">Ouvrir</router-link></td>
                                </tr> -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary"
                :to="`/saisie/saisie-contexte/${idDataset}`">Saisir</router-link><router-link class="btn btn-outline-secondary"
                to="/saisie/import-excel-csv">Importer Excel / CSV</router-link><a class="btn btn-outline-secondary"
                href="#" data-demo>Exporter CSV</a><a class="btn btn-outline-secondary" href="#" data-demo>Exporter
                Excel</a></div>
    
  </main>
</template>
