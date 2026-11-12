<script setup>
import { usePage } from '../../composables/usePage'
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getItems, patchItem, request } from '../../services/api'

usePage()

const route = useRoute()
const router = useRouter()

const idDataset = route.params.idDataset

const form = reactive({
    datasetName: '',
    licenceID: '',
    rightsHolder: ''
})

const licences = ref([])
const loadingLicences = ref(false)
const loadingDataset = ref(false)
const loadFailed = ref(false)
const loading = ref(false)
const error = ref('')

async function loadLicences() {
    loadingLicences.value = true

    try {
        const { items } = await getItems('licences')
        licences.value = items || []
    } catch (e) {
        console.error('Erreur chargement des licences :', e)
        licences.value = []
    } finally {
        loadingLicences.value = false
    }
}

async function loadDataset() {
    loadingDataset.value = true
    loadFailed.value = false
    error.value = ''

    try {
        const res = await request(`datasets/${idDataset}`, { method: 'GET' })
        const dataset = res?.data ?? res

        if (!dataset?.datasetID) {
            throw new Error('Dataset introuvable.')
        }

        form.datasetName = dataset.datasetName ?? ''
        form.licenceID = dataset.licence?.licenceID ?? ''
        form.rightsHolder = dataset.rightsHolder ?? ''

    } catch (e) {
        console.error('Erreur chargement du dataset :', e)
        loadFailed.value = true
        error.value = 'Impossible de charger ce dataset.'

    } finally {
        loadingDataset.value = false
    }
}

async function updateDataset() {
    error.value = ''

    if (!form.datasetName.trim()) {
        error.value = 'Veuillez saisir le nom du dataset.'
        return
    }
    if (!idDataset) {
        error.value = 'Aucun dataset à modifier.'
        return
    }
    loading.value = true

    try {
        const changes = {
            datasetName: form.datasetName.trim(),
            rightsHolder: form.rightsHolder.trim()
        }

        // Le backend ignore les champs null : on n'envoie la licence que si elle est choisie
        if (form.licenceID) {
            changes.licence = { licenceID: Number(form.licenceID) }
        }

        await patchItem('datasets', changes, idDataset)

        router.push(`/saisie/fiche-d-un-dataset/${idDataset}`)

    } catch (e) {
        console.error('Erreur modification du dataset :', e)

        error.value =
            e.response?.data?.message ||
            e.message ||
            'Une erreur est survenue lors de la modification du dataset.'

    } finally {
        loading.value = false
    }
}

onMounted(() => {
    loadLicences()
    loadDataset()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
                <li class="breadcrumb-item"><router-link to="/recherches/mes-recherches">Mes recherches</router-link></li>
                <li class="breadcrumb-item active" aria-current="page">Modifier le dataset</li>
            </ol>
        </nav>

        <form class="needs-validation d-grid gap-4" @submit.prevent="updateDataset">
            <h1 class="h3 mb-0">Modifier le dataset</h1>

            <div v-if="error" class="alert alert-danger mb-0" role="alert">
                {{ error }}
            </div>

            <div v-if="loadingDataset" class="text-body-secondary">
                Chargement...
            </div>

            <fieldset class="d-grid gap-4" :disabled="loadingDataset || loadFailed">
                <div class="row g-3">
                    <div class="col-md">
                        <label class="form-label" for="f1">Nom du dataset<span class="text-danger"> *</span></label>
                        <input
                            class="form-control"
                            type="text"
                            id="f1"
                            name="datasetName"
                            v-model="form.datasetName"
                            placeholder="Ex: Mammifères"
                            required
                        >
                    </div>

                    <div class="col-md">
                        <label class="form-label" for="f2">Licence</label>

                        <select
                            class="form-select"
                            id="f2"
                            name="licence"
                            v-model="form.licenceID"
                        >
                            <option value="">Sélectionner une licence</option>

                            <option
                                v-for="licence in licences"
                                :key="licence.licenceID"
                                :value="licence.licenceID"
                            >
                                {{ licence.licenceCode }} - {{ licence.licenceName }}
                            </option>
                        </select>
                    </div>

                    <div class="col-md">
                        <label class="form-label" for="f3">Détenteur des droits</label>
                        <input
                            class="form-control"
                            type="text"
                            id="f3"
                            name="rightsHolder"
                            v-model="form.rightsHolder"
                        >
                    </div>
                </div>

                <div class="d-flex flex-wrap gap-2">
                    <router-link
                        class="btn btn-outline-secondary"
                        to="/recherches/mes-recherches"
                    >
                        Annuler
                    </router-link>

                    <button type="submit" class="btn btn-primary" :disabled="loading">
                        <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                        {{ loading ? 'Enregistrement...' : 'Enregistrer les modifications' }}
                    </button>
                </div>
            </fieldset>
        </form>
  </main>
</template>