<script setup>
import { usePage } from "../../composables/usePage";
import { ref, computed, watch, onMounted } from "vue";
import { getItems, patchItem, deleteItem } from "../../services/api";

usePage();

const recherches = ref([]);
const total = ref(0);
const loading = ref(false);

const page = ref(1);
const perPage = ref(10);

const totalPages = computed(() =>
  Math.max(1, Math.ceil(total.value / perPage.value))
);

const selected = ref(null);
const datasets = ref([]);
const detailLoading = ref(false);

// Pagination dédiée aux datasets
const totalDatasets = ref(0);
const datasetPage = ref(1);
const datasetPerPage = ref(10);

const datasetTotalPages = computed(() =>
  Math.max(1, Math.ceil(totalDatasets.value / datasetPerPage.value))
);

const changingStatus = ref(false);
const deleting = ref(false);

const totalCollaborateurs = ref(0);
const collaborateursLoading = ref(false);

let collaborateursRequestId = 0;
let detailRequestId = 0;

const confidentialityLabels = {
  PRIVATE: "Privée",
  PUBLIC: "Publique",
};

const formatDate = (d) =>
  d ? new Date(d).toLocaleDateString("fr-FR") : "-";

const filtre = ref("ALL");

const tabs = [
  { value: "ALL", label: "Tout" },
  { value: "PUBLIC", label: "Publiées" },
  { value: "PRIVATE", label: "Privées" },
];

// Sélection d'une recherche
async function selectRecherche(r) {
  selected.value = r;

  datasets.value = [];
  totalDatasets.value = 0;
  totalCollaborateurs.value = 0;

  fetchCollaborateurs(r.rechercheID);

  // Si on n'est pas déjà page 1, le watcher de datasetPage déclenchera le fetch
  if (datasetPage.value !== 1) {
    datasetPage.value = 1;
  } else {
    fetchDatasets();
  }
}

async function fetchDatasets() {
  if (!selected.value) return;

  const rechercheID = selected.value.rechercheID;
  const requestId = ++detailRequestId;

  detailLoading.value = true;

  try {
    const { items, total: t } = await getItems(
      "datasets",
      { rechercheID, deleted: false },
      { page: datasetPage.value, perPage: datasetPerPage.value }
    );

    if (requestId !== detailRequestId) return;

    // Page devenue vide (ex. dernier élément supprimé) : reculer d'une page
    if (items.length === 0 && datasetPage.value > 1) {
      datasetPage.value--;
      return;
    }

    datasets.value = items;
    totalDatasets.value = t ?? 0;

  } catch (e) {
    if (requestId !== detailRequestId) return;

    console.error(
      "Erreur lors de la récupération des datasets :",
      e
    );

    datasets.value = [];
    totalDatasets.value = 0;

  } finally {
    if (requestId === detailRequestId) {
      detailLoading.value = false;
    }
  }
}

async function fetchCollaborateurs(rechercheID) {
  const requestId = ++collaborateursRequestId;

  collaborateursLoading.value = true;

  try {
    const { total: t } = await getItems(
      "agent-recherches",
      { rechercheID },
      { page: 1, perPage: 1 }
    );

    if (requestId !== collaborateursRequestId) return;

    totalCollaborateurs.value = t ?? 0;

  } catch (e) {
    if (requestId !== collaborateursRequestId) return;

    console.error(
      "Erreur lors du chargement des collaborateurs :",
      e
    );

    totalCollaborateurs.value = 0;

  } finally {
    if (requestId === collaborateursRequestId) {
      collaborateursLoading.value = false;
    }
  }
}

async function fetchRecherches() {
  loading.value = true;

  try {
    const filters =
      filtre.value === "ALL"
        ? {}
        : { confidentialite: filtre.value };

    const { items, total: t } = await getItems(
      "recherches/mes-recherches",
      filters,
      {
        page: page.value,
        perPage: perPage.value,
        sort: "dateCreation,desc",
      }
    );

    if (items.length === 0 && page.value > 1) {
      page.value--;
      return;
    }

    recherches.value = items;
    total.value = t;

    const current = items.find(
      (i) => i.rechercheID === selected.value?.rechercheID
    );

    if (current) {
      selected.value = current;
    } else if (items.length) {
      await selectRecherche(items[0]);
    } else {
      selected.value = null;
      datasets.value = [];
      totalDatasets.value = 0;
      totalCollaborateurs.value = 0;

      detailRequestId++;
      collaborateursRequestId++;
    }

  } catch (e) {
    console.error(
      "Erreur lors de la récupération des recherches :",
      e
    );

  } finally {
    loading.value = false;
  }
}

function releaseFocus() {
  const el = document.activeElement;

  if (el && el !== document.body) {
    el.blur();
  }
}

// Changement de visibilité
async function changerVisibilite(nouvelleValeur) {
  releaseFocus();

  if (!selected.value) return;

  changingStatus.value = true;

  try {
    await patchItem(
      "recherches",
      { confidentialite: nouvelleValeur },
      selected.value.rechercheID
    );

    await fetchRecherches();

  } catch (e) {
    console.error(
      "Erreur lors du changement de visibilité :",
      e
    );

  } finally {
    changingStatus.value = false;
  }
}

const publier = () => changerVisibilite("PUBLIC");
const depublier = () => changerVisibilite("PRIVATE");

// Suppression
async function supprimer() {
  releaseFocus();

  if (!selected.value) return;

  deleting.value = true;

  try {
    await deleteItem(
      "recherches",
      selected.value.rechercheID
    );

    selected.value = null;
    datasets.value = [];
    totalDatasets.value = 0;
    totalCollaborateurs.value = 0;
    await fetchRecherches();
  } catch (e) {
    console.error(
      "Erreur lors de la suppression :",
      e
    );

  } finally {
    deleting.value = false;
  }
}

// Filtres et pagination des recherches
watch(filtre, () => {
  if (page.value !== 1) {
    page.value = 1;
  } else {
    fetchRecherches();
  }
});

watch([page, perPage], fetchRecherches);

// Pagination des datasets
watch(datasetPage, fetchDatasets);

watch(datasetPerPage, () => {
  if (datasetPage.value !== 1) {
    datasetPage.value = 1; // le watcher de datasetPage fera le fetch
  } else {
    fetchDatasets();
  }
});

onMounted(fetchRecherches);

</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <!-- Fil d'Ariane -->
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <router-link to="/accueil">Accueil</router-link>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Mes recherches
        </li>
      </ol>
    </nav>

    <div class="row g-4">

      <!-- Colonne gauche : liste -->
      <div class="col-lg-4 d-grid gap-3 align-content-start">

        <div class="d-flex flex-wrap gap-2">
          <router-link
            class="btn btn-primary"
            to="/recherches/nouvelle-recherche"
          >
            + Nouvelle recherche
          </router-link>
        </div>

        <!-- Filtres -->
        <ul class="nav nav-tabs">
          <li
            v-for="t in tabs"
            :key="t.value"
            class="nav-item"
          >
            <a
              class="nav-link"
              :class="{ active: filtre === t.value }"
              href="#"
              @click.prevent="filtre = t.value"
            >
              {{ t.label }}
            </a>
          </li>
        </ul>

        <!-- Liste des recherches -->
        <div class="list-group">

          <div
            v-if="loading"
            class="list-group-item text-center text-body-secondary"
          >
            Chargement...
          </div>

          <div
            v-else-if="recherches.length === 0"
            class="list-group-item text-center text-body-secondary"
          >
            Aucune recherche trouvée
          </div>

          <template v-else>
            <button
              v-for="r in recherches"
              :key="r.rechercheID"
              type="button"
              class="list-group-item list-group-item-action d-flex justify-content-between align-items-start gap-2 text-start"
              :class="{
                'is-selected':
                  selected?.rechercheID === r.rechercheID
              }"
              :aria-current="
                selected?.rechercheID === r.rechercheID
                  ? 'true'
                  : undefined
              "
              @click="selectRecherche(r)"
            >
              <div>
                <div class="fw-semibold">
                  {{ r.sujet }}
                </div>

                <small class="text-body-secondary">
                  {{ formatDate(r.dateCreation) }}
                </small>
              </div>

              <span
                class="badge"
                :class="{
                  'text-bg-success':
                    r.confidentialite === 'PUBLIC',
                  'text-bg-secondary':
                    r.confidentialite === 'PRIVATE',
                  'text-bg-warning':
                    r.confidentialite === 'DRAFT',
                }"
              >
                {{
                  confidentialityLabels[r.confidentialite]
                  || r.confidentialite
                }}
              </span>
            </button>
          </template>

        </div>

        <!-- Pagination des recherches -->
        <div
          class="d-flex flex-wrap justify-content-between align-items-center gap-2"
        >
          <small class="text-body-secondary">
            {{ total }} recherche(s)
          </small>

          <div class="d-flex align-items-center gap-2">

            <select
              class="form-select form-select-sm w-auto"
              v-model.number="perPage"
              aria-label="Recherches par page"
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

            <span class="small">
              Page {{ page }} / {{ totalPages }}
            </span>

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

      <!-- Colonne droite : détail -->
      <div class="col-lg-8 d-grid gap-3 align-content-start">

        <div
          v-if="!selected"
          class="text-body-secondary text-center p-5"
        >
          Sélectionnez une recherche pour voir ses détails
        </div>

        <template v-else>

          <h1 class="h3 mb-0">
            {{ selected.sujet }}
          </h1>

          <!-- Informations -->
          <ul class="list-unstyled mb-0 d-grid gap-1">

            <li>
              <span class="text-body-secondary">Statut :</span>
              {{
                confidentialityLabels[selected.confidentialite]
                || selected.confidentialite
              }}
            </li>

            <li>
              <span class="text-body-secondary">Créée le :</span>
              {{ formatDate(selected.dateCreation) }}
            </li>

            <li v-if="selected.description">
              <span class="text-body-secondary">Description :</span>
              {{ selected.description }}
            </li>

          </ul>

          <!-- Statistiques -->
          <div class="row row-cols-2 row-cols-lg-4 g-3">

            <!-- Datasets -->
            <div class="col">
              <div class="card">
                <div class="card-body">
                  <div class="fs-3 fw-semibold">
                    {{ totalDatasets }}
                  </div>
                  <div class="text-body-secondary">
                    datasets
                  </div>
                </div>
              </div>
            </div>

            <!-- Observations -->
            <div class="col">
              <div class="card">
                <div class="card-body">
                  <div class="fs-3 fw-semibold">
                    236
                  </div>
                  <div class="text-body-secondary">
                    observations
                  </div>
                </div>
              </div>
            </div>

            <!-- À valider -->
            <div class="col">
              <div class="card border-warning">
                <div class="card-body">
                  <div class="fs-3 fw-semibold">
                    5
                  </div>
                  <div class="text-body-secondary">
                    à valider
                  </div>
                </div>
              </div>
            </div>

            <!-- Collaborateurs -->
            <div class="col">
              <div class="card">
                <div class="card-body">
                  <div class="fs-3 fw-semibold">
                    {{
                      collaborateursLoading
                        ? "..."
                        : totalCollaborateurs
                    }}
                  </div>
                  <div class="text-body-secondary">
                    collaborateurs
                  </div>
                </div>
              </div>
            </div>

          </div>

          <!-- Datasets -->
          <div class="card">

            <div class="card-header fw-semibold d-flex justify-content-between align-items-center">
              <span>Datasets</span>

              <router-link
                class="btn btn-primary btn-sm"
                :to="`/saisie/nouveau-dataset/${selected.rechercheID}`"
              >
                + Nouveau dataset
              </router-link>
            </div>

            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">

                <thead>
                  <tr>
                    <th scope="col">Dataset</th>
                    <th scope="col">Observations</th>
                    <th scope="col"></th>
                  </tr>
                </thead>

                <tbody>

                  <tr v-if="detailLoading">
                    <td
                      colspan="3"
                      class="text-center text-body-secondary"
                    >
                      Chargement...
                    </td>
                  </tr>

                  <tr v-else-if="datasets.length === 0">
                    <td
                      colspan="3"
                      class="text-center text-body-secondary"
                    >
                      Aucun dataset
                    </td>
                  </tr>

                  <template v-else>
                    <tr
                      v-for="d in datasets"
                      :key="d.datasetID"
                    >
                      <td>{{ d.datasetName }}</td>
                      <td>{{ d.nbObservations }}</td>

                      <td class="text-end">
                        <router-link
                          class="btn btn-sm btn-outline-primary"
                          :to="`/saisie/fiche-d-un-dataset/${d.datasetID}`"
                        >
                          Ouvrir
                        </router-link>
                      </td>
                    </tr>
                  </template>

                </tbody>

              </table>
            </div>
          </div>

          <!-- Pagination des datasets -->
          <div
            class="d-flex flex-wrap justify-content-between align-items-center gap-2"
          >
            <small class="text-body-secondary">
              {{ totalDatasets }} dataset(s)
            </small>

            <div class="d-flex align-items-center gap-2">

              <select
                class="form-select form-select-sm w-auto"
                v-model.number="datasetPerPage"
                aria-label="Datasets par page"
              >
                <option :value="5">5</option>
                <option :value="10">10</option>
                <option :value="20">20</option>
              </select>

              <button
                type="button"
                class="btn btn-sm btn-outline-secondary"
                :disabled="datasetPage <= 1 || detailLoading"
                @click="datasetPage--"
              >
                Précédent
              </button>

              <span class="small">
                Page {{ datasetPage }} / {{ datasetTotalPages }}
              </span>

              <button
                type="button"
                class="btn btn-sm btn-outline-secondary"
                :disabled="datasetPage >= datasetTotalPages || detailLoading"
                @click="datasetPage++"
              >
                Suivant
              </button>

            </div>
          </div>

          <!-- Actions -->
          <div class="d-flex flex-wrap gap-2">

            <router-link
              class="btn btn-primary"
              :to="`/recherches/medias-de-la-recherche/${selected.rechercheID}`"
            >
              Ouvrir la recherche
            </router-link>

            <router-link
              class="btn btn-outline-secondary"
              :to="`/recherches/modifier-une-recherche/${selected.rechercheID}`"
            >
              Modifier
            </router-link>

            <router-link
              class="btn btn-outline-secondary"
              :to="`/recherches/medias-de-la-recherche/${selected.rechercheID}`"
            >
              Médias
            </router-link>

            <router-link
              class="btn btn-outline-secondary"
              :to="`/recherches/these/${selected.rechercheID}`"
            >
              Thèse
            </router-link>

            <!-- Publier -->
            <button
              v-if="selected.confidentialite === 'PRIVATE'"
              type="button"
              class="btn btn-success"
              data-confirm="Voulez-vous vraiment publier cette recherche ?"
              data-live
              :disabled="changingStatus || deleting"
              @confirmed="publier"
            >
              {{ changingStatus ? "Publication..." : "Publier" }}
            </button>

            <!-- Dépublier -->
            <button
              v-if="selected.confidentialite === 'PUBLIC'"
              type="button"
              class="btn btn-outline-danger"
              data-confirm="Voulez-vous vraiment dépublier cette recherche ?"
              data-live
              :disabled="changingStatus || deleting"
              @confirmed="depublier"
            >
              {{
                changingStatus
                  ? "Dépublication..."
                  : "Dépublier"
              }}
            </button>

            <!-- Supprimer -->
            <button
              type="button"
              class="btn btn-danger"
              data-confirm="Voulez-vous vraiment supprimer cette recherche ? Cette action est irréversible."
              data-live
              :disabled="changingStatus || deleting"
              @confirmed="supprimer"
            >
              {{ deleting ? "Suppression..." : "Supprimer" }}
            </button>

          </div>

        </template>
      </div>
    </div>
  </main>
</template>