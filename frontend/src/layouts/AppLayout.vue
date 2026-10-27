<script setup>
import { onBeforeUnmount, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Offcanvas } from 'bootstrap'
import Sidebar from '../components/layout/Sidebar.vue'
import Topbar from '../components/layout/Topbar.vue'
import AppFooter from '../components/layout/AppFooter.vue'
import { collapsed } from '../composables/useSidebar'

const route = useRoute()

// custom.css utilise body.collapsed pour replier la sidebar
watch(collapsed, (value) => document.body.classList.toggle('collapsed', value), { immediate: true })
onBeforeUnmount(() => document.body.classList.remove('collapsed'))

// Mobile : referme le menu après un clic (le template rechargeait la page, pas Vue)
watch(
  () => route.fullPath,
  () => Offcanvas.getInstance(document.getElementById('sb'))?.hide(),
)
</script>

<template>
  <div class="app-shell">
    <Sidebar />
    <div class="app-main">
      <Topbar />
      <slot />
      <AppFooter />
    </div>
  </div>
</template>
