<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="auth-page">
        <div class="auth-card auth-simple-card">
            <div class="auth-simple-brand"><img src="../../assets/img/logo.svg" alt="Logo Nexora"><span>Nexora</span></div>
            <div class="auth-tech-strip"><span><i class="bi bi-diagram-3"></i> Profil scientifique</span><span>ÉTAPE 01 / 03</span></div>
            <form class="needs-validation auth-simple-form d-grid gap-4" novalidate><small class="text-body-secondary text-end" data-autosave></small>
                    <div class="auth-form-heading"><span class="auth-icon"><i class="bi bi-person-check"></i></span><span class="auth-kicker">Première connexion</span></div>
                    <div><h1>Activer mon compte</h1><p>Complétez vos informations pour commencer à utiliser Nexora.</p></div>
                    <p class="text-body-secondary mb-0">voahangy.rasoa@peregrine.example</p>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f1">Mot de passe<span class="text-danger">
                                    *</span></label><input class="form-control" type="password" id="f1"
                                name="mot-de-passe" value="motdepasse" required></div>
                        <div class="col-md"><label class="form-label" for="f2">Confirmer le mot de passe<span
                                    class="text-danger"> *</span></label><input class="form-control" type="password"
                                id="f2" name="confirmer-le-mot-de-passe" value="motdepasse" required></div>
                        <div class="col-md"><label class="form-label" for="f3">Prénom</label><input class="form-control"
                                type="text" id="f3" name="prenom" value="Rakoto Andry"></div>
                        <div class="col-md"><label class="form-label" for="f4">Téléphone</label><input
                                class="form-control" type="text" id="f4" name="telephone" value=""></div>
                    </div>
                    <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary"
                            data-next="/accueil">Activer mon compte</button></div>
                </form>
        </div>
    
  </main>
</template>
