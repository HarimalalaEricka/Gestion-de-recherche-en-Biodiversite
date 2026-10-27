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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/nouveau-dataset">Nouveau dataset</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Nouveau dataset</h1>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Nom du dataset<span class="text-danger">
                            *</span></label><input class="form-control" type="text" id="f1" name="nom-du-dataset"
                        value="Mammifères" required></div>
                <div class="col-md"><label class="form-label" for="f2">Licence</label><select class="form-select"
                        id="f2" name="licence">
                        <option>CC BY 4.0</option>
                        <option>CC BY-NC 4.0</option>
                        <option>CC0</option>
                    </select></div>
                <div class="col-md"><label class="form-label" for="f3">Détenteur des droits</label><input
                        class="form-control" type="text" id="f3" name="detenteur-des-droits" value="The Peregrine Fund">
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                    to="/recherches/mes-recherches">Annuler</router-link><button type="submit" class="btn btn-primary"
                    data-next="/saisie/fiche-d-un-dataset">Créer le dataset</button></div>
        </form>
    
  </main>
</template>
