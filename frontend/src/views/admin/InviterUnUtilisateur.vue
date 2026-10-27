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
            <li class="breadcrumb-item active" aria-current="page"><router-link to="/admin/inviter-un-utilisateur">Inviter un utilisateur</router-link></li>
        </ol>
        </nav>
        <form class="needs-validation d-grid gap-4" novalidate><small class="text-body-secondary text-end"
                data-autosave></small>
            <h1 class="h3 mb-0">Inviter un utilisateur</h1>
            <div class="row g-3">
                <div class="col-md"><label class="form-label" for="f1">Nom<span class="text-danger">
                            *</span></label><input class="form-control" type="text" id="f1" name="nom"
                        value="Rakoto Andry" required></div>
                <div class="col-md"><label class="form-label" for="f2">E-mail<span class="text-danger">
                            *</span></label><input class="form-control" type="email" id="f2" name="e-mail"
                        value="andry.rakoto@peregrine.example" required></div>
                <div class="col-md"><label class="form-label" for="f3">Rôle<span class="text-danger">
                            *</span></label><select class="form-select" id="f3" name="role" required>
                        <option>Administrateur</option>
                        <option>Chercheur</option>
                        <option>Agent de saisie</option>
                        <option>Lecteur</option>
                    </select></div>
            </div>
            <p class="text-body-secondary mb-0">Un lien d&#x27;activation valable 7 jours est envoyé par e-mail.</p>
            <div class="d-flex flex-wrap gap-2"><router-link class="btn btn-outline-secondary"
                    to="/admin/utilisateurs">Annuler</router-link><button type="submit" class="btn btn-primary"
                    data-next="/admin/utilisateurs">Envoyer l&#x27;invitation</button><a
                    class="btn btn-outline-secondary" href="#" data-demo>Copier le lien</a></div>
        </form>
    
  </main>
</template>
