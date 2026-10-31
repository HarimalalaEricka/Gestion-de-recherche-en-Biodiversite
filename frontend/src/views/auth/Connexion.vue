<script setup>
import { usePage } from "../../composables/usePage";
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { login } from "../../services/authService";
usePage();
const router = useRouter();
const route = useRoute();

const email = ref("admin@nexora.mg");
const password = ref("Admin123!");
const error = ref("");
const loading = ref(false);

async function loginUser() {
  error.value = "";
  loading.value = true;
  try {
    await login(email.value, password.value);
    router.push(route.query.redirect || "/accueil");
  } catch (e) {
    error.value = e.message.includes("401")
      ? "E-mail ou mot de passe incorrect"
      : "Impossible de se connecter au serveur";
  } finally {
    loading.value = false;
  }
}
</script>
<template>
  <main id="content" class="auth-page">
    <div class="auth-card auth-login-card">
      <section class="auth-brand-panel">
        <div class="auth-brand">
          <img src="../../assets/img/logo.svg" alt="Logo Nexora" /> <span>Nexora</span>
        </div>
        <div class="auth-brand-content">
          <span class="auth-kicker">Gestion de recherche</span>
          <h1>Vos données de biodiversité, mieux organisées.</h1>
          <p>
            Un espace de travail simple pour suivre, structurer et valoriser vos
            recherches.
          </p>
        </div>
        <div class="auth-brand-note">
          <i class="bi bi-shield-check"></i> Un environnement pensé pour vos équipes
        </div>
      </section>
      <section class="auth-form-panel">
        <div class="auth-form-heading">
          <span class="auth-icon"><i class="bi bi-person"></i></span>
          <small data-autosave></small>
        </div>
        <form class="needs-validation d-grid gap-4" @submit.prevent="loginUser">
          <div>
            <span class="auth-kicker">Bienvenue</span>
            <h2>Connexion</h2>
            <p>Connectez-vous à votre espace Nexora.</p>
          </div>

          <div v-if="error" class="alert alert-danger py-2 mb-0">{{ error }}</div>

          <div class="row g-3">
            <div>
              <label class="form-label" for="f1"
                >E-mail<span class="text-danger">*</span></label
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
            <div>
              <label class="form-label" for="f2"
                >Mot de passe<span class="text-danger">*</span></label
              >
              <input
                v-model="password"
                class="form-control"
                type="password"
                id="f2"
                name="mot-de-passe"
                required
              />
            </div>
          </div>

          <div class="d-flex flex-wrap gap-2 auth-actions">
            <button type="submit" class="btn btn-primary" :disabled="loading">
              {{ loading ? "Connexion..." : "Se connecter" }}
            </button>
            <router-link class="btn btn-outline-secondary" to="/auth/mot-de-passe-oublie">
              Mot de passe oublié ?
            </router-link>
          </div>
        </form>
        <div class="auth-footer">
          <i class="bi bi-lock"></i> Accès sécurisé à votre espace de recherche
        </div>
      </section>
    </div>
  </main>
</template>
