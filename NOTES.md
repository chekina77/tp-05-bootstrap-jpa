# TP 04 Mesurer le coût du mapping

## Q5 Prédiction du nombre de requêtes SQL

La méthode `chargerGrapheALaMain` utilise une seule requête SQL avec des jointures vers les tables `fournisseur`, `compo` et `article`. Elle récupère les informations du bon et de ses articles en une seule fois.

Je pense donc qu’un seul appel à `chargerGrapheALaMain(1)` exécute une seule requête SQL.

## Q6 Mesures et SQL réellement exécuté

### Résultats des tests

J’ai testé la méthode avec les bons 1 et 7. Le bon 1 contient trois lignes d’articles et le bon 7 n’en contient aucune. Les résultats sont corrects : le bon 7 est retourné avec une liste vide.

### Temps d’exécution

J’ai lancé trois essais de 100 chargements du bon 1 avec `app.MesureGraphe`.

Essai 1 : 252 ms, soit 2,52 ms par chargement.

Essai 2 : 369 ms, soit 3,69 ms par chargement.

Essai 3 : 266 ms, soit 2,66 ms par chargement.

Le temps moyen est de 295,67 ms pour 100 chargements, donc environ 2,96 ms par chargement.

Calcul : (252 + 369 + 266) / 3 = 295,67 ms, puis 295,67 / 100 = 2,9567 ms.

### SQL exécuté

J’ai regardé le journal `mysql.general_log`. J’ai trouvé 102 requêtes : deux chargements au début, pour les bons 1 et 7, puis les 100 chargements utilisés pour mesurer le temps.

Cela correspond à ma prédiction d’une requête SQL par appel à `chargerGrapheALaMain`.

### Nombre de lignes de code

La méthode `chargerGrapheALaMain` va des lignes 125 à 175, soit 51 lignes. Il y a 3 lignes de commentaires. Sans les commentaires ni les lignes vides, il reste 48 lignes de code.

## Q7 Ajouter la marque de l’article

Pour afficher la marque, il faut ajouter `a.marque` dans la requête SQL de `BonDaoJdbc`, puis récupérer sa valeur dans le `ResultSet`. Il faut aussi ajouter le champ `marque` au record `LigneBrute` et adapter sa construction ainsi que les endroits où il est utilisé.

# TP 05 Bootstrap JPA et première entité

## 2 Dépendance JPA

`createEntityManagerFactory()` retourne un `jakarta.persistence.EntityManagerFactory`. L’API JPA vient de la dépendance `jakarta.persistence`, et Hibernate fournit l’implémentation utilisée dans le projet.

## 4 Première validation du schéma

Au premier lancement de `DemarrageJpa`, Hibernate a échoué parce qu’il cherchait une colonne `adresse_rue` qui n’existe pas dans la table `fournisseur`.

Erreur obtenue :

`Schema validation: missing column [adresse_rue] in table [fournisseur]`

## 5 Correction avec `@AttributeOverrides`

J’ai ajouté `@AttributeOverrides` sur l’attribut `adresse` de `Fournisseur` pour faire correspondre les champs du record `Adresse` aux colonnes `rue`, `code_postal` et `ville`.

Je les ai placés dans `Fournisseur`, car c’est cette entité qui indique comment l’adresse est enregistrée dans la table.

## 6 Résultat après correction

Après la correction, `DemarrageJpa` fonctionne avec `Action.VALIDATE`.

La factory affichée est `org.hibernate.internal.SessionFactoryImpl`.

Le fournisseur 4 affiché est :

`Fournisseur[id=4, nom=Quincaillerie du Maine, ville=Le Mans]`

`estComplete()` retourne `false`, car l’adresse n’a pas à la fois une rue et un code postal renseignés.

Le log montre une requête `SELECT` sur la table `fournisseur` pour charger le fournisseur 4. La validation vérifie le schéma sans le modifier.

## 7 Test avec ArticleCourt

Prédiction du 23/10/2026

Je pense qu’en ajoutant une entité `ArticleCourt` liée à la table `article`, Hibernate vérifiera si les colonnes déclarées dans le mapping existent dans la base.

Observation du 23/10/2026

Le lancement de `DemarrageJpa` réussit. Hibernate ne signale pas d’erreur, même avec `id` déclaré en `Integer` alors que la colonne SQL est de type `BIGINT`.

Après avoir remplacé `Integer` par `String`, le lancement réussit encore.

Ces tests montrent que `Action.VALIDATE` ne détecte pas forcément toutes les différences entre les types Java et SQL.

## 8 Vérification du nom de table

Erreur obtenue :

`Schema validation: missing table [fournisseurs]`

La table réelle s’appelle `fournisseur`, au singulier. Comme le mapping indique `fournisseurs`, Hibernate ne trouve pas la table et la validation échoue.

## 9 Test avec Action.UPDATE

`Action.UPDATE` demande à Hibernate d’adapter automatiquement le schéma de la base. Cela peut être risqué si le compte SQL a les droits nécessaires pour modifier la structure.

Pendant mon test, je n’ai vu aucune requête de modification ni erreur MariaDB dans les logs.

## 10 Deux chargements dans une transaction

Même instance : `true`

Nombre de requêtes SQL : 1

Dans une même transaction, Hibernate réutilise la même instance de `Fournisseur`. Il n’a donc besoin que d’une seule requête SQL.

## 11 Deux transactions successives

Même instance (`==`) : `false`

Égalité (`equals`) : `true`

Nombre de requêtes SQL : 2

Les deux transactions chargent deux instances différentes du fournisseur. Elles ne sont donc pas identiques avec `==`, mais elles sont considérées comme égales avec `equals()`. Chaque transaction a son propre contexte de persistance.