import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'
import { translate } from '../lib/i18n'
import app from '../config/app'
import { isAuthenticated } from '../services/api'

const LOGIN_PATH = '/auth/connexion'   // à adapter au chemin réel de Connexion.vue
const HOME_PATH = '/accueil'

const ADMIN_PAGES = [36, 37, 38, 39, 40, 41]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

function getUser() {
  try {
    const brut = JSON.parse(localStorage.getItem('userConnected') || '{}')
    return brut.data ?? brut   
  } catch {
    return {}
  }
}

// Protection des pages : tout est privé sauf les routes marquées meta.public
router.beforeEach((to) => {
  const connecte = isAuthenticated()
  const estPublique = to.matched.some(route => route.meta.public)

  if (!estPublique && !connecte) {
    return { path: LOGIN_PATH, query: { redirect: to.fullPath } }
  }

  if (to.path === LOGIN_PATH && connecte) {
    return { path: HOME_PATH }
  }

  // Protection par rôle
  const roleRequis =
    to.matched.map(r => r.meta.role).find(Boolean) ||
    (ADMIN_PAGES.includes(to.meta.active) ? 'ADMIN' : null)

  if (roleRequis && getUser().role !== roleRequis) {
    return { path: HOME_PATH }
  }
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