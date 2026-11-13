<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getItems } from '../../services/api'

usePage()

const route = useRoute()

const type = computed(() => route.params.type || 'pays')

const configurations = {
  pays: {
    titre: 'Pays',
    endpoint: 'pays',
    identifiant: 'paysID',
    filtreChamp: 'pays',
    colonnes: [
      { label: 'Pays', champ: 'pays' },
      { label: 'Utilisée par', champ: 'utilisee_par' }
    ]
  },

  provinces: {
    titre: 'Provinces',
    endpoint: 'provinces',
    identifiant: 'provinceID',
    filtreChamp: 'province',
    colonnes: [
      { label: 'Province', champ: 'province' },
      { label: 'Pays', champ: 'pays.pays' },
      { label: 'Utilisée par', champ: 'utilisee_par' }
    ]
  },

  protocols: {
    titre: 'Protocoles',
    endpoint: 'protocols',
    identifiant: 'protocolID',
    filtreChamp: 'protocolName',
    colonnes: [
      { label: 'Protocole', champ: 'protocolName' },
      { label: 'Description', champ: 'protocolDescription' },
      { label: 'Référence', champ: 'protocolReferences' },
      { label: 'Type', champ: 'protocolType.type' },
      { label: 'Remarque', champ: 'protocolRemarks' }
    ]
  }
}

const configuration = computed(() =>
  configurations[type.value] || configurations.pays
)

function getValeur(objet, chemin) {
  return chemin.split('.').reduce(
    (valeur, cle) => valeur?.[cle],
    objet
  ) ?? '-'
}

const donnees = ref([])
const total = ref(0)
const loading = ref(false)

const page = ref(1)
const perPage = ref(10)

const totalPages = computed(() =>
  Math.max(1, Math.ceil(total.value / perPage.value))
)

function debounce(fn, delay = 300) {
  let timer

  const debounced = (...args) => {
    clearTimeout(timer)
    timer = setTimeout(() => fn(...args), delay)
  }

  debounced.cancel = () => clearTimeout(timer)

  return debounced
}

const recherche = ref('')
const requete = ref('')

const applyRequete = debounce((v) => {
  requete.value = v.trim()
})

watch(recherche, applyRequete)

let requestId = 0

async function fetchDonnees() {
  const id = ++requestId

  loading.value = true

  try {
    const filters = {}

    if (requete.value) {
      filters[configuration.value.filtreChamp] = requete.value
    }

    const { items, total: t } = await getItems(
      configuration.value.endpoint,
      filters,
      {
        page: page.value,
        perPage: perPage.value
      }
    )

    if (id !== requestId) return

    if (items.length === 0 && page.value > 1) {
      page.value--
      return
    }

    donnees.value = items
    total.value = t ?? 0

  } catch (error) {
    if (id !== requestId) return

    console.error(
      'Erreur lors de la récupération des données:',
      error
    )

    donnees.value = []
    total.value = 0

  } finally {
    if (id === requestId) {
      loading.value = false
    }
  }
}

watch(type, () => {
  applyRequete.cancel()
  recherche.value = ''
  requete.value = ''
  donnees.value = []
  total.value = 0
})

watch([type, requete, perPage], () => {
  if (page.value !== 1) {
    page.value = 1 
  } else {
    fetchDonnees()
  }
})

watch(page, fetchDonnees)

onMounted(fetchDonnees)
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <!-- Fil d'Ariane -->
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <router-link to="/accueil"> Accueil</router-link>
        </li>
        <li class="breadcrumb-item">
          <router-link to="/admin/utilisateurs"> Administration</router-link>
        </li>
        <li class="breadcrumb-item">
          <router-link to="/admin/referentiels/pays"> Référentiels</router-link>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          {{ configuration.titre }}
        </li>
      </ol>
    </nav>

    <!-- Titre -->
    <h1 class="h3 mb-0">
      Référentiels
    </h1>

    <!-- Onglets -->
    <ul class="nav nav-tabs">
      <li class="nav-item">
        <router-link class="nav-link" :class="{ active: type === 'pays' }" to="/admin/referentiels/pays"> Pays</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :class="{ active: type === 'provinces' }" to="/admin/referentiels/provinces"> Provinces</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :class="{ active: type === 'rangs' }" to="/admin/referentiels/rangs"> Rangs</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :class="{ active: type === 'types-de-mesure' }" to="/admin/referentiels/types-de-mesure"> Types de mesure</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :class="{ active: type === 'unites' }" to="/admin/referentiels/unites"> Unités</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :class="{ active: type === 'protocols' }" to="/admin/referentiels/protocols"> Protocoles</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :class="{ active: type === 'materiel' }" to="/admin/referentiels/materiel"> Matériel</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" :class="{ active: type === 'interactions' }" to="/admin/referentiels/interactions"> Interactions</router-link>
      </li>
    </ul>

    <!-- Bouton Ajouter -->
    <div class="d-flex flex-wrap gap-2 justify-content-end">
      <button class="btn btn-primary" type="button">
        + Ajouter
      </button>
    </div>

    <!-- Tableau -->
    <div class="card">
      <div class="card-header">
        <input
          v-model="recherche"
          type="search"
          class="form-control form-control-sm table-filter"
          placeholder="Filtrer..."
          aria-label="Filtrer"
        />
      </div>

      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead>
            <tr>
              <th
                v-for="colonne in configuration.colonnes"
                :key="colonne.champ"
                scope="col"
              >
                {{ colonne.label }}
              </th>
              <th scope="col"></th>
            </tr>
          </thead>

          <tbody>

            <tr v-if="loading">
              <td
                :colspan="configuration.colonnes.length + 1"
                class="text-center text-body-secondary"
              >
                Chargement...
              </td>
            </tr>

            <tr v-else-if="donnees.length === 0">
              <td
                :colspan="configuration.colonnes.length + 1"
                class="text-center text-body-secondary"
              >
                Aucun élément disponible
              </td>
            </tr>

            <template v-else>
              <tr
                v-for="p in donnees"
                :key="p[configuration.identifiant]"
              >
                <td
                  v-for="colonne in configuration.colonnes"
                  :key="colonne.champ"
                >
                  {{ getValeur(p, colonne.champ) }}
                </td>

                <td class="text-end">
                  <router-link
                    class="btn btn-sm btn-outline-primary"
                    :to="`/recherches/mes-recherches/${p[configuration.identifiant]}`"
                  >
                    Ouvrir
                  </router-link>
                </td>
              </tr>
            </template>

          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div
        class="card-footer d-flex flex-wrap justify-content-between align-items-center gap-2"
      >
        <small class="text-body-secondary">
          {{ total }} élément(s)
        </small>

        <div class="d-flex align-items-center gap-2">

          <select
            class="form-select form-select-sm w-auto"
            v-model.number="perPage"
            aria-label="Éléments par page"
          >
            <option :value="5">5</option>
            <option :value="10">10</option>
            <option :value="20">20</option>
            <option :value="50">50</option>
          </select>

          <button
            type="button"
            class="btn btn-sm btn-outline-secondary"
            :disabled="page <= 1 || loading"
            @click="page--"
          >
            Précédent
          </button>

          <span class="small">
            Page {{ page }} / {{ totalPages }}
          </span>

          <button
            type="button"
            class="btn btn-sm btn-outline-secondary"
            :disabled="page >= totalPages || loading"
            @click="page++"
          >
            Suivant
          </button>

        </div>
      </div>
    </div>
  </main>
</template>