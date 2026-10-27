# Ce qu'il faut pour une bonne application

Checklist pour la plateforme de gestion des données de recherche en biodiversité (thème **Forêt**, Bootstrap).
Les points marqués **(clé)** comptent particulièrement pour ce projet.

---

## 1. Identité visuelle

- [ ] **Nom du produit** et slogan court
- [ ] **Logo** en deux versions : complète (menu large) et icône seule (menu replié, favicon), en SVG
- [ ] **Favicon** et icône d'onglet
- [ ] **Palette** : thème Forêt, avec les couleurs de statut (succès, alerte, erreur, information)
- [ ] **Typographie** : une police lisible pour l'interface, à chiffres réguliers pour les tableaux
- [ ] **Icônes** : un seul jeu cohérent (Bootstrap Icons suffit)
- [ ] **Page de connexion** soignée : logo, nom de The Peregrine Fund, éventuellement une image de terrain

## 2. Structure et navigation

- [ ] **Barre latérale (sidebar)** à gauche, repliable (recommandée plutôt que le menu horizontal, vu le nombre de rubriques)
- [ ] **Barre du haut** : recherche globale, indicateur hors ligne, notifications, menu du profil
- [ ] **Fil d'Ariane** (Mes recherches › Sujet › Dataset) sur les pages profondes
- [ ] **Pied de page** : version, contact, mentions légales
- [ ] **Page d'accueil** après connexion : résumé personnel, saisies en cours, actions rapides

Menu de la sidebar proposé :

| Entrée | Contenu | Visible par |
|---|---|---|
| Accueil | Résumé personnel | Tous |
| Mes recherches | Liste et détail des recherches | Tous |
| Saisie | Brouillons hors ligne, import | Chercheur, saisie |
| À valider | Saisies des agents | Chercheur |
| Espace partagé | Explorer, Taxons, Jeux de données | Tous |
| Administration | Utilisateurs, référentiels, agents, journal | Admin |

## 3. Composants d'interface

- [ ] Boutons (principal, secondaire, danger), champs, listes déroulantes, cases, interrupteurs, sélecteurs de date
- [ ] **Tableaux** triables et filtrables, avec pagination **(clé : beaucoup de données)**
- [ ] Cartes, onglets, étapes (stepper) pour la saisie en 4 étapes
- [ ] Fenêtres de confirmation (suppression, rejet) et menus déroulants
- [ ] Badges de statut : Brouillon, Publiée, En attente, Validée, Rejetée
- [ ] **Carte géographique** pour les lieux et les observations **(clé)**
- [ ] Graphiques du dashboard
- [ ] Zone de dépôt de fichiers (photos, vidéos, Excel/CSV) avec aperçu

## 4. États et retours à l'utilisateur

- [ ] **Indicateur hors ligne / en ligne** visible partout, avec le nombre de saisies en attente **(clé)**
- [ ] Messages de succès et d'erreur (alertes temporaires)
- [ ] **Validation des champs** en direct, avec un message clair sous le champ
- [ ] **États vides** (« Aucune recherche pour l'instant » + bouton pour en créer une)
- [ ] **Chargement** : indicateurs et squelettes de tableaux, surtout pour les imports longs
- [ ] **Progression de l'import** et rapport d'erreurs lisible **(clé)**
- [ ] Confirmation avant toute suppression ou changement de statut
- [ ] Sauvegarde automatique des brouillons de saisie

## 5. Comptes et sécurité

- [ ] Connexion, activation par invitation, mot de passe oublié
- [ ] Déconnexion automatique après inactivité
- [ ] Menu du profil (profil, brouillons, déconnexion)
- [ ] Pages d'erreur : 403, 404, erreur serveur, maintenance
- [ ] Affichage clair de la visibilité (privée ou partagée) sur chaque recherche **(clé)**

## 6. Qualité d'usage

- [ ] **Adaptation aux tailles d'écran** : portable, tablette sur le terrain
- [ ] **Accessibilité** : contrastes suffisants, navigation au clavier, libellés sur tous les champs
- [ ] **Langue** : français d'abord, structure prête pour ajouter l'anglais (partage avec d'autres chercheurs) et d'autres langues
- [ ] **Mode sombre** en option
- [ ] **Impression et export** (PDF, CSV, Excel) des fiches et résultats
- [ ] **Aide** : info-bulles sur les termes scientifiques (basisOfRecord, effort d'échantillonnage…), page d'aide ou guide court

## 7. Contenu et conformité

- [ ] **Licences des données** affichées et choisies à la création d'un dataset
- [ ] **Conditions d'utilisation** et politique de protection des données (à valider avec The Peregrine Fund)
- [ ] **Citation recommandée** sur les jeux de données partagés
- [ ] Page « À propos » : mission, contact, équipe

## 8. Côté technique (hors design)

- [ ] Documentation de l'API (Swagger, déjà prévu)
- [ ] Sauvegardes de la base et des fichiers MinIO
- [ ] Journal des événements
- [ ] Tests, déploiement Docker, suivi des erreurs

---

## Pour la première version

Au minimum :

1. Logo et identité (thème Forêt)
2. Sidebar + barre du haut
3. Indicateur hors ligne
4. États vides et chargement
5. Validation des formulaires
6. Pages d'erreur
7. Page de connexion soignée

Le reste peut venir par étapes.

## Questions à trancher

- Avez-vous déjà un **nom de produit** et un **logo** ? Sinon, des emplacements `[Logo]` seront prévus.
- Menu **sidebar** (recommandé) ou menu horizontal ?
- Langues : français seul au départ, ou français + anglais ?
- Mode sombre : utile pour le terrain ?