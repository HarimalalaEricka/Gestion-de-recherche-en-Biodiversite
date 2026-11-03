<script setup>
import { usePage } from "../../composables/usePage";
import { ref, computed, watch, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getItems, patchItem } from "../../services/api";

usePage();
const router = useRouter();

// Données
const utilisateurs = ref([]);
const total = ref(0);
const loading = ref(false);

// Pagination
const page = ref(1);
const perPage = ref(10);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / perPage.value)));

// Filtres
const recherche = ref("");
const role = ref("");
const statut = ref("");

// Affichage
const roleLabels = {
  ADMIN: "Administrateur",
  RESEARCHER: "Chercheur",
  VIEWER: "Lecteur",
  DATA_ENTRY: "Agent de saisie",
  STUDENT: "Étudiant",
};
const roleOptions = Object.entries(roleLabels).map(([value, label]) => ({
  value,
  label,
}));

const statutLabels = { ACTIVE: "Actif", INVITE: "Invité", DESACTIVE: "Désactivé" };

// Statuts que l'on peut choisir dans le tableau (seulement actif / désactivé)
const statutOptions = [
  { value: "ACTIVE", label: "Actif" },
  { value: "DESACTIVE", label: "Désactivé" },
];

const formatDate = (d) => (d ? new Date(d).toLocaleDateString("fr-FR") : "-");

async function fetchUtilisateurs() {
  loading.value = true;
  try {
    const { items, total: t } = await getItems(
      "utilisateurs",
      { q: recherche.value, role: role.value, statut: statut.value },
      { page: page.value, perPage: perPage.value, sort: "dateCreation,desc" }
    );
    utilisateurs.value = items;
    total.value = t;
  } catch (e) {
    console.error("Erreur lors de la récupération des utilisateurs :", e);
  } finally {
    loading.value = false;
  }
}

// Modifie un champ (role ou statut) d'un utilisateur
async function modifierChamp(u, champ, valeur) {
  const ancienne = u[champ];
  try {
    await patchItem("utilisateurs", { [champ]: valeur }, u.utilisateurID);
    u[champ] = valeur;
  } catch (e) {
    console.error(`Erreur lors de la modification de ${champ} :`, e);
    u[champ] = ancienne;
    await fetchUtilisateurs(); // resynchronise les selects
  }
}

// Changement de page ou de taille : on recharge
watch([page, perPage], fetchUtilisateurs);

// Changement de filtre : retour page 1 puis rechargement
let timer;
watch([recherche, role, statut], () => {
  clearTimeout(timer);
  timer = setTimeout(() => {
    if (page.value === 1) fetchUtilisateurs();
    else page.value = 1; // le watch de page rechargera
  }, 300);
});

onMounted(fetchUtilisateurs);
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
    <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
      <ol class="breadcrumb mb-0">
        <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
        <li class="breadcrumb-item">
          <router-link to="/admin/utilisateurs">Administration</router-link>
        </li>
        <li class="breadcrumb-item active" aria-current="page">Utilisateurs</li>
      </ol>
    </nav>

    <div class="d-grid gap-4">
      <h1 class="h3 mb-0">Utilisateurs</h1>

      <div class="d-flex flex-wrap gap-2">
        <router-link to="/admin/inviter-un-utilisateur" class="btn btn-primary">
          + Inviter un utilisateur
        </router-link>
      </div>

      <!-- Filtres -->
      <div class="row g-3">
        <div class="col-md">
          <label class="form-label" for="f1">Rechercher</label>
          <input
            class="form-control"
            type="text"
            id="f1"
            v-model="recherche"
            placeholder="Nom, prénom, téléphone ou e-mail..."
          />
        </div>
        <div class="col-md">
          <label class="form-label" for="f2">Rôle</label>
          <select class="form-select" id="f2" v-model="role">
            <option value="">Tous</option>
            <option v-for="r in roleOptions" :key="r.value" :value="r.value">
              {{ r.label }}
            </option>
          </select>
        </div>
        <div class="col-md">
          <label class="form-label" for="f3">Statut</label>
          <select class="form-select" id="f3" v-model="statut">
            <option value="">Tous</option>
            <option v-for="(label, value) in statutLabels" :key="value" :value="value">
              {{ label }}
            </option>
          </select>
        </div>
      </div>

      <!-- Tableau -->
      <div class="card">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead>
              <tr>
                <th scope="col">Nom</th>
                <th scope="col">E-mail</th>
                <th scope="col">Téléphone</th>
                <th scope="col">Rôle</th>
                <th scope="col">Statut</th>
                <th scope="col">Créé le</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="loading">
                <td colspan="7" class="text-center text-body-secondary">Chargement...</td>
              </tr>
              <tr v-else-if="utilisateurs.length === 0">
                <td colspan="7" class="text-center text-body-secondary">
                  Aucun utilisateur trouvé
                </td>
              </tr>
              <tr v-else v-for="u in utilisateurs" :key="u.utilisateurID">
                <td>{{ u.nom }} {{ u.prenom }}</td>
                <td>{{ u.email }}</td>
                <td>{{ u.telephone }}</td>

                <!-- Rôle -->
                <td>
                  <select
                    class="form-select form-select-sm w-auto"
                    :value="u.role"
                    @change="modifierChamp(u, 'role', $event.target.value)"
                  >
                    <option v-for="r in roleOptions" :key="r.value" :value="r.value">
                      {{ r.label }}
                    </option>
                  </select>
                </td>

                <!-- Statut (actif / désactivé) -->
                <td>
                  <select
                    class="form-select form-select-sm w-auto"
                    :class="u.statut === 'ACTIVE' ? 'border-success' : 'border-secondary'"
                    :value="u.statut"
                    @change="modifierChamp(u, 'statut', $event.target.value)"
                  >
                    <!-- Statut actuel s'il n'est pas dans la liste (ex. INVITE) : affiché mais non choisissable -->
                    <option
                      v-if="!statutOptions.some((s) => s.value === u.statut)"
                      :value="u.statut"
                      disabled
                    >
                      {{ statutLabels[u.statut] ?? u.statut }}
                    </option>
                    <option v-for="s in statutOptions" :key="s.value" :value="s.value">
                      {{ s.label }}
                    </option>
                  </select>
                </td>

                <td>{{ formatDate(u.dateCreation) }}</td>
                <td class="text-end">
                  <router-link
                    class="btn btn-sm btn-outline-primary"
                    :to="`/admin/fiche-d-un-utilisateur/${u.utilisateurID}`"
                    >Ouvrir</router-link
                  >
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div
          class="card-footer d-flex flex-wrap justify-content-between align-items-center gap-2"
        >
          <small class="text-body-secondary">{{ total }} utilisateur(s)</small>

          <div class="d-flex align-items-center gap-2">
            <select
              class="form-select form-select-sm w-auto"
              v-model.number="perPage"
              aria-label="Par page"
            >
              <option :value="5">5</option>
              <option :value="10">10</option>
              <option :value="20">20</option>
            </select>

            <button
              type="button"
              class="btn btn-sm btn-outline-secondary"
              :disabled="page <= 1"
              @click="page--"
            >
              Précédent
            </button>
            <span class="small">Page {{ page }} / {{ totalPages }}</span>
            <button
              type="button"
              class="btn btn-sm btn-outline-secondary"
              :disabled="page >= totalPages"
              @click="page++"
            >
              Suivant
            </button>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>
