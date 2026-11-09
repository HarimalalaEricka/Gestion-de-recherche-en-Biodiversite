
<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted, onUnmounted } from "vue";
import { getItems, getItemById, uploadMedia } from "../../services/api";

usePage();

const props = defineProps({
  idRecherche: {
    type: String,
    required: true
  }
});

// Recherche
const recherche = ref(null);

// Liste des documents
const loading = ref(false);
const error = ref("");
const medias = ref([]);
const total = ref(0);

const page = ref(1);
const perPage = ref(10);

const totalPages = computed(() =>
  Math.max(1, Math.ceil(total.value / perPage.value))
);

// Recherche et filtres
const terme = ref("");

const filtresActifs = computed(() =>
  terme.value.trim() !== ""
);

// Dépôt de documents
const fileInput = ref(null);
const queue = ref([]);
const uploading = ref(false);
const dragging = ref(false);
const uploadError = ref("");

const MAX_SIZE = 50 * 1024 * 1024;

const EXTENSIONS_AUTORISEES = [
  "pdf",
  "doc",
  "docx",
  "xls",
  "xlsx",
  "csv",
  "txt"
];

let uid = 0;

// Document ouvert
const viewer = ref(null);

const formatDate = (date) =>
  date ? new Date(date).toLocaleDateString("fr-FR") : "-";

const formatSize = (size) =>
  size < 1024 * 1024
    ? `${Math.round(size / 1024)} Ko`
    : `${(size / 1024 / 1024).toFixed(1)} Mo`;

const extension = (uri) =>
  (uri || "").split("?")[0].split(".").pop().toLowerCase();

function docIcon(uri) {
  const ext = extension(uri);

  if (ext === "pdf") return "bi-file-earmark-pdf";
  if (["doc", "docx"].includes(ext)) return "bi-file-earmark-word";
  if (["xls", "xlsx", "csv"].includes(ext)) return "bi-file-earmark-excel";

  return "bi-file-earmark-text";
}

const canPreviewDoc = (media) =>
  ["pdf", "txt"].includes(extension(media.accessURI));

// Chargement de la recherche
async function fetchRecherche() {
  try {
    recherche.value = await getItemById(
      "recherches",
      props.idRecherche
    );
  } catch (e) {
    console.error("Erreur de chargement de la recherche :", e);
  }
}

// Chargement des documents de la thèse
let requestId = 0;

async function fetchMedias() {
  const id = ++requestId;

  loading.value = true;
  error.value = "";

  try {
    const { items, total: t } = await getItems(
      "medias-recherche/mes-medias",
      {
        rechercheID: props.idRecherche,
        typeMedia: "DOCUMENT",
        typeMediaRecherche: "THESE",
        q: terme.value.trim()
      },
      {
        page: page.value,
        perPage: perPage.value,
        sort: "dateCreation,desc"
      }
    );

    if (id !== requestId) return;

    medias.value = items;
    total.value = t;

  } catch (e) {
    if (id !== requestId) return;

    console.error("Erreur de chargement des documents :", e);
    error.value = "Impossible de charger les documents.";

  } finally {
    if (id === requestId) loading.value = false;
  }
}

// Recherche avec délai
let searchTimer = null;

function applyFilters() {
  if (page.value !== 1) {
    page.value = 1;
  } else {
    fetchMedias();
  }
}

watch(terme, () => {
  clearTimeout(searchTimer);
  searchTimer = setTimeout(applyFilters, 300);
});

watch([page, perPage], fetchMedias);

function resetFilters() {
  clearTimeout(searchTimer);
  terme.value = "";
}

// Vérification des fichiers
function addFiles(fileList) {
  uploadError.value = "";

  for (const file of fileList) {
    const ext = file.name.split(".").pop().toLowerCase();

    if (!EXTENSIONS_AUTORISEES.includes(ext)) {
      uploadError.value =
        `${file.name} : format non autorisé. Seuls les documents sont acceptés.`;
      continue;
    }

    if (file.size > MAX_SIZE) {
      uploadError.value =
        `${file.name} dépasse la limite de 50 Mo.`;
      continue;
    }

    queue.value.push({
      id: ++uid,
      file,
      caption: file.name.replace(/\.[^.]+$/, ""),
      comments: "",
      status: "pending"
    });
  }
}

function onFileInput(event) {
  addFiles(event.target.files);
  event.target.value = "";
}

function onDrop(event) {
  dragging.value = false;
  addFiles(event.dataTransfer.files);
}

function removeFromQueue(id) {
  queue.value = queue.value.filter(item => item.id !== id);
}

// Envoi des documents
async function uploadAll() {
  uploading.value = true;
  uploadError.value = "";

  for (const item of queue.value) {
    if (item.status === "done") continue;

    item.status = "uploading";

    try {
      await uploadMedia(
        props.idRecherche,
        item.file,
        item.caption,
        item.comments,
        "THESE"
      );

      item.status = "done";

    } catch (e) {
      console.error("Erreur d'envoi :", e);
      item.status = "error";
    }
  }

  uploading.value = false;

  const echecs = queue.value.filter(
    item => item.status === "error"
  ).length;

  if (echecs) {
    uploadError.value =
      `${echecs} document(s) n'ont pas pu être envoyés. Tu peux réessayer.`;
  }

  queue.value = queue.value.filter(
    item => item.status !== "done"
  );

  if (page.value !== 1) {
    page.value = 1;
  } else {
    await fetchMedias();
  }
}

// Visualisation
function openViewer(media) {
  viewer.value = media;
}

function closeViewer() {
  viewer.value = null;
}

function onKeydown(event) {
  if (event.key === "Escape") {
    closeViewer();
  }
}

onMounted(() => {
  fetchRecherche();
  fetchMedias();

  window.addEventListener("keydown", onKeydown);
});

onUnmounted(() => {
  window.removeEventListener("keydown", onKeydown);
  clearTimeout(searchTimer);
  document.body.style.overflow = "";
});
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <router-link to="/accueil">Accueil</router-link>
        </li>
        <li class="breadcrumb-item">
          <router-link to="/recherches/mes-recherches">
            Mes recherches
          </router-link>
        </li>
        <li class="breadcrumb-item active">
          Documents de la thèse
        </li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">
      {{ recherche?.sujet || "Documents de la thèse" }}
    </h1>

    <ul class="nav nav-tabs">
      <li class="nav-item">
        <router-link class="nav-link" to="/recherches/mes-recherches">
          Recherches
        </router-link>
      </li>
      <li class="nav-item">
        <router-link
          class="nav-link"
          :to="`/recherches/medias-de-la-recherche/${idRecherche}`"
        >
          Médias
        </router-link>
      </li>
      <li class="nav-item">
        <router-link
          class="nav-link"
          :to="`/recherches/collaborateurs/${idRecherche}`"
        >
          Collaborateurs
        </router-link>
      </li>
      <li class="nav-item">
        <router-link
          class="nav-link"
          :to="`/recherches/metadonnees-de-la-recherche/${idRecherche}`"
        >
          Métadonnées
        </router-link>
      </li>
      <li class="nav-item">
        <router-link
          class="nav-link active"
          :to="`/recherches/these/${idRecherche}`"
        >
          Thèse
        </router-link>
      </li>
      <li class="nav-item">
        <router-link
          class="nav-link"
          :to="`/recherches/notes-personnelles/${idRecherche}`"
        >
          Notes
        </router-link>
      </li>
    </ul>

    <!-- Dépôt de documents -->
    <div
      class="upload-zone"
      :class="{ 'is-dragging': dragging }"
      role="button"
      tabindex="0"
      @click="fileInput.click()"
      @keydown.enter.prevent="fileInput.click()"
      @keydown.space.prevent="fileInput.click()"
      @dragover.prevent="dragging = true"
      @dragleave.prevent="dragging = false"
      @drop.prevent="onDrop"
    >
      <i class="bi bi-cloud-upload upload-icon"></i>

      <div class="fw-semibold">
        Glissez vos documents ici
      </div>

      <div class="text-body-secondary small mb-3">
        PDF, Word, Excel, CSV ou TXT · 50 Mo maximum
      </div>

      <span class="btn btn-outline-primary btn-sm">
        <i class="bi bi-folder2-open me-1"></i>
        Choisir des documents
      </span>

      <input
        ref="fileInput"
        type="file"
        multiple
        hidden
        accept=".pdf,.doc,.docx,.xls,.xlsx,.csv,.txt"
        @change="onFileInput"
      >
    </div>

    <div v-if="uploadError" class="alert alert-warning mb-0">
      {{ uploadError }}
    </div>

    <!-- Documents en attente -->
    <div v-if="queue.length" class="card">
      <div class="card-header fw-semibold">
        Documents à envoyer ({{ queue.length }})
      </div>

      <ul class="list-group list-group-flush">
        <li
          v-for="item in queue"
          :key="item.id"
          class="list-group-item"
        >
          <div class="d-flex justify-content-between align-items-start gap-2 mb-2">
            <div>
              <div class="fw-semibold">{{ item.file.name }}</div>
              <small class="text-body-secondary">
                {{ formatSize(item.file.size) }}
              </small>
            </div>

            <div class="d-flex align-items-center gap-2">
              <span
                v-if="item.status === 'uploading'"
                class="badge text-bg-secondary"
              >
                Envoi...
              </span>

              <span
                v-else-if="item.status === 'error'"
                class="badge text-bg-danger"
              >
                Échec
              </span>

              <button
                type="button"
                class="btn btn-sm btn-outline-danger"
                :disabled="uploading"
                @click="removeFromQueue(item.id)"
              >
                Retirer
              </button>
            </div>
          </div>

          <div class="row g-2">
            <div class="col-md-5">
              <input
                v-model="item.caption"
                type="text"
                class="form-control form-control-sm"
                placeholder="Légende"
                :disabled="uploading"
              >
            </div>

            <div class="col-md-7">
              <input
                v-model="item.comments"
                type="text"
                class="form-control form-control-sm"
                placeholder="Commentaire"
                :disabled="uploading"
              >
            </div>
          </div>
        </li>
      </ul>

      <div class="card-footer d-flex justify-content-end gap-2">
        <button
          type="button"
          class="btn btn-outline-secondary"
          :disabled="uploading"
          @click="queue = []"
        >
          Tout vider
        </button>

        <button
          type="button"
          class="btn btn-primary"
          :disabled="uploading"
          @click="uploadAll"
        >
          {{ uploading ? "Envoi en cours..." : "Envoyer" }}
        </button>
      </div>
    </div>

    <div v-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <!-- Liste des documents -->
    <div class="card">

      <div class="card-header d-flex flex-wrap align-items-center gap-2">
        <div class="input-group input-group-sm filter-search">
          <span class="input-group-text">
            <i class="bi bi-search"></i>
          </span>

          <input
            v-model="terme"
            type="search"
            class="form-control"
            placeholder="Légende, commentaire, auteur..."
          >
        </div>

        <button
          v-if="filtresActifs"
          type="button"
          class="btn btn-sm btn-outline-secondary"
          @click="resetFilters"
        >
          <i class="bi bi-x-circle me-1"></i>
          Réinitialiser
        </button>
      </div>

      <div class="table-responsive">
        <table
          class="table table-hover align-middle mb-0"
          :class="{ 'opacity-50': loading }"
        >
          <thead>
            <tr>
              <th>Document</th>
              <th>Légende</th>
              <th>Commentaire</th>
              <th>Auteur</th>
              <th>Date</th>
              <th></th>
            </tr>
          </thead>

          <tbody>
            <tr v-if="loading && medias.length === 0">
              <td colspan="6" class="text-center text-body-secondary">
                Chargement...
              </td>
            </tr>

            <tr v-else-if="medias.length === 0">
              <td colspan="6" class="text-center text-body-secondary">
                {{ filtresActifs
                  ? "Aucun document ne correspond à la recherche"
                  : "Aucun document pour cette thèse" }}
              </td>
            </tr>

            <tr
              v-for="media in medias"
              :key="media.mediaRechercheID"
            >
              <td>
                <div class="d-flex align-items-center gap-2">
                  <i
                    class="bi fs-3"
                    :class="docIcon(media.accessURI)"
                  ></i>

                  <span class="text-truncate" style="max-width: 180px">
                    {{ media.accessURI?.split('/').pop() }}
                  </span>
                </div>
              </td>

              <td>{{ media.caption }}</td>
              <td>{{ media.comments }}</td>

              <td>
                {{ media.utilisateur?.nom }}
                {{ media.utilisateur?.prenom }}
              </td>

              <td>{{ formatDate(media.dateCreation) }}</td>

              <td class="text-end">
                <button
                  type="button"
                  class="btn btn-sm btn-outline-primary"
                  @click="openViewer(media)"
                >
                  Voir
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div class="card-footer d-flex flex-wrap justify-content-between align-items-center gap-2">
        <small class="text-body-secondary">
          {{ total }} document(s)
        </small>

        <div class="d-flex align-items-center gap-2">
          <select
            class="form-select form-select-sm w-auto"
            v-model.number="perPage"
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

    <!-- Visualiseur de documents -->
    <Teleport to="body">
      <div
        v-if="viewer"
        class="viewer"
        role="dialog"
        aria-modal="true"
      >
        <div class="viewer-bar">
          <div class="viewer-title">
            {{ viewer.caption }}
          </div>

          <div class="d-flex gap-2">
            <a
              :href="viewer.accessURI"
              target="_blank"
              rel="noopener"
              class="btn btn-sm btn-light"
            >
              Ouvrir dans un onglet
            </a>

            <button
              type="button"
              class="btn btn-sm btn-light"
              @click="closeViewer"
            >
              Fermer
            </button>
          </div>
        </div>

        <div class="viewer-body" @click.self="closeViewer">
          <iframe
            v-if="canPreviewDoc(viewer)"
            :src="viewer.accessURI"
            class="viewer-frame"
            :title="viewer.caption"
          ></iframe>

          <div v-else class="text-center text-white">
            <i
              class="bi fs-1 d-block mb-2"
              :class="docIcon(viewer.accessURI)"
            ></i>

            <p>
              L'aperçu de ce document n'est pas disponible.
            </p>

            <a
              :href="viewer.accessURI"
              target="_blank"
              rel="noopener"
              class="btn btn-light"
            >
              Ouvrir le document
            </a>
          </div>
        </div>

        <div v-if="viewer.comments" class="viewer-caption">
          {{ viewer.comments }}
        </div>
      </div>
    </Teleport>

  </main>
</template>

<style scoped>
.upload-zone {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px dashed var(--bs-border-color);
  border-radius: 0.75rem;
  padding: 2rem 1rem;
  text-align: center;
  cursor: pointer;
  transition: border-color 0.15s, background-color 0.15s;
}

.upload-zone:hover,
.upload-zone:focus-visible {
  border-color: #2e7d5b;
  background-color: rgba(46, 125, 91, 0.05);
  outline: none;
}

.upload-zone.is-dragging {
  border-color: #2e7d5b;
  background-color: rgba(46, 125, 91, 0.1);
}

.upload-icon {
  font-size: 2.5rem;
  color: #2e7d5b;
  line-height: 1;
  margin-bottom: 0.5rem;
}

.filter-search {
  flex: 1 1 240px;
  max-width: 340px;
}

.viewer {
  position: fixed;
  inset: 0;
  z-index: 2000;
  display: flex;
  flex-direction: column;
  background: rgba(0, 0, 0, 0.88);
}

.viewer-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  padding: 0.75rem 1rem;
  color: #fff;
}

.viewer-title {
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.viewer-body {
  flex: 1;
  min-height: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 1rem 1rem;
}

.viewer-frame {
  width: 100%;
  height: 100%;
  border: 0;
  background: #fff;
  border-radius: 0.25rem;
}

.viewer-caption {
  padding: 0 1rem 1rem;
  color: #fff;
  text-align: center;
}
</style>
