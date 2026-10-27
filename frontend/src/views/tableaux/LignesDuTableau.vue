<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/tableaux/lignes-du-tableau">Lignes du tableau</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Suivi des nids</h1>
        <ul class="list-unstyled mb-0 d-grid gap-1">
            <li><span class="text-body-secondary">Dataset :</span> Reptiles</li>
            <li><span class="text-body-secondary">Propriétaire :</span> Randria Tojo</li>
        </ul>
        <div class="row row-cols-2 row-cols-lg-3 g-3">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">5</div>
                        <div class="text-body-secondary">lignes</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">12</div>
                        <div class="text-body-secondary">colonnes</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card border-warning">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">48</div>
                        <div class="text-body-secondary">à valider</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary"
                to="/tableaux/saisie-dans-un-tableau">+ Nouvelle ligne</router-link><router-link
                class="btn btn-outline-secondary" to="/tableaux/import-dans-un-tableau">Importer des
                lignes</router-link><a class="btn btn-outline-secondary" href="#" data-demo>Exporter CSV</a><router-link
                class="btn btn-outline-secondary" to="/tableaux/creer-un-tableau">Modifier la structure</router-link>
        </div>
        <div class="card">
            <div class="card-header"><input type="search" form="nf" class="form-control form-control-sm table-filter"
                    placeholder="Filtrer..." aria-label="Filtrer"></div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" data-table>
                    <thead>
                        <tr>
                            <th scope="col">Nom de l&#x27;espèce</th>
                            <th scope="col">Hauteur du nid (m)</th>
                            <th scope="col">Nombre d&#x27;œufs</th>
                            <th scope="col">Nid actif</th>
                            <th scope="col">Date de découverte</th>
                            <th scope="col">État du nid</th>
                            <th scope="col">Validation</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Lemur catta</td>
                            <td>12,5</td>
                            <td>2</td>
                            <td>Oui</td>
                            <td>12/09/2026</td>
                            <td>Intact</td>
                            <td><span class="badge text-bg-warning">En attente</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/tableaux/saisie-dans-un-tableau">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Propithecus verreauxi</td>
                            <td>8,0</td>
                            <td>0</td>
                            <td>Non</td>
                            <td>03/09/2026</td>
                            <td>Abandonné</td>
                            <td><span class="badge text-bg-warning">En attente</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/tableaux/saisie-dans-un-tableau">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Furcifer pardalis</td>
                            <td>15,2</td>
                            <td>3</td>
                            <td>Oui</td>
                            <td>28/08/2026</td>
                            <td>Endommagé</td>
                            <td><span class="badge text-bg-success">Validée</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/tableaux/saisie-dans-un-tableau">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Eulemur fulvus</td>
                            <td>12,5</td>
                            <td>2</td>
                            <td>Oui</td>
                            <td>15/08/2026</td>
                            <td>Intact</td>
                            <td><span class="badge text-bg-warning">En attente</span></td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/tableaux/saisie-dans-un-tableau">Ouvrir</router-link></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    
  </main>
</template>
