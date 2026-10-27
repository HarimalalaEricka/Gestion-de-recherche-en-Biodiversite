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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/admin/referentiels">R&#195;&#169;f&#195;&#169;rentiels</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Référentiels</h1>
        <ul class="nav nav-tabs">
            <li class="nav-item"><router-link class="nav-link active" to="/admin/referentiels">Pays</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/referentiels">Provinces</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/referentiels">Rangs</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/referentiels">Types de mesure</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/referentiels">Unités</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/referentiels">Protocoles</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/referentiels">Matériel</router-link></li>
            <li class="nav-item"><router-link class="nav-link" to="/admin/referentiels">Interactions</router-link></li>
        </ul>
        <div class="d-flex flex-wrap gap-2"><a class="btn btn-primary" href="#" data-demo>+ Ajouter</a></div>
        <div class="card">
            <div class="card-header"><input type="search" form="nf" class="form-control form-control-sm table-filter"
                    placeholder="Filtrer..." aria-label="Filtrer"></div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" data-table>
                    <thead>
                        <tr>
                            <th scope="col">Libellé</th>
                            <th scope="col">Utilisée par</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Madagascar</td>
                            <td>12</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Antananarivo</td>
                            <td>48</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Toamasina</td>
                            <td>236</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Mahajanga</td>
                            <td>5</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/recherches/mes-recherches">Ouvrir</router-link></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    
  </main>
</template>
