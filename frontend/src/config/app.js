// Configuration unique : nom, logo, menu (p = numéro de page, voir router/pages.js),
// utilisateur et notifications de test. À remplacer par les vraies données de l'API.
export default {
  "name": "Nexora",
  "logo": "N",
  "org": "The Peregrine Fund",
  "version": "0.1.0",
  "user": {
    "name": "Rasoa Voahangy",
    "ini": "RV",
    "role": "Chercheur principal"
  },
  "menu": [
    {
      "p": 0,
      "t": "Accueil",
      "i": "house"
    },
    {
      "p": 6,
      "t": "Mes recherches",
      "i": "folder2-open"
    },
    {
      "p": 17,
      "t": "Saisie",
      "i": "pencil-square"
    },
    {
      "l": "Tableaux personnalisés"
    },
    {
      "p": 46,
      "t": "Mes tableaux",
      "i": "table"
    },
    {
      "p": 51,
      "t": "Catalogue des attributs",
      "i": "collection"
    },
    {
      "p": 25,
      "t": "À valider",
      "i": "check2-circle",
      "b": 3
    },
    {
      "l": "Espace partagé"
    },
    {
      "p": 29,
      "t": "Explorer",
      "i": "globe2"
    },
    {
      "p": 30,
      "t": "Taxons",
      "i": "diagram-3"
    },
    {
      "p": 33,
      "t": "Jeux de données",
      "i": "database"
    },
    {
      "l": "Administration"
    },
    {
      "p": 36,
      "t": "Utilisateurs",
      "i": "people"
    },
    {
      "p": 39,
      "t": "Référentiels",
      "i": "list-ul"
    },
    {
      "p": 40,
      "t": "Agents",
      "i": "person-badge"
    },
    {
      "p": 41,
      "t": "Journal des événements",
      "i": "journal-text"
    },
    {
      "l": "Aide"
    },
    {
      "p": 44,
      "t": "Aide",
      "i": "question-circle"
    },
    {
      "p": 45,
      "t": "Mentions légales",
      "i": "file-text"
    }
  ],
  "notifs": [
    {
      "i": "check2-circle",
      "t": "Saisie validée",
      "d": "Reptiles · il y a 2 h"
    },
    {
      "i": "person-plus",
      "t": "Invitation à une recherche",
      "d": "Oiseaux d'Ankarafantsika · hier"
    },
    {
      "i": "share",
      "t": "Recherche partagée",
      "d": "Mammifères d'Andasibe · il y a 3 jours"
    }
  ]
}
