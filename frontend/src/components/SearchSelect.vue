<script setup>
import { ref, computed, watch } from 'vue'

/*
 * Choisir un élément existant :
 *  - clic dans le champ : la liste s'ouvre
 *  - on tape : la liste se filtre
 *  - clic sur un élément : il est choisi
 *
 * Deux modes :
 *  - local  (défaut) : le composant filtre `items` lui-même
 *  - remote          : le composant émet « search » et le parent recharge `items`
 *                      depuis le backend (le composant n'applique alors aucun filtre)
 *
 * v-model : null | l'objet complet de l'élément choisi
 */
const props = defineProps({
    modelValue: { type: Object, default: null },
    items: { type: Array, default: () => [] },
    labelKey: { type: String, default: 'label' },   // champ affiché
    idKey: { type: String, default: 'id' },         // clé unique
    inputId: { type: String, default: undefined },
    placeholder: { type: String, default: 'Taper un nom...' },
    invalid: { type: Boolean, default: false },
    remote: { type: Boolean, default: false },
    loading: { type: Boolean, default: false }
})

const emit = defineEmits(['update:modelValue', 'search'])

const labelDe = (item) => item?.[props.labelKey] ?? ''

const text = ref(labelDe(props.modelValue))
const open = ref(false)

watch(() => props.modelValue, (v) => {
    if (v) text.value = labelDe(v)
})

const filtered = computed(() => {
    if (props.remote) return props.items
    const q = text.value.trim().toLowerCase()
    if (!q || props.modelValue) return props.items
    return props.items.filter((i) => labelDe(i).toLowerCase().includes(q))
})

function onInput() {
    // Le texte a changé : l'ancien choix n'est plus valable
    emit('update:modelValue', null)
    open.value = true
    if (props.remote) emit('search', text.value.trim())
}

function onFocus() {
    open.value = true
    // Un choix est déjà fait : on recharge toute la liste pour pouvoir en changer
    if (props.remote) emit('search', props.modelValue ? '' : text.value.trim())
}

function choose(item) {
    emit('update:modelValue', item)
    text.value = labelDe(item)
    open.value = false
}
</script>

<template>
  <div class="position-relative">
    <input
      :id="inputId"
      type="text"
      class="form-control"
      :class="{ 'is-valid': modelValue, 'is-invalid': invalid && !modelValue }"
      :placeholder="placeholder"
      autocomplete="off"
      v-model="text"
      @input="onInput"
      @focus="onFocus"
      @blur="open = false"
      @keydown.esc="open = false"
      @keydown.enter.prevent
    />

    <ul
      v-if="open"
      class="list-group position-absolute w-100 shadow"
      style="z-index: 1050; max-height: 240px; overflow-y: auto"
    >
      <li v-if="loading" class="list-group-item text-body-secondary">Recherche...</li>

      <li v-else-if="filtered.length === 0" class="list-group-item text-body-secondary">
        Aucun résultat
      </li>

      <!-- mousedown.prevent : garde le focus, sinon le blur ferme la liste avant le clic -->
      <template v-else>
        <li
          v-for="i in filtered"
          :key="i[idKey]"
          class="list-group-item list-group-item-action"
          style="cursor: pointer"
          @mousedown.prevent
          @click="choose(i)"
        >
          {{ labelDe(i) }}
        </li>
      </template>
    </ul>
  </div>
</template>