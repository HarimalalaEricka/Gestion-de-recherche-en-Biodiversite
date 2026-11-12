<script setup>
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { usePage } from '../../composables/usePage'
import { getItemById, patchItem, deleteItem } from "../../services/api";
usePage()

const props = defineProps({ id: { type: String, required: true } })
const router = useRouter()

const loading = ref(false);
const saving = ref(false);
const error = ref("");
const submitted = ref(false);

const form = reactive({
    sujet: "",
    dateDebut: "",
    dateFin: "",
    confidentialite: "PRIVATE",
    description: "",
});

const confidentialiteOptions = [
    { value: "PRIVATE", label: "Privée" },
    { value: "DRAFT", label: "Brouillon" },
    { value: "PUBLIC", label: "Publiée" },
];

const toInputDate = (d) => (d ? String(d).slice(0, 10) : "");

async function fetchRecherche() {
    loading.value = true;
    error.value = "";
    try {
        const r = await getItemById("recherches", props.id);
        form.sujet = r.sujet ?? "";
        form.dateDebut = toInputDate(r.dateDebut);
        form.dateFin = toInputDate(r.dateFin);
        form.confidentialite = r.confidentialite ?? "PRIVATE";
        form.description = r.description ?? "";
    } catch (e) {
        console.error("Erreur lors du chargement de la recherche :", e);
        error.value = "Impossible de charger la recherche.";
    } finally {
        loading.value = false;
    }
}

const dateInvalide = () =>
    form.dateFin && form.dateDebut && form.dateFin < form.dateDebut;

async function save() {
    submitted.value = true;
    if (!form.sujet.trim() || !form.dateDebut || dateInvalide()) return;

    saving.value = true;
    error.value = "";
    try {
        await patchItem("recherches", {
                sujet: form.sujet.trim(),
                dateDebut: form.dateDebut,
                dateFin: form.dateFin || null,
                confidentialite: form.confidentialite,
                description: form.description,
                }, props.id);
        router.push("/recherches/mes-recherches");
    } catch (e) {
        console.error("Erreur lors de l'enregistrement :", e);
        error.value = "Échec de l'enregistrement. Réessaie.";
    } finally {
        saving.value = false;
    }
}


onMounted(fetchRecherche);
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item"><router-link to="/recherches/mes-recherches">Mes recherches</router-link></li>
        <li class="breadcrumb-item active" aria-current="page">Modifier une recherche</li>
      </ol>
    </nav>

    <div v-if="loading" class="text-center text-body-secondary p-5">Chargement...</div>

    <form v-else class="d-grid gap-4" novalidate @submit.prevent="save">
      <h1 class="h3 mb-0">Modifier la recherche</h1>

      <div v-if="error" class="alert alert-danger mb-0" role="alert">{{ error }}</div>

      <div class="row g-3">
        <div class="col-md">
          <label class="form-label" for="f1">Sujet<span class="text-danger"> *</span></label>
          <input
            id="f1"
            v-model="form.sujet"
            class="form-control"
            :class="{ 'is-invalid': submitted && !form.sujet.trim() }"
            type="text"
            name="sujet"
            required
          >
          <div class="invalid-feedback">Le sujet est obligatoire.</div>
        </div>

        <div class="col-md">
          <label class="form-label" for="f2">Date de début<span class="text-danger"> *</span></label>
          <input
            id="f2"
            v-model="form.dateDebut"
            class="form-control"
            :class="{ 'is-invalid': submitted && !form.dateDebut }"
            type="date"
            name="date-de-debut"
            required
          >
          <div class="invalid-feedback">La date de début est obligatoire.</div>
        </div>

        <div class="col-md">
          <label class="form-label" for="f3">Date de fin</label>
          <input
            id="f3"
            v-model="form.dateFin"
            class="form-control"
            :class="{ 'is-invalid': submitted && dateInvalide() }"
            type="date"
            name="date-de-fin"
            :min="form.dateDebut || undefined"
          >
          <div class="invalid-feedback">La date de fin doit être après la date de début.</div>
        </div>

        <div class="col-md">
          <label class="form-label" for="f4">Visibilité</label>
          <select id="f4" v-model="form.confidentialite" class="form-select" name="visibilite">
            <option v-for="o in confidentialiteOptions" :key="o.value" :value="o.value">
              {{ o.label }}
            </option>
          </select>
        </div>
      </div>

      <div class="row g-3">
        <div class="col-md">
          <label class="form-label" for="f5">Description</label>
          <textarea
            id="f5"
            v-model="form.description"
            class="form-control"
            name="description"
            rows="4"
          ></textarea>
        </div>
      </div>

      <div class="d-flex flex-wrap gap-2">
        <router-link class="btn btn-outline-secondary" to="/recherches/mes-recherches">Annuler</router-link>
        <button type="submit" class="btn btn-primary" :disabled="saving">
          {{ saving ? "Enregistrement..." : "Enregistrer" }}
        </button>
      </div>
    </form>
  </main>
</template>