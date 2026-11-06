<script setup>
import { usePage } from '../../composables/usePage'
import { ref, watch, computed, onMounted } from "vue";
import { getItems } from "../../services/api";

usePage()

const props = defineProps({ idRecherche: { type: String, required: true } })

const loading = ref(false);
const error = ref("");
const medias = ref([]);
const total = ref(0);

const page = ref(1);
const perPage = ref(10);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / perPage.value)));

async function fetchMedias() {
    loading.value = true;
    error.value = "";
    try {
        const { items, total: t } = await getItems(
            `medias-recherche/mes-medias/${props.idRecherche}`,
            {},
            { page: page.value, perPage: perPage.value, sort: "mediaRechercheID,desc" }
        );
        medias.value = items;
        total.value = t;
    } catch (e) {
        console.error("Erreur lors du chargement des médias :", e);
        error.value = "Impossible de charger les médias.";
    } finally {
        loading.value = false;
    }
}

watch([page, perPage], fetchMedias);

onMounted(fetchMedias);
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/recherches/mes-recherches">Mes recherches</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/recherches/medias-de-la-recherche">M&#195;&#169;dias de la recherche</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>
        <ul class="nav nav-tabs">
            <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Recherches</router-link></li>
            <li class="nav-item"><router-link class="nav-link active"
                    to="/recherches/medias-de-la-recherche">Médias</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/collaborateurs">Collaborateurs</router-link></li>
            <li class="nav-item"><router-link class="nav-link"
                    to="/recherches/metadonnees-de-la-recherche">Métadonnées</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/these">Thèse</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/notes-personnelles">Notes</router-link></li>
        </ul>
        <div class="d-flex flex-wrap gap-2"><a class="btn btn-primary" href="#" data-demo>+ Ajouter des fichiers</a>
        </div>
        <div class="dropzone"><i class="bi bi-cloud-upload fs-2"></i>
            <div>Déposer ici photos, vidéos, documents</div><input type="file" multiple class="form-control mt-2"
                aria-label="Fichiers">
            <ul class="files list-unstyled small mb-0 mt-2"></ul>
        </div>
        <div class="card">
            <div class="card-header"><input type="search" form="nf" class="form-control form-control-sm table-filter"
                    placeholder="Filtrer..." aria-label="Filtrer"></div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" data-table>
                    <thead>
                        <tr>
                            <th scope="col">Aperçu</th>
                            <th scope="col">Type</th>
                            <th scope="col">Légende</th>
                            <th scope="col">Commentaire</th>
                            <th scope="col">Auteur</th>
                            <th scope="col">Date</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="media in medias" :key="media.mediaRechercheID">
                            <th><img :src="media.accessURI" :alt="media.caption" class="img-fluid" style="max-height: 100px;" width="50" height="50"></th>
                            <td>{{ media.typeMedia }}</td>
                            <td>{{ media.caption }}</td>
                            <td>{{ media.comments }}</td>
                            <td>{{ media.utilisateur.nom }} {{ media.utilisateur.prenom }}</td>
                            <td>{{ media.dateCreation }}</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    :to="`/recherches/mes-recherches/${media.mediaRechercheID}`">Ouvrir</router-link></td>
                        </tr>
                    </tbody>
                </table>
                <!-- Pagination -->
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
                    <small class="text-body-secondary">{{ total }} media(s)</small>

                    <div class="d-flex align-items-center gap-2">
                        <select
                        class="form-select form-select-sm w-auto"
                        v-model.number="perPage"
                        aria-label="Par page"
                        >
                        <option :value="5">5</option>
                        <option :value="10">10</option>
                        <option :value="20">20</option>
                        </select>

                        <button
                        type="button"
                        class="btn btn-sm btn-outline-secondary"
                        :disabled="page <= 1"
                        @click="page--"
                        >
                        Précédent
                        </button>
                        <span class="small">Page {{ page }} / {{ totalPages }}</span>
                        <button
                        type="button"
                        class="btn btn-sm btn-outline-secondary"
                        :disabled="page >= totalPages"
                        @click="page++"
                        >
                        Suivant
                        </button>
                    </div>
                </div>
            </div>
        </div>
    
  </main>
</template>
