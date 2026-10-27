<!-- Copiez ce fichier dans views/<groupe>/MaPage.vue, ajustez le chemin d'import,
     puis déclarez la route dans router/routes.js (et le menu dans config/app.js). -->
<script setup>
import { usePage } from '../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
          
          <h1 class="h3 mb-0">Titre de la page</h1>
          <p class="text-body-secondary mb-0">Description courte.</p>
          <div class="card">
               <div class="card-header fw-semibold">Bloc</div>
               <div class="card-body">Contenu</div>
          </div>
     
  </main>
</template>
