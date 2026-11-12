<script setup>
import { usePage } from '../../composables/usePage'
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getItems, insertItem, request } from '../../services/api'

usePage()

const route = useRoute()
const router = useRouter()

const idRecherche = route.params.idRecherche
const agent = ref(null)
// const idAgent = 10;

const form = reactive({
    datasetName: '',
    licenceID: '',
    rightsHolder: 'The Peregrine Fund'
})

const licences = ref([])
const loadingLicences = ref(false)
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

async function fetchCurrentAgent() {
    try {
        const { data } = await request(
            'utilisateurs/my-agent',
            { method: 'GET' }
        )
        agent.value = data

        return data
    } catch (e) {
        console.error('Erreur lors de la récupération de l’agent connecté :', e)
        agent.value = null
        return null
    }
}

async function createDataset() {
    error.value = ''

    if (!form.datasetName.trim()) {
        error.value = 'Veuillez saisir le nom du dataset.'
        return
    }
    if (!idRecherche) {
        error.value = 'Aucune recherche associée à ce dataset.'
        return
    }
    loading.value = true

    try {
        const newDataset = {
            datasetName: form.datasetName.trim(),
            rightsHolder: form.rightsHolder.trim(),
            recherche: {
                rechercheID: Number(idRecherche)
            },
            licence: {
                licenceID: form.licenceID ? Number(form.licenceID) : null
            },
            agent: {
                agentID: agent.value.agentID
            }
        }

        const response = await insertItem('datasets', newDataset)
        const created = response?.data ?? response
        const datasetID =
            created?.datasetID ??
            created?.item?.datasetID

        if (!datasetID) {
            throw new Error(
                "Le dataset a été créé, mais son identifiant n'a pas été retourné par l'API."
            )
        }
        router.push(`/saisie/fiche-d-un-dataset/${datasetID}`)

    } catch (e) {
        console.error('Erreur création du dataset :', e)

        error.value =
            e.response?.data?.message ||
            e.message ||
            'Une erreur est survenue lors de la création du dataset.'

    } finally {
        loading.value = false
    }
}
onMounted(() => {
    loadLicences()
    fetchCurrentAgent()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/saisie/saisie-contexte">Saisie</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/nouveau-dataset">Nouveau dataset</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" @submit.prevent="createDataset">
        <small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Nouveau dataset</h1>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Nom du dataset<span class="text-danger">
                            *</span></label><input class="form-control" type="text" id="f1" name="datasetName" v-model="form.datasetName" placeholder="Ex: Mammifères" required></div>
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
                <div class="col-md"><label class="form-label" for="f3">Détenteur des droits</label><input
                        class="form-control" type="text" id="f3" name="rightsHolder" v-model="form.rightsHolder" >
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                    to="/recherches/mes-recherches">Annuler</router-link>
                <button type="submit" class="btn btn-primary" :disabled="loading"
                >
                    <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                    {{ loading ? 'Création...' : 'Créer le dataset' }}
                </button>
            </div>
        </form>
    
  </main>
</template>
