/* Configuration unique : nom, logo, menu, utilisateur et notifications de test.
   Les liens sont relatifs à la racine du dossier. Pour changer le menu, modifiez `menu`. */
window.APP={
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
 ],
 "pages": {
  "1": {
   "href": "pages/auth/01-connexion.html"
  },
  "2": {
   "href": "pages/auth/02-activation-du-compte.html"
  },
  "3": {
   "href": "pages/auth/03-mot-de-passe-oublie.html"
  },
  "4": {
   "href": "pages/auth/04-reinitialisation.html"
  },
  "5": {
   "href": "pages/auth/05-mon-profil.html"
  },
  "8": {
   "href": "pages/recherches/08-modifier-une-recherche.html"
  },
  "9": {
   "href": "pages/recherches/09-medias-de-la-recherche.html"
  },
  "10": {
   "href": "pages/recherches/10-collaborateurs.html"
  },
  "11": {
   "href": "pages/recherches/11-metadonnees-de-la-recherche.html"
  },
  "12": {
   "href": "pages/recherches/12-these.html"
  },
  "13": {
   "href": "pages/recherches/13-notes-personnelles.html"
  },
  "14": {
   "href": "pages/saisie/14-nouveau-dataset.html"
  },
  "16": {
   "href": "pages/saisie/16-import-excel-csv.html"
  },
  "21": {
   "href": "pages/saisie/21-saisie-modules-avances.html"
  },
  "22": {
   "href": "pages/saisie/22-saisie-recapitulatif.html"
  },
  "23": {
   "href": "pages/saisie/23-brouillons-hors-ligne.html"
  },
  "24": {
   "href": "pages/saisie/24-detail-d-un-evenement.html"
  },
  "26": {
   "href": "pages/validation/26-detail-d-une-saisie-a-valider.html"
  },
  "27": {
   "href": "pages/validation/27-notifications.html"
  },
  "30": {
   "href": "pages/partage/30-liste-des-taxons.html"
  },
  "33": {
   "href": "pages/partage/33-jeux-de-donnees-partages.html"
  },
  "34": {
   "href": "pages/partage/34-recherche-publiee.html"
  },
  "36": {
   "href": "pages/admin/36-utilisateurs.html"
  },
  "37": {
   "href": "pages/admin/37-inviter-un-utilisateur.html"
  },
  "38": {
   "href": "pages/admin/38-fiche-d-un-utilisateur.html"
  },
  "39": {
   "href": "pages/admin/39-referentiels.html"
  },
  "40": {
   "href": "pages/admin/40-agents.html"
  },
  "41": {
   "href": "pages/admin/41-journal-des-evenements.html"
  },
  "42": {
   "href": "pages/erreurs/42-acces-refuse.html"
  },
  "43": {
   "href": "pages/erreurs/43-page-introuvable.html"
  },
  "0": {
   "href": "pages/accueil/00-accueil.html"
  },
  "6": {
   "href": "pages/recherches/06-mes-recherches.html"
  },
  "7": {
   "href": "pages/recherches/07-nouvelle-recherche.html"
  },
  "15": {
   "href": "pages/saisie/15-fiche-d-un-dataset.html"
  },
  "17": {
   "href": "pages/saisie/17-saisie-contexte.html"
  },
  "18": {
   "href": "pages/saisie/18-saisie-observations.html"
  },
  "19": {
   "href": "pages/saisie/19-saisie-identification.html"
  },
  "20": {
   "href": "pages/saisie/20-saisie-complements.html"
  },
  "25": {
   "href": "pages/validation/25-saisies-a-valider.html"
  },
  "28": {
   "href": "pages/validation/28-tableau-de-bord.html"
  },
  "29": {
   "href": "pages/partage/29-explorer.html"
  },
  "31": {
   "href": "pages/partage/31-fiche-taxon.html"
  },
  "32": {
   "href": "pages/partage/32-detail-d-une-observation.html"
  },
  "35": {
   "href": "pages/partage/35-tableau-de-bord-partage.html"
  },
  "44": {
   "href": "pages/info/44-aide.html"
  },
  "45": {
   "href": "pages/info/45-mentions-legales-et-donnees.html"
  },
  "46": {
   "href": "pages/tableaux/46-tableaux-personnalises.html"
  },
  "51": {
   "href": "pages/tableaux/51-catalogue-des-attributs.html"
  }
 }
};
