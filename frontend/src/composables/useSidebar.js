import { ref, watch } from 'vue'

// Sidebar repliée (écrans larges). Mémorisé comme dans le template d'origine.
export const collapsed = ref(localStorage.getItem('collapsed') === '1')

watch(collapsed, (value) => localStorage.setItem('collapsed', value ? '1' : '0'))
