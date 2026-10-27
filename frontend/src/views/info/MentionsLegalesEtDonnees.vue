<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/info/aide">Aide</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/info/mentions-legales-et-donnees">Mentions l&#195;&#169;gales et donn&#195;&#169;es</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Mentions légales et données</h1>
        <div class="card">
            <div class="card-header fw-semibold">Licences des données</div>
            <div class="card-body d-grid gap-3">
                <p class="text-body-secondary mb-0">Chaque dataset est publié sous la licence choisie par son auteur
                    (par exemple CC BY 4.0).</p>
                <ul class="list-unstyled mb-0 d-grid gap-1">
                    <li><span class="text-body-secondary">Détenteur des droits :</span> The Peregrine Fund</li>
                </ul>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">Citation recommandée</div>
            <div class="card-body d-grid gap-3">
                <p class="text-body-secondary mb-0">Auteurs (année). Titre du jeu de données. The Peregrine Fund.</p>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">Protection des données</div>
            <div class="card-body d-grid gap-3">
                <p class="text-body-secondary mb-0">Les données personnelles des utilisateurs servent uniquement à gérer
                    les comptes. Texte à valider avec The Peregrine Fund.</p>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">Contact</div>
            <div class="card-body d-grid gap-3">
                <ul class="list-unstyled mb-0 d-grid gap-1">
                    <li><span class="text-body-secondary">Courriel :</span> contact@peregrine.example</li>
                </ul>
            </div>
        </div>
    
  </main>
</template>
