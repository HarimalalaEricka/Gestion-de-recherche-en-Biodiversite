<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { Offcanvas } from "bootstrap";
import app from "../../config/app";
import { pagePath } from "../../router/pages";
import { language, toggleLanguage, translate as t } from "../../lib/i18n";
import { showToast } from "../../lib/ui";
import { theme, toggleTheme } from "../../composables/useTheme";
import { collapsed } from "../../composables/useSidebar";
import {
  isOffline,
  pendingDrafts,
  toggleSimulation,
} from "../../composables/useConnection";
import { removeToken } from "../../services/api";

const router = useRouter();
const search = ref("");

// Grand écran : replie la sidebar. Mobile : ouvre/ferme le menu (offcanvas).
function toggleSidebar() {
  if (window.matchMedia("(min-width: 992px)").matches) {
    collapsed.value = !collapsed.value;
    return;
  }
  Offcanvas.getOrCreateInstance(document.getElementById("sb")).toggle();
}

function submitSearch() {
  router.push({ path: pagePath(29), query: { q: search.value } });
}

function simulateOffline() {
  toggleSimulation();
  showToast(isOffline.value ? "Mode hors ligne simulé" : "Retour en ligne", "secondary");
}

function logout() {
  removeToken();
  router.push(pagePath(1));
}
</script>

<template>
  <header class="topbar">
    <div class="topbar-main">
      <button
        class="btn btn-outline-secondary"
        id="sidebar-toggle"
        aria-label="Menu"
        @click="toggleSidebar"
      >
        <i class="bi bi-layout-sidebar-inset"></i>
      </button>

      <form class="topsearch" role="search" @submit.prevent="submitSearch">
        <i class="bi bi-search"></i>
        <input
          v-model="search"
          name="q"
          class="form-control"
          type="search"
          :placeholder="t('Rechercher une espèce, un lieu, un dataset...')"
          :aria-label="t('Rechercher')"
        />
      </form>

      <span id="pill">
        <span v-if="isOffline" class="badge rounded-pill text-bg-warning">
          <i class="bi bi-wifi-off"></i>
          {{ t("Hors ligne") }} · {{ pendingDrafts }} {{ t("en attente") }}
        </span>
        <span v-else class="badge rounded-pill text-bg-success">
          <i class="bi bi-wifi"></i>
          {{ t("En ligne") }}
        </span>
      </span>

      <button
        class="btn btn-outline-secondary"
        aria-label="Mode sombre / clair"
        @click="toggleTheme"
      >
        <i class="bi" :class="theme === 'dark' ? 'bi-sun' : 'bi-moon-stars'"></i>
      </button>
      <button
        class="btn btn-outline-secondary"
        aria-label="Langue / Language"
        @click="toggleLanguage"
      >
        <i class="bi bi-translate"></i>
        {{ language === "en" ? "FR" : "EN" }}
      </button>

      <div class="dropdown">
        <button
          class="btn btn-outline-secondary position-relative"
          data-bs-toggle="dropdown"
          aria-label="Notifications"
        >
          <i class="bi bi-bell"></i>
          <span
            class="badge text-bg-success position-absolute top-0 start-100 translate-middle rounded-pill"
          >
            {{ app.notifs.length }}
          </span>
        </button>
        <ul class="dropdown-menu dropdown-menu-end shadow" style="width: 320px">
          <li v-for="(notification, index) in app.notifs" :key="index">
            <router-link class="dropdown-item small text-wrap" :to="pagePath(27)">
              <i class="bi me-2" :class="`bi-${notification.i}`"></i
              >{{ t(notification.t) }}
              <div class="text-body-secondary">{{ t(notification.d) }}</div>
            </router-link>
          </li>
          <li><hr class="dropdown-divider" /></li>
          <li>
            <router-link class="dropdown-item text-center small" :to="pagePath(27)">{{
              t("Tout voir")
            }}</router-link>
          </li>
        </ul>
      </div>

      <div class="dropdown profile-menu">
        <button class="profile-trigger" data-bs-toggle="dropdown">
          <span class="avatar">{{ app.user.ini }}</span>
          <span class="profile-summary d-none d-md-flex">
            <strong>{{ app.user.name }}</strong>
            <small>{{ t(app.user.role) }}</small>
          </span>
          <i class="bi bi-chevron-down profile-chevron"></i>
        </button>
        <ul class="dropdown-menu dropdown-menu-end shadow profile-dropdown">
          <li class="profile-dropdown-heading">
            <span class="avatar">{{ app.user.ini }}</span>
            <span>
              <strong>{{ app.user.name }}</strong>
              <small>{{ t(app.user.role) }}</small>
            </span>
          </li>
          <li><hr class="dropdown-divider" /></li>
          <li>
            <router-link class="dropdown-item" :to="pagePath(5)">
              <i class="bi bi-person me-2"></i>{{ t("Mon profil") }}
            </router-link>
          </li>
          <li>
            <router-link class="dropdown-item" :to="pagePath(23)">
              <i class="bi bi-cloud-arrow-up me-2"></i>{{ t("Brouillons hors ligne") }}
            </router-link>
          </li>
          <li>
            <button class="dropdown-item" @click="simulateOffline">
              <i class="bi bi-wifi-off me-2"></i>{{ t("Simuler hors ligne") }}
            </button>
          </li>
          <li><hr class="dropdown-divider" /></li>
          <li>
            <button class="dropdown-item" @click="logout">
              <i class="bi bi-box-arrow-right me-2"></i>{{ t("Déconnexion") }}
            </button>
            <!-- <router-link class="dropdown-item" @click="logout">
              <i class="bi bi-box-arrow-right me-2"></i>{{ t("Déconnexion") }}
            </router-link> -->
          </li>
        </ul>
      </div>
    </div>
  </header>
</template>
