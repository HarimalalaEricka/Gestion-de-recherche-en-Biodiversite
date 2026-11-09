<script setup>
import { usePage } from '../../composables/usePage'
import { ref, reactive, watch, onMounted, onBeforeUnmount } from 'vue'
import { patchItem, getItemById } from '../../services/api'

usePage()

const props = defineProps({
    idRecherche: {
        type: String,
        required: true
    }
})

const form = reactive({
    notesPersonnelles: ''
})

const saving = ref(false)
const error = ref('')
const saveStatus = ref('')
const loading = ref(true)

let debounceTimer = null

// Chargement des notes existantes
async function loadNotes() {
    loading.value = true

    try {
        const data = await getItemById(`recherches/${props.idRecherche}`)

        form.notesPersonnelles = data.notePerso || ''

    } catch (e) {
        console.error('Erreur chargement des notes :', e)
        error.value = 'Impossible de charger les notes personnelles.'
    } finally {
        loading.value = false
    }
}

// Sauvegarde des notes
async function saveNotes() {
    saving.value = true
    error.value = ''
    saveStatus.value = 'Enregistrement...'

    try {
        await patchItem(`recherches/${props.idRecherche}`, {
            notePerso: form.notesPersonnelles
        })

        saveStatus.value = 'Enregistré automatiquement'

    } catch (e) {
        console.error('Erreur lors de la sauvegarde :', e)
        error.value = 'Impossible d’enregistrer les notes personnelles.'
        saveStatus.value = 'Échec de l’enregistrement'
    } finally {
        saving.value = false
    }
}

// Détection des modifications
watch(
    () => form.notesPersonnelles,
    () => {
        if (loading.value) return

        clearTimeout(debounceTimer)

        saveStatus.value = 'Modifications non enregistrées'

        debounceTimer = setTimeout(() => {
            saveNotes()
        }, 1000)
    }
)

onMounted(() => {
    loadNotes()
})

onBeforeUnmount(() => {
    clearTimeout(debounceTimer)
})
</script>


<template>
    <main id="content" class="p-4 d-grid gap-4 align-content-start">

        <!-- Fil d'Ariane -->
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
                <li class="breadcrumb-item active" aria-current="page">
                    Notes personnelles
                </li>
            </ol>
        </nav>

        <!-- Formulaire -->
        <form class="needs-validation d-grid gap-4" @submit.prevent>

            <!-- Titre -->
            <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>

            <!-- Navigation -->
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
                        class="nav-link"
                        :to="`/recherches/these/${idRecherche}`"
                    >
                        Thèse
                    </router-link>
                </li>

                <li class="nav-item">
                    <router-link
                        class="nav-link active"
                        :to="`/recherches/notes-personnelles/${idRecherche}`"
                    >
                        Notes
                    </router-link>
                </li>
            </ul>

            <!-- Notes personnelles -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span class="fw-semibold">
                        <i class="bi bi-journal-text me-2"></i>
                        Notes personnelles
                    </span>

                    <!-- État de sauvegarde -->
                    <small class="text-body-secondary">

                        <span v-if="loading">
                            <i class="bi bi-hourglass-split me-1"></i>
                            Chargement...
                        </span>

                        <span v-else-if="saving">
                            <span class="spinner-border spinner-border-sm me-1"></span>
                            Enregistrement...
                        </span>

                        <span v-else-if="error" class="text-danger">
                            <i class="bi bi-exclamation-circle me-1"></i>
                            Échec de l'enregistrement
                        </span>

                        <span
                            v-else-if="saveStatus === 'Enregistré automatiquement'"
                            class="text-success"
                        >
                            <i class="bi bi-check-circle me-1"></i>
                            Enregistré
                        </span>

                        <span v-else>
                            {{ saveStatus }}
                        </span>

                    </small>
                </div>

                <div class="card-body d-grid gap-3">

                    <!-- Erreur -->
                    <div v-if="error" class="alert alert-danger mb-0">
                        <i class="bi bi-exclamation-triangle me-2"></i>
                        {{ error }}
                    </div>

                    <!-- Zone de texte -->
                    <div>
                        <label class="form-label" for="notes">
                            Mes notes
                        </label>

                        <textarea
                            v-model="form.notesPersonnelles"
                            class="form-control"
                            id="notes"
                            name="notes-personnelles"
                            rows="12"
                            placeholder="Écrivez vos notes personnelles ici..."
                            :disabled="loading"
                        ></textarea>
                    </div>

                    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                        <small class="text-body-secondary">
                            <i class="bi bi-lock me-1"></i>
                            Visibles par vous seul
                        </small>

                        <small class="text-body-secondary">
                            <i class="bi bi-cloud-check me-1"></i>
                            Enregistrement automatique
                        </small>
                    </div>

                </div>
            </div>

        </form>

    </main>
</template>
