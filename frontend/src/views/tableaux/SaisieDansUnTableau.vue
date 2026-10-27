<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/tableaux/saisie-dans-un-tableau">Saisie dans un tableau</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Suivi des nids : nouvelle ligne</h1>
            <p class="text-body-secondary mb-0">Le formulaire est généré à partir des colonnes du tableau.</p>
            <div class="card">
                <div class="card-header fw-semibold">Nouvelle ligne</div>
                <div class="card-body">
                    <div id="table-entry-grid" class="row g-3"></div>
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                    to="/tableaux/lignes-du-tableau">Annuler</router-link><router-link class="btn btn-outline-secondary"
                    to="/tableaux/saisie-dans-un-tableau">Enregistrer et ajouter une autre</router-link><button
                    type="submit" class="btn btn-primary" data-next="/tableaux/lignes-du-tableau">Enregistrer
                    la ligne</button></div>
        </form>
    
  </main>
</template>
