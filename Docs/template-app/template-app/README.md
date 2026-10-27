# Nexora : template d'application (Bootstrap + JavaScript)

Maquette interactive de la plateforme de gestion des donnees de recherche en biodiversite, sans serveur ni base de donnees.
Ouvrez `index.html` dans un navigateur, puis cliquez sur « Demarrer : Connexion » (tout mot de passe fonctionne).

## Structure

```
index.html               sommaire des pages
assets/
  bootstrap/             Bootstrap 5.3.3 et Bootstrap Icons 1.11.3, en local (marche hors ligne)
  css/custom.css         theme Foret (couleurs, sidebar, mode sombre)
  js/config.js           nom, logo, menu, utilisateur et notifications de test
  js/i18n.js             traductions francais -> anglais
  js/app.js              layout (sidebar, barre du haut, pied de page), langue, theme, tableaux, formulaires
pages/
  accueil/ auth/ recherches/ saisie/ validation/ partage/ admin/ erreurs/ info/
  _modele.html           page vide a copier
```

## Ce que le template contient
- Sidebar repliable (menu mobile), barre du haut avec fil d'Ariane, recherche globale, indicateur en ligne / hors ligne, notifications, menu du profil, pied de page.
- Langues francais et anglais (bouton FR / EN), mode sombre / clair (menu du profil), theme Foret.
- 46 pages avec donnees de test : comptes, recherches, saisie en 4 etapes, import, validation, espace partage, administration, aide, mentions legales, pages d'erreur.
- Tableaux triables et filtrables, formulaires avec validation Bootstrap et sauvegarde automatique du brouillon (localStorage), fenetres de confirmation, messages (toasts), zones de depot de fichiers, etats vides.
- Simulation du hors ligne : menu du profil > « Simuler hors ligne ».

## Reutiliser le template
1. Nom, logo, menu, utilisateur : `assets/js/config.js`.
2. Couleurs : variables en haut de `assets/css/custom.css`.
3. Nouvelle page : copier `pages/_modele.html`, puis l'ajouter dans `config.js`.
4. Nouvelle langue : ajouter `window.I18N.xx = {...}` dans `i18n.js` et un bouton dans `app.js`.
5. Donnees de test : elles sont ecrites dans les pages HTML ; a remplacer par l'API (Spring Boot) plus tard.

## Limites
- Les actions qui demandent un serveur (export, copie, ajout de ligne, ma position, etc.) affichent « Action de demonstration ».
- Les roles ne sont pas simules : toutes les pages sont accessibles.
- Les brouillons sont gardes dans localStorage ; l'application reelle utilisera IndexedDB (Dexie.js) et un Service Worker pour le vrai mode hors ligne.
- Les cartes sont des emplacements (a brancher sur Leaflet ou OpenLayers).
