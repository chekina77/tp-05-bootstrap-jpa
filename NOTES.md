# TP 04 Mesurer le coût du mapping

## Q5  Prédiction du nombre de requêtes SQL

La méthode `chargerGrapheALaMain` utilise une seule requête SQL avec une jointure interne (`JOIN`) vers la table `fournisseur` 
et deux jointures externes (`LEFT JOIN`) vers les tables `compo` et `article`. Ces jointures permettent de récupérer les informations du bon, du fournisseur et des articles associés en une seule fois, sans exécuter de requête supplémentaire pour chaque article.

Je prévois donc **une seule requête SQL par appel** à `chargerGrapheALaMain(1)`.
## Q6  Mesures et SQL réellement exécuté

### Résultats des tests

J’ai testé la méthode `chargerGrapheALaMain` avec les bons de commande 1 et 7. Le bon 1 contient trois lignes d’articles, tandis que le bon 7 ne contient aucune ligne. Les résultats obtenus correspondent aux données attendues : le bon 7 est retourné avec une liste vide, sans ligne fantôme.

### Temps d’exécution

J’ai effectué trois essais de 100 chargements du bon 1 avec `app.MesureGraphe`.

- Essai 1 : 252 ms au total, soit 2,52 ms par chargement.
- Essai 2 : 369 ms au total, soit 3,69 ms par chargement.
- Essai 3 : 266 ms au total, soit 2,66 ms par chargement.

Le temps total moyen est de 295,67 ms pour 100 chargements, soit environ **2,96 ms par chargement**.

Calcul :

- Temps total moyen : (252 + 369 + 266) / 3 = 295,67 ms.
- Temps moyen par chargement : 295,67 / 100 = 2,9567 ms.

### SQL réellement exécuté

J’ai consulté le journal `mysql.general_log`. J’y ai observé 102 exécutions de type `Query` pendant 
l’exécution de `MesureGraphe` : deux chargements initiaux, un pour le bon 1 et un pour le bon 7, puis
100 chargements du bon 1 pour mesurer les performances.

Ce résultat est cohérent avec la prédiction d’une seule requête SQL par appel à `chargerGrapheALaMain`.

### Nombre de lignes de code

La méthode `chargerGrapheALaMain` s’étend des lignes 125 à 175, soit 51 lignes au total. 
Elle contient 3 lignes de commentaires. Le nombre exact de lignes de code, sans commentaires ni lignes vides, doit être 48.

## Q7  Ajouter la marque de l’article

Pour afficher la marque de l’article, il faut ajouter la colonne `a.marque` dans la requête SQL de 
`BonDaoJdbc`, puis récupérer sa valeur dans le `ResultSet`. Il faut également ajouter un champ `marque` 
au record `LigneBrute` et adapter sa construction ainsi que les endroits qui utilisent ce record. 
Si les deux records sont déclarés dans `BonDaoJdbc.java`, un seul fichier Java est nécessaire, 
mais plusieurs endroits de ce fichier devront être modifiés.

