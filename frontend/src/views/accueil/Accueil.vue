<script setup>
import { usePage } from "../../composables/usePage";
import { ref, computed, watch, onMounted } from "vue";
import { getItems } from "../../services/api";

usePage();

/* ------------------------------------------------------------------ */
/* Statistiques (cartes du haut)                                       */
/* ------------------------------------------------------------------ */

const totalRecherches = ref(0);
const totalDatasets = ref(0);
const statsLoading = ref(false);

async function fetchStats() {
  statsLoading.value = true;

  try {
    // On ne demande qu'un élément : seul le total nous intéresse
    const [recherchesRes, datasetsRes] = await Promise.all([
      getItems("recherches/mes-recherches", {}, { page: 1, perPage: 1 }),
      getItems("datasets", { deleted: false }, { page: 1, perPage: 1 }),
    ]);

    totalRecherches.value = recherchesRes.total ?? 0;
    totalDatasets.value = datasetsRes.total ?? 0;

  } catch (e) {
    console.error("Erreur lors de la récupération des statistiques :", e);

    totalRecherches.value = 0;
    totalDatasets.value = 0;

  } finally {
    statsLoading.value = false;
  }
}

/* ------------------------------------------------------------------ */
/* Liste des recherches (pagination + filtre par statut)               */
/* ------------------------------------------------------------------ */

const recherches = ref([]);
const recherchesTotal = ref(0);
const recherchesLoading = ref(false);

const recherchePage = ref(1);
const recherchePerPage = ref(10);

const rechercheTotalPages = computed(() =>
  Math.max(1, Math.ceil(recherchesTotal.value / recherchePerPage.value))
);

const filtre = ref("ALL");

// Évite d'envoyer une requête au backend à chaque frappe
function debounce(fn, delay = 300) {
  let timer;

  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}

// Recherche par nom (sujet) : valeur saisie / valeur envoyée au backend
const rechercheSearch = ref("");
const rechercheQuery = ref("");

const applyRechercheQuery = debounce((v) => {
  rechercheQuery.value = v.trim();
});

watch(rechercheSearch, applyRechercheQuery);

const tabs = [
  { value: "ALL", label: "Tout" },
  { value: "PUBLIC", label: "Publiées" },
  { value: "PRIVATE", label: "Privées" },
];

const confidentialityLabels = {
  PRIVATE: "Privée",
  PUBLIC: "Publique",
};

const formatDate = (d) =>
  d ? new Date(d).toLocaleDateString("fr-FR") : "-";

let recherchesRequestId = 0;

async function fetchRecherches() {
  const requestId = ++recherchesRequestId;

  recherchesLoading.value = true;

  try {
    const filters = {};

    if (filtre.value !== "ALL") {
      filters.confidentialite = filtre.value;
    }

    if (rechercheQuery.value) {
      filters.sujet = rechercheQuery.value;
    }

    const { items, total: t } = await getItems(
      "recherches/mes-recherches",
      filters,
      {
        page: recherchePage.value,
        perPage: recherchePerPage.value,
        sort: "dateCreation,desc",
      }
    );

    if (requestId !== recherchesRequestId) return;

    // Page devenue vide : reculer d'une page
    if (items.length === 0 && recherchePage.value > 1) {
      recherchePage.value--;
      return;
    }

    recherches.value = items;
    recherchesTotal.value = t ?? 0;

  } catch (e) {
    if (requestId !== recherchesRequestId) return;

    console.error(
      "Erreur lors de la récupération des recherches :",
      e
    );

    recherches.value = [];
    recherchesTotal.value = 0;

  } finally {
    if (requestId === recherchesRequestId) {
      recherchesLoading.value = false;
    }
  }
}

watch([filtre, rechercheQuery], () => {
  if (recherchePage.value !== 1) {
    recherchePage.value = 1; // le watcher de recherchePage fera le fetch
  } else {
    fetchRecherches();
  }
});

watch(recherchePerPage, () => {
  if (recherchePage.value !== 1) {
    recherchePage.value = 1;
  } else {
    fetchRecherches();
  }
});

watch(recherchePage, fetchRecherches);

/* ------------------------------------------------------------------ */
/* Liste des datasets (pagination + filtre par nom)                    */
/* ------------------------------------------------------------------ */

const datasets = ref([]);
const datasetsTotal = ref(0);
const datasetsLoading = ref(false);

const datasetPage = ref(1);
const datasetPerPage = ref(10);

const datasetTotalPages = computed(() =>
  Math.max(1, Math.ceil(datasetsTotal.value / datasetPerPage.value))
);

// Recherche par nom : valeur saisie / valeur envoyée au backend
const datasetSearch = ref("");
const datasetQuery = ref("");

const applyDatasetQuery = debounce((v) => {
  datasetQuery.value = v.trim();
});

watch(datasetSearch, applyDatasetQuery);

let datasetsRequestId = 0;

async function fetchDatasets() {
  const requestId = ++datasetsRequestId;

  datasetsLoading.value = true;

  try {
    const filters = { deleted: false };

    if (datasetQuery.value) {
      filters.datasetName = datasetQuery.value;
    }

    const { items, total: t } = await getItems(
      "datasets",
      filters,
      { page: datasetPage.value, perPage: datasetPerPage.value }
    );

    if (requestId !== datasetsRequestId) return;

    // Page devenue vide : reculer d'une page
    if (items.length === 0 && datasetPage.value > 1) {
      datasetPage.value--;
      return;
    }

    datasets.value = items;
    datasetsTotal.value = t ?? 0;

  } catch (e) {
    if (requestId !== datasetsRequestId) return;

    console.error(
      "Erreur lors de la récupération des datasets :",
      e
    );

    datasets.value = [];
    datasetsTotal.value = 0;

  } finally {
    if (requestId === datasetsRequestId) {
      datasetsLoading.value = false;
    }
  }
}

watch(datasetPerPage, () => {
  if (datasetPage.value !== 1) {
    datasetPage.value = 1;
  } else {
    fetchDatasets();
  }
});

watch(datasetPage, fetchDatasets);

// Une nouvelle recherche par nom repart de la page 1
watch(datasetQuery, () => {
  if (datasetPage.value !== 1) {
    datasetPage.value = 1; // le watcher de datasetPage fera le fetch
  } else {
    fetchDatasets();
  }
});

/* ------------------------------------------------------------------ */

onMounted(() => {
  fetchStats();
  fetchRecherches();
  fetchDatasets();
});
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <h1 class="h3 mb-0">Bienvenue sur Nexora</h1>
    <p class="text-body-secondary mb-0">Voici où vous en êtes.</p>

    <div class="d-flex flex-wrap gap-2">
      <router-link class="btn btn-primary" to="/recherches/nouvelle-recherche">
        + Nouvelle recherche
      </router-link>
      <router-link class="btn btn-outline-secondary" to="/saisie/saisie-contexte">
        Nouvelle saisie
      </router-link>
      <router-link class="btn btn-outline-secondary" to="/saisie/import-excel-csv">
        Importer un fichier
      </router-link>
    </div>

    <!-- Statistiques -->
    <div class="row row-cols-2 row-cols-lg-4 g-3">
      <div class="col">
        <div class="card">
          <div class="card-body">
            <div class="fs-3 fw-semibold">
              {{ statsLoading ? "..." : totalRecherches }}
            </div>
            <div class="text-body-secondary">Recherches</div>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <div class="card-body">
            <div class="fs-3 fw-semibold">
              {{ statsLoading ? "..." : totalDatasets }}
            </div>
            <div class="text-body-secondary">Datasets</div>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <div class="card-body">
            <div class="fs-3 fw-semibold">5</div>
            <div class="text-body-secondary">Observations</div>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card border-warning">
          <div class="card-body">
            <div class="fs-3 fw-semibold">12</div>
            <div class="text-body-secondary">Saisies à valider</div>
          </div>
        </div>
      </div>
    </div>

    <div class="row g-4">

      <!-- Colonne gauche : datasets + recherches -->
      <div class="col-lg-8 d-grid gap-3 align-content-start">

        <!-- Liste des datasets -->
        <div class="card">
          <div class="card-header fw-semibold">Mes datasets</div>

          <div class="card-body d-grid gap-3">

            <input
              v-model="datasetSearch"
              type="search"
              class="form-control form-control-sm"
              placeholder="Filtrer par nom..."
              aria-label="Filtrer les datasets"
            />

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

                  <tr v-if="datasetsLoading">
                    <td colspan="3" class="text-center text-body-secondary">
                      Chargement...
                    </td>
                  </tr>

                  <tr v-else-if="datasets.length === 0">
                    <td colspan="3" class="text-center text-body-secondary">
                      Aucun dataset
                    </td>
                  </tr>

                  <template v-else>
                    <tr v-for="d in datasets" :key="d.datasetID">
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

            <!-- Pagination des datasets -->
            <div
              class="d-flex flex-wrap justify-content-between align-items-center gap-2"
            >
              <small class="text-body-secondary">
                {{ datasetsTotal }} dataset(s)
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
                  :disabled="datasetPage <= 1 || datasetsLoading"
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
                  :disabled="datasetPage >= datasetTotalPages || datasetsLoading"
                  @click="datasetPage++"
                >
                  Suivant
                </button>

              </div>
            </div>

          </div>
        </div>

        <!-- Liste des recherches -->
        <div class="card">
          <div class="card-header fw-semibold">Mes recherches</div>

          <div class="card-body d-grid gap-3">

            <input
              v-model="rechercheSearch"
              type="search"
              class="form-control form-control-sm"
              placeholder="Filtrer par nom..."
              aria-label="Filtrer les recherches"
            />

            <!-- Filtres -->
            <ul class="nav nav-tabs">
              <li v-for="t in tabs" :key="t.value" class="nav-item">
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

            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">

                <thead>
                  <tr>
                    <th scope="col">Recherche</th>
                    <th scope="col">Créée le</th>
                    <th scope="col">Statut</th>
                    <th scope="col"></th>
                  </tr>
                </thead>

                <tbody>

                  <tr v-if="recherchesLoading">
                    <td colspan="4" class="text-center text-body-secondary">
                      Chargement...
                    </td>
                  </tr>

                  <tr v-else-if="recherches.length === 0">
                    <td colspan="4" class="text-center text-body-secondary">
                      Aucune recherche trouvée
                    </td>
                  </tr>

                  <template v-else>
                    <tr v-for="r in recherches" :key="r.rechercheID">
                      <td>{{ r.sujet }}</td>
                      <td>{{ formatDate(r.dateCreation) }}</td>
                      <td>
                        <span
                          class="badge"
                          :class="{
                            'text-bg-success': r.confidentialite === 'PUBLIC',
                            'text-bg-secondary': r.confidentialite === 'PRIVATE',
                            'text-bg-warning': r.confidentialite === 'DRAFT',
                          }"
                        >
                          {{
                            confidentialityLabels[r.confidentialite]
                            || r.confidentialite
                          }}
                        </span>
                      </td>
                      <td class="text-end">
                        <router-link
                          class="btn btn-sm btn-outline-primary"
                          :to="`/recherches/medias-de-la-recherche/${r.rechercheID}`"
                        >
                          Ouvrir
                        </router-link>
                      </td>
                    </tr>
                  </template>

                </tbody>

              </table>
            </div>

            <!-- Pagination des recherches -->
            <div
              class="d-flex flex-wrap justify-content-between align-items-center gap-2"
            >
              <small class="text-body-secondary">
                {{ recherchesTotal }} recherche(s)
              </small>

              <div class="d-flex align-items-center gap-2">

                <select
                  class="form-select form-select-sm w-auto"
                  v-model.number="recherchePerPage"
                  aria-label="Recherches par page"
                >
                  <option :value="5">5</option>
                  <option :value="10">10</option>
                  <option :value="20">20</option>
                </select>

                <button
                  type="button"
                  class="btn btn-sm btn-outline-secondary"
                  :disabled="recherchePage <= 1 || recherchesLoading"
                  @click="recherchePage--"
                >
                  Précédent
                </button>

                <span class="small">
                  Page {{ recherchePage }} / {{ rechercheTotalPages }}
                </span>

                <button
                  type="button"
                  class="btn btn-sm btn-outline-secondary"
                  :disabled="recherchePage >= rechercheTotalPages || recherchesLoading"
                  @click="recherchePage++"
                >
                  Suivant
                </button>

              </div>
            </div>

          </div>
        </div>

      </div>

      <!-- Colonne droite -->
      <div class="col-lg-4 d-grid gap-3 align-content-start">

        <div class="card">
          <div class="card-header fw-semibold">Synchronisation</div>
          <div class="card-body d-grid gap-3">
            <ul class="list-unstyled mb-0 d-grid gap-1">
              <li>
                <span class="text-body-secondary">
                  Saisies enregistrées dans ce navigateur :
                </span>
                48
              </li>
            </ul>
            <div class="d-flex flex-wrap gap-2">
              <router-link
                class="btn btn-primary"
                to="/saisie/brouillons-hors-ligne"
              >
                Synchroniser
              </router-link>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-header fw-semibold">À valider</div>
          <div class="card-body d-grid gap-3">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead>
                  <tr>
                    <th scope="col">Agent</th>
                    <th scope="col">Dataset</th>
                    <th scope="col"></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>Rakoto Andry</td>
                    <td>Mammifères</td>
                    <td class="text-end">
                      <router-link
                        class="btn btn-sm btn-outline-primary"
                        to="/recherches/mes-recherches"
                      >
                        Ouvrir
                      </router-link>
                    </td>
                  </tr>
                  <tr>
                    <td>Rasoa Voahangy</td>
                    <td>Reptiles</td>
                    <td class="text-end">
                      <router-link
                        class="btn btn-sm btn-outline-primary"
                        to="/recherches/mes-recherches"
                      >
                        Ouvrir
                      </router-link>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-header fw-semibold">Notifications</div>
          <div class="card-body d-grid gap-3">
            <p class="text-body-secondary mb-0">
              Rien de nouveau pour l'instant.
            </p>
          </div>
        </div>

      </div>
    </div>

  </main>
</template>