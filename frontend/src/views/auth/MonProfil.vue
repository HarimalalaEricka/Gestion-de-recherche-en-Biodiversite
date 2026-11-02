<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, onMounted } from 'vue'
import { request } from '../../services/api'
usePage()

const utilisateurId = ref(null)
const nom = ref('')
const prenom = ref('')
const email = ref('')
const telephone = ref('')
const role = ref('')
const statut = ref('')

const ancienMdp = ref('')
const nouveauMdp = ref('')
const confirmationMdp = ref('')

const erreur = ref('')
const message = ref('')
const enCours = ref(false)

const initiales = computed(() => {
  const p = (prenom.value || '').trim()
  const n = (nom.value || '').trim()
  return ((p[0] ?? '') + (n[0] ?? '')).toUpperCase() || '?'
})

const roleLabels = {
  ADMIN: 'Administrateur',
  RESEARCHER: 'Chercheur',
  VIEWER: 'Lecteur',
  DATA_ENTRY: 'Agent de saisie',
  STUDENT: 'Étudiant',
}
const roleLabel = computed(() => roleLabels[role.value] ?? role.value)

async function loadProfile() {
  try {
    const { data } = await request('auth/me')
    utilisateurId.value = data.utilisateurID
    nom.value = data.nom
    prenom.value = data.prenom
    email.value = data.email
    telephone.value = data.telephone
    role.value = data.role
    statut.value = data.statut
  } catch (e) {
    console.error(e)
    erreur.value = 'Impossible de charger le profil'
  }
}

function messageErreur(e) {
  const brut = e?.message || ''
  try {
    return JSON.parse(brut.slice(brut.indexOf('{'))).erreur
  } catch {
    return 'Une erreur est survenue. Veuillez réessayer.'
  }
}

async function enregistrer() {
  erreur.value = ''
  message.value = ''

  const veutChangerMdp = ancienMdp.value || nouveauMdp.value || confirmationMdp.value

    enCours.value = true
  try {
    if (veutChangerMdp) {
      await request('auth/me/mot-de-passe', {
        method: 'PUT',
        body: {
          ancienMotDePasse: ancienMdp.value,
          nouveauMotDePasse: nouveauMdp.value,
          confirmation: confirmationMdp.value,
        },
      })
      ancienMdp.value = nouveauMdp.value = confirmationMdp.value = ''
    }

    await request(`utilisateurs/${utilisateurId.value}`, {
      method: 'PATCH',
      body: {
        nom: nom.value,
        prenom: prenom.value,
        telephone: telephone.value,
      },
    })

    message.value = veutChangerMdp
      ? 'Profil et mot de passe mis à jour'
      : 'Profil mis à jour'
  } catch (e) {
    erreur.value = messageErreur(e)
  } finally {
    enCours.value = false
  }
}

onMounted(loadProfile)
</script>

<template>
  <main id="content" class="p-4 profile-page">
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link to="/auth/mon-profil">Compte</router-link></li>
        <li class="breadcrumb-item active" aria-current="page"><router-link to="/auth/mon-profil">Mon profil</router-link></li>
      </ol>
    </nav>

    <div class="profile-heading">
      <div>
        <span class="profile-eyebrow"><i class="bi bi-person-badge"></i> Espace personnel</span>
        <h1>Mon profil</h1>
        <p>Gérez vos informations personnelles et vos accès à Nexora.</p>
      </div>
    </div>

    <form class="needs-validation profile-layout" novalidate @submit.prevent="enregistrer">
      <aside class="profile-identity">
        <div class="profile-identity-cover"></div>
        <div class="profile-identity-body">
          <div class="profile-page-avatar">{{ initiales }}<span></span></div>
          <h2>{{ prenom }} {{ nom }}</h2>
          <p class="profile-role"><i class="bi bi-mortarboard"></i> {{ roleLabel }}</p>
          <p class="profile-status"><i class="bi bi-circle-fill"></i> {{ statut }}</p>
          <hr>
          <div class="profile-identity-detail">
            <span><i class="bi bi-envelope"></i></span> <span>{{ email }}</span>
          </div>
          <div class="profile-identity-detail">
            <span><i class="bi bi-telephone"></i></span> <span>{{ telephone || 'Non renseigné' }}</span>
          </div>
          <div class="profile-identity-detail">
            <span><i class="bi bi-calendar3"></i></span> <span>Membre de Nexora</span>
          </div>
        </div>
      </aside>

      <div class="profile-sections">
        <section class="profile-section">
          <div class="profile-section-heading">
            <span class="profile-section-icon"><i class="bi bi-person"></i></span>
            <span><strong>Informations personnelles</strong><small>Ces informations permettent de vous identifier.</small></span>
          </div>
          <div class="profile-fields">
            <div>
              <label class="form-label" for="f1">Nom<span class="text-danger"> *</span></label>
              <input class="form-control" type="text" id="f1" name="nom" v-model="nom" required>
            </div>
            <div>
              <label class="form-label" for="f2">Prénom</label>
              <input class="form-control" type="text" id="f2" name="prenom" v-model="prenom">
            </div>
            <div>
              <label class="form-label" for="f3">Téléphone</label>
              <input class="form-control" type="text" id="f3" name="telephone" v-model="telephone" placeholder="+261 ...">
            </div>
            <div>
              <label class="form-label" for="f4">E-mail</label>
              <input class="form-control" type="email" id="f4" name="e-mail" v-model="email" readonly>
            </div>
          </div>
        </section>

        <section class="profile-section">
          <div class="profile-section-heading">
            <span class="profile-section-icon"><i class="bi bi-shield-lock"></i></span>
            <span><strong>Sécurité du compte</strong><small>Laissez vide si vous ne voulez pas changer de mot de passe.</small></span>
          </div>
          <div class="profile-fields profile-password-fields">
            <div>
              <label class="form-label" for="f5">Ancien mot de passe</label>
              <input class="form-control" type="password" id="f5" name="ancien-mot-de-passe" v-model="ancienMdp" autocomplete="current-password">
            </div>
            <div>
              <label class="form-label" for="f6">Nouveau mot de passe</label>
              <input class="form-control" type="password" id="f6" name="nouveau-mot-de-passe" v-model="nouveauMdp" autocomplete="new-password">
            </div>
            <div>
              <label class="form-label" for="f7">Confirmation</label>
              <input class="form-control" type="password" id="f7" name="confirmation" v-model="confirmationMdp" autocomplete="new-password">
            </div>
          </div>
        </section>

        <div v-if="erreur" class="text-danger">{{ erreur }}</div>
        <div v-if="message" class="text-success">{{ message }}</div>

        <div class="profile-actions">
          <span><i class="bi bi-lock"></i> Vos données restent confidentielles.</span>
          <button type="submit" class="btn btn-primary" :disabled="enCours">
            <i class="bi bi-check2 me-1"></i>{{ enCours ? 'Enregistrement...' : 'Enregistrer' }}
          </button>
        </div>
      </div>
    </form>
  </main>
</template>