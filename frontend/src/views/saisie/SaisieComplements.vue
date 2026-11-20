<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import SearchSelect from '../../components/SearchSelect.vue'
import { getItems, insertItem, getItemById } from '../../services/api'
import { useAccessError } from '../../composables/useAccessError'

usePage()
const { gererAcces } = useAccessError()
const route = useRoute()

const idOccurrence = Number(route.params.idOccurrence)
const idEvent = ref(0)
const idDataset = ref(0)
const occurrenceValide = Number.isInteger(idOccurrence) && idOccurrence > 0

const formatDate = (d) =>
    d ? new Date(d).toLocaleDateString('fr-FR') : '-'

const afficherValeur = (v) => String(v ?? '').replace('.', ',')

function debounce(fn, delay = 300) {
    let timer

    return (...args) => {
        clearTimeout(timer)
        timer = setTimeout(() => fn(...args), delay)
    }
}

/* ------------------------------------------------------------------ */
/* Tableau des mesures (pagination côté backend)                       */
/* ------------------------------------------------------------------ */

const mesures = ref([])
const total = ref(0)
const loading = ref(false)
const page = ref(1)
const perPage = ref(5)

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / perPage.value)))

let mesuresRequestId = 0
const occurrence = ref(null)
async function fetchOccurrence() {
    try {
        occurrence.value = await getItemById('occurrences', idOccurrence)
        idEvent.value = occurrence.value.event?.eventID || 0
        idDataset.value = occurrence.value.event?.dataset?.datasetID || 0
    } catch (error) {
        if (gererAcces(error)) return
        console.error("Erreur lors de la récupération de l'occurrence :", error)
    }
}

async function fetchMesures() {
    const requestId = ++mesuresRequestId

    loading.value = true

    try {
        const { items, total: totalCount } = await getItems(
            'measurement-or-facts',
            { 'occurrence.occurrenceID': idOccurrence },
            { page: page.value, perPage: perPage.value, sort: 'measurementDeterminedDate,desc' }
        )

        if (requestId !== mesuresRequestId) return

        // Page devenue vide : reculer d'une page
        if (items.length === 0 && page.value > 1) {
            page.value--
            return
        }

        mesures.value = items
        total.value = totalCount ?? 0

    } catch (e) {
        if (requestId !== mesuresRequestId) return

        console.error('Erreur chargement des mesures :', e)
        mesures.value = []
        total.value = 0

    } finally {
        if (requestId === mesuresRequestId) {
            loading.value = false
        }
    }
}

watch(perPage, () => {
    if (page.value !== 1) {
        page.value = 1
    } else {
        fetchMesures()
    }
})

watch(page, fetchMesures)

/* ------------------------------------------------------------------ */
/* Recherche du type et de l'unité (filtre côté backend)               */
/* ------------------------------------------------------------------ */

function createRemoteSearch(resource, field) {
    const items = ref([])
    const searching = ref(false)
    let requestId = 0

    async function search(q = '') {
        const id = ++requestId

        searching.value = true

        try {
            const filters = q ? { [field]: q } : {}

            const result = await getItems(resource, filters, {
                page: 1,
                perPage: 8,
                sort: `${field},asc`
            })

            if (id !== requestId) return

            items.value = result.items

        } catch (e) {
            if (id !== requestId) return

            console.error(`Erreur recherche (${resource}) :`, e)
            items.value = []

        } finally {
            if (id === requestId) {
                searching.value = false
            }
        }
    }

    return { items, searching, search, searchDebounced: debounce(search) }
}

const {
    items: types,
    searching: typesLoading,
    searchDebounced: searchTypes
} = createRemoteSearch('measurement-types', 'measurementType')

const {
    items: unites,
    searching: unitesLoading,
    searchDebounced: searchUnites
} = createRemoteSearch('measurement-units', 'measurementUnit')

/* ------------------------------------------------------------------ */
/* Formulaire d'ajout d'une mesure                                     */
/* ------------------------------------------------------------------ */

const showForm = ref(false)
const formRef = ref(null)
const saving = ref(false)
const success = ref('')
const error = ref('')

// Type : 'existant' (recherche dans la liste) ou 'nouveau' (création côté backend)
const typeMode = ref('existant')
const typeChoisi = ref(null)     // objet complet renvoyé par l'API, ou null
const typeNom = ref('')

// Unité : même principe
const uniteMode = ref('existant')
const uniteChoisie = ref(null)
const uniteNom = ref('')

const valeur = ref('')
const methode = ref('')
const precision = ref('')
const personne = ref('')
const dateDetermination = ref('')
const remarque = ref('')

const submitted = ref(false)
const comboKey = ref(0)          // change pour vider les listes de recherche

function ouvrirFormulaire() {
    success.value = ''
    error.value = ''
    showForm.value = true
}

function resetForm() {
    typeChoisi.value = null
    typeNom.value = ''
    uniteChoisie.value = null
    uniteNom.value = ''
    valeur.value = ''
    methode.value = ''
    precision.value = ''
    personne.value = ''
    dateDetermination.value = ''
    remarque.value = ''
    submitted.value = false
    comboKey.value++
    formRef.value?.classList.remove('was-validated')
    // les modes (existant / nouveau) sont conservés : on saisit souvent plusieurs lignes de suite
}

function fermerFormulaire() {
    showForm.value = false
    error.value = ''
    resetForm()
}

const nullSiVide = (s) => {
    const t = String(s ?? '').trim()
    return t === '' ? null : t
}

async function enregistrer() {
    success.value = ''
    error.value = ''
    submitted.value = true
    formRef.value.classList.add('was-validated')

    if (!formRef.value.checkValidity()) return

    if (typeMode.value === 'existant' && !typeChoisi.value) {
        error.value = 'Choisissez un type dans la liste.'
        return
    }
    if (uniteMode.value === 'existant' && !uniteChoisie.value) {
        error.value = 'Choisissez une unité dans la liste.'
        return
    }

    saving.value = true

    try {
        // Un seul appel : si le type ou l'unité est nouveau, on envoie son nom
        // sans identifiant et le backend le crée (ou réutilise celui qui existe déjà).
        await insertItem('measurement-or-facts', {
            occurrence: { occurrenceID: idOccurrence },

            measurementType: typeMode.value === 'existant'
                ? { measurementTypeID: typeChoisi.value.measurementTypeID }
                : { measurementType: typeNom.value.trim() },

            measurementUnit: uniteMode.value === 'existant'
                ? { measurementUnitID: uniteChoisie.value.measurementUnitID }
                : { measurementUnit: uniteNom.value.trim() },

            measurementValue: String(valeur.value),
            measurementMethod: nullSiVide(methode.value),
            measurementAccuracy: nullSiVide(precision.value),
            measurementDeterminedBy: nullSiVide(personne.value),
            measurementDeterminedDate: dateDetermination.value
                    ? `${dateDetermination.value}T00:00:00`
                    : null,
            measurementRemarks: nullSiVide(remarque.value)
        })

        success.value = 'Mesure enregistrée.'
        resetForm()

        // La nouvelle ligne apparaît en tête de liste
        if (page.value !== 1) {
            page.value = 1
        } else {
            fetchMesures()
        }

    } catch (e) {
        console.error("Erreur enregistrement de la mesure :", e)
        error.value = "Erreur lors de l'enregistrement de la mesure"
    } finally {
        saving.value = false
    }
}

onMounted(() => {
    if (!occurrenceValide) return
    fetchMesures()
    fetchOccurrence()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link to="/saisie/saisie-contexte">Saisie</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">Saisie : compléments</li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">Nouvelle observation</h1>

    <ul class="nav nav-tabs">
      <li class="nav-item">
        <router-link class="nav-link" :to="`/saisie/saisie-contexte/${idDataset}`">
          1 Contexte
        </router-link>
      </li>
      <li class="nav-item"><router-link class="nav-link" :to="`/saisie/saisie-observations/${idEvent}`">2 Observations</router-link></li>
      <li class="nav-item">
        <router-link class="nav-link" :to="`/saisie/saisie-identification/${idOccurrence}`">3 Identification</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link active" :to="`/saisie/saisie-complements/${idOccurrence}`">4 Compléments</router-link>
      </li>
    </ul>

    <!-- ID d'occurrence absent ou invalide -->
    <!-- <div v-if="!occurrenceValide" class="alert alert-warning mb-0" role="alert">
      Aucune occurrence sélectionnée. Commencez par l'étape
      <router-link to="/saisie/saisie-observations">Observations</router-link>.
    </div> -->

    <div>

      <!-- Liste des mesures -->
      <div class="card">
        <div class="card-header fw-semibold">Mesures de l'occurrence n° {{ idOccurrence }}</div>

        <div class="card-body d-grid gap-3">

          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col">Type</th>
                  <th scope="col">Valeur</th>
                  <th scope="col">Unité</th>
                  <th scope="col">Méthode</th>
                  <th scope="col">Précision</th>
                  <th scope="col">Date</th>
                </tr>
              </thead>

              <tbody>
                <tr v-if="loading">
                  <td colspan="7" class="text-center text-body-secondary">Chargement...</td>
                </tr>

                <tr v-else-if="mesures.length === 0">
                  <td colspan="7" class="text-center text-body-secondary">
                    Aucune mesure pour cette occurrence.
                  </td>
                </tr>

                <template v-else>
                  <tr v-for="m in mesures" :key="m.measurementOrFactID" :title="m.measurementRemarks || undefined">
                    <td>{{ m.measurementType?.measurementType ?? '-' }}</td>
                    <td>{{ afficherValeur(m.measurementValue) }}</td>
                    <td>{{ m.measurementUnit?.measurementUnit ?? '-' }}</td>
                    <td>{{ m.measurementMethod ?? '—' }}</td>
                    <td>{{ m.measurementAccuracy ?? '—' }}</td>
                    <td>{{ formatDate(m.measurementDeterminedDate) }}</td>
                  </tr>
                </template>
              </tbody>
            </table>
          </div>

          <!-- Pagination -->
          <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
            <small class="text-body-secondary">{{ total }} mesure(s)</small>

            <div class="d-flex align-items-center gap-2">
              <select
                class="form-select form-select-sm w-auto"
                v-model.number="perPage"
                aria-label="Mesures par page"
              >
                <option :value="5">5</option>
                <option :value="10">10</option>
                <option :value="20">20</option>
              </select>

              <button
                type="button"
                class="btn btn-sm btn-outline-secondary"
                :disabled="page <= 1 || loading"
                @click="page--"
              >
                Précédent
              </button>

              <span class="small">Page {{ page }} / {{ totalPages }}</span>

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

          <div v-if="success" class="alert alert-success mb-0" role="status">{{ success }}</div>

          <div v-if="!showForm" class="d-flex flex-wrap gap-2">
            <button type="button" class="btn btn-outline-secondary" @click="ouvrirFormulaire">
              + Ajouter une mesure
            </button>
          </div>

        </div>
      </div>

      <!-- Formulaire d'ajout d'une mesure -->
      <div v-if="showForm" class="card">
        <div class="card-header fw-semibold">Nouvelle mesure</div>

        <form
          ref="formRef"
          class="card-body d-grid gap-3 needs-validation"
          novalidate
          @submit.prevent="enregistrer"
        >

          <div class="row g-3">

            <!-- Type : nouveau ou existant -->
            <div class="col-md-6 d-grid gap-2 align-content-start">
              <div>
                <div class="form-label">Type de mesure</div>
                <div class="btn-group" role="group" aria-label="Type de mesure">
                  <input type="radio" class="btn-check" id="type-existant" value="existant"
                         v-model="typeMode" autocomplete="off" />
                  <label class="btn btn-outline-primary" for="type-existant">Type existant</label>

                  <input type="radio" class="btn-check" id="type-nouveau" value="nouveau"
                         v-model="typeMode" autocomplete="off" />
                  <label class="btn btn-outline-primary" for="type-nouveau">Nouveau type</label>
                </div>
              </div>

              <div v-if="typeMode === 'existant'">
                <label class="form-label" for="m-type">
                  Rechercher un type<span class="text-danger"> *</span>
                </label>
                <SearchSelect
                  :key="`type-${comboKey}`"
                  v-model="typeChoisi"
                  :items="types"
                  label-key="measurementType"
                  id-key="measurementTypeID"
                  input-id="m-type"
                  :invalid="submitted"
                  remote
                  :loading="typesLoading"
                  @search="searchTypes"
                />
              </div>

              <div v-else>
                <label class="form-label" for="m-type-nom">
                  Nom du type<span class="text-danger"> *</span>
                </label>
                <input id="m-type-nom" class="form-control" type="text" v-model="typeNom"
                       placeholder="ex. Longueur de l'aile" maxlength="250" required />
              </div>
            </div>

            <!-- Unité : nouvelle ou existante -->
            <div class="col-md-6 d-grid gap-2 align-content-start">
              <div>
                <div class="form-label">Unité</div>
                <div class="btn-group" role="group" aria-label="Unité">
                  <input type="radio" class="btn-check" id="unite-existant" value="existant"
                         v-model="uniteMode" autocomplete="off" />
                  <label class="btn btn-outline-primary" for="unite-existant">Unité existante</label>

                  <input type="radio" class="btn-check" id="unite-nouveau" value="nouveau"
                         v-model="uniteMode" autocomplete="off" />
                  <label class="btn btn-outline-primary" for="unite-nouveau">Nouvelle unité</label>
                </div>
              </div>

              <div v-if="uniteMode === 'existant'">
                <label class="form-label" for="m-unite">
                  Rechercher une unité<span class="text-danger"> *</span>
                </label>
                <SearchSelect
                  :key="`unite-${comboKey}`"
                  v-model="uniteChoisie"
                  :items="unites"
                  label-key="measurementUnit"
                  id-key="measurementUnitID"
                  input-id="m-unite"
                  :invalid="submitted"
                  remote
                  :loading="unitesLoading"
                  @search="searchUnites"
                />
              </div>

              <div v-else>
                <label class="form-label" for="m-unite-nom">
                  Nom de l'unité<span class="text-danger"> *</span>
                </label>
                <input id="m-unite-nom" class="form-control" type="text" v-model="uniteNom"
                       placeholder="ex. décimètre" maxlength="250" required />
              </div>
            </div>

          </div>

          <hr class="my-0" />

          <div class="row g-3">
            <div class="col-md">
              <label class="form-label" for="m-valeur">
                Valeur<span class="text-danger"> *</span>
              </label>
              <input id="m-valeur" class="form-control" type="number" step="any"
                     v-model="valeur" required />
            </div>

            <div class="col-md">
              <label class="form-label" for="m-methode">Méthode</label>
              <input id="m-methode" class="form-control" type="text" v-model="methode"
                     placeholder="ex. Mètre ruban" maxlength="250" />
            </div>

            <div class="col-md">
              <label class="form-label" for="m-precision">Précision</label>
              <input id="m-precision" class="form-control" type="text" v-model="precision"
                     placeholder="ex. ±0,5" maxlength="250" />
            </div>
          </div>

          <div class="row g-3">

            <div class="col-md">
              <label class="form-label" for="m-date">Date de la mesure</label>
              <input id="m-date" class="form-control" type="date" v-model="dateDetermination" />
            </div>

            <div class="col-md">
              <label class="form-label" for="m-remarque">Remarque</label>
              <input id="m-remarque" class="form-control" type="text" v-model="remarque" />
            </div>
          </div>

          <div v-if="error" class="alert alert-danger mb-0" role="alert">{{ error }}</div>

          <div class="d-flex flex-wrap gap-2">
            <button type="submit" class="btn btn-primary" :disabled="saving">
              {{ saving ? 'Enregistrement...' : 'Enregistrer la ligne' }}
            </button>
            <button type="button" class="btn btn-outline-secondary" :disabled="saving" @click="fermerFormulaire">
              Fermer
            </button>
          </div>

        </form>
      </div>

      <div class="card">
        <div class="card-header fw-semibold">Options avancées</div>
        <div class="card-body d-grid gap-3">
          <ul class="list-unstyled mb-0 d-grid gap-1">
            <li>▸ Contexte géologique (fossiles)</li>
            <li>▸ Interactions entre organismes</li>
            <li>▸ Matériel / collection</li>
            <li>▸ Analyse ADN</li>
          </ul>
        </div>
      </div>

      <div class="d-flex flex-wrap gap-2">
        <router-link class="btn btn-outline-secondary" :to="`/saisie/saisie-identification/${idOccurrence}`">
          Précédent
        </router-link>
      </div>

    </div>

  </main>
</template>