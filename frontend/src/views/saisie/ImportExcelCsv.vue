<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/saisie/saisie-contexte">Saisie</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/import-excel-csv">Import Excel / CSV</router-link></li>
        </ol>
        </nav>
        <p class="text-body-secondary mb-0">Dataset : Mammifères</p>
        <h1 class="h3 mb-0">Importer des données</h1>
        <div class="card">
            <div class="card-header fw-semibold">1. Fichier</div>
            <div class="card-body d-grid gap-3">
                <div class="dropzone"><i class="bi bi-cloud-upload fs-2"></i>
                    <div>Déposer un fichier Excel ou CSV</div><input type="file" multiple class="form-control mt-2"
                        aria-label="Fichiers">
                    <ul class="files list-unstyled small mb-0 mt-2"></ul>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">2. Correspondance des colonnes</div>
            <div class="card-body d-grid gap-3">
                <div class="card">
                    <div class="card-header"><input type="search" form="nf"
                            class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                            aria-label="Filtrer"></div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0" data-table>
                            <thead>
                                <tr>
                                    <th scope="col">Colonne du fichier</th>
                                    <th scope="col">Champ de la base</th>
                                    <th scope="col">Exemple</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Espèce</td>
                                    <td>scientificName</td>
                                    <td>Lemur catta</td>
                                </tr>
                                <tr>
                                    <td>Date</td>
                                    <td>eventDate</td>
                                    <td>12/09/2026</td>
                                </tr>
                                <tr>
                                    <td>Site</td>
                                    <td>locality</td>
                                    <td>Andasibe</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">3. Contrôle</div>
            <div class="card-body d-grid gap-3">
                <div class="row row-cols-2 row-cols-lg-3 g-3">
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">48</div>
                                <div class="text-body-secondary">lignes valides</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">236</div>
                                <div class="text-body-secondary">doublons détectés</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">5</div>
                                <div class="text-body-secondary">erreurs</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                to="/saisie/fiche-d-un-dataset">Annuler</router-link><router-link class="btn btn-primary"
                to="/saisie/fiche-d-un-dataset">Lancer l&#x27;import</router-link></div>
    
  </main>
</template>
