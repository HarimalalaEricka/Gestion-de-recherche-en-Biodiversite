<script setup>
import { usePage } from '../../composables/usePage'
usePage()
</script>

<template>
  <main id="content" class="p-4 profile-page">
        <nav class="page-breadcrumb breadcrumb" aria-label="Fil d'Ariane">
            <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><router-link to="/accueil">Accueil</router-link></li>
            <li class="breadcrumb-item"><router-link to="/auth/mon-profil">Compte</router-link></li>
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/auth/mon-profil">Mon profil</router-link></li>
        </ol>
        </nav>
        <div class="profile-heading">
            <div>
                <span class="profile-eyebrow"><i class="bi bi-person-badge"></i> Espace personnel</span>
                <h1>Mon profil</h1>
                <p>Gérez vos informations personnelles et vos accès à Nexora.</p>
            </div>
            <small class="text-body-secondary" data-autosave></small>
        </div>
        <form class="needs-validation profile-layout" novalidate>
            <aside class="profile-identity">
                <div class="profile-identity-cover"></div>
                <div class="profile-identity-body">
                    <div class="profile-page-avatar">RA<span></span></div>
                    <h2>Rakoto Andry</h2>
                    <p class="profile-role"><i class="bi bi-mortarboard"></i> Étudiant</p>
                    <p class="profile-status"><i class="bi bi-circle-fill"></i> Compte actif</p>
                    <hr>
                    <div class="profile-identity-detail">
                        <span><i class="bi bi-envelope"></i></span> <span>andry.rakoto@peregrine.example</span>
                    </div>
                    <div class="profile-identity-detail">
                        <span><i class="bi bi-telephone"></i></span> <span>+261 23 456 789</span>
                    </div>
                    <div class="profile-identity-detail">
                        <span><i class="bi bi-calendar3"></i></span> <span>Membre de Nexora</span>
                    </div>
                </div>
            </aside>
            <div class="profile-sections">
                <section class="profile-section">
                    <div class="profile-section-heading">
                        <span class="profile-section-icon"><i class="bi bi-person"></i></span> <span><strong>Informations personnelles</strong><small>Ces informations permettent de vous identifier.</small></span>
                    </div>
                    <div class="profile-fields">
                        <div><label class="form-label" for="f1">Nom<span class="text-danger"> *</span></label><input class="form-control" type="text" id="f1" name="nom" value="Rakoto Andry" required></div>
                        <div><label class="form-label" for="f2">Prénom</label><input class="form-control" type="text" id="f2" name="prenom" value="Rakoto Andry"></div>
                        <div><label class="form-label" for="f3">Téléphone</label><input class="form-control" type="text" id="f3" name="telephone" placeholder="+261 ..."></div>
                        <div><label class="form-label" for="f4">E-mail<span class="text-danger"> *</span></label><input class="form-control" type="email" id="f4" name="e-mail" value="andry.rakoto@peregrine.example" required></div>
                    </div>
                </section>
                <section class="profile-section">
                    <div class="profile-section-heading">
                        <span class="profile-section-icon"><i class="bi bi-shield-lock"></i></span> <span><strong>Sécurité du compte</strong><small>Modifiez votre mot de passe régulièrement.</small></span>
                    </div>
                    <div class="profile-fields profile-password-fields">
                        <div><label class="form-label" for="f5">Ancien mot de passe</label><input class="form-control" type="password" id="f5" name="ancien-mot-de-passe" value="motdepasse"></div>
                        <div><label class="form-label" for="f6">Nouveau mot de passe</label><input class="form-control" type="password" id="f6" name="nouveau-mot-de-passe" value="motdepasse"></div>
                        <div><label class="form-label" for="f7">Confirmation</label><input class="form-control" type="password" id="f7" name="confirmation"></div>
                    </div>
                </section>
                <div class="profile-actions">
                    <span><i class="bi bi-lock"></i> Vos données restent confidentielles.</span> <button type="submit" class="btn btn-primary" data-next="/accueil"><i class="bi bi-check2 me-1"></i>Enregistrer</button>
                </div>
            </div>
        </form>
    
  </main>
</template>
