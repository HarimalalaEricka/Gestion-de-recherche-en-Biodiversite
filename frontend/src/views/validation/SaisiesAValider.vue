<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/validation/saisies-a-valider">Validation</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/validation/saisies-a-valider">Saisies &#195;&#160; valider</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Saisies à valider</h1>
        <ul class="nav nav-tabs">
            <li class="nav-item"><router-link class="nav-link active" to="/validation/saisies-a-valider">En
                    attente</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/validation/saisies-a-valider">Validées</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/validation/saisies-a-valider">Rejetées</router-link></li>
        </ul>
        <div class="card">
            <div class="card-header"><input type="search" form="nf" class="form-control form-control-sm table-filter"
                    placeholder="Filtrer..." aria-label="Filtrer"></div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" data-table>
                    <thead>
                        <tr>
                            <th scope="col">Agent</th>
                            <th scope="col">Date de saisie</th>
                            <th scope="col">Dataset</th>
                            <th scope="col">Taxon</th>
                            <th scope="col">Lieu</th>
                            <th scope="col">Validation</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Rakoto Andry</td>
                            <td>12/09/2026</td>
                            <td>Mammifères</td>
                            <td>Lemur catta</td>
                            <td>Parc national d&#x27;Andasibe</td>
                            <td><span class="badge text-bg-warning">En attente</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/validation/detail-d-une-saisie-a-valider">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Rasoa Voahangy</td>
                            <td>03/09/2026</td>
                            <td>Reptiles</td>
                            <td>Propithecus verreauxi</td>
                            <td>Réserve de Berenty</td>
                            <td><span class="badge text-bg-warning">En attente</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/validation/detail-d-une-saisie-a-valider">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Randria Tojo</td>
                            <td>28/08/2026</td>
                            <td>Ovipares</td>
                            <td>Furcifer pardalis</td>
                            <td>Forêt d&#x27;Ankarafantsika</td>
                            <td><span class="badge text-bg-warning">En attente</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/validation/detail-d-une-saisie-a-valider">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Razafy Mialy</td>
                            <td>15/08/2026</td>
                            <td>Mammifères</td>
                            <td>Eulemur fulvus</td>
                            <td>Parc national de Ranomafana</td>
                            <td><span class="badge text-bg-warning">En attente</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/validation/detail-d-une-saisie-a-valider">Ouvrir</router-link></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary"
                to="/validation/saisies-a-valider">Valider la sélection</router-link><a class="btn btn-outline-danger"
                href="/validation/detail-d-une-saisie-a-valider"
                data-confirm="Voulez-vous vraiment effectuer cette action ?">Rejeter avec un motif</a></div>
    
  </main>
</template>
