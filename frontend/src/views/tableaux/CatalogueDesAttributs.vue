<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/tableaux/catalogue-des-attributs">Catalogue des attributs</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Catalogue des attributs</h1>
            <p class="text-body-secondary mb-0">Les attributs sont communs à tous les chercheurs. Réutilisez-les pour
                éviter les doublons.</p>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Rechercher un attribut</label><input
                        class="form-control" type="text" id="f1" name="rechercher-un-attribut" value="Nom"></div>
            </div>
            <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary">+ Nouvel
                    attribut</button><a class="btn btn-outline-secondary" href="#" data-demo>Fusionner des doublons</a>
            </div>
            <div class="card">
                <div class="card-header"><input type="search" form="nf"
                        class="form-control form-control-sm table-filter" placeholder="Filtrer..." aria-label="Filtrer">
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0" data-table>
                        <thead>
                            <tr>
                                <th scope="col">Attribut</th>
                                <th scope="col">Type d&#x27;attribut</th>
                                <th scope="col">Utilisé dans</th>
                                <th scope="col">Créé par</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Nom</td>
                                <td>Texte</td>
                                <td>3</td>
                                <td>Rakoto Andry</td>
                            </tr>
                            <tr>
                                <td>Hauteur</td>
                                <td>Décimal</td>
                                <td>2</td>
                                <td>Rasoa Voahangy</td>
                            </tr>
                            <tr>
                                <td>Nombre</td>
                                <td>Entier</td>
                                <td>5</td>
                                <td>Randria Tojo</td>
                            </tr>
                            <tr>
                                <td>Actif</td>
                                <td>Oui/non</td>
                                <td>1</td>
                                <td>Razafy Mialy</td>
                            </tr>
                            <tr>
                                <td>Date</td>
                                <td>Date</td>
                                <td>4</td>
                                <td>Rakoto Andry</td>
                            </tr>
                            <tr>
                                <td>État</td>
                                <td>Liste</td>
                                <td>2</td>
                                <td>Rasoa Voahangy</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    
  </main>
</template>
