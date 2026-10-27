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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/brouillons-hors-ligne">Brouillons hors ligne</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Brouillons hors ligne</h1>
        <div class="row row-cols-2 row-cols-lg-3 g-3">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">48</div>
                        <div class="text-body-secondary">en attente</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">236</div>
                        <div class="text-body-secondary">synchronisées</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">5</div>
                        <div class="text-body-secondary">en conflit</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-header"><input type="search" form="nf" class="form-control form-control-sm table-filter"
                    placeholder="Filtrer..." aria-label="Filtrer"></div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" data-table>
                    <thead>
                        <tr>
                            <th scope="col">Date</th>
                            <th scope="col">Dataset</th>
                            <th scope="col">Lignes</th>
                            <th scope="col">État</th>
                            <th scope="col"></th>
                            <th scope="col">Supprimer</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>12/09/2026</td>
                            <td>Mammifères</td>
                            <td>12</td>
                            <td><span class="badge text-bg-secondary">Brouillon</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/saisie/saisie-contexte">Ouvrir</router-link></td>
                            <td>Exemple</td>
                        </tr>
                        <tr>
                            <td>03/09/2026</td>
                            <td>Reptiles</td>
                            <td>48</td>
                            <td><span class="badge text-bg-warning">En attente de sync</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/saisie/saisie-contexte">Ouvrir</router-link></td>
                            <td>Exemple</td>
                        </tr>
                        <tr>
                            <td>28/08/2026</td>
                            <td>Ovipares</td>
                            <td>236</td>
                            <td><span class="badge text-bg-success">Synchronisée</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/saisie/saisie-contexte">Ouvrir</router-link></td>
                            <td>Exemple</td>
                        </tr>
                        <tr>
                            <td>15/08/2026</td>
                            <td>Mammifères</td>
                            <td>5</td>
                            <td><span class="badge text-bg-danger">En conflit</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/saisie/saisie-contexte">Ouvrir</router-link></td>
                            <td>Exemple</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary"
                to="/saisie/brouillons-hors-ligne">Synchroniser tout</router-link></div>
        <p class="text-body-secondary mb-0">Les brouillons restent dans ce navigateur : utilisez le même pour les
            retrouver.</p>
    
  </main>
</template>
