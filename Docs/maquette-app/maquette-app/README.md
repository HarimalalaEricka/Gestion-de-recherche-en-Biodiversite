# Maquette cliquable de l'application

Prototype HTML/CSS **sans JavaScript et sans données** : ouvrez `index.html`, puis cliquez sur « Démarrer : Connexion ».

- `index.html` : sommaire des 43 pages (numérotées comme dans `pages-plateforme.md`).
- `css/style.css` : styles communs.
- `pages/` : une page HTML par écran.

Navigation
- Le menu du haut (Espace partagé, Mes recherches, Collaborateurs, Notifications, Brouillons hors ligne, Mon profil, Administration, Déconnexion) est présent sur toutes les pages connectées ; l'administration a son propre menu.
- Les boutons et onglets mènent à la page logique suivante (ex. : Connexion → Mes recherches ; Saisie étape 1 → 2 → 3 → 4 → Récapitulatif).
- Les boutons qui ont besoin de données ou de code (exporter, copier, ajouter une ligne, ma position...) pointent vers `#` et ne font rien.
- Les rôles ne sont pas simulés : toutes les pages sont accessibles, y compris l'administration.
- Les pages 28 (dashboard personnel) et 35 (dashboard partagé) ont le même gabarit.
