<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/recherches/mes-recherches">Mes recherches</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/recherches/mes-recherches">Mes recherches</router-link></li>
        </ol>
        </nav>
        <div class="row g-4">
            <div class="col-lg-4 d-grid gap-3 align-content-start">
                <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary"
                        to="/recherches/nouvelle-recherche">+ Nouvelle recherche</router-link></div>
                <ul class="nav nav-tabs">
                    <li class="nav-item"><router-link class="nav-link active" to="/recherches/mes-recherches">Tout</router-link>
                    </li>
                    <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Publiées</router-link>
                    </li>
                    <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Brouillons</router-link>
                    </li>
                </ul>
                <div class="list-group"><router-link
                        class="list-group-item list-group-item-action d-flex justify-content-between align-items-start gap-2"
                        to="/recherches/mes-recherches">
                        <div>
                            <div class="fw-semibold">Suivi des reptiles de Berenty</div><small
                                class="text-body-secondary">28/08/2026 · 5 datasets</small>
                        </div><span class="badge text-bg-success">Publiée</span>
                    </router-link><router-link
                        class="list-group-item list-group-item-action d-flex justify-content-between align-items-start gap-2"
                        to="/recherches/mes-recherches">
                        <div>
                            <div class="fw-semibold">Inventaire des lémuriens d&#x27;Andasibe</div><small
                                class="text-body-secondary">03/09/2026 · 236 datasets</small>
                        </div><span class="badge text-bg-secondary">Privée</span>
                    </router-link><router-link
                        class="list-group-item list-group-item-action d-flex justify-content-between align-items-start gap-2"
                        to="/recherches/mes-recherches">
                        <div>
                            <div class="fw-semibold">Amphibiens de Ranomafana</div><small
                                class="text-body-secondary">12/09/2026 · 48 datasets</small>
                        </div><span class="badge text-bg-secondary">Brouillon</span>
                    </router-link><router-link
                        class="list-group-item list-group-item-action d-flex justify-content-between align-items-start gap-2"
                        to="/recherches/mes-recherches">
                        <div>
                            <div class="fw-semibold">Oiseaux d&#x27;Ankarafantsika</div><small
                                class="text-body-secondary">15/08/2026 · 12 datasets</small>
                        </div><span class="badge text-bg-secondary">Brouillon</span>
                    </router-link></div>
            </div>
            <div class="col-lg-8 d-grid gap-3 align-content-start">
                <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>
                <ul class="list-unstyled mb-0 d-grid gap-1">
                    <li><span class="text-body-secondary">Statut :</span> Publiée</li>
                    <li><span class="text-body-secondary">Période :</span> 28/08/2026 – 15/08/2026</li>
                    <li><span class="text-body-secondary">Description :</span> Inventaire des lémuriens du parc national
                        d&#x27;Andasibe, saison 2026.</li>
                </ul>
                <div class="row row-cols-2 row-cols-lg-4 g-3">
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">48</div>
                                <div class="text-body-secondary">datasets</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">236</div>
                                <div class="text-body-secondary">observations</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card border-warning">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">5</div>
                                <div class="text-body-secondary">à valider</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">12</div>
                                <div class="text-body-secondary">collaborateurs</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header fw-semibold">Datasets</div>
                    <div class="card-body d-grid gap-3">
                        <div class="card">
                            <div class="card-header"><input type="search" form="nf"
                                    class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                                    aria-label="Filtrer"></div>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0" data-table>
                                    <thead>
                                        <tr>
                                            <th scope="col">Dataset</th>
                                            <th scope="col">Observations</th>
                                            <th scope="col"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>Mammifères</td>
                                            <td>12</td>
                                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                                        </tr>
                                        <tr>
                                            <td>Reptiles</td>
                                            <td>48</td>
                                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                                        </tr>
                                        <tr>
                                            <td>Ovipares</td>
                                            <td>236</td>
                                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary"
                        to="/saisie/fiche-d-un-dataset">Ouvrir la recherche</router-link><router-link
                        class="btn btn-outline-secondary"
                        to="/recherches/modifier-une-recherche">Modifier</router-link><router-link
                        class="btn btn-outline-secondary"
                        to="/recherches/medias-de-la-recherche">Médias</router-link><router-link
                        class="btn btn-outline-secondary" to="/recherches/these">Thèse</router-link><a
                        class="btn btn-outline-danger" href="/recherches/mes-recherches"
                        data-confirm="Voulez-vous vraiment effectuer cette action ?">Dépublier</a></div>
            </div>
        </div>
    
  </main>
</template>
