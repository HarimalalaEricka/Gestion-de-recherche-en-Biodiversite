<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getItems, getItemById } from '../../services/api'

usePage()


const route = useRoute()

const idTaxon = Number(route.params.idTaxon)
const taxon = ref(null)
const identifications = ref([])

const page = ref(1);
const perPage = ref(10);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / perPage.value)));

const formatDate = (d) => (d ? new Date(d).toLocaleDateString("fr-FR") : "-");
async function fetchTaxon() {
    try {
        taxon.value = await getItemById("taxons", idTaxon);
    } catch(e) {
        console.error("Erreur lors du chargement du taxon: ", e);
    }
}

const total = ref(0)

async function fetchIdentifications() {
    try {
        const { items, total: totalCount } = await getItems(
            "identifications",
            { 'taxon.taxonID': idTaxon },
            { page: page.value, perPage: perPage.value, sort: "dateIdentified,desc" }
        )
        identifications.value = items
        total.value = totalCount
    } catch (e) {
        console.error("Erreur lors du chargement des identifications: ", e)
    }
}

onMounted(() => {
    fetchTaxon()
    fetchIdentifications()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/partage/fiche-taxon">Fiche taxon</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0"> {{taxon?.scientificName}} </h1>
        <ul class="list-unstyled mb-0 d-grid gap-1">
            <li>
                <span class="text-body-secondary">Règne :</span>
                {{ taxon?.kingdom || '-' }}
            </li>
            <li>
                <span class="text-body-secondary">Embranchement :</span>
                {{ taxon?.phylum || '-' }}
            </li>
            <li>
                <span class="text-body-secondary">Classe :</span>
                {{ taxon?.taxonomicClass || '-' }}
            </li>
            <li>
                <span class="text-body-secondary">Ordre :</span>
                {{ taxon?.order || '-' }}
            </li>
            <li>
                <span class="text-body-secondary">Famille :</span>
                {{ taxon?.family || '-' }}
            </li>
            <li>
                <span class="text-body-secondary">Genre :</span>
                {{ taxon?.genus || '-' }}
            </li>
            <li>
                <span class="text-body-secondary">Nom scientifique :</span>
                {{ taxon?.scientificName || '-' }}
            </li>
        </ul>
        <div class="row row-cols-2 row-cols-lg-4 g-3">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">236</div>
                        <div class="text-body-secondary">observations</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">5</div>
                        <div class="text-body-secondary">datasets</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">12</div>
                        <div class="text-body-secondary">lieux</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">2019 – 2026</div>
                        <div class="text-body-secondary">période</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row g-4">
            <div class="col-lg-8 d-grid gap-3 align-content-start">
                <div class="map-ph">
                    <div class="text-center"><i class="bi bi-geo-alt fs-2"></i>
                        <div>Carte des observations</div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header fw-semibold">Observations</div>
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
                                            <th scope="col">Dataset</th>
                                            <th scope="col">Nombre</th>
                                            <th scope="col">Identifié par</th>
                                            <th scope="col"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>12/09/2026</td>
                                            <td>Parc national d&#x27;Andasibe</td>
                                            <td>Mammifères</td>
                                            <td>Rakoto Andry</td>
                                            <td>Rakoto Andry</td>
                                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                    to="/partage/detail-d-une-observation">Ouvrir</router-link></td>
                                        </tr>
                                        <tr>
                                            <td>03/09/2026</td>
                                            <td>Réserve de Berenty</td>
                                            <td>Reptiles</td>
                                            <td>Rasoa Voahangy</td>
                                            <td>Rasoa Voahangy</td>
                                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                    to="/partage/detail-d-une-observation">Ouvrir</router-link></td>
                                        </tr>
                                        <tr>
                                            <td>28/08/2026</td>
                                            <td>Forêt d&#x27;Ankarafantsika</td>
                                            <td>Ovipares</td>
                                            <td>Randria Tojo</td>
                                            <td>Randria Tojo</td>
                                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                    to="/partage/detail-d-une-observation">Ouvrir</router-link></td>
                                        </tr>
                                        <tr>
                                            <td>15/08/2026</td>
                                            <td>Parc national de Ranomafana</td>
                                            <td>Mammifères</td>
                                            <td>Razafy Mialy</td>
                                            <td>Razafy Mialy</td>
                                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                    to="/partage/detail-d-une-observation">Ouvrir</router-link></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 d-grid gap-3 align-content-start">
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
                                            <th scope="col">Min</th>
                                            <th scope="col">Max</th>
                                            <th scope="col">Unité</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>Poids</td>
                                            <td>1,9</td>
                                            <td>3,6</td>
                                            <td>kg</td>
                                        </tr>
                                        <tr>
                                            <td>Longueur du corps</td>
                                            <td>1,9</td>
                                            <td>3,6</td>
                                            <td>cm</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-secondary" href="#" data-demo>Modifier la fiche
                (chercheur principal)</a></div>
    
  </main>
</template>
