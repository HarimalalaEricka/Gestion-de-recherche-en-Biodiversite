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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/saisie/detail-d-un-evenement">D&#195;&#169;tail d&#39;un &#195;&#169;v&#195;&#169;nement</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Détail de l&#x27;événement</h1>
        <div class="card">
            <div class="card-header fw-semibold">Détails</div>
            <div class="card-body d-grid gap-3">
                <ul class="list-unstyled mb-0 d-grid gap-1">
                    <li><span class="text-body-secondary">Lieu :</span> Réserve de Berenty</li>
                    <li><span class="text-body-secondary">Protocole :</span> Piège photographique</li>
                    <li><span class="text-body-secondary">Méthode :</span> Description d&#x27;exemple</li>
                    <li><span class="text-body-secondary">Références :</span> Transect linéaire, Point d&#x27;écoute
                    </li>
                </ul>
                <div class="map-ph">
                    <div class="text-center"><i class="bi bi-geo-alt fs-2"></i>
                        <div>Carte du lieu</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-header fw-semibold">Observations</div>
            <div class="card-body d-grid gap-3">
                <div class="card">
                    <div class="card-header"><input type="search" form="nf"
                            class="form-control form-control-sm table-filter" placeholder="Filtrer..."
                            aria-label="Filtrer"></div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0" data-table>
                            <thead>
                                <tr>
                                    <th scope="col">Taxon</th>
                                    <th scope="col">Statut</th>
                                    <th scope="col">Nombre</th>
                                    <th scope="col">Identifié par</th>
                                    <th scope="col"></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Lemur catta</td>
                                    <td><span class="badge text-bg-success">Publiée</span></td>
                                    <td>Rakoto Andry</td>
                                    <td>Rakoto Andry</td>
                                    <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                            to="/partage/detail-d-une-observation">Ouvrir</router-link></td>
                                </tr>
                                <tr>
                                    <td>Propithecus verreauxi</td>
                                    <td><span class="badge text-bg-secondary">Privée</span></td>
                                    <td>Rasoa Voahangy</td>
                                    <td>Rasoa Voahangy</td>
                                    <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                            to="/partage/detail-d-une-observation">Ouvrir</router-link></td>
                                </tr>
                                <tr>
                                    <td>Furcifer pardalis</td>
                                    <td><span class="badge text-bg-secondary">Brouillon</span></td>
                                    <td>Randria Tojo</td>
                                    <td>Randria Tojo</td>
                                    <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                            to="/partage/detail-d-une-observation">Ouvrir</router-link></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-danger"
                href="/saisie/fiche-d-un-dataset"
                data-confirm="Voulez-vous vraiment effectuer cette action ?">Supprimer</a><router-link class="btn btn-primary"
                to="/recherches/modifier-une-recherche">Modifier</router-link></div>
    
  </main>
</template>
