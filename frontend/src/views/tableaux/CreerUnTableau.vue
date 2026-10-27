<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item">
                    <router-link to="/accueil">Accueil</router-link>
                </li>
                <li class="breadcrumb-item">
                    <router-link to="/tableaux/tableaux-personnalises">Tableaux personnalisés</router-link>
                </li>
                <li class="breadcrumb-item active" aria-current="page">Créer un tableau</li>
            </ol>
        </nav>

        <form class="needs-validation d-grid gap-4" novalidate>
            <small class="text-body-secondary text-end" data-autosave></small>
            <h1 class="h3 mb-0">Créer un tableau</h1>

            <div class="card">
                <div class="card-header fw-semibold">Informations</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md">
                            <label class="form-label" for="table-name">
                                Nom du tableau<span class="text-danger"> *</span>
                            </label> <input class="form-control" type="text" id="table-name"
                                   name="nom-du-tableau" value="Suivi des nids" required>
                        </div>
                        <div class="col-md">
                            <label class="form-label" for="table-dataset">
                                Dataset<span class="text-danger"> *</span>
                            </label> <select class="form-select" id="table-dataset" name="dataset" required>
                                <option>Mammifères</option>
                                <option selected>Reptiles</option>
                                <option>Ovipares</option>
                            </select>
                        </div>
                    </div>
                    <div>
                        <label class="form-label" for="table-description">Description</label>
                        <textarea class="form-control" id="table-description"
                                  name="description" rows="3">Inventaire des lémuriens du parc national d'Andasibe, saison 2026.</textarea>
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span class="fw-semibold">Structure du tableau</span> <span class="text-body-secondary small">Ajoutez les colonnes horizontalement</span>
                </div>
                <div class="card-body">
                    <p class="text-body-secondary">
                        Chaque colonne commence par une recherche d'attribut. Un attribut existant
                        peut être réutilisé ; sinon, il sera créé dans ce tableau de démonstration.
                    </p>

                    <div class="table-builder-wrap">
                        <table class="table table-bordered align-middle mb-0" id="table-builder">
                            <thead>
                                <tr id="table-columns">
                                <th class="table-builder-add" data-add-column>
                                    <button type="button" class="btn btn-outline-primary"
                                            aria-label="Ajouter une colonne">
                                            <i class="bi bi-plus-lg"></i>
                                        </button>
                                    </th>
                                </tr>
                            </thead>
                            <tbody id="table-rows">
                                <tr data-table-row>
                                <td class="table-builder-empty">
                                    Ajoutez une colonne pour commencer
                                </td>
                                <td class="table-builder-actions">
                                    <button type="button" class="btn btn-sm btn-outline-danger"
                                            data-remove-table-row aria-label="Supprimer la ligne"
                                            title="Supprimer la ligne">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <datalist id="attribute-catalog">
                        <option value="Nom" label="Texte"></option>
                        <option value="Hauteur" label="Décimal"></option>
                        <option value="Nombre" label="Entier"></option>
                        <option value="Actif" label="Oui/non"></option>
                        <option value="Date" label="Date"></option>
                        <option value="État" label="Liste"></option>
                        <option value="Diamètre du tronc" label="Décimal"></option>
                        <option value="Poids" label="Décimal"></option>
                    </datalist>

                    <div class="d-flex flex-wrap gap-2 mt-3">
                        <button type="button" class="btn btn-outline-secondary"
                                data-add-table-row>
                            <i class="bi bi-plus-lg"></i> Nouvelle ligne
                        </button> <router-link class="btn btn-outline-secondary"
                           to="/tableaux/catalogue-des-attributs">
                            Voir le catalogue des attributs
                        </router-link>
                    </div>
                </div>
            </div>

            <div class="d-flex flex-wrap gap-2">
                <router-link class="btn btn-outline-secondary"
                   to="/tableaux/tableaux-personnalises">Annuler</router-link> <button type="submit" class="btn btn-primary"
                        data-next="/tableaux/tableaux-personnalises">
                    Enregistrer le tableau
                </button>
            </div>
        </form>
    
  </main>
</template>
