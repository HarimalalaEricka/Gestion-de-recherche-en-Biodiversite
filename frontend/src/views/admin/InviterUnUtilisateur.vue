<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { usePage } from '../../composables/usePage'
import { insertItem } from '../../services/api'

usePage()

const router = useRouter()

const nom = ref('')
const email = ref('')
const role = ref('RESEARCHER')
const message = ref('')
const error = ref('')
const loading = ref(false)

async function inviter() {
  error.value = ''
  message.value = ''
  loading.value = true
  try {
    const data = await insertItem('admin/utilisateurs/invitation', {
      nom: nom.value,
      email: email.value,
      role: role.value,
      date_creation: new Date().toISOString()
    })
    message.value = data.message || 'Invitation envoyée'
    setTimeout(() => router.push('/admin/utilisateurs'), 1500)
  } catch (e) {
    if (e.message.includes('409')) error.value = 'Cette adresse est déjà utilisée'
    else if (e.message.includes('403')) error.value = 'Accès réservé aux administrateurs'
    else if (e.message.includes('400')) error.value = 'Données invalides : vérifiez le nom, l’e-mail et le rôle'
    else error.value = 'Impossible d’envoyer l’invitation'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link to="/admin/utilisateurs">Administration</router-link></li>
        <li class="breadcrumb-item active" aria-current="page"><router-link to="/admin/inviter-un-utilisateur">Inviter un utilisateur</router-link></li>
      </ol>
    </nav>

    <form class="needs-validation d-grid gap-4" @submit.prevent="inviter">
      <small class="text-body-secondary text-end" data-autosave></small>
      <h1 class="h3 mb-0">Inviter un utilisateur</h1>

      <div v-if="message" class="alert alert-success py-2 mb-0">{{ message }}</div>
      <div v-if="error" class="alert alert-danger py-2 mb-0">{{ error }}</div>

      <div class="row g-3">
        <div class="col-md">
          <label class="form-label" for="f1">Nom<span class="text-danger"> *</span></label>
          <input v-model="nom" class="form-control" type="text" id="f1" name="nom" required>
        </div>
        <div class="col-md">
          <label class="form-label" for="f2">E-mail<span class="text-danger"> *</span></label>
          <input v-model="email" class="form-control" type="email" id="f2" name="e-mail" required>
        </div>
        <div class="col-md">
          <label class="form-label" for="f3">Rôle<span class="text-danger"> *</span></label>
          <select v-model="role" class="form-select" id="f3" name="role" required>
            <option value="ADMIN">Administrateur</option>
            <option value="RESEARCHER">Chercheur</option>
            <option value="DATA_ENTRY">Agent de saisie</option>
            <option value="VIEWER">Lecteur</option>
            <option value="STUDENT">Étudiant</option>
          </select>
        </div>
      </div>

      <p class="text-body-secondary mb-0">Un lien d’activation valable 7 jours est envoyé par e-mail.</p>

      <div class="d-flex flex-wrap gap-2">
        <router-link class="btn btn-outline-secondary" to="/admin/utilisateurs">Annuler</router-link>
        <button type="submit" class="btn btn-primary" :disabled="loading || !!message">
          {{ loading ? 'Envoi...' : 'Envoyer l’invitation' }}
        </button>
      </div>
    </form>
  </main>
</template>