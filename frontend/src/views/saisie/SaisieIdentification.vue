
<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getItemById, getItems, insertItem } from '../../services/api'

usePage()

// URL de l'API GBIF, définie dans .env
const GBIF_API = (import.meta.env.VITE_GBIF_API_URL || '').replace(/\/$/, '')

const route = useRoute()
const idOccurrence = ref(route.params.idOccurrence)

/* ------------------------------------------------------------------ */
/* Helpers                                                             */
/* ------------------------------------------------------------------ */

function debounce(fn, delay = 300) {
    let timer

    return (...args) => {
        clearTimeout(timer)
        timer = setTimeout(() => fn(...args), delay)
    }
}

const formatDate = (d) =>
    d ? new Date(d).toLocaleDateString('fr-FR') : '-'

// Affichage du nom de l'agent ou de son identifiant
const nomAgent = (a) =>
    a ? [a.nom, a.prenom].filter(Boolean).join(' ') || `Agent #${a.agentID}` : '-'

// Récupération d'un utilisateur à partir de son ID
async function getutilisateurById(id) {
    if (!id) return null

    try {
        return await getItemById('utilisateurs', id)
    } catch (e) {
        console.error(`Erreur lors du chargement de l'utilisateur ${id} :`, e)
        return null
    }
}

// Valeur pour <input type="datetime-local">
function toDatetimeLocal(date = new Date()) {
    const p = (n) => String(n).padStart(2, '0')

    return `${date.getFullYear()}-${p(date.getMonth() + 1)}-${p(date.getDate())}`
        + `T${p(date.getHours())}:${p(date.getMinutes())}`
}

/* ------------------------------------------------------------------ */
/* Occurrence                                                           */
/* ------------------------------------------------------------------ */

const occurrence = ref(null)

async function fetchOccurrence() {
    try {
        occurrence.value = await getItemById('occurrences', idOccurrence.value)
    } catch (error) {
        console.error("Erreur lors de la récupération de l'occurrence :", error)
    }
}

/* ------------------------------------------------------------------ */
/* Liste des identifications                                           */
/* ------------------------------------------------------------------ */

const identifications = ref([])
const idTotal = ref(0)
const idLoading = ref(false)

const idPage = ref(1)
const idPerPage = ref(10)

const idTotalPages = computed(() =>
    Math.max(1, Math.ceil(idTotal.value / idPerPage.value))
)

let idRequestId = 0

async function fetchIdentifications() {
    const requestId = ++idRequestId

    idLoading.value = true

    try {
        const { items, total } = await getItems(
            'identifications',
            { 'occurrence.occurrenceID': idOccurrence.value },
            {
                page: idPage.value,
                perPage: idPerPage.value,
                sort: 'dateIdentified,desc'
            }
        )

        if (requestId !== idRequestId) return

        // Page devenue vide : revenir à la page précédente
        if (items.length === 0 && idPage.value > 1) {
            idPage.value--
            return
        }

        // Récupération de l'utilisateur à partir de :
        // identification -> agent -> utilisateurId
        identifications.value = await Promise.all(
            items.map(async (i) => {
                const utilisateur = await getutilisateurById(
                    i.agent?.utilisateurId
                )

                return {
                    ...i,
                    utilisateur
                }
            })
        )

        if (requestId !== idRequestId) return

        idTotal.value = total ?? 0

    } catch (e) {
        if (requestId !== idRequestId) return

        console.error('Erreur chargement des identifications :', e)

        identifications.value = []
        idTotal.value = 0

    } finally {
        if (requestId === idRequestId) {
            idLoading.value = false
        }
    }
}

// Nouvelle taille de page : retour page 1
watch(idPerPage, () => {
    if (idPage.value !== 1) {
        idPage.value = 1
    } else {
        fetchIdentifications()
    }
})

watch(idPage, fetchIdentifications)

/* ------------------------------------------------------------------ */
/* Recherche de taxon                                                   */
/* ------------------------------------------------------------------ */

const taxonInput = ref('')
const taxonResults = ref([])
const gbifResults = ref([])
const taxonOpen = ref(false)
const taxonLoading = ref(false)
const gbifLoading = ref(false)
const taxonSelected = ref(null)

let taxonRequestId = 0

const nomGbif = (g) => g.canonicalName || g.scientificName

async function fetchGbifSuggestions(q) {
    if (!GBIF_API) {
        console.warn('VITE_GBIF_API_URL absent du .env : recherche GBIF ignorée.')
        return []
    }

    const url = `${GBIF_API}/species/suggest?q=${encodeURIComponent(q)}&limit=10`

    console.debug('Requête GBIF :', url)

    const res = await fetch(url)

    if (!res.ok) throw new Error(`GBIF HTTP ${res.status}`)

    return res.json()
}

async function searchTaxons(texte) {
    const requestId = ++taxonRequestId
    const q = texte.trim()

    if (q.length < 2) {
        taxonResults.value = []
        gbifResults.value = []
        taxonLoading.value = false
        gbifLoading.value = false
        taxonOpen.value = false
        return
    }

    taxonOpen.value = true
    taxonResults.value = []
    gbifResults.value = []
    taxonLoading.value = true
    gbifLoading.value = true

    // 1. Recherche dans notre base
    let locaux = []

    try {
        const { items } = await getItems(
            'taxons',
            { scientificName: q },
            { page: 1, perPage: 10, sort: 'scientificName,asc' }
        )

        if (requestId !== taxonRequestId) return

        locaux = items
        taxonResults.value = items

    } catch (e) {
        if (requestId !== taxonRequestId) return

        console.error('Erreur recherche de taxon :', e)

    } finally {
        if (requestId === taxonRequestId) {
            taxonLoading.value = false
        }
    }

    // 2. Recherche dans GBIF
    try {
        const suggestions = await fetchGbifSuggestions(q)

        if (requestId !== taxonRequestId) return

        // Masquer les taxons déjà présents dans notre base
        const connus = new Set(
            locaux.map((t) => t.scientificName?.toLowerCase()).filter(Boolean)
        )

        gbifResults.value = suggestions.filter(
            (g) =>
                !connus.has(nomGbif(g)?.toLowerCase()) &&
                !connus.has(g.scientificName?.toLowerCase())
        )

    } catch (e) {
        if (requestId !== taxonRequestId) return

        console.error('Erreur recherche GBIF :', e)
        gbifResults.value = []

    } finally {
        if (requestId === taxonRequestId) {
            gbifLoading.value = false
        }
    }
}

const debouncedSearchTaxons = debounce(searchTaxons)

function onTaxonInput() {
    taxonSelected.value = null
    debouncedSearchTaxons(taxonInput.value)
}

function onTaxonFocus() {
    if (!taxonSelected.value && taxonInput.value.trim().length >= 2) {
        taxonOpen.value = true
    }
}

function chooseTaxon(t) {
    taxonSelected.value = {
        source: 'local',
        taxonID: t.taxonID,
        scientificName: t.scientificName,
        taxonRank: t.taxonRank
    }

    taxonInput.value = t.scientificName
    taxonOpen.value = false
}

function chooseGbifTaxon(g) {
    taxonSelected.value = {
        source: 'gbif',
        scientificName: nomGbif(g),
        taxonRank: g.rank,
        gbif: g
    }

    taxonInput.value = nomGbif(g)
    taxonOpen.value = false
}

// Données d'un taxon GBIF envoyées au backend
function taxonDepuisGbif(g) {
    return {
        gbifKey: g.key,
        scientificName: g.scientificName,
        taxonRank: { taxonRank: g.rank },
        kingdom: g.kingdom,
        phylum: g.phylum,
        class: g.class,
        order: g.order,
        family: g.family,
        genus: g.genus,
        specificEpithet: g.rank === 'SPECIES'
            ? g.canonicalName?.split(' ')[1]
            : undefined
    }
}

/* ------------------------------------------------------------------ */
/* Formulaire d'ajout                                                   */
/* ------------------------------------------------------------------ */

const showForm = ref(false)
const formRef = ref(null)

const dateIdentified = ref(toDatetimeLocal())
const identificationQualifier = ref('')
const isAcceptedIdentification = ref(true)

const saving = ref(false)
const error = ref('')
const success = ref('')

function resetFormulaire() {
    taxonInput.value = ''
    taxonResults.value = []
    gbifResults.value = []
    taxonLoading.value = false
    gbifLoading.value = false
    taxonOpen.value = false
    taxonSelected.value = null

    taxonRequestId++

    dateIdentified.value = toDatetimeLocal()
    identificationQualifier.value = ''
    isAcceptedIdentification.value = true

    error.value = ''
    formRef.value?.classList.remove('was-validated')
}

function ouvrirFormulaire() {
    success.value = ''
    resetFormulaire()
    showForm.value = true
}

function fermerFormulaire() {
    showForm.value = false
    resetFormulaire()
}

async function enregistrer() {
    error.value = ''
    success.value = ''

    formRef.value?.classList.add('was-validated')

    if (!taxonSelected.value) {
        error.value = 'Choisissez un taxon dans la liste de résultats.'
        return
    }

    if (!formRef.value?.checkValidity()) {
        error.value = 'Renseignez les champs obligatoires.'
        return
    }

    saving.value = true

    try {
        // Taxon local : envoi de son ID
        // Taxon GBIF : envoi de ses données
        const taxon =
            taxonSelected.value.source === 'gbif'
                ? taxonDepuisGbif(taxonSelected.value.gbif)
                : { taxonID: taxonSelected.value.taxonID }

        await insertItem('identifications', {
            dateIdentified: `${dateIdentified.value}:00`,
            identificationQualifier: identificationQualifier.value.trim() || null,
            isAcceptedIdentification: isAcceptedIdentification.value,
            taxon,
            occurrence: {
                occurrenceID: Number(idOccurrence.value)
            }
        })

        success.value = 'Identification enregistrée.'

        showForm.value = false
        resetFormulaire()

        // Retour à la première page pour afficher la nouvelle identification
        if (idPage.value !== 1) {
            idPage.value = 1
        } else {
            await fetchIdentifications()
        }

    } catch (e) {
        console.error("Erreur lors de l'enregistrement de l'identification :", e)

        error.value = "Impossible d'enregistrer l'identification. Réessayez."

    } finally {
        saving.value = false
    }
}

/* ------------------------------------------------------------------ */

onMounted(() => {
    fetchOccurrence()
    fetchIdentifications()
})
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item">
          <router-link to="/accueil">Accueil</router-link>
        </li>
        <li class="breadcrumb-item">
          <router-link to="/saisie/saisie-contexte">Saisie</router-link>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Saisie : identification
        </li>
      </ol>
    </nav>

    <h1 class="h3 mb-0">Nouvelle observation</h1>

    <ul class="nav nav-tabs">
      <li class="nav-item">
        <router-link class="nav-link" to="/saisie/saisie-contexte">
          1 Contexte
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/saisie/saisie-observations">
          2 Observations
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link active" to="/saisie/saisie-identification">
          3 Identification
        </router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/saisie/saisie-complements">
          4 Compléments
        </router-link>
      </li>
    </ul>

    <!-- Rappel de l'occurrence -->
    <ul class="list-unstyled mb-0 d-grid gap-1">
      <li>
        <span class="text-body-secondary">Organisme :</span>
        {{ occurrence?.organism?.organismName }}
      </li>
      <li>
        <span class="text-body-secondary">Détection :</span>
        {{ occurrence?.occurrenceStatus }}
      </li>
      <li>
        <span class="text-body-secondary">Date :</span>
        {{ formatDate(occurrence?.event?.eventDate) }}
      </li>
      <li>
        <span class="text-body-secondary">Lieu :</span>
        {{ occurrence?.event?.location?.municipality }}
      </li>
    </ul>

    <!-- Liste des identifications -->
    <div class="card">
      <div class="card-header fw-semibold">
        Une ligne = une identification enregistrée
      </div>

      <div class="card-body d-grid gap-3">

        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead>
              <tr>
                <th scope="col">Taxon</th>
                <th scope="col">Qualificatif</th>
                <th scope="col">Date d'identification</th>
                <th scope="col">Identifié par</th>
                <th scope="col">Statut</th>
                <th scope="col"></th>
              </tr>
            </thead>

            <tbody>
              <tr v-if="idLoading">
                <td colspan="6" class="text-center text-body-secondary">
                  Chargement...
                </td>
              </tr>

              <tr v-else-if="identifications.length === 0">
                <td colspan="6" class="text-center text-body-secondary">
                  Aucune identification pour cette occurrence.
                </td>
              </tr>

              <template v-else>
                <tr
                  v-for="i in identifications"
                  :key="i.identificationID"
                >
                  <td class="fst-italic">
                    {{ i.taxon?.scientificName || '-' }}
                  </td>

                  <td>{{ i.identificationQualifier || '—' }}</td>

                  <td>{{ formatDate(i.dateIdentified) }}</td>

                  <td>
                    {{ nomAgent(i.utilisateur || i.agent) }}
                  </td>

                  <td>
                    <span
                      class="badge"
                      :class="i.isAcceptedIdentification
                        ? 'text-bg-success'
                        : 'text-bg-secondary'"
                    >
                      {{ i.isAcceptedIdentification
                        ? 'Acceptée'
                        : 'Non acceptée' }}
                    </span>
                  </td>

                  <td class="text-end">
                    <router-link
                      class="btn btn-sm btn-outline-secondary"
                      :to="`/partage/fiche-d-un-taxon/${i.taxon?.taxonID}`"
                    >
                      Fiche du taxon
                    </router-link>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
          <small class="text-body-secondary">
            {{ idTotal }} identification(s)
          </small>

          <div class="d-flex align-items-center gap-2">
            <select
              class="form-select form-select-sm w-auto"
              v-model.number="idPerPage"
              aria-label="Identifications par page"
            >
              <option :value="5">5</option>
              <option :value="10">10</option>
              <option :value="20">20</option>
            </select>

            <button
              type="button"
              class="btn btn-sm btn-outline-secondary"
              :disabled="idPage <= 1 || idLoading"
              @click="idPage--"
            >
              Précédent
            </button>

            <span class="small">
              Page {{ idPage }} / {{ idTotalPages }}
            </span>

            <button
              type="button"
              class="btn btn-sm btn-outline-secondary"
              :disabled="idPage >= idTotalPages || idLoading"
              @click="idPage++"
            >
              Suivant
            </button>
          </div>
        </div>

        <div v-if="success" class="alert alert-success mb-0" role="status">
          {{ success }}
        </div>

        <div v-if="!showForm" class="d-flex flex-wrap gap-2">
          <button
            type="button"
            class="btn btn-outline-secondary"
            @click="ouvrirFormulaire"
          >
            + Ajouter une identification
          </button>
        </div>

      </div>
    </div>

    <!-- Formulaire d'ajout -->
    <div v-if="showForm" class="card">
      <div class="card-header fw-semibold">
        Nouvelle identification
      </div>

      <form
        ref="formRef"
        class="card-body d-grid gap-3 needs-validation"
        novalidate
        @submit.prevent="enregistrer"
      >

        <!-- Recherche d'un taxon -->
        <div>
          <label class="form-label" for="t-recherche">
            Taxon<span class="text-danger"> *</span>
          </label>

          <div class="position-relative">
            <input
              id="t-recherche"
              type="text"
              class="form-control"
              :class="{ 'is-valid': taxonSelected }"
              placeholder="Taper un nom scientifique (2 lettres minimum)..."
              autocomplete="off"
              v-model="taxonInput"
              @input="onTaxonInput"
              @focus="onTaxonFocus"
              @blur="taxonOpen = false"
              @keydown.esc="taxonOpen = false"
              @keydown.enter.prevent
            />

            <ul
              v-if="taxonOpen"
              class="list-group position-absolute w-100 shadow"
              style="z-index: 1050; max-height: 320px; overflow-y: auto"
            >
              <!-- Notre base -->
              <li
                v-if="taxonLoading"
                class="list-group-item text-body-secondary"
              >
                Recherche dans notre base...
              </li>

              <template v-else>
                <li
                  v-if="taxonResults.length"
                  class="list-group-item bg-body-tertiary small fw-semibold"
                >
                  Dans notre base
                </li>

                <li
                  v-for="t in taxonResults"
                  :key="`local-${t.taxonID}`"
                  class="list-group-item list-group-item-action d-flex justify-content-between align-items-center gap-2"
                  style="cursor: pointer"
                  @mousedown.prevent
                  @click="chooseTaxon(t)"
                >
                  <span>
                    <span class="fst-italic">{{ t.scientificName }}</span>
                    <small class="text-body-secondary ms-1">
                      {{ t.taxonRank }}
                    </small>
                  </span>

                  <router-link
                    class="small"
                    :to="`/partage/fiche-d-un-taxon/${t.taxonID}`"
                    target="_blank"
                    @click.stop
                  >
                    Voir la fiche ↗
                  </router-link>
                </li>
              </template>

              <!-- GBIF -->
              <li
                v-if="!taxonLoading && gbifLoading"
                class="list-group-item text-body-secondary"
              >
                Recherche dans GBIF...
              </li>

              <template v-else-if="!taxonLoading">
                <li
                  v-if="gbifResults.length"
                  class="list-group-item bg-body-tertiary small fw-semibold"
                >
                  GBIF (sera ajouté à notre base si vous le choisissez)
                </li>

                <li
                  v-for="g in gbifResults"
                  :key="`gbif-${g.key}`"
                  class="list-group-item list-group-item-action d-flex justify-content-between align-items-center gap-2"
                  style="cursor: pointer"
                  @mousedown.prevent
                  @click="chooseGbifTaxon(g)"
                >
                  <span>
                    <span class="fst-italic">{{ nomGbif(g) }}</span>

                    <small class="text-body-secondary ms-1">
                      {{ g.rank }}
                    </small>

                    <small
                      v-if="g.status && g.status !== 'ACCEPTED'"
                      class="text-warning ms-1"
                    >
                      {{ g.status }}
                    </small>
                  </span>

                  <a
                    class="small"
                    :href="`https://www.gbif.org/species/${g.key}`"
                    target="_blank"
                    rel="noopener"
                    @click.stop
                  >
                    Voir la fiche ↗
                  </a>
                </li>
              </template>

              <li
                v-if="!taxonLoading && !gbifLoading
                  && !taxonResults.length && !gbifResults.length"
                class="list-group-item text-body-secondary"
              >
                Aucun taxon trouvé
              </li>
            </ul>
          </div>

          <!-- Taxon choisi -->
          <div v-if="taxonSelected" class="form-text">
            Sélectionné :
            <span class="fst-italic">
              {{ taxonSelected.scientificName }}
            </span>

            <span v-if="taxonSelected.source === 'gbif'">
              (GBIF, ajouté à notre base à l'enregistrement)
            </span>

            ·

            <a
              v-if="taxonSelected.source === 'gbif'"
              :href="`https://www.gbif.org/species/${taxonSelected.gbif.key}`"
              target="_blank"
              rel="noopener"
            >
              Voir la fiche ↗
            </a>

            <router-link
              v-else
              :to="`/partage/fiche-d-un-taxon/${taxonSelected.taxonID}`"
              target="_blank"
            >
              Voir la fiche ↗
            </router-link>
          </div>
        </div>

        <div class="row g-3">
          <div class="col-md">
            <label class="form-label" for="i-date">
              Date d'identification<span class="text-danger"> *</span>
            </label>

            <input
              id="i-date"
              class="form-control"
              type="datetime-local"
              v-model="dateIdentified"
              required
            />
          </div>
        </div>

        <div class="row g-3 align-items-end">
          <div class="col-md">
            <label class="form-label" for="i-qualif">
              Qualificatif (cf., aff., ?)
            </label>

            <input
              id="i-qualif"
              class="form-control"
              type="text"
              v-model="identificationQualifier"
              placeholder="ex. cf."
            />
          </div>

          <div class="col-md">
            <div class="form-check">
              <input
                id="i-accepte"
                class="form-check-input"
                type="checkbox"
                v-model="isAcceptedIdentification"
              />

              <label class="form-check-label" for="i-accepte">
                Identification acceptée
              </label>
            </div>
          </div>
        </div>

        <div v-if="error" class="alert alert-danger mb-0" role="alert">
          {{ error }}
        </div>

        <div class="d-flex flex-wrap gap-2">
          <button
            type="submit"
            class="btn btn-primary"
            :disabled="saving"
          >
            {{ saving ? 'Enregistrement...' : 'Enregistrer la ligne' }}
          </button>

          <button
            type="button"
            class="btn btn-outline-secondary"
            :disabled="saving"
            @click="fermerFormulaire"
          >
            Fermer
          </button>
        </div>

      </form>
    </div>

    <div class="d-flex flex-wrap gap-2">
      <router-link
        class="btn btn-outline-secondary"
        to="/saisie/saisie-observations"
      >
        Précédent
      </router-link>

      <a
        class="btn btn-outline-secondary"
        href="/saisie/brouillons-hors-ligne"
        data-toast="Brouillon enregistré"
      >
        Enregistrer le brouillon
      </a>

      <router-link
        class="btn btn-primary"
        to="/saisie/saisie-complements"
      >
        Suivant : compléments
      </router-link>
    </div>

  </main>
</template>
