<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/recherches/mes-recherches">Mes recherches</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/recherches/collaborateurs">Collaborateurs</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Suivi des reptiles de Berenty</h1>
            <ul class="nav nav-tabs">
                <li class="nav-item"><router-link class="nav-link" to="/recherches/mes-recherches">Données</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/recherches/medias-de-la-recherche">Médias</router-link>
                </li>
                <li class="nav-item"><router-link class="nav-link active"
                        to="/recherches/collaborateurs">Collaborateurs</router-link></li>
                <li class="nav-item"><router-link class="nav-link"
                        to="/recherches/metadonnees-de-la-recherche">Métadonnées</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/recherches/these">Thèse</router-link></li>
                <li class="nav-item"><router-link class="nav-link" to="/recherches/notes-personnelles">Notes</router-link></li>
            </ul>
            <div class="card">
                <div class="card-header fw-semibold">Inviter</div>
                <div class="card-body d-grid gap-3">
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f1">Nom ou e-mail</label><input
                                class="form-control" type="email" id="f1" name="nom-ou-e-mail"
                                value="andry.rakoto@peregrine.example"></div>
                        <div class="col-md"><label class="form-label" for="f2">Rôle sur la recherche</label><select
                                class="form-select" id="f2" name="role-sur-la-recherche">
                                <option>Administrateur</option>
                                <option>Chercheur</option>
                                <option>Agent de saisie</option>
                                <option>Lecteur</option>
                            </select></div>
                    </div>
                    <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary"
                            data-next="/recherches/collaborateurs">Inviter</button></div>
                </div>
            </div>
            <div class="card">
                <div class="card-header"><input type="search" form="nf"
                        class="form-control form-control-sm table-filter" placeholder="Filtrer..." aria-label="Filtrer">
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0" data-table>
                        <thead>
                            <tr>
                                <th scope="col">Personne</th>
                                <th scope="col">Rôle</th>
                                <th scope="col">Statut du compte</th>
                                <th scope="col">Ajouté le</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Rakoto Andry</td>
                                <td>Chercheur principal</td>
                                <td><span class="badge text-bg-success">Actif</span></td>
                                <td>12/09/2026</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/recherches/mes-recherches">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Rasoa Voahangy</td>
                                <td>Agent de saisie</td>
                                <td><span class="badge text-bg-success">Actif</span></td>
                                <td>03/09/2026</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/recherches/mes-recherches">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Randria Tojo</td>
                                <td>Lecteur</td>
                                <td><span class="badge text-bg-info">Invité</span></td>
                                <td>28/08/2026</td>
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
