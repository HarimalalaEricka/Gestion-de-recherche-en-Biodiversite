<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/tableaux/import-dans-un-tableau">Import dans un tableau</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Importer des lignes</h1>
        <ul class="list-unstyled mb-0 d-grid gap-1">
            <li><span class="text-body-secondary">Tableau :</span> Inventaire de la végétation</li>
        </ul>
        <div class="card">
            <div class="card-header fw-semibold">1. Fichier</div>
            <div class="card-body d-grid gap-3">
                <div class="dropzone"><i class="bi bi-cloud-upload fs-2"></i>
                    <div>Déposer un fichier Excel ou CSV</div><input type="file" multiple class="form-control mt-2"
                        aria-label="Fichiers">
                    <ul class="files list-unstyled small mb-0 mt-2"></ul>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">2. Correspondance des colonnes</div>
            <div class="card-body d-grid gap-3">
                <p class="text-body-secondary mb-0">Chaque colonne du fichier est associée à un attribut existant ;
                    sinon un nouvel attribut est créé.</p>
                <div class="table-responsive">
                    <table class="table align-middle" id="map">
                        <thead>
                            <tr>
                                <th scope="col">Colonne du fichier</th>
                                <th scope="col">Attribut</th>
                                <th scope="col">Type</th>
                                <th scope="col">Unité</th>
                                <th scope="col">Obligatoire</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Espèce</td>
                                <td style="min-width:200px"><input class="form-control" list="cat" data-attr value="Nom"
                                        aria-label="Attribut"><span class="badge mt-1" data-info></span></td>
                                <td><select class="form-select" data-type aria-label="Type">
                                        <option value="Texte" selected>Texte</option>
                                        <option value="Entier">Entier</option>
                                        <option value="Décimal">Décimal</option>
                                        <option value="Oui/non">Oui/non</option>
                                        <option value="Date">Date</option>
                                        <option value="Date et heure">Date et heure</option>
                                        <option value="Liste">Liste</option>
                                    </select></td>
                                <td><input class="form-control" type="text" value="" aria-label="Unité"></td>
                                <td class="text-center">
                                    <div class="form-check form-switch d-inline-block"><input class="form-check-input"
                                            type="checkbox" role="switch" checked aria-label="Obligatoire"></div>
                                </td>
                                <td class="text-end"><button type="button" class="btn btn-sm btn-outline-danger"
                                        data-del-row aria-label="Supprimer"><i class="bi bi-trash"></i></button></td>
                            </tr>
                            <tr>
                                <td>Hauteur (m)</td>
                                <td style="min-width:200px"><input class="form-control" list="cat" data-attr
                                        value="Hauteur" aria-label="Attribut"><span class="badge mt-1" data-info></span>
                                </td>
                                <td><select class="form-select" data-type aria-label="Type">
                                        <option value="Texte">Texte</option>
                                        <option value="Entier">Entier</option>
                                        <option value="Décimal" selected>Décimal</option>
                                        <option value="Oui/non">Oui/non</option>
                                        <option value="Date">Date</option>
                                        <option value="Date et heure">Date et heure</option>
                                        <option value="Liste">Liste</option>
                                    </select></td>
                                <td><input class="form-control" type="text" value="m" aria-label="Unité"></td>
                                <td class="text-center">
                                    <div class="form-check form-switch d-inline-block"><input class="form-check-input"
                                            type="checkbox" role="switch" aria-label="Obligatoire"></div>
                                </td>
                                <td class="text-end"><button type="button" class="btn btn-sm btn-outline-danger"
                                        data-del-row aria-label="Supprimer"><i class="bi bi-trash"></i></button></td>
                            </tr>
                            <tr>
                                <td>Nb œufs</td>
                                <td style="min-width:200px"><input class="form-control" list="cat" data-attr
                                        value="Nombre" aria-label="Attribut"><span class="badge mt-1" data-info></span>
                                </td>
                                <td><select class="form-select" data-type aria-label="Type">
                                        <option value="Texte">Texte</option>
                                        <option value="Entier" selected>Entier</option>
                                        <option value="Décimal">Décimal</option>
                                        <option value="Oui/non">Oui/non</option>
                                        <option value="Date">Date</option>
                                        <option value="Date et heure">Date et heure</option>
                                        <option value="Liste">Liste</option>
                                    </select></td>
                                <td><input class="form-control" type="text" value="" aria-label="Unité"></td>
                                <td class="text-center">
                                    <div class="form-check form-switch d-inline-block"><input class="form-check-input"
                                            type="checkbox" role="switch" aria-label="Obligatoire"></div>
                                </td>
                                <td class="text-end"><button type="button" class="btn btn-sm btn-outline-danger"
                                        data-del-row aria-label="Supprimer"><i class="bi bi-trash"></i></button></td>
                            </tr>
                            <tr>
                                <td>Actif</td>
                                <td style="min-width:200px"><input class="form-control" list="cat" data-attr
                                        value="Actif" aria-label="Attribut"><span class="badge mt-1" data-info></span>
                                </td>
                                <td><select class="form-select" data-type aria-label="Type">
                                        <option value="Texte">Texte</option>
                                        <option value="Entier">Entier</option>
                                        <option value="Décimal">Décimal</option>
                                        <option value="Oui/non" selected>Oui/non</option>
                                        <option value="Date">Date</option>
                                        <option value="Date et heure">Date et heure</option>
                                        <option value="Liste">Liste</option>
                                    </select></td>
                                <td><input class="form-control" type="text" value="" aria-label="Unité"></td>
                                <td class="text-center">
                                    <div class="form-check form-switch d-inline-block"><input class="form-check-input"
                                            type="checkbox" role="switch" checked aria-label="Obligatoire"></div>
                                </td>
                                <td class="text-end"><button type="button" class="btn btn-sm btn-outline-danger"
                                        data-del-row aria-label="Supprimer"><i class="bi bi-trash"></i></button></td>
                            </tr>
                            <tr>
                                <td>Date</td>
                                <td style="min-width:200px"><input class="form-control" list="cat" data-attr
                                        value="Date" aria-label="Attribut"><span class="badge mt-1" data-info></span>
                                </td>
                                <td><select class="form-select" data-type aria-label="Type">
                                        <option value="Texte">Texte</option>
                                        <option value="Entier">Entier</option>
                                        <option value="Décimal">Décimal</option>
                                        <option value="Oui/non">Oui/non</option>
                                        <option value="Date" selected>Date</option>
                                        <option value="Date et heure">Date et heure</option>
                                        <option value="Liste">Liste</option>
                                    </select></td>
                                <td><input class="form-control" type="text" value="" aria-label="Unité"></td>
                                <td class="text-center">
                                    <div class="form-check form-switch d-inline-block"><input class="form-check-input"
                                            type="checkbox" role="switch" checked aria-label="Obligatoire"></div>
                                </td>
                                <td class="text-end"><button type="button" class="btn btn-sm btn-outline-danger"
                                        data-del-row aria-label="Supprimer"><i class="bi bi-trash"></i></button></td>
                            </tr>
                        </tbody>
                    </table>
                </div><datalist id="cat">
                    <option value="Nom" label="Texte"></option>
                    <option value="Hauteur" label="Décimal"></option>
                    <option value="Nombre" label="Entier"></option>
                    <option value="Actif" label="Oui/non"></option>
                    <option value="Date" label="Date"></option>
                    <option value="État" label="Liste"></option>
                    <option value="Diamètre du tronc" label="Décimal"></option>
                    <option value="Poids" label="Décimal"></option>
                </datalist>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">3. Contrôle</div>
            <div class="card-body d-grid gap-3">
                <div class="row row-cols-2 row-cols-lg-3 g-3">
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">236</div>
                                <div class="text-body-secondary">lignes valides</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">5</div>
                                <div class="text-body-secondary">doublons détectés</div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-body">
                                <div class="fs-3 fw-semibold">12</div>
                                <div class="text-body-secondary">erreurs</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                to="/tableaux/lignes-du-tableau">Annuler</router-link><router-link class="btn btn-primary"
                to="/tableaux/lignes-du-tableau">Lancer l&#x27;import</router-link></div>
    
  </main>
</template>
