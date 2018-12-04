# README

## Utilisation des routes

Documentation de l'API

## Routes

### /

#### GET

retourne :

```json
{
  "message": "bonjour-in"
}
```

### /users

#### POST

ajoute un utilisateur au format :

```json
{
  "nom": "nom",
  "prenom": "prenom",
  "mail": "adresse mail",
  "username": "pseudo",
  "pwd": "mot de passer",
  "adresse": "adresse postale",
  "photo": "url d'une image"
}
```

retourne l'utilisateur au format :

```json
{
  "id": 0,
  "nom": "nom",
  "prenom": "prenom",
  "mail": "adresse mail",
  "username": "pseudo",
  "pwd": "mot de passer",
  "adresse": "adresse postale",
  "photo": "url d'une image"
}
```

Le champ `id` est généré à partir de l'incrément du dernier `id` présent en base.

Si la valeur du champ `username` est présente en base, le retour est un `statuscode 409` et avec le message :

```json
{
  "message": "l'utilisateur existe déjà"
}
```

### /usercheck

#### POST

Permet de vérifier si un couple `username` / `pwd` appartient à un utilisateur connu, le couple a vérifier est au format :

```json
{
"username" : "username",
"pwd" : "pwd"
}
```

retourne l'utilisateur au format :

```json
{
  "id": 0,
  "nom": "nom",
  "prenom": "prenom",
  "mail": "adresse mail",
  "username": "pseudo",
  "pwd": "mot de passer",
  "adresse": "adresse postale",
  "photo": "url d'une image"
}
```

retourne `null` si l'utilisateur n'existe pas

### /user/:id

#### GET

retourne l'utilisateur `id` si il existe en base au format :

```json
{
  "id": 0,
  "nom": "nom",
  "prenom": "prenom",
  "mail": "adresse mail",
  "username": "pseudo",
  "pwd": "mot de passer",
  "adresse": "adresse postale",
  "photo": "url d'une image"
}
```

Si l'utilisateur n'existe pas, retourne alors : `null`

### /annonces

#### POST

ajoute à la base de données une annonce de la forme :

```json
{
  "nom": "nom",
  "description": "description",
  "prix_min": 0.00,
  "creePar": "creePar",
  "duree": 5,
  "photo": "url image"
}
```

retourne l'annonce insérée au format :

```json
{
  "id": 0,
  "nom": "nom",
  "description": "description",
  "prix_min": 0.00,
  "dateCreation": "2018-01-01 00:00:01",
  "creePar": "creePar",
  "duree": 5,
  "photo": "url image",
  "etat": "active",
  "derniereEnchere": 0.00,
  "utilisateurEnchere": "utilisateurEnchere"
}
```

#### GET

Retourne une liste d'annonces au format :

```json
[{
  "id": 0,
  "nom": "nom",
  "description": "description",
  "prix_min": 0.00,
  "dateCreation": "2018-01-01 00:00:01",
  "creePar": "creePar",
  "duree": 5,
  "photo": "url image",
  "etat": "active",
  "derniereEnchere": 0.00,
  "utilisateurEnchere": "utilisateurEnchere"
},
{
  "id": 1,
  "nom": "nom2",
  "description": "description",
  "prix_min": 0.00,
  "dateCreation": "2018-01-01 00:00:01",
  "creePar": "creePar",
  "duree": 5,
  "photo": "url image",
  "etat": "active",
  "derniereEnchere": 0.00,
  "utilisateurEnchere": "utilisateurEnchere"
}]
```

### /annonce/:id

#### GET

retourne l'annonce `id` sous la forme :

```json
{
  "id": 0,
  "nom": "nom",
  "description": "description",
  "prix_min": 0.00,
  "dateCreation": "2018-01-01 00:00:01",
  "creePar": "creePar",
  "duree": 5,
  "photo": "url image",
  "etat": "active",
  "derniereEnchere": 0.00,
  "utilisateurEnchere": "utilisateurEnchere"
}
```

retourne `null` si `id` n'est pas présent en base

#### PUT

met à jour une annonce en utilisant en entrée un format :

```json
{
  "id": 0,
  "derniereEnchere": 0.00,
  "utilisateurEnchere": "utilisateurEnchere"
}
```

retourne si l'annonce a été modifié au format :

```json
{
  "ok": 1,
  "nModified": 1,
  "n": 1
}
```
