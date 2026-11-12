
<script setup>
import { usePage } from '../../composables/usePage'
import { ref, onMounted } from 'vue'
import { getItems } from '../../services/api'

usePage()

const licences = ref([])
const loadingList = ref(false)

async function loadLicences() {
    loadingList.value = true

    try {
        const response = await getItems('licences')

        // Prend en charge une réponse paginée ou une liste directe
        licences.value = Array.isArray(response)
            ? response
            : response.items || response.data || []

    } catch (e) {
        console.error('Erreur chargement des licences :', e)
        licences.value = []
    } finally {
        loadingList.value = false
    }
}

onMounted(loadLicences)
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <!-- Fil d'Ariane -->
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <router-link to="/accueil">Accueil</router-link>
        </li>
        <li class="breadcrumb-item">
          <router-link to="/info/aide">Aide</router-link>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Mentions légales et données
        </li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">Mentions légales et données</h1>

    <!-- Licences -->
    <div class="card">
      <div class="card-header fw-semibold">
        Licences des données
      </div>

      <div class="card-body d-grid gap-3">

        <p class="text-body-secondary mb-0">
          Chaque dataset est publié sous la licence choisie par son auteur
          (par exemple CC BY 4.0).
        </p>

        <ul class="list-unstyled mb-0 d-grid gap-1">
          <li>
            <span class="text-body-secondary">Détenteur des droits :</span>
            The Peregrine Fund
          </li>
        </ul>

        <div class="table-responsive">
          <table
            class="table table-hover align-middle mb-0"
            :class="{ 'opacity-50': loadingList }"
          >
            <thead>
              <tr>
                <th>Code</th>
                <th>Nom</th>
                <th>Description</th>
                <th>Statut</th>
                <th>URI</th>
              </tr>
            </thead>

            <tbody>
              <tr v-if="loadingList">
                <td colspan="5" class="text-center text-body-secondary">
                  Chargement...
                </td>
              </tr>

              <tr v-else-if="licences.length === 0">
                <td colspan="5" class="text-center text-body-secondary">
                  Aucune licence trouvée.
                </td>
              </tr>

              <tr
                v-for="licence in licences"
                :key="licence.licenceID"
              >
                <td>{{ licence.licenceCode }}</td>
                <td>{{ licence.licenceName }}</td>
                <td>{{ licence.description || '-' }}</td>

                <td>
                  <span
                    class="badge"
                    :class="licence.isActive
                      ? 'text-bg-success'
                      : 'text-bg-secondary'"
                  >
                    {{ licence.isActive ? 'Active' : 'Inactive' }}
                  </span>
                </td>

                <td>
                  <a
                    v-if="licence.licenceURI"
                    :href="licence.licenceURI"
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    Consulter
                  </a>
                  <span v-else>-</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>
    </div>

    <!-- Citation -->
    <div class="card">
      <div class="card-header fw-semibold">
        Citation recommandée
      </div>

      <div class="card-body d-grid gap-3">
        <p class="text-body-secondary mb-0">
          Auteurs (année). Titre du jeu de données. The Peregrine Fund.
        </p>
      </div>
    </div>

    <!-- Protection des données -->
    <div class="card">
      <div class="card-header fw-semibold">
        Protection des données
      </div>

      <div class="card-body d-grid gap-3">
        <p class="text-body-secondary mb-0">
          Les données personnelles des utilisateurs servent uniquement
          à gérer les comptes. Texte à valider avec The Peregrine Fund.
        </p>
      </div>
    </div>

    <!-- Contact -->
    <div class="card">
      <div class="card-header fw-semibold">
        Contact
      </div>

      <div class="card-body d-grid gap-3">
        <ul class="list-unstyled mb-0 d-grid gap-1">
          <li>
            <span class="text-body-secondary">Courriel :</span>
            contact@peregrine.example
          </li>
        </ul>
      </div>
    </div>

  </main>
</template>
