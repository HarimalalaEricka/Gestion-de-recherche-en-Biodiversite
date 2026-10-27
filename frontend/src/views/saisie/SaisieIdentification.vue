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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/saisie-identification">Saisie : identification</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Nouvelle observation</h1>
            <ul class="nav nav-tabs">
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-contexte">1 Contexte</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-observations">2 Observations</router-link>
                </li>
                <li class="nav-item"><router-link class="nav-link active" to="/saisie/saisie-identification">3
                        Identification</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-complements">4 Compléments</router-link>
                </li>
            </ul>
            <div class="card">
                <div class="card-header fw-semibold">Identification de la ligne 1</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f1">Taxon<span class="text-danger">
                                    *</span></label><input class="form-control" type="text" id="f1" name="taxon"
                                value="Lemur catta" required></div>
                        <div class="col-md"><label class="form-label" for="f2">Date d&#x27;identification<span
                                    class="text-danger"> *</span></label><input class="form-control" type="text" id="f2"
                                name="date-d-identification" value="12/09/2026" required></div>
                        <div class="col-md"><label class="form-label" for="f3">Identifié par<span class="text-danger">
                                    *</span></label><select class="form-select" id="f3" name="identifie-par" required>
                                <option>Rakoto Andry</option>
                                <option>Rasoa Voahangy</option>
                                <option>Randria Tojo</option>
                                <option>Razafy Mialy</option>
                            </select></div>
                    </div>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f4">Qualificatif (cf., aff., ?)</label><input
                                class="form-control" type="text" id="f4" name="qualificatif-cf-aff" value="cf."></div>
                        <div class="col-md"><label class="form-label" for="f5">Références</label><select
                                class="form-select" id="f5" name="references">
                                <option>Auteur, 2019. Titre de la référence.</option>
                            </select></div>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="card-header fw-semibold">Nouveau taxon</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f6">Nom scientifique<span
                                    class="text-danger"> *</span></label><input class="form-control" type="text" id="f6"
                                name="nom-scientifique" value="Lemur catta" required></div>
                        <div class="col-md"><label class="form-label" for="f7">Rang<span class="text-danger">
                                    *</span></label><select class="form-select" id="f7" name="rang" required>
                                <option>Espèce</option>
                                <option>Genre</option>
                                <option>Famille</option>
                            </select></div>
                    </div>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f8">Règne</label><input class="form-control"
                                type="text" id="f8" name="regne" value="Animalia"></div>
                        <div class="col-md"><label class="form-label" for="f9">Embranchement</label><input
                                class="form-control" type="text" id="f9" name="embranchement" value="Chordata"></div>
                        <div class="col-md"><label class="form-label" for="f10">Classe</label><input
                                class="form-control" type="text" id="f10" name="classe" value="Mammalia"></div>
                        <div class="col-md"><label class="form-label" for="f11">Ordre</label><input class="form-control"
                                type="text" id="f11" name="ordre" value="Primates"></div>
                    </div>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f12">Famille</label><input
                                class="form-control" type="text" id="f12" name="famille" value="Lemuridae"></div>
                        <div class="col-md"><label class="form-label" for="f13">Genre</label><input class="form-control"
                                type="text" id="f13" name="genre" value="Lemur"></div>
                        <div class="col-md"><label class="form-label" for="f14">Épithète spécifique</label><input
                                class="form-control" type="text" id="f14" name="epithete-specifique" value="catta">
                        </div>
                    </div>
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                    to="/saisie/saisie-observations">Précédent</router-link><a class="btn btn-outline-secondary"
                    href="/saisie/brouillons-hors-ligne" data-toast="Brouillon enregistré">Enregistrer le
                    brouillon</a><button type="submit" class="btn btn-primary"
                    data-next="/saisie/saisie-complements">Suivant : compléments</button></div>
        </form>
    
  </main>
</template>
