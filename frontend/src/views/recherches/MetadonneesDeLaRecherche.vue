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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/recherches/metadonnees-de-la-recherche">M&#195;&#169;tadonn&#195;&#169;es de la recherche</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>
        <ul class="nav nav-tabs">
            <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Données</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/medias-de-la-recherche">Médias</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/collaborateurs">Collaborateurs</router-link></li>
            <li class="nav-item"><router-link class="nav-link active"
                    to="/recherches/metadonnees-de-la-recherche">Métadonnées</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/these">Thèse</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/notes-personnelles">Notes</router-link></li>
        </ul>
        <div class="card">
            <div class="card-header fw-semibold">Recherche</div>
            <div class="card-body d-grid gap-3">
                <ul class="list-unstyled mb-0 d-grid gap-1">
                    <li><span class="text-body-secondary">Méthodes :</span> Description d&#x27;exemple</li>
                    <li><span class="text-body-secondary">Effort d&#x27;échantillonnage :</span> Description
                        d&#x27;exemple</li>
                    <li><span class="text-body-secondary">Auteurs :</span> Rakoto Andry, Rasoa Voahangy</li>
                </ul>
            </div>
        </div>
        <div class="card">
            <div class="card-header"><input type="search" form="nf" class="form-control form-control-sm table-filter"
                    placeholder="Filtrer..." aria-label="Filtrer"></div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" data-table>
                    <thead>
                        <tr>
                            <th scope="col">Dataset</th>
                            <th scope="col">Licence</th>
                            <th scope="col">Détenteur des droits</th>
                            <th scope="col">Protocoles</th>
                            <th scope="col">Références</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Mammifères</td>
                            <td>CC BY 4.0</td>
                            <td>The Peregrine Fund</td>
                            <td>Transect linéaire</td>
                            <td>Auteur, 2019. Titre de la référence.</td>
                        </tr>
                        <tr>
                            <td>Reptiles</td>
                            <td>CC BY-NC 4.0</td>
                            <td>The Peregrine Fund</td>
                            <td>Point d&#x27;écoute</td>
                            <td>Auteur, 2019. Titre de la référence.</td>
                        </tr>
                        <tr>
                            <td>Ovipares</td>
                            <td>CC0</td>
                            <td>The Peregrine Fund</td>
                            <td>Piège photographique</td>
                            <td>Auteur, 2019. Titre de la référence.</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary"
                to="/recherches/modifier-une-recherche">Modifier</router-link></div>
    
  </main>
</template>
