<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/partage/jeux-de-donnees-partages">Jeux de donn&#195;&#169;es partag&#195;&#169;s</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Jeux de données partagés</h1>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Rechercher</label><input class="form-control"
                        type="text" id="f1" name="rechercher" value="Inventaire des lémuriens d&#x27;Andasibe"></div>
                <div class="col-md"><label class="form-label" for="f2">Licence</label><select class="form-select"
                        id="f2" name="licence">
                        <option>CC BY 4.0</option>
                        <option>CC BY-NC 4.0</option>
                        <option>CC0</option>
                    </select></div>
                <div class="col-md"><label class="form-label" for="f3">Période</label><select class="form-select"
                        id="f3" name="periode">
                        <option>Toutes</option>
                        <option>Cette année</option>
                        <option>30 derniers jours</option>
                    </select></div>
            </div>
            <div class="card">
                <div class="card-header"><input type="search" form="nf"
                        class="form-control form-control-sm table-filter" placeholder="Filtrer..." aria-label="Filtrer">
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0" data-table>
                        <thead>
                            <tr>
                                <th scope="col">Dataset</th>
                                <th scope="col">Recherche</th>
                                <th scope="col">Auteurs</th>
                                <th scope="col">Licence</th>
                                <th scope="col">Observations</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Mammifères</td>
                                <td>Inventaire des lémuriens d&#x27;Andasibe</td>
                                <td>Rakoto Andry, Rasoa Voahangy</td>
                                <td>CC BY 4.0</td>
                                <td>12</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/saisie/fiche-d-un-dataset">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Reptiles</td>
                                <td>Suivi des reptiles de Berenty</td>
                                <td>Rakoto Andry, Rasoa Voahangy</td>
                                <td>CC BY-NC 4.0</td>
                                <td>48</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/saisie/fiche-d-un-dataset">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Ovipares</td>
                                <td>Oiseaux d&#x27;Ankarafantsika</td>
                                <td>Rakoto Andry, Rasoa Voahangy</td>
                                <td>CC0</td>
                                <td>236</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/saisie/fiche-d-un-dataset">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Mammifères</td>
                                <td>Amphibiens de Ranomafana</td>
                                <td>Rakoto Andry, Rasoa Voahangy</td>
                                <td>CC BY 4.0</td>
                                <td>5</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/saisie/fiche-d-un-dataset">Ouvrir</router-link></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    
  </main>
</template>
