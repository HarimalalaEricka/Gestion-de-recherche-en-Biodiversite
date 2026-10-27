<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/tableaux/tableaux-personnalises">Tableaux personnalis&#195;&#169;s</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Tableaux personnalisés</h1>
        <p class="text-body-secondary mb-0">Tableaux de données créés par les chercheurs pour des informations hors des
            tables standards.</p>
        <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-primary" to="/tableaux/creer-un-tableau">+
                Nouveau tableau</router-link></div>
        <div class="card">
            <div class="card-header"><input type="search" form="nf" class="form-control form-control-sm table-filter"
                    placeholder="Filtrer..." aria-label="Filtrer"></div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" data-table>
                    <thead>
                        <tr>
                            <th scope="col">Tableau</th>
                            <th scope="col">Dataset</th>
                            <th scope="col">Colonnes</th>
                            <th scope="col">Lignes</th>
                            <th scope="col">Propriétaire</th>
                            <th scope="col">Dernière modification</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Suivi des nids</td>
                            <td>Mammifères</td>
                            <td>6</td>
                            <td>12</td>
                            <td>Rakoto Andry</td>
                            <td>12/09/2026</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/tableaux/lignes-du-tableau">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Inventaire de la végétation</td>
                            <td>Reptiles</td>
                            <td>2</td>
                            <td>48</td>
                            <td>Rasoa Voahangy</td>
                            <td>03/09/2026</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/tableaux/lignes-du-tableau">Ouvrir</router-link></td>
                        </tr>
                        <tr>
                            <td>Mesures des œufs</td>
                            <td>Ovipares</td>
                            <td>4</td>
                            <td>236</td>
                            <td>Randria Tojo</td>
                            <td>28/08/2026</td>
                            <td class="text-end"><router-link class="btn btn-sm btn-outline-primary"
                                    to="/tableaux/lignes-du-tableau">Ouvrir</router-link></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    
  </main>
</template>
