<script setup>
import { usePage } from '../../composables/usePage'
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { insertItem, getItems } from '../../services/api'

usePage()

const route = useRoute()
const router = useRouter()
const idDataset = Number(route.params.idDataset)

const pays = ref([])
const provinces = ref([])

const dateEvenement = ref('')
const paysSelected = ref(null)
const provinceSelected = ref(null)
const commune = ref('')
const localite = ref('')
const latitude = ref('')
const longitude = ref('')

const formRef = ref(null)
const error = ref('')
const loading = ref(false)


const protocolInput = ref('')          
const protocolSelected = ref(null)     
const protocolResults = ref([])
const protocolOpen = ref(false)
const protocolLoading = ref(false)
const protocolActive = ref(-1)         

function debounce(fn, delay = 300) {
    let timer

    return (...args) => {
        clearTimeout(timer)
        timer = setTimeout(() => fn(...args), delay)
    }
}

let protocolRequestId = 0

async function searchProtocols() {
    const requestId = ++protocolRequestId

    protocolLoading.value = true

    try {
        const filters = {}
        const q = protocolInput.value.trim()

        if (q) {
            filters.protocolName = q
        }

        const { items } = await getItems('protocols', filters, {
            page: 1,
            perPage: 4,
            sort: 'protocolName,asc'
        })

        if (requestId !== protocolRequestId) return

        protocolResults.value = items
        protocolActive.value = items.length ? 0 : -1

    } catch (e) {
        if (requestId !== protocolRequestId) return

        console.error('Erreur recherche des protocoles :', e)
        protocolResults.value = []
        protocolActive.value = -1

    } finally {
        if (requestId === protocolRequestId) {
            protocolLoading.value = false
        }
    }
}

const searchProtocolsDebounced = debounce(searchProtocols)

function onProtocolInput() {
    // Le texte a changé : l'ancien choix n'est plus valable
    protocolSelected.value = null
    protocolOpen.value = true
    searchProtocolsDebounced()
}

function onProtocolFocus() {
    protocolOpen.value = true
    searchProtocols()
}

function closeProtocols() {
    protocolOpen.value = false
}

function chooseProtocol(p) {
    protocolSelected.value = p.protocolID
    protocolInput.value = p.protocolName
    protocolOpen.value = false
}

function clearProtocol() {
    protocolSelected.value = null
    protocolInput.value = ''
    protocolResults.value = []
    protocolOpen.value = false
}

function onProtocolKeydown(e) {
    if (!protocolOpen.value && e.key === 'ArrowDown') {
        protocolOpen.value = true
        searchProtocols()
        return
    }

    if (!protocolOpen.value) return

    if (e.key === 'ArrowDown') {
        e.preventDefault()
        protocolActive.value =
            (protocolActive.value + 1) % Math.max(protocolResults.value.length, 1)
    } else if (e.key === 'ArrowUp') {
        e.preventDefault()
        protocolActive.value =
            (protocolActive.value - 1 + protocolResults.value.length) %
            Math.max(protocolResults.value.length, 1)
    } else if (e.key === 'Enter') {
        // Évite aussi la soumission du formulaire quand la liste est ouverte
        e.preventDefault()
        const p = protocolResults.value[protocolActive.value]
        if (p) chooseProtocol(p)
    } else if (e.key === 'Escape') {
        protocolOpen.value = false
    }
}


const provincesFiltrees = computed(() =>
    provinces.value.filter(
        (pr) => !paysSelected.value || pr.pays?.paysID === paysSelected.value
    )
)

watch(paysSelected, () => {
    provinceSelected.value = null
})

function parseCoord(v) {
    const s = String(v ?? '').trim().replace(',', '.')
    return s === '' ? null : Number(s)
}


async function submit() {
    error.value = ''

    if (!formRef.value.checkValidity()) {
        formRef.value.classList.add('was-validated')
        return
    }

    // Du texte tapé mais aucun protocole choisi dans la liste
    if (protocolInput.value.trim() && !protocolSelected.value) {
        error.value = 'Choisissez un protocole dans la liste, ou videz le champ.'
        return
    }

    const lat = parseCoord(latitude.value)
    const lng = parseCoord(longitude.value)

    if (Number.isNaN(lat) || Number.isNaN(lng)) {
        error.value = 'Latitude ou longitude invalide'
        return
    }

    loading.value = true
    try {
        const location = await insertItem('locations', {
            municipality: commune.value,
            locality: localite.value,
            decimalLatitude: lat,
            decimalLongitude: lng,
            province: provinceSelected.value
                ? { provinceID: provinceSelected.value }
                : null
        })

        const event = await insertItem('events', {
            eventDate: `${dateEvenement.value}T00:00:00`,
            dataset: { datasetID: idDataset },
            location: { locationID: location.locationID },
            protocol: protocolSelected.value
                ? { protocolID: protocolSelected.value }
                : null
        })

        router.push(`/saisie/saisie-observations/${idDataset}`)

    } catch (e) {
        console.error('Erreur enregistrement du contexte :', e)
        error.value = "Erreur lors de l'enregistrement du contexte"
    } finally {
        loading.value = false
    }
}

const TOUT = { page: 1, perPage: 1000 }

async function loadLocation() {
    try {
        const [paysRes, provincesRes] = await Promise.all([
            getItems('pays', {}, TOUT),
            getItems('provinces', {}, TOUT)
        ])
        pays.value = paysRes.items
        provinces.value = provincesRes.items
    } catch (e) {
        console.error('Erreur chargement des lieux :', e)
    }
}

onMounted(loadLocation)
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">

    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link to="/saisie/saisie-contexte">Saisie</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">
          <router-link to="/saisie/saisie-contexte">Saisie : contexte</router-link>
        </li>
      </ol>
    </nav>

    <form
      ref="formRef"
      class="needs-validation d-grid gap-4"
      novalidate
      @submit.prevent="submit"
    >
      <small class="text-body-secondary text-end" data-autosave></small>

      <h1 class="h3 mb-0">Nouvelle observation</h1>

      <ul class="nav nav-tabs">
        <li class="nav-item">
          <router-link class="nav-link active" to="/saisie/saisie-contexte">1 Contexte</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/saisie/saisie-observations">2 Observations</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/saisie/saisie-identification">3 Identification</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/saisie/saisie-complements">4 Compléments</router-link>
        </li>
      </ul>

      <!-- Événement -->
      <div class="card">
        <div class="card-header fw-semibold">Événement</div>
        <div class="card-body d-grid gap-3">
          <div class="row g-3">
            <div class="col-md">
              <label class="form-label" for="f2">
                Date de l'événement<span class="text-danger"> *</span>
              </label>
              <input
                class="form-control"
                type="date"
                id="f2"
                name="date-de-l-evenement"
                v-model="dateEvenement"
                required
              />
            </div>

            <!-- Protocole : recherche avec suggestions -->
            <div class="col-md">
              <label class="form-label" for="f3">Protocole</label>

              <div class="position-relative">
                <div class="input-group">
                  <input
                    id="f3"
                    name="protocole"
                    type="text"
                    class="form-control"
                    :class="{ 'is-valid': protocolSelected }"
                    placeholder="Rechercher un protocole..."
                    autocomplete="off"
                    role="combobox"
                    aria-autocomplete="list"
                    aria-controls="protocol-list"
                    :aria-expanded="protocolOpen"
                    v-model="protocolInput"
                    @input="onProtocolInput"
                    @focus="onProtocolFocus"
                    @blur="closeProtocols"
                    @keydown="onProtocolKeydown"
                  />
                  <button
                    v-if="protocolInput"
                    type="button"
                    class="btn btn-outline-secondary"
                    aria-label="Effacer le protocole"
                    @mousedown.prevent
                    @click="clearProtocol"
                  >
                    &times;
                  </button>
                </div>

                <ul
                  v-if="protocolOpen"
                  id="protocol-list"
                  class="list-group position-absolute w-100 shadow"
                  style="z-index: 1050; max-height: 240px; overflow-y: auto"
                  role="listbox"
                >
                  <li
                    v-if="protocolLoading"
                    class="list-group-item text-body-secondary"
                  >
                    Recherche...
                  </li>

                  <li
                    v-else-if="protocolResults.length === 0"
                    class="list-group-item text-body-secondary"
                  >
                    Aucun protocole trouvé
                  </li>

                  <template v-else>
                    <!-- mousedown.prevent : garde le focus, sinon le blur ferme la liste avant le clic -->
                    <li
                      v-for="(p, i) in protocolResults"
                      :key="p.protocolID"
                      class="list-group-item list-group-item-action"
                      :class="{ active: i === protocolActive }"
                      role="option"
                      :aria-selected="i === protocolActive"
                      style="cursor: pointer"
                      @mousedown.prevent
                      @click="chooseProtocol(p)"
                      @mouseenter="protocolActive = i"
                    >
                      {{ p.protocolName }}
                    </li>
                  </template>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Lieu -->
      <div class="card">
        <div class="card-header fw-semibold">Lieu</div>
        <div class="card-body d-grid gap-3">

          <div class="d-flex flex-wrap gap-2">
            <a class="btn btn-outline-secondary" href="#" data-demo>
              Choisir un lieu existant
            </a>
          </div>

          <div class="row g-3">
            <div class="col-md">
              <label class="form-label" for="f5">Pays</label>
              <select class="form-select" id="f5" name="pays" v-model="paysSelected">
                <option :value="null">-- Choisir --</option>
                <option v-for="p in pays" :key="p.paysID" :value="p.paysID">
                  {{ p.pays }}
                </option>
              </select>
            </div>

            <div class="col-md">
              <label class="form-label" for="f6">
                Province<span class="text-danger"> *</span>
              </label>
              <select class="form-select" id="f6" name="province" v-model="provinceSelected" required
              >
                <option :value="null" disabled>-- Choisir --</option>
                <option v-for="pr in provincesFiltrees" :key="pr.provinceID" :value="pr.provinceID"
                >
                  {{ pr.province }}
                </option>
              </select>
            </div>

            <div class="col-md">
              <label class="form-label" for="f7">
                Commune<span class="text-danger"> *</span>
              </label>
              <input class="form-control" type="text" id="f7" name="commune" v-model="commune" required
              />
            </div>
          </div>

          <div class="row g-3">
            <div class="col-md">
              <label class="form-label" for="f8">Localité</label>
              <input class="form-control" type="text" id="f8" name="localite" v-model="localite"
              />
            </div>

            <div class="col-md">
              <label class="form-label" for="f9">Latitude</label>
              <input class="form-control" type="text" inputmode="decimal" id="f9" name="latitude" v-model="latitude" placeholder="-18.9"
              />
            </div>

            <div class="col-md">
              <label class="form-label" for="f10">Longitude</label>
              <input class="form-control" type="text" inputmode="decimal" id="f10" name="longitude" v-model="longitude" placeholder="48.4"
              />
            </div>
          </div>

          <div class="d-flex flex-wrap gap-2">
            <a class="btn btn-outline-secondary" href="#" data-demo>Ma position</a>
          </div>
        </div>
      </div>

      <div v-if="error" class="alert alert-danger mb-0" role="alert">
        {{ error }}
      </div>

      <div class="d-flex flex-wrap gap-2">
        <router-link class="btn btn-outline-secondary" to="/saisie/brouillons-hors-ligne" data-toast="Brouillon enregistré"
        >
          Enregistrer le brouillon
        </router-link>

        <button type="submit" class="btn btn-primary" :disabled="loading">
          {{ loading ? 'Enregistrement...' : 'Suivant' }}
        </button>
      </div>
    </form>

  </main>
</template>