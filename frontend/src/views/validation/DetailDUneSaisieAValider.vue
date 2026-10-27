<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/validation/saisies-a-valider">Validation</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/validation/detail-d-une-saisie-a-valider">D&#195;&#169;tail d&#39;une saisie &#195;&#160; valider</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Saisie à valider</h1>
            <div class="card">
                <div class="card-header fw-semibold">Contenu</div>
                <div class="card-body d-grid gap-3">
                    <ul class="list-unstyled mb-0 d-grid gap-1">
                        <li><span class="text-body-secondary">Dataset :</span> Reptiles</li>
                        <li><span class="text-body-secondary">Lieu :</span> Forêt d&#x27;Ankarafantsika</li>
                        <li><span class="text-body-secondary">Date :</span> 15/08/2026</li>
                    </ul>
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
                                        <th scope="col">Mesures</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Lemur catta</td>
                                        <td><span class="badge text-bg-success">Publiée</span></td>
                                        <td>Rakoto Andry</td>
                                        <td>12</td>
                                    </tr>
                                    <tr>
                                        <td>Propithecus verreauxi</td>
                                        <td><span class="badge text-bg-secondary">Privée</span></td>
                                        <td>Rasoa Voahangy</td>
                                        <td>48</td>
                                    </tr>
                                    <tr>
                                        <td>Furcifer pardalis</td>
                                        <td><span class="badge text-bg-secondary">Brouillon</span></td>
                                        <td>Randria Tojo</td>
                                        <td>236</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Motif du rejet</label><textarea
                        class="form-control" id="f1" name="motif-du-rejet" rows="4"></textarea></div>
            </div>
            <div class="d-flex flex-wrap gap-2"><a class="btn btn-outline-danger"
                    href="/validation/saisies-a-valider"
                    data-confirm="Voulez-vous vraiment effectuer cette action ?">Rejeter</a><router-link
                    class="btn btn-outline-secondary" to="/saisie/saisie-contexte">Modifier</router-link><button
                    type="submit" class="btn btn-primary"
                    data-next="/validation/saisies-a-valider">Valider</button></div>
        </form>
    
  </main>
</template>
