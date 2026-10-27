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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/saisie-recapitulatif">Saisie : r&#195;&#169;capitulatif</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Récapitulatif avant envoi</h1>
        <div class="card">
            <div class="card-header fw-semibold">Événement</div>
            <div class="card-body d-grid gap-3">
                <ul class="list-unstyled mb-0 d-grid gap-1">
                    <li><span class="text-body-secondary">Dataset :</span> Reptiles</li>
                    <li><span class="text-body-secondary">Date :</span> 28/08/2026</li>
                    <li><span class="text-body-secondary">Lieu :</span> Parc national de Ranomafana</li>
                    <li><span class="text-body-secondary">Protocole :</span> Point d&#x27;écoute</li>
                </ul>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">Observations</div>
            <div class="card-body d-grid gap-3">
                <div class="card">
                    <div class="card-header"><input type="search" form="nf"
                            class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                            aria-label="Filtrer"></div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0" data-table>
                            <thead>
                                <tr>
                                    <th scope="col">Taxon</th>
                                    <th scope="col">Statut</th>
                                    <th scope="col">Nombre</th>
                                    <th scope="col">Mesures</th>
                                    <th scope="col">Médias</th>
                                    <th scope="col">Alerte</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Lemur catta</td>
                                    <td><span class="badge text-bg-success">Publiée</span></td>
                                    <td>Rakoto Andry</td>
                                    <td>12</td>
                                    <td>12</td>
                                    <td>—</td>
                                </tr>
                                <tr>
                                    <td>Propithecus verreauxi</td>
                                    <td><span class="badge text-bg-secondary">Privée</span></td>
                                    <td>Rasoa Voahangy</td>
                                    <td>48</td>
                                    <td>48</td>
                                    <td>Date future</td>
                                </tr>
                                <tr>
                                    <td>Furcifer pardalis</td>
                                    <td><span class="badge text-bg-secondary">Brouillon</span></td>
                                    <td>Randria Tojo</td>
                                    <td>236</td>
                                    <td>236</td>
                                    <td>—</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">Erreurs à corriger</div>
            <div class="card-body d-grid gap-3">
                <ul class="list-unstyled mb-0 d-grid gap-1">
                    <li><span class="text-body-secondary">Date de l&#x27;événement :</span> champ obligatoire</li>
                </ul>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                to="/saisie/saisie-complements">Précédent</router-link><a class="btn btn-outline-secondary"
                href="/saisie/brouillons-hors-ligne" data-toast="Brouillon enregistré">Enregistrer le
                brouillon</a><router-link class="btn btn-primary" to="/saisie/fiche-d-un-dataset">Soumettre</router-link></div>
    
  </main>
</template>
