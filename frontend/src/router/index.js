import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'
import { translate } from '../lib/i18n'
import app from '../config/app'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

// Reproduit les attributs que le template posait sur <body> : custom.css s'en sert
// (ex. body[data-page="1"] .auth-page) et le titre de l'onglet est traduit.
router.afterEach((to) => {
  const { page, active, layout, title } = to.meta
  document.body.dataset.page = String(page ?? '')
  document.body.dataset.active = active == null ? '' : String(active)
  if (layout === 'blank') document.body.dataset.layout = 'blank'
  else delete document.body.dataset.layout
  if (title) document.title = `${translate(title)} · ${app.name}`
})

export default router
