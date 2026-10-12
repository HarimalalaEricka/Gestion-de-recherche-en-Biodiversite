# Pages à créer : système de gestion des données de recherche en biodiversité

Basé sur le cahier des charges v1.0 (07/10/2026) et le script SQL PostgreSQL. Application **web uniquement** (pas de mobile), avec saisie hors ligne.

**Légende**
- **Accès** : `Public` = sans connexion · `Tous` = tout utilisateur connecté · `Chercheur` = ADMIN ou RESEARCHER · `Saisie` = DATA_ENTRY (sur les recherches où il est invité) · `Admin` = ADMIN
- **Prio** : priorité MoSCoW du cahier des charges (M = Must, S = Should, C = Could)
- Les tables citées sont celles du script SQL. Un `⚠` renvoie à la section « Manques dans le script SQL » en bas.

**Total : 43 pages**, réparties en 7 groupes.

---

## A. Authentification et profil (5 pages)

| # | Page (route) | Accès | Ce qu'elle affiche / permet | Tables | Prio |
|---|---|---|---|---|---|
| 1 | Connexion `/connexion` | Public | Champs e-mail et mot de passe, lien « Mot de passe oublié » | Utilisateur | M |
| 2 | Activation du compte `/activation/:token` | Public | Nom et e-mail en lecture seule ; champs mot de passe (obligatoire), prénom, téléphone ; message si lien expiré ⚠ | Utilisateur, invitation ⚠ | M |
| 3 | Mot de passe oublié `/mot-de-passe-oublie` | Public | Champ e-mail, envoi d'un lien à usage unique | Utilisateur | S |
| 4 | Réinitialisation `/reinitialisation/:token` | Public | Nouveau mot de passe et confirmation | Utilisateur | S |
| 5 | Mon profil `/profil` | Tous | Nom, prénom, téléphone, e-mail, rôle (lecture) ; changement de mot de passe | Utilisateur | M |

## B. Recherches : espace personnel (8 pages)

| # | Page (route) | Accès | Ce qu'elle affiche / permet | Tables | Prio |
|---|---|---|---|---|---|
| 6 | Mes recherches `/recherches` | Tous | **Liste à gauche** (sujet, statut, date de modification ; filtre Tout / Publiées / Brouillons ; bouton « + Nouvelle recherche »). **Détail à droite** : sujet, description, dates, compteurs (datasets, observations, à valider, collaborateurs), liste des datasets avec bouton « Saisir », actions Ouvrir, Exporter, Médias, Thèse | recherche, Dataset, Agent_Recherche | M |
| 7 | Nouvelle recherche `/recherches/nouvelle` | Chercheur | Sujet (obligatoire), description, date de début (obligatoire), date de fin, visibilité (privée par défaut / publiée), ajout de collaborateurs ; boutons « Enregistrer comme brouillon » et « Créer la recherche » | recherche, Agent_Recherche | M |
| 8 | Modifier une recherche `/recherches/:id/modifier` | Chercheur | Même formulaire que la création, plus suppression et dépublication | recherche | M |
| 9 | Médias de la recherche `/recherches/:id/medias` | Chercheur, Saisie | Liste des fichiers (type IMAGE / VIDEO / AUDIO / DOCUMENT, légende, commentaire, auteur) ; dépôt de fichiers ; téléchargement et suppression | Media_recherche, Utilisateur | S |
| 10 | Collaborateurs `/recherches/:id/collaborateurs` | Chercheur | Liste des personnes de la recherche avec leur rôle ; invitation d'un agent de saisie ; retrait d'un collaborateur | Agent_Recherche, Agent, Agent_User, Utilisateur | S |
| 11 | Métadonnées de la recherche `/recherches/:id/metadonnees` | Chercheur | Description standardisée : méthodes, effort d'échantillonnage, auteurs, licence et détenteur des droits de chaque dataset, protocoles utilisés, références | recherche, Dataset, Protocol, bibliographic_resource | M |
| 12 | Thèse `/recherches/:id/these` | Chercheur | Dépôt du fichier de thèse quand la recherche est terminée ; état (en cours / déposée) ; téléchargement ⚠ | Media_recherche, bibliographic_resource | C |
| 13 | Notes personnelles `/recherches/:id/notes` | Chercheur | Zone de texte privée, enregistrée automatiquement | recherche (Note_perso) | C |

## C. Datasets et saisie des données (11 pages)

| # | Page (route) | Accès | Ce qu'elle affiche / permet | Tables | Prio |
|---|---|---|---|---|---|
| 14 | Nouveau dataset `/recherches/:id/datasets/nouveau` | Chercheur | Nom (obligatoire), licence, détenteur des droits | Dataset | M |
| 15 | Fiche d'un dataset `/datasets/:id` | Tous (lecture si partagé) | Métadonnées (recherche, auteurs, licence, détenteur des droits, période, protocoles, références) ; compteurs (événements, observations, taxons) ; liste des événements (date, lieu, protocole, nombre d'observations) ; boutons Saisir, Importer, Exporter CSV / Excel | Dataset, Event, Occurrence, Location, Protocol | M |
| 16 | Import Excel / CSV `/datasets/:id/importer` | Chercheur, Saisie | Dépôt du fichier ; correspondance colonnes du fichier ↔ champs ; aperçu ; rapport ETL (erreurs, doublons détectés, lignes rejetées) ; résultat de l'import | Event, Occurrence, Taxon, Location… | M |
| 17 | Saisie, étape 1 : contexte `/datasets/:id/saisie/contexte` | Chercheur, Saisie | Dataset, date, protocole, méthode d'échantillonnage ; lieu existant ou nouveau (pays → province → commune, localité, latitude, longitude, bouton « Ma position ») | Event, Location, Pays, Province, Protocol | M |
| 18 | Saisie, étape 2 : observations `/datasets/:id/saisie/observations` | Chercheur, Saisie | Grille « une ligne = un organisme » : taxon, statut (détecté / non détecté), type de donnée, nombre, portée (individu, groupe, colonie, population), boutons Mesure, Photo, Copier ; options avancées repliées | Organism, Occurrence | M |
| 19 | Saisie, étape 3 : identification `/datasets/:id/saisie/identification` | Chercheur, Saisie | Par ligne : taxon (autocomplétion), date, identifié par, qualificatif, identification acceptée, références ; mini-formulaire « Nouveau taxon » (nom, rang, règne → épithète) | identification, Taxon, taxonRank, Agent | M |
| 20 | Saisie, étape 4 : compléments `/datasets/:id/saisie/complements` | Chercheur, Saisie | Mesures (type, valeur, unité, méthode, précision, date) ; médias (dépôt, légende, appareil, commentaire) | measurement_or_fact, Media | S |
| 21 | Saisie, modules avancés `/datasets/:id/saisie/avance` | Chercheur, Saisie | Contexte géologique (si fossile), matériel et collection, analyse ADN (protocole, séquence, lectures), interactions entre organismes ; affichés selon le dataset | geological_context, MaterialEntity, NucleotideAnalysis, NucleotideSequence, MolecularProtocol, organism_interaction | C |
| 22 | Récapitulatif avant envoi `/datasets/:id/saisie/recapitulatif` | Chercheur, Saisie | Résumé de l'événement et de toutes les observations saisies ; erreurs de validation à corriger ; bouton « Soumettre » (ou « Enregistrer » pour le chercheur) | Toutes les tables de saisie | M |
| 23 | Brouillons hors ligne `/brouillons` | Chercheur, Saisie | Saisies enregistrées dans le navigateur (date, dataset, nombre de lignes) ; état de synchronisation ; bouton « Synchroniser » ; alerte si un autre navigateur est utilisé ; conflits éventuels | IndexedDB (local) | M |
| 24 | Détail d'un événement `/evenements/:id` | Tous (modif. : Chercheur) | Date, lieu, protocole, dataset ; liste des observations de l'événement ; modification, suppression, références | Event, Occurrence, Location, event_reference | M |

## D. Validation et suivi (4 pages)

| # | Page (route) | Accès | Ce qu'elle affiche / permet | Tables | Prio |
|---|---|---|---|---|---|
| 25 | Saisies à valider `/validation` | Chercheur | Onglets En attente / Validées / Rejetées ; tableau (agent, date de saisie, dataset, taxon, lieu, statut) ; validation ou rejet en lot ⚠ | Event, Occurrence, Agent | M |
| 26 | Détail d'une saisie à valider `/validation/:id` | Chercheur | Contenu de la saisie en lecture ; champ « Motif du rejet » ; boutons Valider, Rejeter, Modifier ⚠ | Event, Occurrence, identification | M |
| 27 | Notifications `/notifications` | Tous | Liste des événements (saisie validée ou rejetée, invitation à une recherche, partage) ; marquage lu / non lu ⚠ | notification ⚠ | S |
| 28 | Dashboard personnel `/tableau-de-bord` | Tous | Compteurs (recherches, datasets, observations, taxons) ; graphiques par famille, par année, par province ; limité à mes recherches | recherche, Occurrence, Taxon, Location | C |

## E. Espace partagé (7 pages)

| # | Page (route) | Accès | Ce qu'elle affiche / permet | Tables | Prio |
|---|---|---|---|---|---|
| 29 | Explorer `/partage/explorer` | Tous | Barre de recherche ; filtres (taxon, pays, province, période, dataset, type de donnée, statut, avec médias, avec ADN) ; carte ; tableau des résultats (taxon, date, lieu, dataset, nombre, type, médias) ; export des résultats | Occurrence, Event, Taxon, Location, Dataset | M |
| 30 | Liste des taxons `/partage/taxons` | Tous | Recherche par nom ; tableau (nom scientifique, rang, famille, genre, nombre d'observations) | Taxon, taxonRank | M |
| 31 | Fiche taxon `/taxons/:id` | Tous (modif. : chercheur principal) | Classification (règne → épithète) ; compteurs (observations, datasets, lieux, période) ; carte ; liste des observations ; médias ; mesures (min, max, unité) | Taxon, Occurrence, Media, measurement_or_fact | M |
| 32 | Détail d'une observation `/observations/:id` | Tous | Événement, lieu, organisme et observation, identifications, mesures, médias, matériel et ADN, références, interactions ; bouton Exporter | Occurrence et tables liées | M |
| 33 | Jeux de données partagés `/partage/datasets` | Tous | Liste des datasets partagés (nom, recherche, auteurs, licence, nombre d'observations) ; filtres ; accès à la fiche (#15) | Dataset, recherche | M |
| 34 | Recherche publiée `/partage/recherches/:id` | Tous | Version en lecture seule d'une recherche partagée : description, auteurs, datasets, métadonnées | recherche, Dataset, Agent_Recherche | M |
| 35 | Dashboard partagé `/partage/tableau-de-bord` | Tous | Compteurs globaux et graphiques (par famille, par année, par province) sur les données partagées | Occurrence, Taxon, Location | C |

## F. Administration (6 pages)

| # | Page (route) | Accès | Ce qu'elle affiche / permet | Tables | Prio |
|---|---|---|---|---|---|
| 36 | Utilisateurs `/admin/utilisateurs` | Admin | Tableau (nom, e-mail, rôle, statut : invité / actif / désactivé, dernière connexion) ; filtres ; bouton « Inviter » | Utilisateur ⚠ | M |
| 37 | Inviter un utilisateur `/admin/utilisateurs/inviter` | Admin | Nom, e-mail, rôle ; envoi du lien d'activation ; possibilité de copier le lien ⚠ | Utilisateur, invitation ⚠ | M |
| 38 | Fiche d'un utilisateur `/admin/utilisateurs/:id` | Admin | Modification du nom et du rôle ; désactivation (départ ou fin de contrat) ; renvoi de l'invitation ; recherches de la personne et transfert à un autre chercheur | Utilisateur, recherche, Agent_User | M |
| 39 | Référentiels `/admin/referentiels` | Admin | Onglets pour gérer : pays, provinces, rangs taxonomiques, types de mesure, unités, types de protocole, types et catégories de matériel, types d'interaction ; ajout, modification, suppression | Pays, Province, taxonRank, measurementType, measurementUnit, protocolType, materialEntityType, materialEntityCategory, organismInteractionType | S |
| 40 | Agents `/admin/agents` | Admin | Liste des agents (personne, groupe, institution) ; fusion de doublons ; lien avec un utilisateur ⚠ | Agent, Agent_User | S |
| 41 | Journal des événements `/admin/journal` | Admin | Historique des actions (qui, quoi, quand : création, modification, validation, partage, suppression) ; filtres par utilisateur, recherche, date ⚠ | journal ⚠ | S |

## G. Pages techniques (2 pages)

| # | Page (route) | Accès | Ce qu'elle affiche | Prio |
|---|---|---|---|---|
| 42 | Accès refusé `/403` | Tous | Message : droits insuffisants, retour à l'accueil | M |
| 43 | Page introuvable `/404` | Tous | Message : page inexistante, retour à l'accueil | M |

---

## Navigation principale

- **Barre du haut :** logo · Espace partagé · Mes recherches · Collaborateurs · Notifications · avatar (Mon profil, Brouillons hors ligne, Déconnexion)
- **Menu Administration** (visible par l'admin seulement) : Utilisateurs · Référentiels · Agents · Journal
- **Indicateur hors ligne** visible sur toutes les pages de saisie : nombre de saisies en attente de synchronisation

---

## Manques dans le script SQL pour que ces pages fonctionnent (⚠)

1. **`recherche`** : ajouter `statut` (BROUILLON / PUBLIEE) en plus de `Confidentialite`. Éventuellement `couvertureURI`.
2. **`Utilisateur`** : ajouter `statut` (INVITE / ACTIF / DESACTIVE), `date_creation`, `cree_par` ; rendre `mot_de_passe` facultatif jusqu'à l'activation.
3. **Table `invitation`** : token (haché), e-mail, rôle, recherche concernée (facultatif), date d'expiration, date d'utilisation, invité par.
4. **Validation des saisies** : un statut (DRAFT / PENDING / VALIDATED / REJECTED), l'auteur de la saisie et le motif de rejet sur `Event` ou `Occurrence`.
5. **`Agent`** : ajouter `agentName` (aujourd'hui, une institution ou une personne externe n'a pas de nom).
6. **Table `notification`** : destinataire, type, contenu, lu / non lu, date.
7. **Table `journal`** (historique des événements) : utilisateur, action, objet concerné, date.
8. **Table `form_template`** : configuration JSON des champs obligatoires ou facultatifs par dataset.
9. **Thèse** : pas de champ dédié. Soit utiliser `Media_recherche` (type DOCUMENT), soit ajouter un statut de thèse sur `recherche`.
10. **Rôle par recherche** : `role` est global sur `Utilisateur` ; pour un rôle différent selon la recherche, ajouter un rôle dans `Agent_Recherche`.
11. **Erreurs de syntaxe à corriger :** virgule en trop dans `Media_recherche` et `bibliographic_resource` ; `recherche` doit être créée avant `Dataset` ; supprimer l'index `idx_recherche_dataset` (colonne inexistante).