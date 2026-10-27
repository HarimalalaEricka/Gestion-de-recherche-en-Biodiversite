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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/recherches/modifier-une-recherche">Modifier une recherche</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Modifier la recherche</h1>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Sujet<span class="text-danger">
                            *</span></label><input class="form-control" type="text" id="f1" name="sujet"
                        value="Inventaire des lémuriens d&#x27;Andasibe" required></div>
                <div class="col-md"><label class="form-label" for="f2">Date de début<span class="text-danger">
                            *</span></label><input class="form-control" type="text" id="f2" name="date-de-debut"
                        value="12/09/2026" required></div>
                <div class="col-md"><label class="form-label" for="f3">Date de fin</label><input class="form-control"
                        type="text" id="f3" name="date-de-fin" value="12/09/2026"></div>
                <div class="col-md"><label class="form-label" for="f4">Visibilité</label><select class="form-select"
                        id="f4" name="visibilite">
                        <option>Privée</option>
                        <option>Publiée</option>
                    </select></div>
            </div>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f5">Description</label><textarea class="form-control"
                        id="f5" name="description"
                        rows="4">Inventaire des lémuriens du parc national d&#x27;Andasibe, saison 2026.</textarea>
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-danger"
                    href="/recherches/mes-recherches"
                    data-confirm="Voulez-vous vraiment effectuer cette action ?">Supprimer</a><a
                    class="btn btn-outline-danger" href="/recherches/mes-recherches"
                    data-confirm="Voulez-vous vraiment effectuer cette action ?">Dépublier</a><button type="submit"
                    class="btn btn-primary" data-next="/recherches/mes-recherches">Enregistrer</button></div>
        </form>
    
  </main>
</template>
