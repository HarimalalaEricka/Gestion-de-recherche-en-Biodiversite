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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/recherches/notes-personnelles">Notes personnelles</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>
            <ul class="nav nav-tabs">
                <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Données</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/recherches/medias-de-la-recherche">Médias</router-link>
                </li>
                <li class="nav-item"><router-link class="nav-link" to="/recherches/collaborateurs">Collaborateurs</router-link>
                </li>
                <li class="nav-item"><router-link class="nav-link"
                        to="/recherches/metadonnees-de-la-recherche">Métadonnées</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/recherches/these">Thèse</router-link></li>
                <li class="nav-item"><router-link class="nav-link active"
                        to="/recherches/notes-personnelles">Notes</router-link></li>
            </ul>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Notes personnelles</label><textarea
                        class="form-control" id="f1" name="notes-personnelles" rows="4">Rakoto Andry</textarea></div>
            </div>
            <p class="text-body-secondary mb-0">Visibles par vous seul · enregistrées automatiquement</p>
        </form>
    
  </main>
</template>
