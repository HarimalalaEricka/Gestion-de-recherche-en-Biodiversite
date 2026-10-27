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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/admin/fiche-d-un-utilisateur">Fiche d&#39;un utilisateur</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Rasoa Voahangy</h1>
            <ul class="list-unstyled mb-0 d-grid gap-1">
                <li><span class="text-body-secondary">Statut :</span> Actif</li>
            </ul>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Nom<span class="text-danger">
                            *</span></label><input class="form-control" type="text" id="f1" name="nom"
                        value="Rakoto Andry" required></div>
                <div class="col-md"><label class="form-label" for="f2">Prénom</label><input class="form-control"
                        type="text" id="f2" name="prenom" value="Rakoto Andry"></div>
                <div class="col-md"><label class="form-label" for="f3">Téléphone</label><input class="form-control"
                        type="text" id="f3" name="telephone" value=""></div>
                <div class="col-md"><label class="form-label" for="f4">Rôle</label><select class="form-select" id="f4"
                        name="role">
                        <option>Administrateur</option>
                        <option>Chercheur</option>
                        <option>Agent de saisie</option>
                        <option>Lecteur</option>
                    </select></div>
            </div>
            <div class="card">
                <div class="card-header fw-semibold">Recherches de la personne</div>
                <div class="card-body d-grid gap-3">
                    <div class="card">
                        <div class="card-header"><input type="search" form="nf"
                                class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                                aria-label="Filtrer"></div>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0" data-table>
                                <thead>
                                    <tr>
                                        <th scope="col">Recherche</th>
                                        <th scope="col">Statut</th>
                                        <th scope="col"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Inventaire des lémuriens d&#x27;Andasibe</td>
                                        <td><span class="badge text-bg-success">Publiée</span></td>
                                        <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                to="/recherches/mes-recherches">Ouvrir</router-link></td>
                                    </tr>
                                    <tr>
                                        <td>Suivi des reptiles de Berenty</td>
                                        <td><span class="badge text-bg-secondary">Privée</span></td>
                                        <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                to="/recherches/mes-recherches">Ouvrir</router-link></td>
                                    </tr>
                                    <tr>
                                        <td>Oiseaux d&#x27;Ankarafantsika</td>
                                        <td><span class="badge text-bg-secondary">Brouillon</span></td>
                                        <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                                to="/recherches/mes-recherches">Ouvrir</router-link></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary"
                    data-next="/admin/utilisateurs">Enregistrer</button><a class="btn btn-outline-secondary"
                    href="#" data-demo>Renvoyer l&#x27;invitation</a><a class="btn btn-outline-secondary" href="#"
                    data-demo>Transférer ses recherches</a><a class="btn btn-outline-danger"
                    href="/admin/utilisateurs"
                    data-confirm="Voulez-vous vraiment effectuer cette action ?">Désactiver le compte</a></div>
        </form>
    
  </main>
</template>
