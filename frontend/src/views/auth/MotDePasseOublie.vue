<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="auth-page">
        <div class="auth-card auth-simple-card">
            <div class="auth-simple-brand"><img src="../../assets/img/logo.svg" alt="Logo Nexora"><span>Nexora</span></div>
            <div class="auth-tech-strip"><span><i class="bi bi-broadcast-pin"></i> Vérification d’accès</span><span>CANAL SÉCURISÉ</span></div>
            <form class="needs-validation auth-simple-form d-grid gap-4" novalidate><small class="text-body-secondary text-end" data-autosave></small>
                    <div class="auth-form-heading"><span class="auth-icon"><i class="bi bi-envelope"></i></span><span class="auth-kicker">Récupération</span></div>
                    <div><h1>Mot de passe oublié</h1><p>Recevez un lien pour définir un nouveau mot de passe.</p></div>
                    <div class="row g-3">
                        <div class="col-md"><label class="form-label" for="f1">E-mail<span class="text-danger">
                                    *</span></label><input class="form-control" type="email" id="f1" name="e-mail"
                                value="andry.rakoto@peregrine.example" required></div>
                    </div>
                    <p class="text-body-secondary mb-0">Le lien envoyé est valable 7 jours.</p>
                    <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary"
                            data-next="/auth/reinitialisation">Envoyer le lien</button></div>
                </form>
        </div>
    
  </main>
</template>
