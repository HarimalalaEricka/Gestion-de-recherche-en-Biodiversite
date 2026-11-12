
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePage } from '../../composables/usePage'
import { insertItem, getItemById } from '../../services/api'

usePage()

const route = useRoute()
const router = useRouter()

const token = computed(() => route.query.token || '')
const rechercheId = computed(() => Number(route.query.recherche))

const error = ref('')
const message = ref('')
const loading = ref(false)
const success = ref(false)
const declined = ref(false)
const recherche = ref(null)

const lienValide = computed(() => {
    return !!token.value &&
        Number.isInteger(rechercheId.value) &&
        rechercheId.value > 0
})

async function accepter() {
    error.value = ''
    message.value = ''
    declined.value = false

    if (!lienValide.value) {
        error.value = 'Lien invalide : les informations de l’invitation sont manquantes.'
        return
    }

    loading.value = true

    try {
        await insertItem('agent-recherches/accepter', {
            token: token.value,
            rechercheId: rechercheId.value
        })

        success.value = true
        message.value = 'Vous avez accepté l’invitation. Bienvenue dans la recherche !'

        setTimeout(() => {
            router.push('/auth/connexion')
        }, 2500)

    } catch (e) {

        const erreur = e.message || ''

        if (erreur.includes('403')) {
            error.value = 'Cette invitation ne vous est pas destinée.'
        } else if (erreur.includes('410')) {
            error.value = 'Cette invitation est invalide, expirée ou déjà utilisée.'
        } else if (erreur.includes('409')) {
            error.value = 'Aucune invitation en attente pour cette recherche.'
        } else if (erreur.includes('401')) {
            error.value = 'Vous devez vous connecter pour accepter cette invitation.'
        } else if (erreur.includes('400')) {
            error.value = 'Les informations de l’invitation sont invalides.'
        } else if (erreur.includes('404')) {
            error.value = 'Invitation ou recherche introuvable.'
        } else {
            error.value = 'Impossible de traiter votre invitation. Veuillez réessayer.'
        }
    } finally {
        loading.value = false
    }
}

async function getRecherche()
{
    if( rechercheId.value == null || rechercheId.value <= 0 )
    {
        error.value = 'Lien invalide : les informations de l’invitation sont manquantes.'
        return
    }
    recherche.value  = await getItemById(`recherches/${rechercheId.value}`)
    return recherche.value
}

function refuser() {
    declined.value = true
    error.value = ''
    message.value = 'Vous avez choisi de ne pas accepter pour le moment. Votre invitation reste en attente.'
}

onMounted(() => {
    if (lienValide.value) {
        getRecherche()
    }
})
</script>

<template>
    <main
        id="content"
        class="min-vh-100 d-flex justify-content-center align-items-center p-4"
    >

        <div class="auth-card auth-simple-card">
            <div class="auth-tech-strip">
                <span>
                    <i class="bi bi-people"></i>
                    Collaboration scientifique
                </span>
                <span>INVITATION</span>
            </div>

            <div class="auth-simple-form d-grid gap-4">

                <div class="auth-form-heading">
                    <span class="auth-icon">
                        <i class="bi bi-envelope-check"></i>
                    </span>
                    <span class="auth-kicker">
                        Invitation à collaborer
                    </span>
                </div>

                <div>
                    <h1>Rejoindre une recherche</h1>
                    <p>
                        Vous avez été invité à participer à un projet
                        de recherche sur Nexora.
                        Vous pouvez accepter l'invitation ou la laisser
                        en attente.
                    </p>
                </div>

                <div v-if="!lienValide" class="alert alert-danger">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    Lien d'invitation invalide ou incomplet.
                </div>

                <div v-if="error" class="alert alert-danger">
                    {{ error }}
                </div>

                <div v-if="success" class="alert alert-success">
                    <i class="bi bi-check-circle me-2"></i>
                    {{ message }}
                    <div class="small mt-2">
                        Redirection vers la connexion...
                    </div>
                </div>

                <div v-if="declined" class="alert alert-info">
                    <i class="bi bi-clock-history me-2"></i>
                    {{ message }}
                    <div class="small mt-2">
                        Vous pourrez revenir sur ce lien pour accepter
                        l'invitation tant qu'il sera valide.
                    </div>
                </div>

                <div v-if="lienValide && !success" class="card">
                    <div class="card-body">

                        <div class="d-flex align-items-center gap-3">
                            <div class="auth-icon">
                                <i class="bi bi-diagram-3"></i>
                            </div>

                            <div>
                                <div class="fw-semibold">
                                    Invitation à une recherche
                                </div>
                                <div class="text-body-secondary small">
                                    Recherche  {{ recherche?.sujet || '...' }}
                                </div>
                            </div>
                        </div>

                        <hr>

                        <p class="small text-body-secondary mb-0">
                            En acceptant, vous rejoindrez l'équipe
                            de recherche avec les permissions
                            associées à votre rôle.
                        </p>
                    </div>
                </div>

                <div v-if="lienValide && !success" class="d-grid gap-3">

                    <button
                        type="button"
                        class="btn btn-primary"
                        :disabled="loading"
                        @click="accepter"
                    >
                        <span
                            v-if="loading"
                            class="spinner-border spinner-border-sm me-2"
                        ></span>

                        <i v-else class="bi bi-check-circle me-2"></i>

                        {{ loading ? 'Acceptation...' : 'Accepter l’invitation' }}
                    </button>

                    <button
                        type="button"
                        class="btn btn-outline-secondary"
                        :disabled="loading || declined"
                        @click="refuser"
                    >
                        <i class="bi bi-clock me-2"></i>
                        Laisser en attente
                    </button>

                </div>

                <div class="text-center small text-body-secondary">
                    <i class="bi bi-shield-check me-1"></i>
                    Nexora – Plateforme de gestion de recherche scientifique
                </div>

            </div>
        </div>
    </main>
</template>
