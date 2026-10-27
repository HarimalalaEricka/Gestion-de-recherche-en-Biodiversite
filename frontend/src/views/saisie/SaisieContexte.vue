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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/saisie-contexte">Saisie : contexte</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Nouvelle observation</h1>
            <ul class="nav nav-tabs">
                <li class="nav-item"><router-link class="nav-link active" to="/saisie/saisie-contexte">1 Contexte</router-link>
                </li>
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-observations">2 Observations</router-link>
                </li>
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-identification">3
                        Identification</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/saisie/saisie-complements">4 Compléments</router-link>
                </li>
            </ul>
            <div class="card">
                <div class="card-header fw-semibold">Événement</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f1">Dataset<span class="text-danger">
                                    *</span></label><select class="form-select" id="f1" name="dataset" required>
                                <option>Mammifères</option>
                                <option>Reptiles</option>
                                <option>Ovipares</option>
                            </select></div>
                        <div class="col-md"><label class="form-label" for="f2">Date de l&#x27;événement<span
                                    class="text-danger"> *</span></label><input class="form-control" type="text" id="f2"
                                name="date-de-l-evenement" value="12/09/2026" required></div>
                        <div class="col-md"><label class="form-label" for="f3">Protocole</label><select
                                class="form-select" id="f3" name="protocole">
                                <option>Transect linéaire</option>
                                <option>Point d&#x27;écoute</option>
                                <option>Piège photographique</option>
                            </select></div>
                    </div>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f4">Méthode
                                d&#x27;échantillonnage</label><input class="form-control" type="text" id="f4"
                                name="methode-d-echantillonnage" value="Transect linéaire"></div>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="card-header fw-semibold">Lieu</div>
                <div class="card-body d-grid gap-3">
                    <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-secondary" href="#" data-demo>Choisir
                            un lieu existant</a></div>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f5">Pays</label><select class="form-select"
                                id="f5" name="pays">
                                <option>Madagascar</option>
                            </select></div>
                        <div class="col-md"><label class="form-label" for="f6">Province<span class="text-danger">
                                    *</span></label><select class="form-select" id="f6" name="province" required>
                                <option>Alaotra-Mangoro</option>
                                <option>Atsimo-Andrefana</option>
                                <option>Boeny</option>
                                <option>Vakinankaratra</option>
                            </select></div>
                        <div class="col-md"><label class="form-label" for="f7">Commune<span class="text-danger">
                                    *</span></label><input class="form-control" type="text" id="f7" name="commune"
                                value="Andasibe" required></div>
                    </div>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f8">Localité</label><input
                                class="form-control" type="text" id="f8" name="localite" value="Piste de Mantadia">
                        </div>
                        <div class="col-md"><label class="form-label" for="f9">Latitude</label><input
                                class="form-control" type="text" id="f9" name="latitude" value="-18,9"></div>
                        <div class="col-md"><label class="form-label" for="f10">Longitude</label><input
                                class="form-control" type="text" id="f10" name="longitude" value="48,4"></div>
                    </div>
                    <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-secondary" href="#" data-demo>Ma
                            position</a></div>
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-secondary"
                    href="/saisie/brouillons-hors-ligne" data-toast="Brouillon enregistré">Enregistrer le
                    brouillon</a><button type="submit" class="btn btn-primary"
                    data-next="/saisie/saisie-observations">Suivant</button></div>
        </form>
    
  </main>
</template>
