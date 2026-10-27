<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="min-vh-100 d-flex align-items-center justify-content-center p-3">
        <div class="card shadow-sm w-100" style="max-width:440px">
            <div class="card-body p-4 d-grid gap-3">
                <div class="text-center">
                    <img src="../../assets/img/logo-large.svg" alt="Logo Nexora" width="120" height="84" class="mb-3">
                    <div class="fw-bold">Nexora</div>
                </div>
                <h1 class="h3 mb-0">Accès refusé</h1>
                <p class="text-body-secondary mb-0">Vous n&#x27;avez pas les droits pour voir cette page.</p>
                <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary" to="/accueil">Retour
                        à l&#x27;accueil</router-link></div>
            </div>
        </div>
    
  </main>
</template>
