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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/admin/utilisateurs">Utilisateurs</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Utilisateurs</h1>
            <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary"
                    data-next="/admin/inviter-un-utilisateur">+ Inviter un utilisateur</button></div>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Rechercher</label><input class="form-control"
                        type="text" id="f1" name="rechercher" value="Inventaire des lémuriens d&#x27;Andasibe"></div>
                <div class="col-md"><label class="form-label" for="f2">Rôle</label><select class="form-select" id="f2"
                        name="role">
                        <option>Administrateur</option>
                        <option>Chercheur</option>
                        <option>Agent de saisie</option>
                        <option>Lecteur</option>
                    </select></div>
                <div class="col-md"><label class="form-label" for="f3">Statut</label><select class="form-select" id="f3"
                        name="statut">
                        <option>Tous</option>
                        <option>Actif</option>
                        <option>Invité</option>
                        <option>Désactivé</option>
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
                                <th scope="col">E-mail</th>
                                <th scope="col">Rôle</th>
                                <th scope="col">Statut</th>
                                <th scope="col">Dernière connexion</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Rakoto Andry</td>
                                <td>andry.rakoto@peregrine.example</td>
                                <td>Chercheur</td>
                                <td><span class="badge text-bg-success">Publiée</span></td>
                                <td>12/09/2026</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/admin/fiche-d-un-utilisateur">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Rasoa Voahangy</td>
                                <td>voahangy.rasoa@peregrine.example</td>
                                <td>Étudiant</td>
                                <td><span class="badge text-bg-secondary">Privée</span></td>
                                <td>03/09/2026</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/admin/fiche-d-un-utilisateur">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Randria Tojo</td>
                                <td>tojo.randria@peregrine.example</td>
                                <td>Agent de saisie</td>
                                <td><span class="badge text-bg-secondary">Brouillon</span></td>
                                <td>28/08/2026</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/admin/fiche-d-un-utilisateur">Ouvrir</router-link></td>
                            </tr>
                            <tr>
                                <td>Razafy Mialy</td>
                                <td>mialy.razafy@peregrine.example</td>
                                <td>Administrateur</td>
                                <td><span class="badge text-bg-success">Publiée</span></td>
                                <td>15/08/2026</td>
                                <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                        to="/admin/fiche-d-un-utilisateur">Ouvrir</router-link></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    
  </main>
</template>
