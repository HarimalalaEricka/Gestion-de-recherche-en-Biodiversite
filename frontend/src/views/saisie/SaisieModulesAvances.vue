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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/saisie-modules-avances">Saisie : modules avanc&#195;&#169;s</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Nouvelle observation : modules avancés</h1>
            <div class="card">
                <div class="card-header fw-semibold">Contexte géologique (si fossile)</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f1">Groupe</label><input class="form-control"
                                type="text" id="f1" name="groupe" value=""></div>
                        <div class="col-md"><label class="form-label" for="f2">Formation</label><input
                                class="form-control" type="text" id="f2" name="formation" value=""></div>
                        <div class="col-md"><label class="form-label" for="f3">Membre</label><input class="form-control"
                                type="text" id="f3" name="membre" value=""></div>
                        <div class="col-md"><label class="form-label" for="f4">Couche</label><input class="form-control"
                                type="text" id="f4" name="couche" value=""></div>
                        <div class="col-md"><label class="form-label" for="f5">Période la plus ancienne</label><input
                                class="form-control" type="text" id="f5" name="periode-la-plus-ancienne"
                                value="2019 – 2026"></div>
                        <div class="col-md"><label class="form-label" for="f6">Période la plus récente</label><input
                                class="form-control" type="text" id="f6" name="periode-la-plus-recente"
                                value="2019 – 2026"></div>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="card-header fw-semibold">Matériel / collection</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f7">Code institution</label><input
                                class="form-control" type="text" id="f7" name="code-institution" value=""></div>
                        <div class="col-md"><label class="form-label" for="f8">Code collection</label><input
                                class="form-control" type="text" id="f8" name="code-collection" value=""></div>
                        <div class="col-md"><label class="form-label" for="f9">N° de catalogue</label><input
                                class="form-control" type="text" id="f9" name="n-de-catalogue" value=""></div>
                        <div class="col-md"><label class="form-label" for="f10">Catégorie<span class="text-danger">
                                    *</span></label><select class="form-select" id="f10" name="categorie" required>
                                <option>Spécimen</option>
                                <option>Échantillon</option>
                            </select></div>
                        <div class="col-md"><label class="form-label" for="f11">Type<span class="text-danger">
                                    *</span></label><select class="form-select" id="f11" name="type" required>
                                <option>Personne</option>
                                <option>Groupe</option>
                                <option>Institution</option>
                            </select></div>
                        <div class="col-md"><label class="form-label" for="f12">Préparations</label><input
                                class="form-control" type="text" id="f12" name="preparations" value=""></div>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="card-header fw-semibold">Analyse ADN</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f13">Protocole moléculaire</label><select
                                class="form-select" id="f13" name="protocole-moleculaire">
                                <option>COI barcoding</option>
                            </select></div>
                        <div class="col-md"><label class="form-label" for="f14">Séquence</label><input
                                class="form-control" type="text" id="f14" name="sequence" value=""></div>
                        <div class="col-md"><label class="form-label" for="f15">Nombre de lectures</label><input
                                class="form-control" type="text" id="f15" name="nombre-de-lectures"
                                value="Rakoto Andry"></div>
                        <div class="col-md"><label class="form-label" for="f16">Total des lectures</label><input
                                class="form-control" type="text" id="f16" name="total-des-lectures" value=""></div>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="card-header fw-semibold">Interactions</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f17">Avec l&#x27;observation</label><select
                                class="form-select" id="f17" name="avec-l-observation">
                                <option>Observation 1042</option>
                                <option>Observation 1043</option>
                            </select></div>
                        <div class="col-md"><label class="form-label" for="f18">Type d&#x27;interaction</label><select
                                class="form-select" id="f18" name="type-d-interaction">
                                <option>Prédation</option>
                                <option>Parasitisme</option>
                                <option>Symbiose</option>
                            </select></div>
                        <div class="col-md"><label class="form-label" for="f19">Description</label><input
                                class="form-control" type="text" id="f19" name="description"
                                value="Inventaire des lémuriens du parc national d&#x27;Andasibe, saison 2026."></div>
                    </div>
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                    to="/saisie/saisie-observations">Précédent</router-link><button type="submit"
                    class="btn btn-primary" data-next="/saisie/saisie-recapitulatif">Enregistrer</button>
            </div>
        </form>
    
  </main>
</template>
