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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/info/aide">Aide</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Aide</h1>
        <div class="accordion">
            <div class="accordion-item">
                <h2 class="accordion-header"><button class="accordion-button" type="button" data-bs-toggle="collapse"
                        data-bs-target="#a0">Comment créer une recherche ?</button></h2>
                <div id="a0" class="accordion-collapse collapse show">
                    <div class="accordion-body">Dans « Mes recherches », cliquez sur « Nouvelle recherche », renseignez
                        le sujet et les dates, puis choisissez la visibilité.</div>
                </div>
            </div>
            <div class="accordion-item">
                <h2 class="accordion-header"><button class="accordion-button collapsed" type="button"
                        data-bs-toggle="collapse" data-bs-target="#a1">Comment saisir sans connexion ?</button></h2>
                <div id="a1" class="accordion-collapse collapse">
                    <div class="accordion-body">Ouvrez la saisie dans le même navigateur que celui de votre connexion.
                        Les brouillons sont gardés dans le navigateur puis envoyés au retour du réseau.</div>
                </div>
            </div>
            <div class="accordion-item">
                <h2 class="accordion-header"><button class="accordion-button collapsed" type="button"
                        data-bs-toggle="collapse" data-bs-target="#a2">Comment importer un fichier Excel ou CSV
                        ?</button></h2>
                <div id="a2" class="accordion-collapse collapse">
                    <div class="accordion-body">Dans la fiche d&#x27;un dataset, choisissez « Importer Excel / CSV »,
                        associez les colonnes puis lancez l&#x27;import.</div>
                </div>
            </div>
            <div class="accordion-item">
                <h2 class="accordion-header"><button class="accordion-button collapsed" type="button"
                        data-bs-toggle="collapse" data-bs-target="#a3">Qui peut voir mes données ?</button></h2>
                <div id="a3" class="accordion-collapse collapse">
                    <div class="accordion-body">Vos recherches sont privées par défaut. Vous choisissez de les publier
                        dans l&#x27;espace partagé.</div>
                </div>
            </div>
            <div class="accordion-item">
                <h2 class="accordion-header"><button class="accordion-button collapsed" type="button"
                        data-bs-toggle="collapse" data-bs-target="#a4">Comment valider les saisies d&#x27;un agent
                        ?</button></h2>
                <div id="a4" class="accordion-collapse collapse">
                    <div class="accordion-body">Ouvrez « À valider », contrôlez la saisie puis validez-la ou rejetez-la
                        avec un motif.</div>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">Glossaire</div>
            <div class="card-body d-grid gap-3">
                <ul class="list-unstyled mb-0 d-grid gap-1">
                    <li><span class="text-body-secondary">Dataset :</span> jeu de données regroupant des événements
                        d&#x27;observation.</li>
                    <li><span class="text-body-secondary">Taxon :</span> groupe d&#x27;organismes (espèce, genre,
                        famille).</li>
                    <li><span class="text-body-secondary">Métadonnées :</span> propriétés d&#x27;une donnée (méthode,
                        auteur, licence).</li>
                </ul>
            </div>
        </div>
    
  </main>
</template>
