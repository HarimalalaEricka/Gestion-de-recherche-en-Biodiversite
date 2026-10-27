<script setup>
import { useRoute } from 'vue-router'
import app from '../../config/app'
import { pagePath } from '../../router/pages'
import { translate as t } from '../../lib/i18n'
import logo from '../../assets/img/logo.svg'

const route = useRoute()
</script>

<template>
  <aside id="sb" class="sidebar offcanvas-lg offcanvas-start" tabindex="-1">
    <div class="brand">
      <div class="mark">
        <img :src="logo" alt="" width="64" height="64" />
      </div>
      <div class="brand-t">
        <b>{{ app.name }}</b>
        <span>{{ app.org }}</span>
      </div>
    </div>
    <nav class="side-nav">
      <template v-for="(item, index) in app.menu" :key="index">
        <div v-if="item.l" class="side-label">{{ t(item.l) }}</div>
        <router-link
          v-else
          class="side-item"
          :class="{ active: item.p === route.meta.active }"
          :to="pagePath(item.p)"
          :title="t(item.t)"
        >
          <i class="bi" :class="`bi-${item.i}`"></i>
          <span>{{ t(item.t) }}</span>
          <span v-if="item.b" class="badge text-bg-warning ms-auto">{{ item.b }}</span>
        </router-link>
      </template>
    </nav>
  </aside>
</template>
