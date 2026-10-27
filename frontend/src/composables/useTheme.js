import { ref, watch } from 'vue'

export const theme = ref(localStorage.getItem('theme') || 'light')

export function applyTheme() {
  document.documentElement.dataset.bsTheme = theme.value
}

watch(theme, (value) => {
  localStorage.setItem('theme', value)
  applyTheme()
})

export function toggleTheme() {
  theme.value = theme.value === 'dark' ? 'light' : 'dark'
}
