PARTIE 1
Dataset
Agent
Event
Location
Occurrence
Taxon
Identification

        +

PARTIE 2
Organism
Media
MaterialSample
MeasurementOrFact
GeologicalContext
OrganismInteraction
ResourceRelationship

        +

PARTIE 3
MaterialEntity
MaterialCitation
Protocol
Provenance
MolecularProtocol
NucleotideAnalysis
NucleotideSequence
BibliographicResource
Assertion

maquette-app/
├── index.html                  Sommaire des 43 pages + bouton « Démarrer »
├── README.md                   Mode d'emploi
│
├── assets/
│   ├── bootstrap/
│   │   ├── css/bootstrap.min.css      Bootstrap en local (marche sans connexion)
│   │   └── js/bootstrap.bundle.min.js Menus et onglets de Bootstrap
│   ├── css/
│   │   └── custom.css                 Vos couleurs et ajustements
│   └── img/
│       └── logo.svg                   Logo à remplacer
│
└── pages/
    ├── auth/                   A. Authentification (5 pages)
    │   ├── 01-connexion.html
    │   ├── 02-activation-du-compte.html
    │   ├── 03-mot-de-passe-oublie.html
    │   ├── 04-reinitialisation-du-mot-de-passe.html
    │   └── 05-mon-profil.html
    │
    ├── recherches/             B. Recherches (8 pages)
    │   ├── 06-mes-recherches.html
    │   ├── 07-nouvelle-recherche.html
    │   ├── 08-modifier-une-recherche.html
    │   ├── 09-medias.html
    │   ├── 10-collaborateurs.html
    │   ├── 11-metadonnees.html
    │   ├── 12-these.html
    │   └── 13-notes.html
    │
    ├── saisie/                 C. Datasets et saisie (11 pages)
    │   ├── 14-nouveau-dataset.html
    │   ├── 15-fiche-dataset.html
    │   ├── 16-import-excel-csv.html
    │   ├── 17-etape-1-contexte.html
    │   ├── 18-etape-2-observations.html
    │   ├── 19-etape-3-identification.html
    │   ├── 20-etape-4-complements.html
    │   ├── 21-modules-avances.html
    │   ├── 22-recapitulatif.html
    │   ├── 23-brouillons-hors-ligne.html
    │   └── 24-detail-evenement.html
    │
    ├── validation/             D. Validation et suivi (4 pages)
    │   ├── 25-saisies-a-valider.html
    │   ├── 26-detail-saisie.html
    │   ├── 27-notifications.html
    │   └── 28-dashboard-personnel.html
    │
    ├── partage/                E. Espace partagé (7 pages)
    │   ├── 29-explorer.html
    │   ├── 30-liste-taxons.html
    │   ├── 31-fiche-taxon.html
    │   ├── 32-detail-observation.html
    │   ├── 33-jeux-de-donnees.html
    │   ├── 34-recherche-publiee.html
    │   └── 35-dashboard-partage.html
    │
    ├── admin/                  F. Administration (6 pages)
    │   ├── 36-utilisateurs.html
    │   ├── 37-inviter-utilisateur.html
    │   ├── 38-fiche-utilisateur.html
    │   ├── 39-referentiels.html
    │   ├── 40-agents.html
    │   └── 41-journal.html
    │
    └── erreurs/                G. Pages techniques (2 pages)
        ├── 42-acces-refuse.html
        └── 43-page-introuvable.html