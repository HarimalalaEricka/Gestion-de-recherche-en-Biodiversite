<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/partage/liste-des-taxons">Liste des taxons</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Taxons</h1>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Rechercher un nom scientifique</label><input
                        class="form-control" type="text" id="f1" name="rechercher-un-nom-scientifique"
                        value="Lemur catta"></div>
            </div>
            <div class="card">
                <div class="card-header"><input type="search" form="nf"
                        class="form-control form-control-sm table-filter" placeholder="Filtrer..." aria-label="Filtrer">
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0" data-table>
                        <thead>
                            <tr>
                                <th scope="col">Nom scientifique</th>
                                <th scope="col">Rang</th>
                                <th scope="col">Famille</th>
                                <th scope="col">Genre</th>
                                <th scope="col">Observations</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Lemur catta</td>
                                <td>Espèce</td>
                                <td>Lemuridae</td>
                                <td>Lemur</td>
                                <td>12</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/partage/fiche-taxon">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Propithecus verreauxi</td>
                                <td>Espèce</td>
                                <td>Indriidae</td>
                                <td>Propithecus</td>
                                <td>48</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/partage/fiche-taxon">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Furcifer pardalis</td>
                                <td>Espèce</td>
                                <td>Chamaeleonidae</td>
                                <td>Furcifer</td>
                                <td>236</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/partage/fiche-taxon">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Eulemur fulvus</td>
                                <td>Espèce</td>
                                <td>Lemuridae</td>
                                <td>Eulemur</td>
                                <td>5</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/partage/fiche-taxon">Ouvrir</router-link></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    
  </main>
</template>
