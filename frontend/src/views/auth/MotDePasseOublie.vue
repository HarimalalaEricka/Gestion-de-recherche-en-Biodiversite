<script setup>
import { ref } from "vue";
import { forgotPassword } from "../../services/authService";

const email = ref("harimalalaerickarandria@gmail.com");
const message = ref("");
const error = ref("");
const loading = ref(false);

async function envoyer() {
  error.value = "";
  message.value = "";
  loading.value = true;
  try {
    const data = await forgotPassword(email.value);
    message.value = data.message;
  } catch (e) {
    console.error("Erreur mot de passe oublié :", e);
    error.value = "Impossible de contacter le serveur";
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <main id="content" class="auth-page">
    <div class="auth-card auth-simple-card">
      <div class="auth-simple-brand">
        <img src="../../assets/img/logo.svg" alt="Logo Nexora" /><span>Nexora</span>
      </div>
      <div class="auth-tech-strip">
        <span><i class="bi bi-broadcast-pin"></i> Vérification d’accès</span>
        <span>CANAL SÉCURISÉ</span>
      </div>

      <form
        class="needs-validation auth-simple-form d-grid gap-4"
        @submit.prevent="envoyer"
      >
        <small class="text-body-secondary text-end" data-autosave></small>

        <div class="auth-form-heading">
          <span class="auth-icon"><i class="bi bi-envelope"></i></span>
          <span class="auth-kicker">Récupération</span>
        </div>

        <div>
          <h1>Mot de passe oublié</h1>
          <p>Recevez un lien pour définir un nouveau mot de passe.</p>
        </div>

        <div v-if="message" class="alert alert-success py-2 mb-0">{{ message }}</div>
        <div v-if="error" class="alert alert-danger py-2 mb-0">{{ error }}</div>

        <div class="row g-3">
          <div class="col-md">
            <label class="form-label" for="f1"
              >E-mail<span class="text-danger"> *</span></label
            >
            <input
              v-model="email"
              class="form-control"
              type="email"
              id="f1"
              name="e-mail"
              required
            />
          </div>
        </div>

        <p class="text-body-secondary mb-0">Le lien envoyé est valable 1 heure.</p>

        <div class="d-flex flex-wrap gap-2">
          <button type="submit" class="btn btn-primary" :disabled="loading">
            {{ loading ? "Envoi..." : "Envoyer le lien" }}
          </button>
          <router-link class="btn btn-outline-secondary" to="/auth/connexion">
            Retour à la connexion
          </router-link>
        </div>
      </form>
    </div>
  </main>
</template>
