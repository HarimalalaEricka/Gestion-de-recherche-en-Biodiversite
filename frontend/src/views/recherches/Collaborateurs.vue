
<script setup>
import { usePage } from '../../composables/usePage'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { insertItem } from '../../services/api'

usePage()

const router = useRouter()

const props = defineProps({ idRecherche: { type: String, required: true } })
console.log('idRecherche:', props.idRecherche)

const error = ref('')
const message = ref('')
const loading = ref(false)

const email = ref('')
const role = ref('CHERCHEUR')

const roles = [
    { label: 'Chercheur Principal', value: 'CHERCHEUR_PRINCIPAL' },
    { label: 'Chercheur', value: 'CO_CHERCHEUR' },
    { label: 'Agent de saisie', value: 'DATA_ENTRY' }
]

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

        setTimeout(() => {
            router.push(`/recherches/collaborateurs/${props.idRecherche}`)
        }, 1500)

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
                    Collaborateurs
                </li>
            </ol>
        </nav>

        <form @submit.prevent="inviter" class="needs-validation d-grid gap-4">

            <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>

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
            <div class="card">
                <div class="card-header fw-semibold">
                    Inviter un collaborateur
                </div>

                <div class="card-body d-grid gap-3">

                    <div v-if="error" class="alert alert-danger mb-0">
                        {{ error }}
                    </div>

                    <div v-if="message" class="alert alert-success mb-0">
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
            </div>

            <!-- Liste des collaborateurs -->
            <div class="card">
                <div class="card-header">
                    Collaborateurs de la recherche
                </div>

                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Personne</th>
                                <th>Rôle</th>
                                <th>Statut du compte</th>
                                <th>Ajouté le</th>
                                <th></th>
                            </tr>
                        </thead>

                        <tbody>
                            <!-- À remplacer par les données provenant du backend -->
                            <tr>
                                <td colspan="5" class="text-center text-body-secondary">
                                    Liste des collaborateurs à charger depuis le backend.
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

        </form>
    </main>
</template>
