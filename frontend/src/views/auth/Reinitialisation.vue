<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="auth-page">
        <div class="auth-card auth-simple-card">
            <div class="auth-simple-brand"><img src="../../assets/img/logo.svg" alt="Logo Nexora"><span>Nexora</span></div>
            <div class="auth-tech-strip"><span><i class="bi bi-shield-check"></i> Protection des données</span><span>CHIFFREMENT ACTIF</span></div>
            <form class="needs-validation auth-simple-form d-grid gap-4" novalidate><small class="text-body-secondary text-end" data-autosave></small>
                    <div class="auth-form-heading"><span class="auth-icon"><i class="bi bi-shield-lock"></i></span><span class="auth-kicker">Sécurité du compte</span></div>
                    <div><h1>Nouveau mot de passe</h1><p>Choisissez un mot de passe solide pour protéger votre compte.</p></div>
                    <div class="row g-3">
                        <div><label class="form-label" for="f1">Nouveau mot de passe<span
                                    class="text-danger"> *</span></label><input class="form-control" type="password"
                                id="f1" name="nouveau-mot-de-passe" value="motdepasse" required></div>
                        <div><label class="form-label" for="f2">Confirmation<span class="text-danger">
                                    *</span></label><input class="form-control" type="text" id="f2" name="confirmation"
                                value="" required></div>
                    </div>
                    <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary"
                            data-next="/auth/connexion">Enregistrer</button></div>
                </form>
        </div>
    
  </main>
</template>
