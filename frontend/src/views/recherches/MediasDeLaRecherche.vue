<script setup>
import { usePage } from '../../composables/usePage'
import { ref, watch, computed, onMounted, onUnmounted } from "vue";
import { getItems, getItemById, uploadMedia } from "../../services/api";

usePage()

const props = defineProps({ idRecherche: { type: String, required: true } })

// Recherche (titre de la page)
const recherche = ref(null);

// Liste des médias
const loading = ref(false);
const error = ref("");
const medias = ref([]);
const total = ref(0);

const page = ref(1);
const perPage = ref(10);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / perPage.value)));

// Filtres (appliqués côté serveur)
const terme = ref("");        // texte : légende, commentaire, auteur
const typeFiltre = ref("");   // "" = tous les types
const typeOptions = [
  { value: "", label: "Tous les types" },
  { value: "IMAGE", label: "Images" },
  { value: "VIDEO", label: "Vidéos" },
  { value: "AUDIO", label: "Audio" },
  { value: "DOCUMENT", label: "Documents" },
];
const filtresActifs = computed(() => terme.value.trim() !== "" || typeFiltre.value !== "");

// Envoi de fichiers
const fileInput = ref(null);
const queue = ref([]);          // fichiers en attente
const uploading = ref(false);
const dragging = ref(false);
const uploadError = ref("");
const MAX_SIZE = 50 * 1024 * 1024; // 50 Mo
let uid = 0;

// Visualiseur plein écran
const viewer = ref(null);        // média actuellement ouvert
const audioSrc = ref(null);      // source de l'audio (blob en mémoire)
const audioLoading = ref(false);

const formatDate = (d) => (d ? new Date(d).toLocaleDateString("fr-FR") : "-");

const formatSize = (n) =>
  n < 1024 * 1024 ? `${Math.round(n / 1024)} Ko` : `${(n / 1024 / 1024).toFixed(1)} Mo`;

const typeLabels = {
  IMAGE: "Image",
  VIDEO: "Vidéo",
  AUDIO: "Audio",
  DOCUMENT: "Document",
};

const extension = (uri) => (uri || "").split("?")[0].split(".").pop().toLowerCase();

// Icône selon l'extension du document
function docIcon(uri) {
  const e = extension(uri);
  if (e === "pdf") return "bi-file-earmark-pdf";
  if (["doc", "docx"].includes(e)) return "bi-file-earmark-word";
  if (["xls", "xlsx", "csv"].includes(e)) return "bi-file-earmark-excel";
  return "bi-file-earmark-text";
}

// Documents qu'un navigateur sait afficher sans téléchargement
const canPreviewDoc = (m) =>
  m.typeMedia === "DOCUMENT" && ["pdf", "txt"].includes(extension(m.accessURI));

// ---------- Visualiseur ----------

// Libère le blob audio de la mémoire
function releaseAudio() {
  if (audioSrc.value && audioSrc.value.startsWith("blob:")) {
    URL.revokeObjectURL(audioSrc.value);
  }
  audioSrc.value = null;
  audioLoading.value = false;
}

// Charge l'audio en mémoire pour que la barre de progression soit toujours déplaçable
async function loadAudio(m) {
  audioLoading.value = true;
  audioSrc.value = null;
  try {
    const res = await fetch(m.accessURI);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const blob = await res.blob();
    // Le visualiseur a pu être fermé ou changé pendant le téléchargement
    if (viewer.value?.mediaRechercheID !== m.mediaRechercheID) return;
    audioSrc.value = URL.createObjectURL(blob);
  } catch (e) {
    console.error("Chargement audio en mémoire impossible, lecture directe :", e);
    if (viewer.value?.mediaRechercheID === m.mediaRechercheID) {
      audioSrc.value = m.accessURI; // repli : lecture directe
    }
  } finally {
    audioLoading.value = false;
  }
}

function openViewer(m) {
  releaseAudio();
  viewer.value = m;
  if (m.typeMedia === "AUDIO") loadAudio(m);
}

function closeViewer() {
  releaseAudio();
  viewer.value = null;
}

function onKeydown(e) {
  if (e.key === "Escape") closeViewer();
}

// Bloque le défilement de la page derrière le visualiseur
watch(viewer, (v) => {
  document.body.style.overflow = v ? "hidden" : "";
});

// ---------- Chargement des données ----------

async function fetchRecherche() {
  try {
    recherche.value = await getItemById("recherches", props.idRecherche);
  } catch (e) {
    console.error("Erreur lors du chargement de la recherche :", e);
  }
}

let requestId = 0; // seule la dernière requête met à jour l'écran

async function fetchMedias() {
  const id = ++requestId;
  loading.value = true;
  error.value = "";
  try {
    const { items, total: t } = await getItems(
      "medias-recherche/mes-medias",
      {
        rechercheID: props.idRecherche,
        typeMedia: typeFiltre.value,     // "" est ignoré par getItems
        q: terme.value.trim(),
      },
      { page: page.value, perPage: perPage.value, sort: "dateCreation,desc" }
    );
    if (id !== requestId) return;
    medias.value = items;
    total.value = t;
  } catch (e) {
    if (id !== requestId) return;
    console.error("Erreur lors du chargement des médias :", e);
    error.value = "Impossible de charger les médias.";
  } finally {
    if (id === requestId) loading.value = false;
  }
}

// ---------- Filtres ----------

let searchTimer = null;

function applyFilters() {
  if (page.value !== 1) page.value = 1; // le watch sur la page recharge
  else fetchMedias();
}

// Recherche texte : on attend 300 ms après la dernière frappe
watch(terme, () => {
  clearTimeout(searchTimer);
  searchTimer = setTimeout(applyFilters, 300);
});

watch(typeFiltre, applyFilters);

function resetFilters() {
  clearTimeout(searchTimer);
  terme.value = "";
  typeFiltre.value = "";
}

watch([page, perPage], fetchMedias);

// ---------- Envoi de fichiers ----------

function addFiles(fileList) {
  uploadError.value = "";
  for (const file of fileList) {
    if (file.size > MAX_SIZE) {
      uploadError.value = `« ${file.name} » dépasse 50 Mo et a été ignoré.`;
      continue;
    }
    queue.value.push({
      id: ++uid,
      file,
      caption: file.name.replace(/\.[^.]+$/, ""),
      comments: "",
      status: "pending", // pending | uploading | done | error
    });
  }
}

function onFileInput(e) {
  addFiles(e.target.files);
  e.target.value = ""; // permet de re-sélectionner le même fichier
}

function onDrop(e) {
  dragging.value = false;
  addFiles(e.dataTransfer.files);
}

function removeFromQueue(id) {
  queue.value = queue.value.filter((i) => i.id !== id);
}

async function uploadAll() {
  uploading.value = true;
  uploadError.value = "";

  for (const item of queue.value) {
    if (item.status === "done") continue;
    item.status = "uploading";
    try {
      await uploadMedia(props.idRecherche, item.file, item.caption, item.comments);
      item.status = "done";
    } catch (e) {
      console.error("Erreur d'envoi :", e);
      item.status = "error";
    }
  }

  uploading.value = false;

  const echecs = queue.value.filter((i) => i.status === "error").length;
  if (echecs) uploadError.value = `${echecs} fichier(s) n'ont pas pu être envoyés. Tu peux réessayer.`;

  // On garde seulement les fichiers en échec dans la liste d'attente
  queue.value = queue.value.filter((i) => i.status !== "done");

  if (page.value !== 1) page.value = 1; // le watch recharge
  else await fetchMedias();
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
  releaseAudio();
});
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link to="/recherches/mes-recherches">Mes recherches</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">Médias de la recherche</li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">{{ recherche?.sujet || "Médias de la recherche" }}</h1>

    <ul class="nav nav-tabs">
      <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Recherches</router-link></li>
      <li class="nav-item">
        <router-link class="nav-link active" :to="`/recherches/medias-de-la-recherche/${idRecherche}`">Médias</router-link>
      </li>
      <li class="nav-item"><router-link class="nav-link" :to="`/recherches/collaborateurs/${idRecherche}`">Collaborateurs</router-link></li>
      <li class="nav-item">
        <router-link class="nav-link" :to="`/recherches/metadonnees-de-la-recherche/${idRecherche}`">Métadonnées</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :to="`/recherches/these/${idRecherche}`">Thèse</router-link>
      </li>
      <li class="nav-item"><router-link class="nav-link" :to="`/recherches/notes-personnelles/${idRecherche}`">Notes</router-link></li>
    </ul>

    <!-- Zone de dépôt (classe "upload-zone" et non "dropzone" : le script du template ne s'y branche pas) -->
    <div
      class="upload-zone"
      :class="{ 'is-dragging': dragging }"
      role="button"
      tabindex="0"
      aria-label="Choisir des fichiers à envoyer"
      @click="fileInput.click()"
      @keydown.enter.prevent="fileInput.click()"
      @keydown.space.prevent="fileInput.click()"
      @dragover.prevent="dragging = true"
      @dragleave.prevent="dragging = false"
      @drop.prevent="onDrop"
    >
      <i class="bi bi-cloud-upload upload-icon"></i>
      <div class="fw-semibold">Glissez vos fichiers ici</div>
      <div class="text-body-secondary small mb-3">
        ou cliquez pour parcourir · photos, vidéos, sons, documents · 50 Mo maximum par fichier
      </div>
      <span class="btn btn-outline-primary btn-sm">
        <i class="bi bi-folder2-open me-1"></i>Choisir des fichiers
      </span>

      <input
        ref="fileInput"
        type="file"
        multiple
        hidden
        aria-hidden="true"
        tabindex="-1"
        @change="onFileInput"
      >
    </div>

    <div v-if="uploadError" class="alert alert-warning mb-0">{{ uploadError }}</div>

    <!-- Fichiers en attente d'envoi -->
    <div v-if="queue.length" class="card">
      <div class="card-header fw-semibold">Fichiers à envoyer ({{ queue.length }})</div>
      <ul class="list-group list-group-flush">
        <li v-for="item in queue" :key="item.id" class="list-group-item">
          <div class="d-flex justify-content-between align-items-start gap-2 mb-2">
            <div>
              <div class="fw-semibold">{{ item.file.name }}</div>
              <small class="text-body-secondary">{{ formatSize(item.file.size) }}</small>
            </div>
            <div class="d-flex align-items-center gap-2">
              <span v-if="item.status === 'uploading'" class="badge text-bg-secondary">Envoi...</span>
              <span v-else-if="item.status === 'error'" class="badge text-bg-danger">Échec</span>
              <button
                type="button"
                class="btn btn-sm btn-outline-danger"
                :disabled="uploading"
                @click="removeFromQueue(item.id)"
              >Retirer</button>
            </div>
          </div>
          <div class="row g-2">
            <div class="col-md-5">
              <input v-model="item.caption" type="text" class="form-control form-control-sm"
                     placeholder="Légende" aria-label="Légende" :disabled="uploading">
            </div>
            <div class="col-md-7">
              <input v-model="item.comments" type="text" class="form-control form-control-sm"
                     placeholder="Commentaire" aria-label="Commentaire" :disabled="uploading">
            </div>
          </div>
        </li>
      </ul>
      <div class="card-footer d-flex justify-content-end gap-2">
        <button type="button" class="btn btn-outline-secondary" :disabled="uploading" @click="queue = []">
          Tout vider
        </button>
        <button type="button" class="btn btn-primary" :disabled="uploading" @click="uploadAll">
          {{ uploading ? "Envoi en cours..." : "Envoyer" }}
        </button>
      </div>
    </div>

    <div v-if="error" class="alert alert-danger mb-0" role="alert">{{ error }}</div>

    <!-- Liste des médias -->
    <div class="card">
      <!-- Filtres -->
      <div class="card-header d-flex flex-wrap align-items-center gap-2">
        <div class="input-group input-group-sm filter-search">
          <span class="input-group-text"><i class="bi bi-search"></i></span>
          <input
            v-model="terme"
            type="search"
            class="form-control"
            placeholder="Légende, commentaire, auteur..."
            aria-label="Rechercher"
          >
        </div>

        <select
          v-model="typeFiltre"
          class="form-select form-select-sm w-auto"
          aria-label="Filtrer par type de média"
        >
          <option v-for="o in typeOptions" :key="o.value" :value="o.value">{{ o.label }}</option>
        </select>

        <button
          v-if="filtresActifs"
          type="button"
          class="btn btn-sm btn-outline-secondary"
          @click="resetFilters"
        >
          <i class="bi bi-x-circle me-1"></i>Réinitialiser
        </button>
      </div>

      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0" :class="{ 'opacity-50': loading }">
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
            <tr v-if="loading && medias.length === 0">
              <td colspan="7" class="text-center text-body-secondary">Chargement...</td>
            </tr>
            <tr v-else-if="medias.length === 0">
              <td colspan="7" class="text-center text-body-secondary">
                {{ filtresActifs ? "Aucun média ne correspond à ces filtres" : "Aucun média" }}
              </td>
            </tr>
            <template v-else>
              <tr v-for="media in medias" :key="media.mediaRechercheID">
                <td style="width: 130px;">
                  <!-- IMAGE : miniature cliquable -->
                  <button
                    v-if="media.typeMedia === 'IMAGE'"
                    type="button"
                    class="thumb"
                    :title="'Agrandir : ' + media.caption"
                    @click="openViewer(media)"
                  >
                    <img :src="media.accessURI" :alt="media.caption">
                  </button>

                  <!-- VIDEO : première image + bouton lecture -->
                  <button
                    v-else-if="media.typeMedia === 'VIDEO'"
                    type="button"
                    class="thumb"
                    :title="'Lire : ' + media.caption"
                    @click="openViewer(media)"
                  >
                    <video :src="media.accessURI + '#t=0.5'" muted preload="metadata"></video>
                    <i class="bi bi-play-circle-fill thumb-play"></i>
                  </button>

                  <!-- AUDIO : s'écoute dans le visualiseur -->
                  <button
                    v-else-if="media.typeMedia === 'AUDIO'"
                    type="button"
                    class="thumb thumb-audio"
                    :title="'Écouter : ' + media.caption"
                    @click="openViewer(media)"
                  >
                    <i class="bi bi-music-note-beamed thumb-play"></i>
                  </button>

                  <!-- DOCUMENT -->
                  <button
                    v-else
                    type="button"
                    class="btn btn-link p-0 border-0 d-inline-flex flex-column align-items-center text-decoration-none"
                    :title="media.caption"
                    @click="openViewer(media)"
                  >
                    <i class="bi fs-1" :class="docIcon(media.accessURI)"></i>
                    <small>{{ canPreviewDoc(media) ? "Aperçu" : "Ouvrir" }}</small>
                  </button>
                </td>
                <td>{{ typeLabels[media.typeMedia] || media.typeMedia }}</td>
                <td>{{ media.caption }}</td>
                <td>{{ media.comments }}</td>
                <td>{{ media.utilisateur?.nom }} {{ media.utilisateur?.prenom }}</td>
                <td>{{ formatDate(media.dateCreation) }}</td>
                <td class="text-end">
                  <button
                    type="button"
                    class="btn btn-sm btn-outline-primary"
                    @click="openViewer(media)"
                  >{{ media.typeMedia === 'AUDIO' ? "Écouter" : "Voir" }}</button>
                </td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div class="card-footer d-flex flex-wrap justify-content-between align-items-center gap-2">
        <small class="text-body-secondary">{{ total }} média(s)</small>

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

    <!-- Visualiseur plein écran -->
    <Teleport to="body">
      <div
        v-if="viewer"
        class="viewer"
        role="dialog"
        aria-modal="true"
        :aria-label="viewer.caption"
      >
        <div class="viewer-bar">
          <div class="viewer-title">{{ viewer.caption }}</div>
          <div class="d-flex gap-2">
            <a
              :href="viewer.accessURI"
              target="_blank"
              rel="noopener"
              class="btn btn-sm btn-light"
            >Ouvrir dans un onglet</a>
            <button type="button" class="btn btn-sm btn-light" @click="closeViewer">Fermer</button>
          </div>
        </div>

        <div class="viewer-body" @click.self="closeViewer">
          <img
            v-if="viewer.typeMedia === 'IMAGE'"
            :src="viewer.accessURI"
            :alt="viewer.caption"
            class="viewer-media"
          >

          <video
            v-else-if="viewer.typeMedia === 'VIDEO'"
            :src="viewer.accessURI"
            class="viewer-media"
            controls
            autoplay
            playsinline
          ></video>

          <!-- AUDIO -->
          <div v-else-if="viewer.typeMedia === 'AUDIO'" class="text-center text-white">
            <i class="bi bi-music-note-beamed d-block mb-3" style="font-size: 4rem;"></i>
            <div v-if="audioLoading" class="mb-2">Chargement de l'audio...</div>
            <audio
              v-else-if="audioSrc"
              :src="audioSrc"
              controls
              autoplay
              preload="auto"
              style="width: min(600px, 90vw);"
            ></audio>
            <div v-else class="mb-2">Impossible de charger l'audio.</div>
          </div>

          <iframe
            v-else-if="canPreviewDoc(viewer)"
            :src="viewer.accessURI"
            class="viewer-frame"
            :title="viewer.caption"
          ></iframe>

          <div v-else class="text-center text-white">
            <i class="bi fs-1 d-block mb-2" :class="docIcon(viewer.accessURI)"></i>
            <p>Ce type de fichier ne peut pas s'afficher dans le navigateur.</p>
            <a :href="viewer.accessURI" target="_blank" rel="noopener" class="btn btn-light">Ouvrir le fichier</a>
          </div>
        </div>

        <div v-if="viewer.comments" class="viewer-caption">{{ viewer.comments }}</div>
      </div>
    </Teleport>
  </main>
</template>

<style scoped>
/* Zone de dépôt */
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

/* Filtres */
.filter-search {
  flex: 1 1 240px;
  max-width: 340px;
}

/* Miniatures */
.thumb {
  position: relative;
  display: inline-block;
  width: 110px;
  height: 80px;
  padding: 0;
  border: 0;
  border-radius: 0.375rem;
  overflow: hidden;
  background: #000;
  cursor: zoom-in;
}

.thumb img,
.thumb video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.thumb-audio {
  background: #2e7d5b;
  cursor: pointer;
}

.thumb-play {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  color: #fff;
  text-shadow: 0 0 6px rgba(0, 0, 0, 0.6);
}

/* Visualiseur plein écran */
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

.viewer-media {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
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