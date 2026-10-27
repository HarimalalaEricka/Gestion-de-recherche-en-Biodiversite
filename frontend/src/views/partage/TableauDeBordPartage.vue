<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 d-grid gap-4 align-content-start">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/partage/tableau-de-bord-partage">Tableau de bord partag&#195;&#169;</router-link></li>
        </ol>
        </nav>
        <h1 class="h3 mb-0">Tableau de bord</h1>
        <div class="row row-cols-2 row-cols-lg-4 g-3">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">48</div>
                        <div class="text-body-secondary">Recherches</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">236</div>
                        <div class="text-body-secondary">Datasets</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">5</div>
                        <div class="text-body-secondary">Observations</div>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="fs-3 fw-semibold">12</div>
                        <div class="text-body-secondary">Taxons</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row g-3">
            <div class="col-lg">
                <div class="card">
                    <div class="card-header fw-semibold">Observations par famille</div>
                    <div class="card-body d-grid gap-3">
                        <div class="d-grid gap-3">
                            <div>
                                <div class="d-flex justify-content-between small"><span>Lemuridae</span><span>90</span>
                                </div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:90%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small"><span>Indriidae</span><span>65</span>
                                </div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:65%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small">
                                    <span>Chamaeleonidae</span><span>40</span></div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:40%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small"><span>Gekkonidae</span><span>25</span>
                                </div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:25%"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg">
                <div class="card">
                    <div class="card-header fw-semibold">Observations par année</div>
                    <div class="card-body d-grid gap-3">
                        <div class="d-grid gap-3">
                            <div>
                                <div class="d-flex justify-content-between small"><span>2023</span><span>30</span></div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:30%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small"><span>2024</span><span>55</span></div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:55%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small"><span>2025</span><span>80</span></div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:80%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small"><span>2026</span><span>60</span></div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:60%"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg">
                <div class="card">
                    <div class="card-header fw-semibold">Observations par province</div>
                    <div class="card-body d-grid gap-3">
                        <div class="d-grid gap-3">
                            <div>
                                <div class="d-flex justify-content-between small">
                                    <span>Alaotra-Mangoro</span><span>85</span></div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:85%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small">
                                    <span>Atsimo-Andrefana</span><span>50</span></div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:50%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small"><span>Boeny</span><span>35</span>
                                </div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:35%"></div>
                                </div>
                            </div>
                            <div>
                                <div class="d-flex justify-content-between small">
                                    <span>Vakinankaratra</span><span>20</span></div>
                                <div class="progress" style="height:8px">
                                    <div class="progress-bar" style="width:20%"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    
  </main>
</template>
