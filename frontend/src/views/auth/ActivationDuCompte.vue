<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePage } from '../../composables/usePage'
import { activateAccount } from '../../services/authService'

usePage()

const route = useRoute()
const router = useRouter()

const token = route.query.token || ''
const password = ref('')
const confirmation = ref('')
const error = ref(token ? '' : 'Lien invalide : le jeton est manquant')
const success = ref(false)
const loading = ref(false)

async function activer() {
  error.value = ''

  if (!token) {
    error.value = 'Lien invalide : le jeton est manquant'
    return
  }
  if (password.value.length < 8) {
    error.value = 'Le mot de passe doit contenir au moins 8 caractères'
    return
  }
  if (password.value !== confirmation.value) {
    error.value = 'Les deux mots de passe ne correspondent pas'
    return
  }

  loading.value = true
  try {
    await activateAccount(token, password.value)
    success.value = true
    setTimeout(() => router.push('/auth/connexion'), 2000)
  } catch (e) {
    error.value = e.message.includes('400')
      ? 'Lien invalide ou expiré. Demandez une nouvelle invitation à un administrateur.'
      : 'Impossible de contacter le serveur'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main id="content" class="auth-page">
    <div class="auth-card auth-simple-card">
      <div class="auth-simple-brand">
        <img src="../../assets/img/logo.svg" alt="Logo Nexora"><span>Nexora</span>
      </div>
      <div class="auth-tech-strip">
        <span><i class="bi bi-diagram-3"></i> Profil scientifique</span>
        <span>ÉTAPE 01 / 01</span>
      </div>

      <form class="needs-validation auth-simple-form d-grid gap-4" @submit.prevent="activer">
        <small class="text-body-secondary text-end" data-autosave></small>

        <div class="auth-form-heading">
          <span class="auth-icon"><i class="bi bi-person-check"></i></span>
          <span class="auth-kicker">Première connexion</span>
        </div>

        <div>
          <h1>Activer mon compte</h1>
          <p>Choisissez votre mot de passe pour commencer à utiliser Nexora.</p>
        </div>

        <div v-if="success" class="alert alert-success py-2 mb-0">
          Compte activé. Redirection vers la connexion...
        </div>
        <div v-if="error" class="alert alert-danger py-2 mb-0">{{ error }}</div>

        <div class="row g-3">
          <div class="col-md">
            <label class="form-label" for="f1">Mot de passe<span class="text-danger"> *</span></label>
            <input v-model="password" class="form-control" type="password" id="f1"
                   name="mot-de-passe" minlength="8" required>
          </div>
          <div class="col-md">
            <label class="form-label" for="f2">Confirmer le mot de passe<span class="text-danger"> *</span></label>
            <input v-model="confirmation" class="form-control" type="password" id="f2"
                   name="confirmer-le-mot-de-passe" required>
          </div>
        </div>

        <div class="d-flex flex-wrap gap-2">
          <button type="submit" class="btn btn-primary" :disabled="loading || success || !token">
            {{ loading ? 'Activation...' : 'Activer mon compte' }}
          </button>
        </div>
      </form>
    </div>
  </main>
</template>