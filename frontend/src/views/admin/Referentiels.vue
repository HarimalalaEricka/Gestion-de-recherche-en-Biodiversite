
<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getItems } from '../../services/api'

usePage()

const route = useRoute()

// Données
const pays = ref([])
const provinces = ref([])
const protocols = ref([])

// Type de référentiel sélectionné
const type = computed(() => route.params.type || 'pays')

// Configuration des référentiels
const configurations = {
  pays: {
    titre: 'Pays',
    endpoint: 'pays',
    champ: 'pays',
    identifiant: 'paysID',
    colonnes: [
      { label: 'Pays', champ: 'pays' },
      { label: 'Utilisée par', champ: 'utilisee_par' }
    ]
  },

  provinces: {
    titre: 'Provinces',
    endpoint: 'provinces',
    champ: 'province',
    identifiant: 'provinceID',
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

const donnees = computed(() => {
  switch (type.value) {
    case 'provinces':
      return provinces.value

    case 'pays':
    default:
      return pays.value

    case 'protocols':
      return protocols.value
  }
})

function getValeur(objet, chemin) {
  return chemin.split('.').reduce(
    (valeur, cle) => valeur?.[cle],
    objet
  ) ?? '-'
}

async function fetchDonnees() {
  try {
    const { items } = await getItems(
      configuration.value.endpoint,
      {},
      {}
    )

    if (type.value === 'provinces') {
      provinces.value = items
    } else if (type.value === 'pays') {
      pays.value = items
    }
    else if (type.value === 'protocols') {
      protocols.value = items
    }

    console.log('Données récupérées:', items)

  } catch (error) {
    console.error(
      'Erreur lors de la récupération des données:',
      error
    )

    if (type.value === 'provinces') {
      provinces.value = []
    } else if (type.value === 'protocols') {
      protocols.value = []
    } else if (type.value === 'pays') {
      pays.value = []
    }
  }
}

// Charger les données à chaque changement d'onglet
watch(type, fetchDonnees, { immediate: true })

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
            <input type="search" class="form-control form-control-sm table-filter" placeholder="Filtrer..." aria-label="Filtrer" >
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0" data-table>
            <thead>
                <tr>
                <th v-for="colonne in configuration.colonnes" :key="colonne.champ" scope="col">
                    {{ colonne.label }}
                </th>

                <th scope="col"></th>
                </tr>
            </thead>

            <tbody>

                <tr v-if="donnees.length === 0">
                <td :colspan="configuration.colonnes.length + 1" class="text-center" >
                    Aucun élément disponible
                </td>
                </tr>

                <tr v-for="p in donnees" :key="p[configuration.identifiant]" >

                <td v-for="colonne in configuration.colonnes" :key="colonne.champ">
                    {{ getValeur(p, colonne.champ) }}
                </td>

                <td class="text-end">
                    <router-link class="btn btn-sm btn-outline-primary" :to="`/recherches/mes-recherches/${p[configuration.identifiant]}`" >
                        Ouvrir
                    </router-link>
                </td>
                </tr>
            </tbody>
            </table>
        </div>
        </div>
  </main>
</template>
