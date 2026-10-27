<script setup>
import { language, toggleLanguage } from '../lib/i18n'
import { theme, toggleTheme } from '../composables/useTheme'
</script>

<template>
  <slot />
  <div class="position-fixed top-0 end-0 m-3 d-flex gap-2">
    <button class="btn btn-sm btn-outline-secondary" aria-label="Mode sombre / clair" @click="toggleTheme">
      <i class="bi" :class="theme === 'dark' ? 'bi-sun' : 'bi-moon-stars'"></i>
    </button>
    <button class="btn btn-sm btn-outline-secondary" aria-label="Langue / Language" @click="toggleLanguage">
      <i class="bi bi-translate"></i>
      {{ language === 'en' ? 'FR' : 'EN' }}
    </button>
  </div>
</template>
