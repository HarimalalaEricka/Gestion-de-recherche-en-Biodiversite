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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/saisie-complements">Saisie : compl&#195;&#169;ments</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Nouvelle observation</h1>
            <ul class="nav nav-tabs">
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-contexte">1 Contexte</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-observations">2 Observations</router-link>
                </li>
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-identification">3
                        Identification</router-link></li>
                <li class="nav-item"><router-link class="nav-link active" to="/saisie/saisie-complements">4
                        Compléments</router-link></li>
            </ul>
            <div class="card">
                <div class="card-header fw-semibold">Mesures (ligne 1)</div>
                <div class="card-body d-grid gap-3">
                    <div class="card">
                        <div class="card-header"><input type="search" form="nf"
                                class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                                aria-label="Filtrer"></div>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0" data-table>
                                <thead>
                                    <tr>
                                        <th scope="col">Type</th>
                                        <th scope="col">Valeur</th>
                                        <th scope="col">Unité</th>
                                        <th scope="col">Méthode</th>
                                        <th scope="col">Précision</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Poids</td>
                                        <td>2,4</td>
                                        <td>kg</td>
                                        <td>Transect linéaire</td>
                                        <td>±0,1</td>
                                    </tr>
                                    <tr>
                                        <td>Longueur du corps</td>
                                        <td>15,8</td>
                                        <td>cm</td>
                                        <td>Pesée</td>
                                        <td>±0,5</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-secondary" href="#" data-demo>+
                            Ajouter une mesure</a></div>
                </div>
            </div>
            <div class="card">
                <div class="card-header fw-semibold">Médias</div>
                <div class="card-body d-grid gap-3">
                    <div class="dropzone"><i class="bi bi-cloud-upload fs-2"></i>
                        <div>Déposer ici des photos, vidéos ou fichiers audio</div><input type="file" multiple
                            class="form-control mt-2" aria-label="Fichiers">
                        <ul class="files list-unstyled small mb-0 mt-2"></ul>
                    </div>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f1">Légende</label><input
                                class="form-control" type="text" id="f1" name="legende" value="Lémur en alimentation">
                        </div>
                        <div class="col-md"><label class="form-label" for="f2">Appareil de capture</label><input
                                class="form-control" type="text" id="f2" name="appareil-de-capture" value=""></div>
                        <div class="col-md"><label class="form-label" for="f3">Commentaire</label><input
                                class="form-control" type="text" id="f3" name="commentaire" value="—"></div>
                    </div>
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                    to="/saisie/saisie-identification">Précédent</router-link><a class="btn btn-outline-secondary"
                    href="/saisie/brouillons-hors-ligne" data-toast="Brouillon enregistré">Enregistrer le
                    brouillon</a><button type="submit" class="btn btn-primary"
                    data-next="/saisie/saisie-recapitulatif">Terminer et soumettre</button></div>
        </form>
    
  </main>
</template>
