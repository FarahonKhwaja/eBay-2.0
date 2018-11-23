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
  "nom": "nom",
  "description": "description",
  "prix_min": 0.00,
  "dateCreation": "2018-01-01 00:00:01",
  "creePar": "creePar",
  "duree": 5,
  "photo": "url image",
  "etat": "active",
  "derniereEnchere": 1000.01,
  "utilisateurEnchere": "Philippe RG"
}
```

#### GET

Retourne une liste d'annonces
