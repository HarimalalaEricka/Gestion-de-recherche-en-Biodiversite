import { computed, ref } from 'vue'

// État de connexion : vrai réseau + simulation « hors ligne » du menu du profil.
const simulated = ref(localStorage.getItem('sim') === '1')
const online = ref(navigator.onLine)

addEventListener('online', () => (online.value = true))
addEventListener('offline', () => (online.value = false))

export const isOffline = computed(() => simulated.value || !online.value)
export const pendingDrafts = +(localStorage.getItem('drafts') || 3)

export function toggleSimulation() {
  simulated.value = !simulated.value
  localStorage.setItem('sim', simulated.value ? '1' : '0')
}
