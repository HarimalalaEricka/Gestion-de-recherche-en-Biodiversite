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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/admin/agents">Agents</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Agents</h1>
            <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary">+ Ajouter un
                    agent</button><a class="btn btn-outline-secondary" href="#" data-demo>Fusionner des doublons</a>
            </div>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Rechercher</label><input class="form-control"
                        type="text" id="f1" name="rechercher" value="Inventaire des lémuriens d&#x27;Andasibe"></div>
                <div class="col-md"><label class="form-label" for="f2">Type</label><select class="form-select" id="f2"
                        name="type">
                        <option>Personne</option>
                        <option>Groupe</option>
                        <option>Institution</option>
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
                                <th scope="col">Nom</th>
                                <th scope="col">Type</th>
                                <th scope="col">Utilisateur lié</th>
                                <th scope="col">Observations</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Rakoto Andry</td>
                                <td>Personne</td>
                                <td>Rakoto Andry</td>
                                <td>12</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/recherches/mes-recherches">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Rasoa Voahangy</td>
                                <td>Institution</td>
                                <td>Rasoa Voahangy</td>
                                <td>48</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/recherches/mes-recherches">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Randria Tojo</td>
                                <td>Groupe</td>
                                <td>Randria Tojo</td>
                                <td>236</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/recherches/mes-recherches">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Razafy Mialy</td>
                                <td>Personne</td>
                                <td>Razafy Mialy</td>
                                <td>5</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/recherches/mes-recherches">Ouvrir</router-link></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    
  </main>
</template>
