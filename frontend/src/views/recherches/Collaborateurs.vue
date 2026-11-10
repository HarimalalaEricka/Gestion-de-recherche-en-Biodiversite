<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { insertItem, getItems, getItemById, deleteItem  } from '../../services/api'

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
const chercheursPrincipauxIds = ref([])

const roles = [
    { label: 'Chercheur Principal', value: 'CHERCHEUR_PRINCIPAL' },
    { label: 'Chercheur', value: 'CO_CHERCHEUR' },
    { label: 'Agent de saisie', value: 'DATA_ENTRY' }
]

const roleLabels = Object.fromEntries(roles.map(r => [r.value, r.label]))

const formatDate = (d) => (d ? new Date(d).toLocaleDateString('fr-FR') : '-')

// ---------- Pagination ----------

const total = ref(0)
const page = ref(1)
const perPage = ref(10)
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / perPage.value)))

// ---------- Filtres (appliqués côté serveur) ----------

const terme = ref('')          // texte : nom, prénom, e-mail
const roleFiltre = ref('')     // '' = tous les rôles
const statutFiltre = ref('')   // '' = tous les statuts

const roleOptions = [{ value: '', label: 'Tous les rôles' }, ...roles.map(r => ({ value: r.value, label: r.label }))]

// À adapter aux valeurs réelles de l'enum de statut côté backend
const statutOptions = [
    { value: '', label: 'Tous les statuts' },
    { value: 'ACCEPTEE', label: 'Acceptée' },
    { value: 'EN_ATTENTE', label: 'En attente' },
    { value: 'REFUSEE', label: 'Refusée' }
]
const statutLabels = Object.fromEntries(statutOptions.filter(s => s.value).map(s => [s.value, s.label]))

const filtresActifs = computed(
    () => terme.value.trim() !== '' || roleFiltre.value !== '' || statutFiltre.value !== ''
)

let searchTimer = null

function applyFilters() {
    if (page.value !== 1) page.value = 1 // le watch sur la page recharge
    else fetchCollaborateurs()
}

// Recherche texte : on attend 300 ms après la dernière frappe
watch(terme, () => {
    clearTimeout(searchTimer)
    searchTimer = setTimeout(applyFilters, 300)
})

watch([roleFiltre, statutFiltre], applyFilters)

function resetFilters() {
    clearTimeout(searchTimer)
    terme.value = ''
    roleFiltre.value = ''
    statutFiltre.value = ''
}

watch([page, perPage], fetchCollaborateurs)

// ---------- Invitation ----------

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

        // Rafraîchit la liste sans changer de page de l'application
        if (page.value !== 1) page.value = 1 // le watch recharge
        else await fetchCollaborateurs()

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

// ---------- Chargement des données ----------

async function getutilisateurById(id) {
    try {
        return await getItemById('utilisateurs', id)
    } catch (e) {
        console.error(`Erreur lors du chargement de l'utilisateur ${id} :`, e)
        return null
    }
}

let requestId = 0 // seule la dernière requête met à jour l'écran

async function fetchCollaborateurs() {
    const id = ++requestId
    loadingList.value = true
    try {
        const { items, total: t } = await getItems(
            'agent-recherches',
            {
                rechercheID: props.idRecherche,
                roleCollaboration: roleFiltre.value,  // "" est ignoré par getItems
                statut: statutFiltre.value,           // "" est ignoré par getItems
                q: terme.value.trim(),
            },
            { page: page.value, perPage: perPage.value }
        )
        if (id !== requestId) return

        // On ne charge que les utilisateurs de la page courante, et pas ceux déjà connus
        const ids = [...new Set(
            items.map(c => c.agent?.utilisateurId).filter(Boolean)
        )].filter(uid => !utilisateurs.value[uid])

        const resultats = await Promise.all(ids.map(uid => getutilisateurById(uid)))
        if (id !== requestId) return

        const map = { ...utilisateurs.value }
        ids.forEach((uid, i) => { map[uid] = resultats[i] })

        // Mise à jour d'un seul coup pour éviter un affichage à moitié vide
        utilisateurs.value = map
        collaborators.value = items
        total.value = t
    } catch (e) {
        if (id !== requestId) return
        console.error('Erreur chargement des collaborateurs :', e)
    } finally {
        if (id === requestId) loadingList.value = false
    }
}

async function fetchRecherche() {
    try {
        recherche.value = await getItemById('recherches', props.idRecherche)
    } catch (e) {
        console.error('Erreur lors du chargement de la recherche :', e)
    }
}

async function fetchChercheursPrincipaux() {
    try {
        const { items } = await getItems(
            'agent-recherches',
            {
                rechercheID: props.idRecherche,
                roleCollaboration: 'CHERCHEUR_PRINCIPAL',
                statut: 'ACCEPTEE'
            },
            { page: 1, perPage: 50 }
        )
        chercheursPrincipauxIds.value = items
            .map(c => c.agent?.utilisateurId)
            .filter(Boolean)
    } catch (e) {
        console.error('Erreur chargement du chercheur principal :', e)
        chercheursPrincipauxIds.value = []
    }
}

function lireUtilisateurConnecte() {
    try {
        return JSON.parse(localStorage.getItem('userConnected')) || null
    } catch {
        return null
    }
}

const utilisateurConnecte = lireUtilisateurConnecte()
const utilisateurConnecteId = Number(utilisateurConnecte?.data?.utilisateurID) || null

const estChercheurPrincipal = computed(
    () => utilisateurConnecteId !== null &&
          chercheursPrincipauxIds.value.map(Number).includes(utilisateurConnecteId)
)

const suppressionEnCours = ref(null) // agentID en cours de suppression

async function supprimer(collaborateur) {
    if (!estChercheurPrincipal.value) return
 
    const agentId = collaborateur.agent?.agentID
    if (!agentId) return
 
    const u = utilisateurs.value[collaborateur.agent?.utilisateurId]
    const nom = u ? `${u.nom} ${u.prenom}` : 'ce collaborateur'
    if (!window.confirm(`Retirer ${nom} de cette recherche ?`)) return
 
    error.value = ''
    message.value = ''
    suppressionEnCours.value = agentId
 
    try {
        // DELETE agent-recherches/{agentId}/{rechercheId}
        await deleteItem('agent-recherches', `${agentId}/${props.idRecherche}`)
        message.value = 'Collaborateur retiré.'
 
        // Si c'était le dernier de la page, on recule d'une page (le watch recharge)
        if (collaborators.value.length === 1 && page.value > 1) page.value--
        else await fetchCollaborateurs()
 
    } catch (e) {
        console.error('Erreur suppression :', e)
 
        if (e.message?.includes('403')) {
            error.value = 'Seul le chercheur principal peut retirer un collaborateur.'
        } else if (e.message?.includes('409')) {
            error.value = 'Vous ne pouvez pas vous retirer vous-même de la recherche.'
        } else if (e.message?.includes('404')) {
            error.value = 'Collaborateur introuvable.'
        } else {
            error.value = 'Impossible de retirer ce collaborateur.'
        }
    } finally {
        suppressionEnCours.value = null
    }
}
 

onMounted(() => {
    fetchCollaborateurs()
    fetchRecherche()
    fetchChercheursPrincipaux()
})

onUnmounted(() => {
    clearTimeout(searchTimer)
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
        <form v-if="estChercheurPrincipal" @submit.prevent="inviter" class="card">
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
            <!-- Filtres -->
            <div class="card-header d-flex flex-wrap align-items-center gap-2">
                <span class="fw-semibold me-2">Collaborateurs de la recherche</span>

                <div class="input-group input-group-sm filter-search">
                    <span class="input-group-text"><i class="bi bi-search"></i></span>
                    <input
                        v-model="terme"
                        type="search"
                        class="form-control"
                        placeholder="Nom, prénom, e-mail..."
                        aria-label="Rechercher"
                    >
                </div>

                <select
                    v-model="roleFiltre"
                    class="form-select form-select-sm w-auto"
                    aria-label="Filtrer par rôle"
                >
                    <option v-for="o in roleOptions" :key="o.value" :value="o.value">{{ o.label }}</option>
                </select>

                <select
                    v-model="statutFiltre"
                    class="form-select form-select-sm w-auto"
                    aria-label="Filtrer par statut"
                >
                    <option v-for="o in statutOptions" :key="o.value" :value="o.value">{{ o.label }}</option>
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
                <table class="table table-hover align-middle mb-0" :class="{ 'opacity-50': loadingList }">
                    <thead>
                        <tr>
                            <th>Personne</th>
                            <th>Email</th>
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
                            <td colspan="5" class="text-center text-body-secondary">
                                {{ filtresActifs ? "Aucun collaborateur ne correspond à ces filtres" : "Aucun collaborateur trouvé." }}
                            </td>
                        </tr>
                        <template v-else>
                            <tr v-for="collaborateur in collaborators" :key="collaborateur.agent?.agentID">
                                <td>
                                    {{ utilisateurs[collaborateur.agent?.utilisateurId]?.nom }}
                                    {{ utilisateurs[collaborateur.agent?.utilisateurId]?.prenom }}
                                </td>
                                <td>{{ utilisateurs[collaborateur.agent?.utilisateurId]?.email }}</td>
                                <td>{{ roleLabels[collaborateur.roleCollaboration] || collaborateur.roleCollaboration }}</td>
                                <td>{{ statutLabels[collaborateur.statut] || collaborateur.statut }}</td>
                                <td>{{ formatDate(collaborateur.accepteLe || collaborateur.inviteLe) }}</td>
                                <td class="text-end">
                                    <button
                                        v-if="estChercheurPrincipal && collaborateur.agent?.utilisateurId !== utilisateurConnecteId"
                                        type="button"
                                        class="btn btn-sm btn-outline-danger"
                                        :disabled="suppressionEnCours === collaborateur.agent?.agentID"
                                        @click="supprimer(collaborateur)"
                                    >
                                        {{ suppressionEnCours === collaborateur.agent?.agentID ? 'Suppression...' : 'Supprimer' }}
                                    </button>
                                </td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <div class="card-footer d-flex flex-wrap justify-content-between align-items-center gap-2">
                <small class="text-body-secondary">{{ total }} collaborateur(s)</small>

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

    </main>
</template>

<style scoped>
.filter-search {
    flex: 1 1 240px;
    max-width: 340px;
}
</style>