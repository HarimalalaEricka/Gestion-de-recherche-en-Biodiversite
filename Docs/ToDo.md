# To-do du projet Nexora – du début à la fin, jour par jour

> Hypothèses : projet de 13 semaines à partir du lundi 05/10/2026 (S1 = semaine du 05/10), travail du lundi au vendredi. Jours fériés pris en compte : **vendredi 25/12** (Noël) et **vendredi 01/01**. Vérifie les autres jours fériés locaux.
>
> Chaque semaine contient un tableau « jour par jour » : la **tâche du jour** et le **résultat attendu le soir**. Si le résultat n'est pas atteint le soir, la tâche déborde sur le lendemain : décale alors la suite et note-le dans ton carnet de bord.

---

## 1. Vue d'ensemble

| Semaines | Dates | Phase | Objectif principal |
|---|---|---|---|
| S1 à S2 | 05/10 → 16/10 | Analyse et conception | Savoir exactement quoi construire |
| S3 à S5 | 19/10 → 06/11 | Développement, itération 1 | Les bases : comptes, recherches, saisie |
| S6 à S7 | 09/11 → 20/11 | Développement, itération 2 | Import, export, tableaux personnalisés, taxon, partage |
| S8 à S10 | 23/11 → 11/12 | Développement, itération 3 | Hors ligne, validation, médias, notifications |
| S11 à S12 | 14/12 → 24/12 | Tests et recette | Vérifier, corriger, faire valider |
| S13 | 28/12 → 05/01 | Mise en production | Déployer, documenter, préparer la soutenance |

Deux tâches avancent **en parallèle pendant tout le projet** : la rédaction du mémoire (section 8) et le guide d'utilisation (section 9).

### Rythme fixe de chaque semaine

- **Lundi matin (15 minutes) :** relire le tableau de la semaine et fixer l'objectif.
- **Vendredi après-midi :** point avec ton encadrant (tu montres ce qui marche), puis mémoire et carnet de bord. Pense aussi à faire un commit GitHub propre.
- **Chaque soir :** deux lignes dans le carnet de bord (ce que j'ai fait, le problème rencontré). Cela te donnera la matière de la partie « Réalisation » du mémoire.

### Règles pour tenir le planning

1. **Les « Must » d'abord.** Dans le cahier des charges, chaque besoin a une priorité (Must, Should, Could). Tu ne passes aux « Should » que quand les « Must » fonctionnent. Les « Could » sont des bonus.
2. **Une version simple avant une version complète.** Surtout pour le mode hors ligne et l'ETL : une première version qui marche vaut mieux qu'une version parfaite qui n'est pas finie.
3. **Une fonctionnalité est « terminée » quand** elle marche de bout en bout (écran, API, base de données), qu'elle a été testée avec des données réalistes et qu'elle est enregistrée sur GitHub.
4. **Un point par semaine avec ton encadrant.** Montre ce qui marche, pas ce qui est prévu. Cela évite les mauvaises surprises en fin de projet.
5. **Commit sur GitHub tous les jours.** Ton code est sauvegardé et tu gardes un historique à montrer dans le mémoire.

---

## 2. Phase 0 – Analyse et conception (S1 à S2, 05/10 → 16/10)

**But :** avoir tout ce qu'il faut sur papier avant d'écrire du code. Plus cette phase est solide, moins tu perdras de temps ensuite.

### Déjà fait

- [x] **Cahier des charges** rédigé (versions 0.5 du 07/10, 0.6 du 12/10 et 0.9 condensée du 14/10). Il décrit les besoins (ce que le système doit faire), pas la façon de le construire.
- [x] **Base de données conçue :** schéma de 41 tables, inspiré de Darwin Core, réalisé avec Looping.
- [x] **Maquettes** faites avec le template Bootstrap (menu latéral, thème « Forêt », français et anglais, mode sombre).

### Reste à faire, jour par jour

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Mer 14/10** | Envoyer le cahier des charges à ton encadrant pour validation. Générer le script SQL final et le tester dans PostgreSQL : exécute-le sur une base vide, corrige les erreurs, ajoute quelques lignes de test. | Cahier des charges envoyé. Script SQL qui s'exécute sans erreur. |
| **Jeu 15/10** | Rédiger le dossier de conception : schéma de la base de données découpé en modules lisibles et maquettes (ils ne vont pas dans le cahier des charges). Préparer 2 ou 3 fichiers Excel/CSV de test, réalistes et un peu « sales ». | Dossier de conception complet. Fichiers de test prêts. |
| **Ven 16/10** | Lister les API à créer (une ligne par endpoint : méthode, URL, rôle autorisé). Faire valider les maquettes avec ton encadrant, et si possible avec un chercheur. Point hebdomadaire. | Liste des API. Maquettes validées, ou liste des retours à intégrer. |

**Pourquoi tout cela avant de coder :** un défaut dans la base ou dans un écran se corrige en quelques minutes maintenant, et en plusieurs jours une fois développé. La validation écrite du cahier des charges (même un e-mail) te protège si le périmètre change.

> Si la validation de l'encadrant tarde, ne bloque pas le début du développement lundi : démarre avec la mise en place technique, qui ne dépend pas de la validation.

---

## 3. Phase 1 – Développement, itération 1 (S3 à S5, 19/10 → 06/11)

**But :** un utilisateur peut se connecter, créer une recherche et saisir des données. C'est le socle sur lequel tout le reste s'appuie.

### Semaine 3 (19/10 → 23/10) : mise en place et authentification

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 19/10** | Créer le dépôt GitHub (dossiers backend et frontend, README). Créer le `docker-compose` avec PostgreSQL et MinIO. Charger le script SQL dans la base Docker. | Un seul `docker compose up` démarre la base et MinIO, avec les 41 tables. |
| **Mar 20/10** | Initialiser le projet Spring Boot (Web, Security, Data JPA, PostgreSQL, Swagger). Connecter la base. Créer les entités de base (Utilisateur, Agent, recherche, Dataset). | Swagger s'affiche et une route de test répond, connectée à PostgreSQL. |
| **Mer 21/10** | Initialiser Vue.js et intégrer le template Bootstrap (layout, menu latéral, routage). Mettre en place le français et l'anglais et le mode sombre dès maintenant. | Squelette d'interface avec menu, changement de langue et de thème qui fonctionnent. |
| **Jeu 22/10** | Authentification côté backend (BF-01, Must) : connexion avec JWT, mots de passe hachés, rôles (chercheur principal, étudiant, agent de saisie, administrateur), routes protégées. | Un appel sans jeton est refusé, un appel avec jeton passe, testé dans Swagger. |
| **Ven 23/10** | Authentification côté frontend : page de connexion, stockage du jeton, protection des pages selon le rôle, déconnexion. Point hebdomadaire. | On se connecte depuis l'interface et on arrive sur le tableau de bord vide. |

### Semaine 4 (26/10 → 30/10) : invitation, profil, recherches

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 26/10** | Envoi d'e-mails (service SMTP, avec un outil de test d'e-mails en local). Invitation : l'administrateur saisit nom, e-mail et rôle, un lien avec jeton est envoyé. | Un e-mail d'invitation est reçu avec un lien valable. |
| **Mar 27/10** | Page « choisir mon mot de passe » via le lien d'invitation. Réinitialisation du mot de passe (même mécanisme). | Un utilisateur invité active son compte et se connecte. |
| **Mer 28/10** | Écran d'administration des utilisateurs : liste, invitation, rôle, statut. | L'administrateur gère les comptes depuis l'interface. |
| **Jeu 29/10** | Profil utilisateur (informations et changement de mot de passe). Backend des recherches (BF-02, Must) : créer, modifier, supprimer, confidentialité « privée » par défaut (RG-01). | Profil modifiable. API des recherches testée dans Swagger. |
| **Ven 30/10** | Frontend des recherches : liste à gauche et détail à droite, création, modification, suppression. Point hebdomadaire. | Une recherche se crée et s'affiche depuis l'interface. |

### Semaine 5 (02/11 → 06/11) : datasets et formulaire de saisie

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 02/11** | Datasets d'une recherche (backend et frontend) : une recherche peut en avoir plusieurs. | Un dataset s'ajoute dans le détail d'une recherche. |
| **Mar 03/11** | Backend de la saisie, version 1 (BF-04, Must) : une occurrence avec lieu, date et taxon. API des listes de référence (pays, provinces, rangs taxonomiques). | L'API enregistre une occurrence complète (Event, Location, Occurrence, Taxon). |
| **Mer 04/11** | Frontend de la saisie, version 1 : formulaire avec champs obligatoires et facultatifs, validation dès l'entrée. | On saisit une occurrence depuis l'interface, avec des messages d'erreur clairs. |
| **Jeu 05/11** | Saisie, version 2 : mesures (type, unité, valeur) et identification. | Une occurrence avec mesures et identification est enregistrée. |
| **Ven 06/11** | Consultation et modification des données saisies. Tests de l'itération et corrections. **Démonstration à l'encadrant.** | Tu montres : invitation, connexion, création d'une recherche, saisie d'une donnée. |

---

## 4. Phase 2 – Développement, itération 2 (S6 à S7, 09/11 → 20/11)

**But :** faire entrer et sortir les données, et gérer les cas hors standards. **C'est la période la plus chargée du planning : il n'y a aucune marge.** Si tu prends du retard, déplace le partage (vendredi 20/11) à la semaine suivante.

### Semaine 6 (09/11 → 13/11) : import et export

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 09/11** | ETL (BF-06, Must), partie 1 : module Python avec pandas, lecture d'un **seul format de fichier** (le plus courant), correspondance colonnes → tables, nettoyage de base. | Le module lit un fichier de test et produit des données propres. |
| **Mar 10/11** | ETL, partie 2 : validation (champs obligatoires, types, dates) et rapport d'erreurs lisible (ligne, colonne, problème). | Un fichier avec erreurs produit un rapport clair. |
| **Mer 11/11** | ETL, partie 3 : dédoublonnage (RG-05) et chargement transactionnel dans PostgreSQL. | Importer deux fois le même fichier ne crée pas de doublons. |
| **Jeu 12/11** | Intégration : envoi du fichier depuis le backend, appel de l'ETL, stockage du fichier source dans MinIO. Page d'import dans l'interface (envoi, suivi, rapport d'erreurs). | Un import complet marche depuis l'interface. |
| **Ven 13/11** | Tests de l'import avec tes fichiers « sales ». Export CSV (BF-08, Must), backend et bouton d'export. Point hebdomadaire. | Import testé, export CSV téléchargeable. |

### Semaine 7 (16/11 → 20/11) : tableaux personnalisés, taxon, partage

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 16/11** | Tableaux personnalisés (BF-05, Must), partie 1 : catalogue d'attributs global et réutilisable (nom, clé, type de donnée). | Un chercheur ajoute et réutilise des attributs. |
| **Mar 17/11** | Tableaux personnalisés, partie 2 : création d'un tableau (nom, attributs choisis dans le catalogue, rattaché à un dataset). | Un tableau personnalisé se crée depuis l'interface. |
| **Mer 18/11** | Tableaux personnalisés, partie 3 : saisie des lignes avec un formulaire qui s'adapte au type de chaque attribut (texte, nombre, booléen, date). | Des lignes se saisissent et s'affichent. L'import dans un tableau personnalisé n'est fait que s'il reste du temps. |
| **Jeu 19/11** | Fiche taxon (BF-07, Must) : consultation pour tous, modification réservée au chercheur principal (RG-04). | La fiche s'affiche. Un étudiant ne peut pas la modifier. |
| **Ven 20/11** | Confidentialité et partage (BF-09, Must) : privé par défaut, option de partage, consultation dans l'espace partagé (RG-02). **Démonstration à l'encadrant.** | Tu importes un vrai fichier, retrouves les données, les exportes et utilises un tableau personnalisé. |

---

## 5. Phase 3 – Développement, itération 3 (S8 à S10, 23/11 → 11/12)

**But :** les fonctionnalités les plus délicates et les « Should ». Ne les commence qu'une fois les « Must » des itérations 1 et 2 stables.

### Semaine 8 (23/11 → 27/11) : mode hors ligne

Le mode hors ligne (BF-14, Must) est la partie la plus risquée du projet : une semaine entière lui est réservée. **Version 1 :** saisie locale, puis envoi automatique au retour de la connexion, avec une file d'attente visible. Garde la gestion fine des conflits pour une version 2. Rappel de la contrainte : cela fonctionne dans le même navigateur que celui de la connexion initiale.

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 23/11** | Service Worker : mise en cache de l'application pour la charger sans réseau. | L'application s'ouvre avec le réseau coupé. |
| **Mar 24/11** | IndexedDB avec Dexie.js : schéma local et enregistrement du formulaire de saisie en local quand il n'y a pas de réseau. | Une saisie hors ligne est conservée dans le navigateur. |
| **Mer 25/11** | Détection de la connexion, indicateur « hors ligne » dans l'interface et file d'attente visible des saisies en attente. | L'utilisateur voit combien de saisies attendent d'être envoyées. |
| **Jeu 26/11** | Synchronisation au retour du réseau : envoi des saisies, gestion des erreurs, protection contre les doublons (identifiant local par saisie). | Les saisies hors ligne arrivent dans la base sans doublon. |
| **Ven 27/11** | Tests (coupure réseau, plusieurs saisies, rechargement de la page) et corrections. Point hebdomadaire. | Scénario complet réussi : saisie hors ligne, retour du réseau, données en base. |

### Semaine 9 (30/11 → 04/12) : validation et médias

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 30/11** | Validation (BF-03 et BF-17, Should), backend : statuts « en attente », « validé », « rejeté ». Les saisies des agents passent en attente (RG-03). | Une saisie d'agent est enregistrée avec le statut « en attente ». |
| **Mar 01/12** | Validation, frontend : écran du chercheur (liste, détail, valider ou rejeter) et historique des événements. | Le chercheur valide ou rejette une saisie, et l'historique le garde. |
| **Mer 02/12** | Médias (BF-10, Should), backend : envoi, téléchargement sécurisé et suppression avec MinIO. | Un fichier s'envoie et se récupère via l'API. |
| **Jeu 03/12** | Médias, frontend : liste, aperçu et envoi de photos et vidéos liées à une recherche ou à une occurrence. | Une photo s'ajoute et s'affiche depuis l'interface. |
| **Ven 04/12** | Corrections et revue de la semaine. Début des captures d'écran pour le guide. Point hebdomadaire. | Validation et médias stables. |

### Semaine 10 (07/12 → 11/12) : recherche, notifications, gel

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 07/12** | Recherche dans l'espace partagé (BF-15, Should) : par espèce, observation ou jeu de données. | Une recherche renvoie les bons résultats. |
| **Mar 08/12** | Notifications (BF-16, Could), backend : création lors des événements (invitation, demande de validation, validation ou rejet, partage reçu), avec la table `notification`. | Les notifications se créent aux bons moments. |
| **Mer 09/12** | Notifications, frontend : cloche avec compteur, liste et marquage « lu ». | La cloche affiche le nombre de notifications non lues. |
| **Jeu 10/12** | **Bonus seulement si tout le reste marche :** tableau de bord et notes personnelles (BF-13) ou espace thèse (BF-11). Sinon, stabilisation. | Une fonctionnalité bonus, ou une application plus stable. |
| **Ven 11/12** | **Gel des fonctionnalités.** Revue complète, corrections, étiquette Git de la version. Point hebdomadaire. | À partir de maintenant, tu n'ajoutes plus rien : tu corriges et tu stabilises. |

---

## 6. Phase 4 – Tests et recette (S11 à S12, 14/12 → 24/12)

**But :** prouver que le système fait ce que le cahier des charges promet.

### Semaine 11 (14/12 → 18/12)

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 14/12** | Écrire le plan de test : un tableau avec une ligne par besoin (BF-01 à BF-17) et le résultat attendu. Il pourra être réutilisé dans le mémoire. | Plan de test prêt. |
| **Mar 15/12** | Tester chaque fonctionnalité de bout en bout avec chaque rôle (chercheur, étudiant, agent, administrateur). Vérifier les droits : personne ne doit voir les données privées d'un autre. | Tableau des résultats rempli pour tous les rôles. |
| **Mer 16/12** | Tester le mode hors ligne (aucune perte, aucun doublon) et l'import avec des fichiers sales (colonnes manquantes, doublons, dates mal formatées, caractères spéciaux). | Liste des anomalies hors ligne et import. |
| **Jeu 17/12** | Quelques tests automatisés sur les parties critiques : authentification, droits, ETL (inutile de tout couvrir). | Tests automatiques qui passent. |
| **Ven 18/12** | Corriger les anomalies les plus graves (perte de données, faille d'accès). Point hebdomadaire. | Plus d'anomalie grave ouverte. |

### Semaine 12 (21/12 → 24/12, vendredi 25/12 férié)

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 21/12** | Corriger les anomalies restantes. Préparer la recette : jeu de données de démonstration et scénario. | Application prête pour la démonstration. |
| **Mar 22/12** | **Recette avec le commanditaire :** démonstration, retours des utilisateurs, liste des corrections. | Liste des corrections demandées. |
| **Mer 23/12** | Corriger les points issus de la recette. | Corrections faites. |
| **Jeu 24/12** | Refaire un test rapide et faire valider le procès-verbal de recette (la validation par e-mail suffit). | Procès-verbal de recette validé. |
| **Ven 25/12** | Férié. | — |

---

## 7. Phase 5 – Mise en production et livrables (S13, 28/12 → 05/01)

**But :** que d'autres personnes puissent installer et utiliser le système sans toi. Cette semaine compte **6 jours ouvrés** (vendredi 01/01 férié).

| Jour | Tâche | Résultat attendu le soir |
|---|---|---|
| **Lun 28/12** | Préparer le déploiement avec Docker : un `docker-compose` complet (frontend, backend, PostgreSQL, MinIO, ETL), variables d'environnement séparées du code, aucun mot de passe écrit en dur. | Tout démarre avec une seule commande sur une machine vierge. |
| **Mar 29/12** | Déployer sur le serveur ou l'environnement indiqué par l'organisation, puis refaire un test rapide en conditions réelles. | Application accessible en ligne et testée. |
| **Mer 30/12** | Rédiger la documentation technique : installation, architecture, variables de configuration, lancement, sauvegarde de la base. | Documentation technique terminée. |
| **Jeu 31/12** | Finaliser le guide d'utilisation (corrections d'après le testeur extérieur) et le livrer en PDF. Rassembler les livrables : application, code sur GitHub, script SQL, documentation technique, guide, procès-verbal de recette. | Tous les livrables sont prêts et remis. |
| **Ven 01/01** | Férié. | — |
| **Lun 04/01** | Préparer la soutenance : 10 à 15 diapositives et la démonstration. Préparer une **vidéo de secours** de la démonstration, au cas où le réseau ou le serveur tombe le jour J. | Diapositives et vidéo de secours prêtes. |
| **Mar 05/01** | Répéter la soutenance (avec chronomètre), dernière vérification des livrables. | Fin du projet. |

---

## 8. Rédaction du mémoire (tout au long du projet)
 
Le mémoire suit **le modèle de l'IT University** (le plan est imposé). Les numéros ci-dessous sont ceux du modèle. Principe : rédige chaque partie **juste après avoir fait le travail correspondant**, quand tout est encore frais et que tu as les captures sous la main.
 
### Temps prévu
 
- **Jusqu'à la semaine 10 :** environ 1 heure par jour pour le mémoire, plus le **vendredi après-midi (3 heures)**, qui sert à rédiger le module de la semaine.
- **À partir de la semaine 11 :** le développement diminue, tu peux consacrer **jusqu'à 2 heures par jour** au mémoire et au guide d'utilisation.
### Règles du modèle à respecter
 
- **Introduction : 1 page maximum. Conclusion : 1 page maximum.**
- **Institution d'accueil :** la présentation tient dans le reste de la page de l'avant-propos et au maximum une page de plus.
- **Technologies :** fais au moins une recherche documentaire sur les technologies alternatives, avec un tableau des avantages et inconvénients. **Pas de copier-coller** (web ou outil d'IA) : écris ce que tu as compris avec tes mots.
- **Problèmes rencontrés :** présente les problèmes les plus intéressants et leurs solutions, **pas tes lacunes personnelles**.
- **Chiffres :** les données chiffrées doivent être datées et récentes.
- **Bilan pour l'entreprise :** à faire avec le contrôle de ton encadreur.
- **Bibliographie :** respecte scrupuleusement les trois formats du modèle (livre, référence web avec « consultée le », thèse ou mémoire), et étoffe tes références pour montrer ta recherche documentaire.
- **Images :** de bonne qualité et libres de droit.
- **Relecture :** correcteur d'orthographe et de grammaire plusieurs fois, relecture par quelqu'un qui écrit mieux le français que toi, et l'IA ou Internet en dernière vérification seulement.
### Plan de rédaction, jour par jour
 
| Dates | Partie du mémoire | Quoi faire |
|---|---|---|
| Mer 14/10 → Ven 16/10 (S2) | Mise en place | Copier le modèle de l'école dans ton propre fichier. Choisir un titre provisoire. Commencer à noter les sources de ta bibliographie au bon format. |
| Mer 14/10 → Ven 16/10 (S2) | Avant-propos : institution d'accueil | Présenter The Peregrine Fund en une page et demie au maximum. Demande à ton encadrant un document ou un lien de référence. |
| Mer 14/10 → Ven 16/10 (S2) | 1.1 Objectifs du projet | Reprendre le cahier des charges (objectifs général et spécifiques) |
| Lun 19/10 → Mer 21/10 (S3) | 2.1.1 Analyse de l'existant | Reprendre la situation actuelle, ses limites et le benchmark (GBIF, Specify, KoboToolbox) du cahier des charges, en le reformulant |
| Jeu 22/10 → Ven 23/10 (S3) | 1.3 Technologies utilisées | Recherche documentaire sur les alternatives (par exemple autre framework frontend, autre backend), tableau des avantages et inconvénients, justification de tes choix |
| Lun 26/10 → Jeu 29/10 (S4) | 2.1.2 Conception de l'application | Pourquoi faire une conception avant de coder. Schéma de la base de données par modules (tes 4 figures), maquettes principales, architecture. Les chiffres (tables, classes, écrans) seront comptés plus tard. |
| Ven 30/10 (S4) | 2.2 Module « Gestion des utilisateurs » | Voir le modèle de module ci-dessous (comptes, invitation, rôles) |
| Ven 06/11 (S5) | 2.2 Module « Recherches, datasets et saisie » | Idem |
| Ven 13/11 (S6) | 2.2 Module « Import de données (ETL) et export » | Idem |
| Ven 20/11 (S7) | 2.2 Module « Tableaux personnalisés, fiche taxon et partage » | Idem |
| Ven 27/11 (S8) | 2.2 Module « Mode hors ligne » | Idem |
| Ven 04/12 (S9) | 2.2 Module « Validation et médias » | Idem |
| Ven 11/12 (S10) | 2.2 Module « Recherche et notifications » | Idem. Après le gel des fonctionnalités, **compter** : nombre de tables, de classes, d'écrans et d'API, pour la partie 2.1.2. |
| Lun 14/12 → Mer 16/12 (S11) | 2.3 État d'analyse et statistiques | Relever des chiffres **datés** : nombre d'enregistrements importés en test, temps d'import, nombre de recherches et de taxons, résultats du plan de test. Si le tableau de bord n'est pas fait, utilise les exports et ces chiffres. |
| Ven 18/12 (S11) | 2.4 Problèmes rencontrés et solutions | Choisir 3 à 5 problèmes intéressants dans ton carnet de bord (par exemple synchronisation hors ligne, doublons à l'import). Pour chacun : le problème, la solution, le résultat. Pas de lacunes personnelles. |
| Lun 21/12 (S12) | 1.2 Planning de réalisation | Diagramme de Gantt (planning prévu et réalisé) avec des commentaires, de préférence en page paysage |
| Mer 23/12 (S12) | 3.1 Bilan pour l'entreprise | Après la recette du mardi 22/12, demande à ton encadreur son retour sur l'atteinte des objectifs. Rédige, puis fais-lui relire. |
| Jeu 24/12 (S12) | 3.3 Extension et évolution | Perspectives : gestion avancée des conflits hors ligne, autres formats d'import, export Darwin Core complet, fonctionnalités « Could » non faites |
| Lun 28/12 (S13) | 3.2 Bilan personnel et Conclusion | Bilan personnel sans insister sur les lacunes. Conclusion en une page, avec 3 points : satisfaction de l'entreprise, impressions personnelles, perspectives. Insiste sur l'apport par rapport à l'existant. |
| Mar 29/12 (S13) | Introduction | En une page maximum : le thème, son intérêt, l'existant et ses inconvénients, les résultats essentiels (liste de réalisations), la structure du mémoire. Elle s'écrit en dernier, quand tu connais tes résultats. |
| Mer 30/12 (S13) | Glossaire, bibliographie, listes | Glossaire par ordre alphabétique (Darwin Core, ETL, JWT, IndexedDB...), bibliographie au bon format, liste des tableaux et liste des figures |
| Jeu 31/12 (S13) | Finitions et relecture | Remerciements, page de titre, jury, table des matières. Envoyer le mémoire complet à ton encadreur et à un relecteur qui écrit bien le français. |
| Lun 04/01 → Mar 05/01 (S13) | Corrections | Intégrer les retours de relecture, dernier passage au correcteur, vérification finale des images et de la mise en page |
 
**Pour la page de titre :** le modèle contient des noms de jury d'exemple (Dr Olivier Robinson, M. Tahina Razafinjoelina, M. Roger Dupont). Remplace-les par ceux de ton école. Ton encadreur professionnel est Jean Marius RAKOTONDRAMANGA.
 
### Modèle pour chaque module (partie 2.2)
 
Le modèle demande pour chaque module :
1. **Description des fonctionnalités** du module.
2. **Pourquoi** il a été développé pour l'entreprise.
3. **Un ou deux scénarios d'utilisation clés** : quel scénario, une capture d'écran avec son explication, l'importance, et comment cela fonctionne.
4. **Un diagramme de séquence.** Prévois 30 minutes pour le dessiner dès que le module marche, sans attendre le vendredi.
Pas besoin d'extrait de code source. Prends tes captures d'écran **pendant le développement du module**, avec des données de test propres.
 
### Habitudes utiles
 
- **Carnet de bord chaque soir :** il fournit la matière des parties 2.2 et 2.4.
- **Un fichier unique pour le mémoire,** sauvegardé sur GitHub ou dans un dossier synchronisé, avec une copie en PDF chaque vendredi.
- **Fais relire chaque module** par ton encadrant dès qu'il est écrit, plutôt qu'en bloc à la fin.
- **La semaine 13 est très chargée** (déploiement, documentation, soutenance, mémoire). La date de dépôt du mémoire n'apparaît pas dans ce planning : ajoute-la dès que tu la connais, et garde au moins 3 jours complets avant cette date pour la relecture et la mise en page. Si elle tombe avant le 05/01, il faudra avancer les parties 3.2, la conclusion et l'introduction dans la semaine 12.
---

## 9. Guide d'utilisation

**But :** permettre à un chercheur ou à un étudiant d'utiliser Nexora sans aide. Écris-le pour quelqu'un qui n'est pas informaticien.

| Dates | Tâche |
|---|---|
| Ven 04/12 → Jeu 10/12 | Prendre des captures d'écran des fonctionnalités stables (1 heure par jour, l'après-midi) |
| Ven 11/12 | Écrire le plan du guide (les 9 chapitres ci-dessous) |
| Lun 14/12 → Ven 18/12 | Rédiger les chapitres 1 à 5 (un chapitre par jour, environ 1 heure) |
| Lun 21/12 → Jeu 24/12 | Rédiger les chapitres 6 à 9 (un chapitre par jour). Jeudi 24/12 : donner le guide à une personne qui ne connaît pas le système |
| Lun 28/12 | Récupérer les remarques du testeur |
| Jeu 31/12 | Corriger et livrer le guide en PDF |

**Structure : un chapitre par tâche**, pas par écran :

1. Se connecter et créer son mot de passe après l'invitation
2. Créer une recherche et un dataset
3. Saisir une donnée (formulaire)
4. Importer un fichier Excel ou CSV et lire le rapport d'erreurs
5. Créer un tableau personnalisé
6. Utiliser le mode hors ligne sur le terrain (et rappeler la contrainte du même navigateur)
7. Valider les données d'un agent de saisie
8. Partager une recherche et exporter des données
9. Questions fréquentes et résolution des problèmes courants

Pour chaque chapitre : à quoi ça sert, les étapes numérotées avec une capture d'écran, ce qu'on doit voir à la fin, puis les erreurs fréquentes. Écris en français simple (avec une version anglaise si le temps le permet, puisque l'interface est bilingue). Si le testeur se bloque quelque part, c'est le guide (ou l'interface) qu'il faut corriger. Livre-le en PDF, et éventuellement en page d'aide dans l'application.

---

## 10. Si tu prends du retard

Dans cet ordre, du moins grave au plus grave :

1. Abandonner les « Could » : tableau de bord, espace thèse, notifications, notes personnelles (jeudi 10/12, et une partie de la semaine 10).
2. Déplacer le partage (vendredi 20/11) à la semaine 8, et simplifier le mode hors ligne (sans gestion avancée des conflits). Limiter l'ETL à un seul format de fichier.
3. Limiter l'export au CSV.
4. Réduire la recherche dans l'espace partagé à une recherche simple.

**À ne jamais sacrifier :** l'authentification et les droits, la saisie, l'import, les tests, le guide d'utilisation et le mémoire.