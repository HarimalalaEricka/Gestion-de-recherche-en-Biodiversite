<script setup>
import { usePage } from "../../composables/usePage";
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { insertItem } from "../../services/api";

usePage();

const router = useRouter();

const sujet = ref("");
const dateDebut = ref("");
const dateFin = ref("");
const description = ref("");
const visibilite = ref("PRIVATE");
const message = ref('')
const error = ref('')
const loading = ref(false)

async function creerRecherche() {
  error.value = ''
  message.value = ''
  loading.value = true
  try {
    await insertItem('recherches/creer', {
      sujet: sujet.value,
      description: description.value,
      dateDebut: dateDebut.value,
      dateFin: dateFin.value,
      confidentialite: visibilite.value,
    })
    message.value = 'Recherche créée'
  } catch (err) {
    console.error('Erreur création recherche :', err)
    error.value = 'Impossible de créer la recherche'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item">
          <router-link to="/recherches/mes-recherches">Mes recherches</router-link>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          <router-link to="/recherches/nouvelle-recherche"
            >Nouvelle recherche</router-link
          >
        </li>
      </ol>
    </nav>
    <form class="needs-validation d-grid gap-4" @submit.prevent="creerRecherche">
      <small class="text-body-secondary text-end" data-autosave></small>
      <h1 class="h3 mb-0">Nouvelle recherche</h1>
      <div class="card">
        <div class="card-header fw-semibold">Informations</div>
        <div class="card-body d-grid gap-3">
          <div class="row g-3">
            <div class="col-md">
              <label class="form-label" for="f1"
                >Sujet<span class="text-danger"> *</span></label
              ><input
                class="form-control"
                type="text"
                id="f1"
                name="sujet"
                v-model="sujet"
                required
              />
            </div>
            <div class="col-md">
              <label class="form-label" for="f2"
                >Date de début<span class="text-danger"> *</span></label
              ><input
                class="form-control"
                type="date"
                id="f2"
                name="date-de-debut"
                v-model="dateDebut"
                required
              />
            </div>
            <div class="col-md">
              <label class="form-label" for="f3">Date de fin</label
              ><input
                class="form-control"
                type="date"
                id="f3"
                name="date-de-fin"
                v-model="dateFin"
              />
            </div>
          </div>
          <div class="row g-3">
            <div class="col-md">
              <label class="form-label" for="f4">Description</label
              ><textarea class="form-control" id="f4" name="description" rows="4" v-model="description"></textarea>
            </div>
          </div>
        </div>
      </div>
      <div class="card">
        <div class="card-header fw-semibold">Visibilité</div>
        <div class="card-body d-grid gap-3">
          <div class="form-check">
            <input
              class="form-check-input"
              type="radio"
              name="visibilite"
              id="r0"
              v-model="visibilite"
              value="PRIVATE"
              checked
            /><label class="form-check-label" for="r0"
              >Privée : visible par moi et mes collaborateurs (par défaut)</label
            >
          </div>
          <div class="form-check">
            <input
              class="form-check-input"
              type="radio"
              name="visibilite"
              id="r1"
                v-model="visibilite"
                value="PUBLIC"
            /><label class="form-check-label" for="r1"
              >Publiée : visible par tous dans l&#x27;espace partagé</label
            >
          </div>
        </div>
      </div>
      <!-- <div class="card">
                <div class="card-header fw-semibold">Collaborateurs</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f5">Ajouter une personne</label><input
                                class="form-control" type="text" id="f5" name="ajouter-une-personne"
                                value="Rakoto Andry"></div>
                    </div>
                    <div class="card">
                        <div class="card-header"><input type="search" form="nf"
                                class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                                aria-label="Filtrer"></div>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0" data-table>
                                <thead>
                                    <tr>
                                        <th scope="col">Nom</th>
                                        <th scope="col">Rôle</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Rakoto Andry</td>
                                        <td>Chercheur principal</td>
                                    </tr>
                                    <tr>
                                        <td>Rasoa Voahangy</td>
                                        <td>Agent de saisie</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div> -->
      <div class="d-flex flex-wrap gap-2">
        <router-link class="btn btn-outline-secondary" to="/recherches/mes-recherches"
          >Annuler</router-link
        ><a
          class="btn btn-outline-secondary"
          href="/saisie/brouillons-hors-ligne"
          data-toast="Brouillon enregistré"
          >Enregistrer comme brouillon</a
        ><button
          type="submit"
          class="btn btn-primary"
          data-next="/recherches/mes-recherches"
          :disabled="loading || !!message"
        >
          {{ loading ? 'Création...' : 'Créer la recherche' }}
        </button>
      </div>
    </form>
  </main>
</template>
