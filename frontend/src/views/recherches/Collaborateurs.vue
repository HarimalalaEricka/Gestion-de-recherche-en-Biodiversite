<script setup>
import { usePage } from '../../composables/usePage'
import { ref, onMounted } from 'vue'
import { insertItem, getItems, getItemById } from '../../services/api'

usePage()

const props = defineProps({ idRecherche: { type: String, required: true } })

const error = ref('')
const message = ref('')
const loading = ref(false)      // envoi de l'invitation
const loadingList = ref(false)  // chargement de la liste

const email = ref('')
const role = ref('CO_CHERCHEUR')
const collaborators = ref([])
const recherche = ref(null)
const utilisateurs = ref({})    

const roles = [
    { label: 'Chercheur Principal', value: 'CHERCHEUR_PRINCIPAL' },
    { label: 'Chercheur', value: 'CO_CHERCHEUR' },
    { label: 'Agent de saisie', value: 'DATA_ENTRY' }
]

const roleLabels = Object.fromEntries(roles.map(r => [r.value, r.label]))

const formatDate = (d) => (d ? new Date(d).toLocaleDateString('fr-FR') : '-')

async function inviter() {
    error.value = ''
    message.value = ''

    if (!email.value.trim() || !role.value) {
        error.value = 'Veuillez renseigner tous les champs.'
        return
    }

    loading.value = true

    try {
        const data = await insertItem(
            `agent-recherches/recherches/${props.idRecherche}/collaborateurs`,
            {
                email: email.value.trim(),
                role: role.value
            }
        )

        message.value = data.message || 'Invitation envoyée.'
        email.value = ''

        // Rafraîchit la liste sans changer de page
        await fetchCollaborateurs()

    } catch (e) {
        console.error('Erreur invitation :', e)

        if (e.message?.includes('409')) {
            error.value = 'Cette adresse est déjà utilisée.'
        } else if (e.message?.includes('403')) {
            error.value = 'Accès réservé aux administrateurs.'
        } else if (e.message?.includes('400')) {
            error.value = 'Données invalides : vérifiez l’adresse e-mail et le rôle.'
        } else if (e.message?.includes('404')) {
            error.value = 'Recherche ou utilisateur introuvable.'
        } else {
            error.value = 'Impossible d’envoyer l’invitation.'
        }
    } finally {
        loading.value = false
    }
}

async function getutilisateurById(id) {
    try {
        return await getItemById('utilisateurs', id)
    } catch (e) {
        console.error(`Erreur lors du chargement de l'utilisateur ${id} :`, e)
        return null
    }
}

async function fetchCollaborateurs() {
    loadingList.value = true
    try {
        const { items } = await getItems(
            'agent-recherches',
            { rechercheID: props.idRecherche }
        )
        collaborators.value = items

        // Ids uniques, sans valeur vide
        const ids = [...new Set(
            items.map(c => c.agent?.utilisateurId).filter(Boolean)
        )]

        // Un appel par utilisateur, en parallèle
        const resultats = await Promise.all(ids.map(id => getutilisateurById(id)))

        const map = {}
        ids.forEach((id, i) => { map[id] = resultats[i] })
        utilisateurs.value = map
    } catch (e) {
        console.error('Erreur chargement des collaborateurs :', e)
    } finally {
        loadingList.value = false
    }
}

async function fetchRecherche() {
    try {
        recherche.value = await getItemById('recherches', props.idRecherche)
    } catch (e) {
        console.error('Erreur lors du chargement de la recherche :', e)
    }
}

onMounted(() => {
    fetchCollaborateurs()
    fetchRecherche()
})
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
                <li class="breadcrumb-item active" aria-current="page">
                    Collaborateurs
                </li>
            </ol>
        </nav>

        <h1 class="h3 mb-0">{{ recherche?.sujet || "Collaborateurs de la recherche" }}</h1>

        <ul class="nav nav-tabs">
            <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Recherches</router-link></li>
            <li class="nav-item">
                <router-link class="nav-link" :to="`/recherches/medias-de-la-recherche/${idRecherche}`">Médias</router-link>
            </li>
            <li class="nav-item"><router-link class="nav-link active" :to="`/recherches/collaborateurs/${idRecherche}`">Collaborateurs</router-link></li>
            <li class="nav-item">
                <router-link class="nav-link" :to="`/recherches/metadonnees-de-la-recherche/${idRecherche}`">Métadonnées</router-link>
            </li>
            <li class="nav-item">
                <router-link class="nav-link" :to="`/recherches/these/${idRecherche}`">Thèse</router-link>
            </li>
            <li class="nav-item"><router-link class="nav-link" :to="`/recherches/notes-personnelles/${idRecherche}`">Notes</router-link></li>
        </ul>

        <!-- Invitation -->
        <form @submit.prevent="inviter" class="card">
            <div class="card-header fw-semibold">
                Inviter un collaborateur
            </div>

            <div class="card-body d-grid gap-3">

                <div v-if="error" class="alert alert-danger mb-0" role="alert">
                    {{ error }}
                </div>

                <div v-if="message" class="alert alert-success mb-0" role="status">
                    {{ message }}
                </div>

                <div class="row g-3">

                    <div class="col-md">
                        <label class="form-label" for="email">
                            Adresse e-mail
                        </label>

                        <input
                            v-model="email"
                            class="form-control"
                            type="email"
                            id="email"
                            placeholder="exemple@email.com"
                            required
                        >
                    </div>

                    <div class="col-md">
                        <label class="form-label" for="role">
                            Rôle sur la recherche
                        </label>

                        <select
                            v-model="role"
                            class="form-select"
                            id="role"
                            required
                        >
                            <option
                                v-for="r in roles"
                                :key="r.value"
                                :value="r.value"
                            >
                                {{ r.label }}
                            </option>
                        </select>
                    </div>

                </div>

                <div>
                    <button
                        type="submit"
                        class="btn btn-primary"
                        :disabled="loading"
                    >
                        <span
                            v-if="loading"
                            class="spinner-border spinner-border-sm me-2"
                        ></span>

                        {{ loading ? 'Envoi...' : 'Inviter' }}
                    </button>
                </div>

            </div>
        </form>

        <!-- Liste des collaborateurs -->
        <div class="card">
            <div class="card-header">
                Collaborateurs de la recherche
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" :class="{ 'opacity-50': loadingList }">
                    <thead>
                        <tr>
                            <th>Personne</th>
                            <th>Rôle</th>
                            <th>Statut</th>
                            <th>Ajouté le</th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-if="loadingList && collaborators.length === 0">
                            <td colspan="5" class="text-center text-body-secondary">Chargement...</td>
                        </tr>
                        <tr v-else-if="collaborators.length === 0">
                            <td colspan="5" class="text-center text-body-secondary">Aucun collaborateur trouvé.</td>
                        </tr>
                        <tr v-for="collaborateur in collaborators" :key="collaborateur.agent?.agentID">
                            <td>
                                {{ utilisateurs[collaborateur.agent?.utilisateurId]?.nom }}
                                {{ utilisateurs[collaborateur.agent?.utilisateurId]?.prenom }}
                            </td>
                            <td>{{ roleLabels[collaborateur.roleCollaboration] || collaborateur.roleCollaboration }}</td>
                            <td>{{ collaborateur.statut }}</td>
                            <td>{{ formatDate(collaborateur.accepteLe || collaborateur.inviteLe) }}</td>
                            <td class="text-end">
                                <button type="button" class="btn btn-sm btn-outline-danger">Supprimer</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </main>
</template>