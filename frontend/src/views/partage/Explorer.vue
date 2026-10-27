<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/partage/explorer">Explorer</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Explorer</h1>
            <div class="row g-4">
                <div class="col-lg-3 d-grid gap-3 align-content-start">
                    <div class="card">
                        <div class="card-header fw-semibold">Filtres</div>
                        <div class="card-body d-grid gap-3">
                            <div class="row g-3">
                                <div class="col-md"><label class="form-label" for="f1">Taxon (famille,
                                        genre)</label><input class="form-control" type="text" id="f1"
                                        name="taxon-famille-genre" value="Lemur catta"></div>
                            </div>
                            <div class="row g-3">
                                <div class="col-md"><label class="form-label" for="f2">Pays</label><select
                                        class="form-select" id="f2" name="pays">
                                        <option>Madagascar</option>
                                    </select></div>
                            </div>
                            <div class="row g-3">
                                <div class="col-md"><label class="form-label" for="f3">Province</label><select
                                        class="form-select" id="f3" name="province">
                                        <option>Alaotra-Mangoro</option>
                                        <option>Atsimo-Andrefana</option>
                                        <option>Boeny</option>
                                        <option>Vakinankaratra</option>
                                    </select></div>
                            </div>
                            <div class="row g-3">
                                <div class="col-md"><label class="form-label" for="f4">Période</label><select
                                        class="form-select" id="f4" name="periode">
                                        <option>Toutes</option>
                                        <option>Cette année</option>
                                        <option>30 derniers jours</option>
                                    </select></div>
                            </div>
                            <div class="row g-3">
                                <div class="col-md"><label class="form-label" for="f5">Dataset</label><select
                                        class="form-select" id="f5" name="dataset">
                                        <option>Mammifères</option>
                                        <option>Reptiles</option>
                                        <option>Ovipares</option>
                                    </select></div>
                            </div>
                            <div class="row g-3">
                                <div class="col-md"><label class="form-label" for="f6">Type de donnée</label><select
                                        class="form-select" id="f6" name="type-de-donnee">
                                        <option>Observation humaine</option>
                                        <option>Spécimen conservé</option>
                                        <option>Observation machine</option>
                                    </select></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-9 d-grid gap-3 align-content-start">
                    <div class="map-ph">
                        <div class="text-center"><i class="bi bi-geo-alt fs-2"></i>
                            <div>Carte des observations</div>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-header fw-semibold">Résultats</div>
                        <div class="card-body d-grid gap-3">
                            <div class="card">
                                <div class="card-header"><input type="search" form="nf"
                                        class="form-control form-control-sm table-filter" data-q
                                        placeholder="Filtrer..." aria-label="Filtrer"></div>
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle mb-0" data-table>
                                        <thead>
                                            <tr>
                                                <th scope="col">Taxon</th>
                                                <th scope="col">Date</th>
                                                <th scope="col">Lieu</th>
                                                <th scope="col">Dataset</th>
                                                <th scope="col">Nombre</th>
                                                <th scope="col">Type de donnée</th>
                                                <th scope="col">Médias</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>Lemur catta</td>
                                                <td>12/09/2026</td>
                                                <td>Parc national d&#x27;Andasibe</td>
                                                <td>Mammifères</td>
                                                <td>Rakoto Andry</td>
                                                <td>Observation humaine</td>
                                                <td>12</td>
                                            </tr>
                                            <tr>
                                                <td>Propithecus verreauxi</td>
                                                <td>03/09/2026</td>
                                                <td>Réserve de Berenty</td>
                                                <td>Reptiles</td>
                                                <td>Rasoa Voahangy</td>
                                                <td>Spécimen conservé</td>
                                                <td>48</td>
                                            </tr>
                                            <tr>
                                                <td>Furcifer pardalis</td>
                                                <td>28/08/2026</td>
                                                <td>Forêt d&#x27;Ankarafantsika</td>
                                                <td>Ovipares</td>
                                                <td>Randria Tojo</td>
                                                <td>Observation machine</td>
                                                <td>236</td>
                                            </tr>
                                            <tr>
                                                <td>Eulemur fulvus</td>
                                                <td>15/08/2026</td>
                                                <td>Parc national de Ranomafana</td>
                                                <td>Mammifères</td>
                                                <td>Razafy Mialy</td>
                                                <td>Observation humaine</td>
                                                <td>5</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-secondary" href="#"
                                    data-demo>Exporter les résultats</a></div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    
  </main>
</template>
