import { getCurrentInstance, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { initPage } from '../lib/pageBehaviors'
import { translatePage } from '../lib/i18n'

/**
 * À appeler dans chaque vue (le <main> en est la racine).
 * Branche les comportements du template : tableaux triables/filtrables, formulaires
 * (validation + brouillon), concepteur de tableaux, zones de dépôt, data-demo/toast/confirm.
 */
export function usePage() {
  const route = useRoute()
  const router = useRouter()
  const instance = getCurrentInstance()

  onMounted(() => {
    const root = instance.proxy.$el
    initPage(root, { page: route.meta.page, route, router })
    translatePage(root)
  })
}
