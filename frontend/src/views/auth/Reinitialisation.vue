<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { resetPassword } from '../../services/authService'

const route = useRoute()
const router = useRouter()

const token = route.query.token || ''
const password = ref('')
const confirmation = ref('')
const error = ref('')
const success = ref(false)
const loading = ref(false)

async function valider() {
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
    await resetPassword(token, password.value)
    success.value = true
    setTimeout(() => router.push('/auth/connexion'), 2000)
  } catch (e) {
    error.value = e.message.includes('400')
      ? 'Lien invalide ou expiré. Refaites une demande.'
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
        <span><i class="bi bi-shield-check"></i> Protection des données</span>
        <span>CHIFFREMENT ACTIF</span>
      </div>

      <form class="needs-validation auth-simple-form d-grid gap-4" @submit.prevent="valider">
        <small class="text-body-secondary text-end" data-autosave></small>

        <div class="auth-form-heading">
          <span class="auth-icon"><i class="bi bi-shield-lock"></i></span>
          <span class="auth-kicker">Sécurité du compte</span>
        </div>

        <div>
          <h1>Nouveau mot de passe</h1>
          <p>Choisissez un mot de passe solide pour protéger votre compte.</p>
        </div>

        <div v-if="success" class="alert alert-success py-2 mb-0">
          Mot de passe modifié. Redirection vers la connexion...
        </div>
        <div v-if="error" class="alert alert-danger py-2 mb-0">{{ error }}</div>

        <div class="row g-3">
          <div>
            <label class="form-label" for="f1">Nouveau mot de passe<span class="text-danger"> *</span></label>
            <input v-model="password" class="form-control" type="password" id="f1"
                   name="nouveau-mot-de-passe" minlength="8" required>
          </div>
          <div>
            <label class="form-label" for="f2">Confirmation<span class="text-danger"> *</span></label>
            <input v-model="confirmation" class="form-control" type="password" id="f2"
                   name="confirmation" required>
          </div>
        </div>

        <div class="d-flex flex-wrap gap-2">
          <button type="submit" class="btn btn-primary" :disabled="loading || success">
            {{ loading ? 'Enregistrement...' : 'Enregistrer' }}
          </button>
          <router-link class="btn btn-outline-secondary" to="/auth/connexion">
            Retour à la connexion
          </router-link>
        </div>
      </form>
    </div>
  </main>
</template>