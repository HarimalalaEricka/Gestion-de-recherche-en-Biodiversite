<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/validation/saisies-a-valider">Validation</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/validation/notifications">Notifications</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Notifications</h1>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                to="/validation/notifications">Tout marquer comme lu</router-link></div>
        <div class="list-group"><router-link
                class="list-group-item list-group-item-action d-flex justify-content-between align-items-start gap-2"
                to="/recherches/mes-recherches">
                <div>
                    <div class="fw-semibold">Saisie validée</div><small class="text-body-secondary">Reptiles · Réserve
                        de Berenty · il y a 2 h</small>
                </div><span class="badge text-bg-info">Nouveau</span>
            </router-link><router-link class="list-group-item list-group-item-action d-flex justify-content-between align-items-start gap-2"
                to="/recherches/mes-recherches">
                <div>
                    <div class="fw-semibold">Invitation à une recherche</div><small class="text-body-secondary">Oiseaux
                        d&#x27;Ankarafantsika · hier</small>
                </div><span class="badge text-bg-info">Nouveau</span>
            </router-link><router-link class="list-group-item list-group-item-action d-flex justify-content-between align-items-start gap-2"
                to="/recherches/mes-recherches">
                <div>
                    <div class="fw-semibold">Recherche partagée</div><small class="text-body-secondary">Mammifères
                        d&#x27;Andasibe · il y a 3 jours</small>
                </div>
            </router-link></div>
    
  </main>
</template>
