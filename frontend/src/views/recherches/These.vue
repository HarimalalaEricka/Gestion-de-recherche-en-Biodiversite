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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/recherches/these">Th&#195;&#168;se</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>
        <ul class="nav nav-tabs">
            <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Données</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/medias-de-la-recherche">Médias</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/collaborateurs">Collaborateurs</router-link></li>
            <li class="nav-item"><router-link class="nav-link"
                    to="/recherches/metadonnees-de-la-recherche">Métadonnées</router-link></li>
            <li class="nav-item"><router-link class="nav-link active" to="/recherches/these">Thèse</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/recherches/notes-personnelles">Notes</router-link></li>
        </ul>
        <ul class="list-unstyled mb-0 d-grid gap-1">
            <li><span class="text-body-secondary">État :</span> En cours</li>
        </ul>
        <div class="dropzone"><i class="bi bi-cloud-upload fs-2"></i>
            <div>Déposer le fichier de thèse (PDF)</div><input type="file" multiple class="form-control mt-2"
                aria-label="Fichiers">
            <ul class="files list-unstyled small mb-0 mt-2"></ul>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary" to="/recherches/these">Déposer la
                thèse</router-link><a class="btn btn-outline-secondary" href="#" data-demo>Télécharger</a></div>
    
  </main>
</template>
