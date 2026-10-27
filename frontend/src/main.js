import { createApp } from 'vue'

// Styles : Bootstrap 5.3.3 + Bootstrap Icons 1.11.3 (npm), puis le thème « Forêt »
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.min.css'
import './assets/css/custom.css'

// JS de Bootstrap : active les attributs data-bs-* (dropdown, collapse, modal…)
import 'bootstrap'

import App from './App.vue'
import router from './router'
import { applyTheme } from './composables/useTheme'
import { language } from './lib/i18n'

applyTheme()
document.documentElement.lang = language

// On installe le router d'abord (il lance la navigation initiale), puis on monte l'app
// une fois la route résolue, pour que le bon layout soit affiché dès le premier rendu.
const app = createApp(App)
app.use(router)
router.isReady().then(() => app.mount('#app'))
