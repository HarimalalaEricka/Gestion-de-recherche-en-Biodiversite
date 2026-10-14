# Carnet de bord – Projet Nexora

> Mode d'emploi : chaque soir, copie le « modèle du jour » plus bas, colle-le **au-dessus des anciennes entrées** (la plus récente en haut) et remplis-le en 5 minutes. Chaque vendredi, remplis aussi le « bilan de la semaine ». Ce carnet sert à écrire les parties 2.2 (modules) et 2.4 (problèmes et solutions) du mémoire.
>
> Emplacement conseillé : `docs/carnet-de-bord.md` dans ton dépôt GitHub (il est sauvegardé avec ton code).

---

## Modèle du jour (à copier)

```markdown
## [Jour] [JJ/MM] – Semaine [n]

- **Prévu aujourd'hui :** (la tâche du planning)
- **Fait :** (ce qui marche vraiment, pas ce qui est commencé)
- **Problème rencontré :** (le message d'erreur, ou ce qui bloquait)
- **Solution :** (ce qui a fonctionné, et pourquoi)
- **Temps passé :** (par exemple 6 h, dont 1 h sur le problème)
- **Capture ou diagramme à garder :** (nom du fichier, ou « aucun »)
- **Demain :** (la première chose à faire)
```

Règles :
- **Court :** 3 à 7 lignes suffisent. Si tu n'as rien rencontré comme problème, écris « aucun ».
- **Précis :** note le vrai message d'erreur et la vraie solution, c'est ce qui sera utile dans quelques semaines.
- **Honnête sur le temps :** il te sert à voir si le planning tient.
- **Jamais de mots de passe ni de clés** dans ce fichier : il est sur GitHub.

---

## Modèle du bilan de la semaine (à copier le vendredi)

```markdown
## Bilan de la semaine [n] ([date début] → [date fin])

- **Objectif de la semaine :**
- **Atteint :** oui / en partie / non
- **Terminé :** (liste)
- **Pas terminé, reporté à :** (liste et nouvelle date)
- **Problèmes les plus importants :** (1 à 3, ceux à raconter dans le mémoire)
- **Décisions prises :** (choix techniques et pourquoi)
- **Retours de l'encadrant :**
- **Module à rédiger pour le mémoire :** (nom du module de la semaine, avec les captures à utiliser)
```

---

## Exemple rempli (pour t'inspirer, à supprimer)

```markdown
## Lundi 19/10 – Semaine 3

- **Prévu aujourd'hui :** dépôt GitHub, docker-compose avec PostgreSQL et MinIO, chargement du script SQL.
- **Fait :** dépôt créé avec backend/ et frontend/, docker-compose qui démarre PostgreSQL et MinIO.
- **Problème rencontré :** le script SQL échoue sur la table Agent_User : « relation "agent" does not exist ».
- **Solution :** le script créait Agent_User avant Agent. Je l'ai déplacé après les deux tables qu'il référence.
- **Temps passé :** 5 h 30, dont 45 min sur ce problème.
- **Capture ou diagramme à garder :** aucun.
- **Demain :** initialiser Spring Boot et connecter la base.
```

Et le bilan du vendredi, vu à travers le mémoire (partie 2.4) donne par exemple :

> « Lors du chargement du schéma, une erreur de dépendance entre tables est apparue : une table d'association était créée avant les tables qu'elle référence. L'ordre de création a été corrigé de façon à respecter les clés étrangères. »

Remarque : cet exemple est inventé, pour montrer le format. Ne le recopie pas dans ton carnet.

---

# Entrées (la plus récente en haut)

<!-- Colle ici tes entrées du jour et tes bilans de semaine -->