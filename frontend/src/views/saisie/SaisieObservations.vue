<script setup>
import { usePage } from '../../composables/usePage'
import { useAccessError } from '../../composables/useAccessError'
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getItemById, getItems, insertItem } from '../../services/api'

usePage()

const route = useRoute()
const { gererAcces } = useAccessError()

const idEvent = Number(route.params.idEvent)
const eventValide = Number.isInteger(idEvent) && idEvent > 0

/* ------------------------------------------------------------------ */
/* Listes (valeurs alignées avec les enums du backend)                 */
/* ------------------------------------------------------------------ */

const STATUT_PRESENT = 'DETECTED'
const STATUT_ABSENT = 'NOT_DETECTED'

const STATUTS = [
    { value: STATUT_PRESENT, label: 'Présent' },
    { value: STATUT_ABSENT, label: 'Absent' }
]

const TYPES = [
    { value: 'FOSSIL_SPECIMEN', label: 'Fossile' },
    { value: 'HUMAN_OBSERVATION', label: 'Observation humaine' },
    { value: 'MACHINE_OBSERVATION', label: 'Observation machine' },
    { value: 'PRESERVED_SPECIMEN', label: 'Spécimen conservé' },
    { value: 'MATERIAL_SPECIMEN', label: 'Spécimen matériel' },
    { value: 'LIVING_SPECIMEN', label: 'Spécimen vivant' },
    { value: 'MATERIAL_SAMPLE', label: 'Échantillon matériel' }
]

const PORTEES = [
    { value: 'INDIVIDUAL', label: 'Individu' },
    { value: 'GROUP', label: 'Groupe' },
    { value: 'COLONY', label: 'Colonie' },
    { value: 'POPULATION', label: 'Population' }
]

const labelDe = (liste, v) =>
    liste.find((x) => x.value === v)?.label ?? v ?? '-'

const formatDate = (d) =>
    d ? new Date(d).toLocaleDateString('fr-FR') : '-'

function debounce(fn, delay = 300) {
    let timer

    return (...args) => {
        clearTimeout(timer)
        timer = setTimeout(() => fn(...args), delay)
    }
}

/* ------------------------------------------------------------------ */
/* Événement courant                                                   */
/* ------------------------------------------------------------------ */

const evenement = ref(null)

async function loadEvenement() {
    try {
        evenement.value = await getItemById(`events/${idEvent}`)
    } catch (e) {
        if (gererAcces(e)) return
        console.error("Erreur chargement de l'événement :", e)
    }
}

// Le dataset vient de l'événement, pas de l'URL
const datasetId = computed(() => evenement.value?.dataset?.datasetID ?? null)

// Onglets et boutons : l'ID de l'événement est ajouté au chemin
const versOnglet = (base) => `${base}/${idEvent}`

const versContexte = computed(() =>
    datasetId.value
        ? `/saisie/saisie-contexte/${datasetId.value}`
        : '/saisie/saisie-contexte'
)

/* ------------------------------------------------------------------ */
/* Liste des occurrences (pagination côté backend)                     */
/* ------------------------------------------------------------------ */

const occurrences = ref([])
const occTotal = ref(0)
const occLoading = ref(false)
const occPage = ref(1)
const occPerPage = ref(10)

const occTotalPages = computed(() =>
    Math.max(1, Math.ceil(occTotal.value / occPerPage.value))
)

let occRequestId = 0

async function fetchOccurrences() {
    const requestId = ++occRequestId

    occLoading.value = true

    try {
        const { items, total } = await getItems(
            'occurrences',
            { 'event.eventID': idEvent },
            { page: occPage.value, perPage: occPerPage.value, sort: 'occurrenceID,desc' }
        )

        if (requestId !== occRequestId) return

        // Page devenue vide : reculer d'une page
        if (items.length === 0 && occPage.value > 1) {
            occPage.value--
            return
        }

        occurrences.value = items
        occTotal.value = total ?? 0

    } catch (e) {
        if (requestId !== occRequestId) return

        console.error('Erreur chargement des occurrences :', e)
        occurrences.value = []
        occTotal.value = 0

    } finally {
        if (requestId === occRequestId) {
            occLoading.value = false
        }
    }
}

watch(occPerPage, () => {
    if (occPage.value !== 1) {
        occPage.value = 1
    } else {
        fetchOccurrences()
    }
})

watch(occPage, fetchOccurrences)

/* ------------------------------------------------------------------ */
/* Formulaire d'ajout d'une occurrence                                 */
/* ------------------------------------------------------------------ */

const showForm = ref(false)
const formRef = ref(null)
const saving = ref(false)
const error = ref('')
const success = ref('')

const mode = ref('nouveau')   // 'nouveau' | 'existant'

// Nouvel organisme
const organismName = ref('')
const organismScope = ref('')
const causeOfDeath = ref('')
const organismRemarks = ref('')

// Organisme existant (recherche avec suggestions, côté backend)
const organismInput = ref('')
const organismSelected = ref(null)
const organismResults = ref([])
const organismOpen = ref(false)
const organismLoading = ref(false)

// Occurrence
const occurrenceStatus = ref(STATUT_PRESENT)
const basisOfRecord = ref('HUMAN_OBSERVATION')
const individualCount = ref('')

// Une occurrence « absente » n'a pas de nombre d'individus
watch(occurrenceStatus, (s) => {
    if (s === STATUT_ABSENT) individualCount.value = ''
})

const nomOrganisme = (o) =>
    o?.organismName || (o?.organismID ? `Organisme #${o.organismID}` : '-')

let organismRequestId = 0

async function searchOrganisms() {
    const requestId = ++organismRequestId

    organismLoading.value = true

    try {
        const filters = {}
        const q = organismInput.value.trim()

        if (q) {
            filters.organismName = q
        }

        const { items } = await getItems('organisms', filters, {
            page: 1,
            perPage: 8,
            sort: 'organismName,asc'
        })

        if (requestId !== organismRequestId) return

        organismResults.value = items

    } catch (e) {
        if (requestId !== organismRequestId) return

        console.error('Erreur recherche des organismes :', e)
        organismResults.value = []

    } finally {
        if (requestId === organismRequestId) {
            organismLoading.value = false
        }
    }
}

const searchOrganismsDebounced = debounce(searchOrganisms)

function onOrganismInput() {
    // Le texte a changé : l'ancien choix n'est plus valable
    organismSelected.value = null
    organismOpen.value = true
    searchOrganismsDebounced()
}

function onOrganismFocus() {
    organismOpen.value = true
    searchOrganisms()
}

function chooseOrganism(o) {
    organismSelected.value = o
    organismInput.value = nomOrganisme(o)
    organismOpen.value = false
}

function ouvrirFormulaire() {
    error.value = ''
    success.value = ''
    showForm.value = true
}

function resetForm() {
    organismName.value = ''
    organismScope.value = ''
    causeOfDeath.value = ''
    organismRemarks.value = ''
    organismInput.value = ''
    organismSelected.value = null
    organismResults.value = []
    individualCount.value = ''
    formRef.value?.classList.remove('was-validated')
    // statut et type d'enregistrement conservés : on saisit souvent plusieurs lignes de suite
}

function fermerFormulaire() {
    showForm.value = false
    resetForm()
}

async function enregistrer() {
    error.value = ''
    success.value = ''

    if (!formRef.value.checkValidity()) {
        formRef.value.classList.add('was-validated')
        return
    }

    if (mode.value === 'existant' && !organismSelected.value) {
        error.value = 'Choisissez un organisme dans la liste.'
        return
    }

    saving.value = true

    try {
        let organismID = organismSelected.value?.organismID

        // 1) Nouvel organisme
        if (mode.value === 'nouveau') {
            const organism = await insertItem('organisms', {
                organismName: organismName.value.trim() || null,
                organismScope: organismScope.value,
                causeOfDeath: causeOfDeath.value.trim() || null,
                organismRemarks: organismRemarks.value.trim() || null
            })
            organismID = organism.organismID
        }

        // 2) L'occurrence, rattachée à l'organisme et à l'événement
        const occurrenceinsert = await insertItem('occurrences', {
            individualCount:
                occurrenceStatus.value === STATUT_ABSENT || individualCount.value === ''
                    ? null
                    : Number(individualCount.value),
            occurrenceStatus: occurrenceStatus.value,
            basisOfRecord: basisOfRecord.value,
            organism: { organismID },
            event: { eventID: idEvent }
        })

        success.value = 'Occurrence enregistrée.'
        resetForm()

        // La nouvelle ligne apparaît en tête de liste
        if (occPage.value !== 1) {
            occPage.value = 1
        } else {
            fetchOccurrences()
        }

    } catch (e) {
        console.error("Erreur enregistrement de l'occurrence :", e)
        error.value = "Erreur lors de l'enregistrement de l'occurrence"
    } finally {
        saving.value = false
    }
}

onMounted(() => {
    if (!eventValide) return
    loadEvenement()
    fetchOccurrences()
})
</script>
<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link :to="versContexte">Saisie</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">
          Saisie : observations
        </li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">Nouvelle observation</h1>

    <ul class="nav nav-tabs">
        <li class="nav-item">
            <span class="nav-link disabled">1 Contexte</span>
        </li>
        <li class="nav-item">
            <router-link class="nav-link active" :to="versOnglet('/saisie/saisie-observations')">2 Observations</router-link>
        </li>
        <li class="nav-item">
          <span class="nav-link disabled" aria-disabled="true">3 Identification</span>
        </li>
        <li class="nav-item">
          <span class="nav-link disabled" aria-disabled="true">4 Compléments</span>
        </li>
    </ul>

    <!-- ID d'événement absent ou invalide -->
    <div v-if="!eventValide" class="alert alert-warning mb-0" role="alert">
      Aucun événement sélectionné. Commencez par l'étape
      <router-link to="/saisie/saisie-contexte">Contexte</router-link>.
    </div>

    <template v-else>

      <!-- Rappel de l'événement -->
      <ul class="list-unstyled mb-0 d-grid gap-1">
        <li>
          <span class="text-body-secondary">Date :</span>
          {{ formatDate(evenement?.eventDate) }}
        </li>
        <li>
          <span class="text-body-secondary">Lieu :</span>
          {{ evenement?.location?.municipality || '-' }}
          <span v-if="evenement?.location?.locality">({{ evenement.location.locality }})</span>
        </li>
        <li>
          <span class="text-body-secondary">Protocole :</span>
          {{ evenement?.protocol?.protocolName || '-' }}
        </li>
      </ul>

      <!-- Liste des occurrences -->
      <div class="card">
        <div class="card-header fw-semibold">
          Une ligne = une occurrence enregistrée
        </div>

        <div class="card-body d-grid gap-3">

          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead>
                <tr>
                  <th scope="col">Organisme</th>
                  <th scope="col">Détection</th>
                  <th scope="col">Type d'enregistrement</th>
                  <th scope="col">Nombre d'individus</th>
                  <th scope="col">Date de l'événement</th>
                  <th scope="col">Lieu</th>
                  <th scope="col"></th>
                </tr>
              </thead>

              <tbody>

                <tr v-if="occLoading">
                  <td colspan="6" class="text-center text-body-secondary">Chargement...</td>
                </tr>

                <tr v-else-if="occurrences.length === 0">
                  <td colspan="6" class="text-center text-body-secondary">
                    Aucune occurrence pour cet événement.
                  </td>
                </tr>

                <template v-else>
                  <tr v-for="o in occurrences" :key="o.occurrenceID">
                    <td>{{ nomOrganisme(o.organism) }}</td>
                    <td>
                      <span
                        class="badge"
                        :class="o.occurrenceStatus === 'ABSENT' ? 'text-bg-secondary' : 'text-bg-success'"
                      >
                        {{ labelDe(STATUTS, o.occurrenceStatus) }}
                      </span>
                    </td>
                    <td>{{ labelDe(TYPES, o.basisOfRecord) }}</td>
                    <td>{{ o.individualCount ?? '—' }}</td>
                    <td>{{ formatDate(o.event?.eventDate ?? evenement?.eventDate) }}</td>
                    <td>{{ o.event?.location?.municipality ?? evenement?.location?.municipality ?? '-' }}</td>
                    <td>
                      <router-link
                        class="btn btn-sm btn-outline-secondary"
                        :to="`/partage/detail-d-une-observation/${o.occurrenceID}`"
                      >
                        Détails
                      </router-link>
                      <router-link
                        class="btn btn-sm btn-outline-primary"
                        :to="`/saisie/saisie-identification/${o.occurrenceID}`"
                        >
                        Identification
                      </router-link>
                    </td>
                  </tr>
                </template>

              </tbody>
            </table>
          </div>

          <!-- Pagination -->
          <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
            <small class="text-body-secondary">{{ occTotal }} occurrence(s)</small>

            <div class="d-flex align-items-center gap-2">
              <select
                class="form-select form-select-sm w-auto"
                v-model.number="occPerPage"
                aria-label="Occurrences par page"
              >
                <option :value="5">5</option>
                <option :value="10">10</option>
                <option :value="20">20</option>
              </select>

              <button
                type="button"
                class="btn btn-sm btn-outline-secondary"
                :disabled="occPage <= 1 || occLoading"
                @click="occPage--"
              >
                Précédent
              </button>

              <span class="small">Page {{ occPage }} / {{ occTotalPages }}</span>

              <button
                type="button"
                class="btn btn-sm btn-outline-secondary"
                :disabled="occPage >= occTotalPages || occLoading"
                @click="occPage++"
              >
                Suivant
              </button>
            </div>
          </div>

          <div v-if="success" class="alert alert-success mb-0" role="status">{{ success }}</div>

          <div v-if="!showForm" class="d-flex flex-wrap gap-2">
            <button type="button" class="btn btn-outline-secondary" @click="ouvrirFormulaire">
              + Ajouter une occurrence
            </button>
          </div>

        </div>
      </div>

      <!-- Formulaire d'ajout -->
      <div v-if="showForm" class="card">
        <div class="card-header fw-semibold">Nouvelle occurrence</div>

        <form
          ref="formRef"
          class="card-body d-grid gap-3 needs-validation"
          novalidate
          @submit.prevent="enregistrer"
        >

          <!-- Organisme : nouveau ou existant -->
          <div>
            <div class="form-label">Organisme</div>
            <div class="btn-group" role="group" aria-label="Type d'organisme">
              <input type="radio" class="btn-check" id="mode-nouveau" value="nouveau"
                     v-model="mode" autocomplete="off" />
              <label class="btn btn-outline-primary" for="mode-nouveau">Nouvel organisme</label>

              <input type="radio" class="btn-check" id="mode-existant" value="existant"
                     v-model="mode" autocomplete="off" />
              <label class="btn btn-outline-primary" for="mode-existant">Organisme existant</label>
            </div>
          </div>

          <!-- Nouvel organisme -->
          <template v-if="mode === 'nouveau'">
            <div class="row g-3">
              <div class="col-md">
                <label class="form-label" for="o-nom">Nom / identifiant</label>
                <input id="o-nom" class="form-control" type="text" v-model="organismName"
                       placeholder="ex. IND-001" maxlength="250" />
              </div>
              <div class="col-md">
                <label class="form-label" for="o-portee">
                  Portée<span class="text-danger"> *</span>
                </label>
                <select id="o-portee" class="form-select" v-model="organismScope" required>
                  <option value="" disabled>-- Choisir --</option>
                  <option v-for="p in PORTEES" :key="p.value" :value="p.value">{{ p.label }}</option>
                </select>
              </div>
            </div>

            <div class="row g-3">
              <div class="col-md">
                <label class="form-label" for="o-cause">Cause du décès</label>
                <input id="o-cause" class="form-control" type="text" v-model="causeOfDeath" />
              </div>
              <div class="col-md">
                <label class="form-label" for="o-remarques">Remarques</label>
                <input id="o-remarques" class="form-control" type="text" v-model="organismRemarks" />
              </div>
            </div>
          </template>

          <!-- Organisme existant -->
          <div v-else>
            <label class="form-label" for="o-existant">
              Rechercher un organisme<span class="text-danger"> *</span>
            </label>

            <div class="position-relative">
              <input
                id="o-existant"
                type="text"
                class="form-control"
                :class="{ 'is-valid': organismSelected }"
                placeholder="Taper un nom..."
                autocomplete="off"
                v-model="organismInput"
                @input="onOrganismInput"
                @focus="onOrganismFocus"
                @blur="organismOpen = false"
                @keydown.esc="organismOpen = false"
                @keydown.enter.prevent
              />

              <ul
                v-if="organismOpen"
                class="list-group position-absolute w-100 shadow"
                style="z-index: 1050; max-height: 240px; overflow-y: auto"
              >
                <li v-if="organismLoading" class="list-group-item text-body-secondary">
                  Recherche...
                </li>
                <li v-else-if="organismResults.length === 0" class="list-group-item text-body-secondary">
                  Aucun organisme trouvé
                </li>
                <template v-else>
                  <!-- mousedown.prevent : garde le focus, sinon le blur ferme la liste avant le clic -->
                  <li
                    v-for="o in organismResults"
                    :key="o.organismID"
                    class="list-group-item list-group-item-action"
                    style="cursor: pointer"
                    @mousedown.prevent
                    @click="chooseOrganism(o)"
                  >
                    {{ nomOrganisme(o) }}
                    <small class="text-body-secondary ms-1">{{ labelDe(PORTEES, o.organismScope) }}</small>
                  </li>
                </template>
              </ul>
            </div>
          </div>

          <hr class="my-0" />

          <!-- Occurrence -->
          <div class="row g-3">
            <div class="col-md">
              <label class="form-label" for="oc-statut">
                Détection<span class="text-danger"> *</span>
              </label>
              <select id="oc-statut" class="form-select" v-model="occurrenceStatus" required>
                <option v-for="s in STATUTS" :key="s.value" :value="s.value">{{ s.label }}</option>
              </select>
            </div>

            <div class="col-md">
              <label class="form-label" for="oc-type">
                Type d'enregistrement<span class="text-danger"> *</span>
              </label>
              <select id="oc-type" class="form-select" v-model="basisOfRecord" required>
                <option v-for="t in TYPES" :key="t.value" :value="t.value">{{ t.label }}</option>
              </select>
            </div>

            <div class="col-md">
              <label class="form-label" for="oc-nb">Nombre d'individus</label>
              <input
                id="oc-nb"
                class="form-control"
                type="number"
                min="0"
                step="1"
                v-model="individualCount"
                :disabled="occurrenceStatus === 'ABSENT'"
              />
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
        <router-link class="btn btn-outline-secondary" :to="versContexte">Précédent</router-link>
        <router-link class="btn btn-outline-secondary" to="/saisie/brouillons-hors-ligne">
          Enregistrer le brouillon
        </router-link>
        <!-- <router-link class="btn btn-primary" :to="versOnglet('/saisie/saisie-identification')">
          Suivant : identification
        </router-link> -->
      </div>

    </template>

  </main>
</template>