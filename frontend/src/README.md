# Nexora : template intégré dans Vue 3

## Installation

```bash
npm install vue-router@4 bootstrap@5.3.3 bootstrap-icons@1.11.3 @popperjs/core
```

Remplacez le dossier `src/` de votre projet (Vue 3 + Vite) par celui-ci. `index.html` doit contenir `<div id="app"></div>`.
Aucun alias ni réglage Vite particulier n'est nécessaire.

Au lancement, `/` redirige vers la connexion (tout mot de passe fonctionne). `/sommaire` liste toutes les pages.

## Organisation

```
main.js, App.vue            démarrage ; App choisit le layout selon la route
router/routes.js            une route par page (meta.page, meta.active, meta.layout)
router/pages.js             numéro de page du template -> chemin
config/app.js               nom, logo, menu, utilisateur et notifications de test
layouts/                    AppLayout (sidebar + barre du haut + pied) et BlankLayout (connexion, erreurs)
components/layout/          Sidebar, Topbar, AppFooter
composables/                useTheme, useSidebar, useConnection, usePage
lib/                        i18n (FR/EN), ui (toasts, confirmation), pageBehaviors (ex-app.js)
i18n/en.json                dictionnaire français -> anglais
views/<groupe>/*.vue        les 52 pages (accueil, auth, recherches, saisie, validation, partage, admin, tableaux, info, erreurs)
assets/                     custom.css (thème Forêt) et logos
```

## Ajouter une page

1. Copier `views/_ModelePage.vue` vers `views/<groupe>/MaPage.vue` et corriger le chemin d'import de `usePage`.
2. Ajouter la route dans `router/routes.js` (et le numéro dans `router/pages.js`).
3. Si besoin, ajouter l'entrée dans `menu` de `config/app.js`.

## À savoir

- Chaque vue est le HTML du template : `<a href>` est devenu `<router-link>`, `data-next` pointe vers une route.
- Les comportements du template (tableaux triables, formulaires avec brouillon, concepteur de tableaux, toasts, confirmations)
  sont branchés par `usePage()` appelé dans chaque vue.
- Langue : le dictionnaire traduit le texte français affiché ; le bouton FR/EN recharge la page, comme avant.
- Les données de test sont encore écrites dans les vues : à remplacer par des appels à l'API (Spring Boot) vue par vue.
