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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/recherches/medias-de-la-recherche">M&#195;&#169;dias de la recherche</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>
        <ul class="nav nav-tabs">
            <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Données</router-link></li>
            <li class="nav-item"><router-link class="nav-link active"
                    to="/recherches/medias-de-la-recherche">Médias</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/collaborateurs">Collaborateurs</router-link></li>
            <li class="nav-item"><router-link class="nav-link"
                    to="/recherches/metadonnees-de-la-recherche">Métadonnées</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/these">Thèse</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/notes-personnelles">Notes</router-link></li>
        </ul>
        <div class="d-flex flex-wrap gap-2"><a class="btn btn-primary" href="#" data-demo>+ Ajouter des fichiers</a>
        </div>
        <div class="dropzone"><i class="bi bi-cloud-upload fs-2"></i>
            <div>Déposer ici photos, vidéos, documents</div><input type="file" multiple class="form-control mt-2"
                aria-label="Fichiers">
            <ul class="files list-unstyled small mb-0 mt-2"></ul>
        </div>
        <div class="card">
            <div class="card-header"><input type="search" form="nf" class="form-control form-control-sm table-filter"
                    placeholder="Filtrer..." aria-label="Filtrer"></div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" data-table>
                    <thead>
                        <tr>
                            <th scope="col">Aperçu</th>
                            <th scope="col">Type</th>
                            <th scope="col">Légende</th>
                            <th scope="col">Commentaire</th>
                            <th scope="col">Auteur</th>
                            <th scope="col">Date</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><i class="bi bi-image fs-4 text-body-secondary"></i></td>
                            <td>Poids</td>
                            <td>Lémur en alimentation</td>
                            <td>—</td>
                            <td>Rakoto Andry</td>
                            <td>12/09/2026</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td><i class="bi bi-image fs-4 text-body-secondary"></i></td>
                            <td>Longueur du corps</td>
                            <td>Photo de caméra piège</td>
                            <td>—</td>
                            <td>Rasoa Voahangy</td>
                            <td>03/09/2026</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td><i class="bi bi-image fs-4 text-body-secondary"></i></td>
                            <td>Envergure</td>
                            <td>Lémur en alimentation</td>
                            <td>—</td>
                            <td>Randria Tojo</td>
                            <td>28/08/2026</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    
  </main>
</template>
