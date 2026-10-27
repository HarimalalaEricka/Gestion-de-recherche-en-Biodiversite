<script setup>
import app from '../../config/app'
import { pagePath } from '../../router/pages'
import { translate as t } from '../../lib/i18n'
</script>

<template>
  <footer class="app-foot">
    <span>{{ app.name }} · <span>{{ t('version') }}</span> {{ app.version }}</span>
    <router-link :to="pagePath(44)">{{ t('Aide') }}</router-link>
    <router-link :to="pagePath(45)">{{ t('Mentions légales') }}</router-link>
    <router-link :to="pagePath(45)">{{ t('Données et licences') }}</router-link>
  </footer>
</template>
