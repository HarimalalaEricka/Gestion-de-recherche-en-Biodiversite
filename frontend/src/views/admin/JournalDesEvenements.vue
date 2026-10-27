<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/admin/utilisateurs">Administration</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/admin/journal-des-evenements">Journal des &#195;&#169;v&#195;&#169;nements</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Journal des événements</h1>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Utilisateur</label><select class="form-select"
                        id="f1" name="utilisateur">
                        <option>Tous</option>
                        <option>Option 1</option>
                        <option>Option 2</option>
                    </select></div>
                <div class="col-md"><label class="form-label" for="f2">Recherche</label><select class="form-select"
                        id="f2" name="recherche">
                        <option>Inventaire des lémuriens d&#x27;Andasibe</option>
                        <option>Suivi des reptiles de Berenty</option>
                        <option>Oiseaux d&#x27;Ankarafantsika</option>
                        <option>Amphibiens de Ranomafana</option>
                    </select></div>
                <div class="col-md"><label class="form-label" for="f3">Du</label><input class="form-control" type="text"
                        id="f3" name="du" value=""></div>
                <div class="col-md"><label class="form-label" for="f4">Au</label><input class="form-control" type="text"
                        id="f4" name="au" value="30/09/2026"></div>
            </div>
            <div class="card">
                <div class="card-header"><input type="search" form="nf"
                        class="form-control form-control-sm table-filter" placeholder="Filtrer..." aria-label="Filtrer">
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0" data-table>
                        <thead>
                            <tr>
                                <th scope="col">Date</th>
                                <th scope="col">Utilisateur</th>
                                <th scope="col">Action</th>
                                <th scope="col">Objet</th>
                                <th scope="col">Détail</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>12/09/2026</td>
                                <td>Rakoto Andry</td>
                                <td>Création</td>
                                <td>Recherche</td>
                                <td>Statut passé à Publiée</td>
                            </tr>
                            <tr>
                                <td>03/09/2026</td>
                                <td>Rasoa Voahangy</td>
                                <td>Validation</td>
                                <td>Dataset</td>
                                <td>Saisie validée</td>
                            </tr>
                            <tr>
                                <td>28/08/2026</td>
                                <td>Randria Tojo</td>
                                <td>Modification</td>
                                <td>Observation</td>
                                <td>Champ modifié : nombre</td>
                            </tr>
                            <tr>
                                <td>15/08/2026</td>
                                <td>Razafy Mialy</td>
                                <td>Partage</td>
                                <td>Recherche</td>
                                <td>Statut passé à Publiée</td>
                            </tr>
                            <tr>
                                <td>12/09/2026</td>
                                <td>Rakoto Andry</td>
                                <td>Création</td>
                                <td>Dataset</td>
                                <td>Saisie validée</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    
  </main>
</template>
