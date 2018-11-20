//// Initialisation serveur

//// packages

// Utilisation de colors pour la mise en couleur dans le log
var colors = require('colors/safe');

// Utilisation de express pour publier l'API
var express = require('express');
var app = express();

// Utilisation de mongoDB pour le stockage des informations
var mongoose = require('mongoose');
require('mongoose-double')(mongoose);

// Définition des schémas pour utiliser mongo
var Schema = mongoose.Schema;
var SchemaTypes = mongoose.Schema.Types;

// utilisation de bodyParser pour récupérer les données d'un POST par exemple
var bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());

//// connexion à la base de données mongoDB

// Préparation accès
var url = 'mongodb://localhost:27017/android';
var options = {
  useNewUrlParser: true
};

mongoose.connect(url, options, function(error) {
  if (error) {
    console.log(error);
  }
  console.log(colors.yellow("Connexion réussie !"));
});
var db = mongoose.connection;

// Définition des schémas
var annonceSchema = mongoose.Schema({
  nom: String,
  description: String,
  prix_min: SchemaTypes.Double,
  dateCreation: String,
  duree: Number,
  photo: String,
  etat: String,
  derniereEnchere: SchemaTypes.Double,
  utilisateurEnchere: String
});

var userSchema = mongoose.Schema({
  nom: String,
  prenom: String,
  mail: String,
  username: String,
  pwd: String,
  adresse: String,
  photo: String
});

// Définition des modèles
var annonceModel = mongoose.model('annonce', annonceSchema);
var userModel = mongoose.model('user', userSchema);

//// Port qui sera utilisé pour l'API REST
var port = 8080;


//// Création des routes

// Récupération d'un routeur
var router = express.Router();

// Fonction de log des routes pour info ou debug
router.use(function(req, res, next) {
  console.log("\t" + colors.green(req.method + " " + req.url));

  // Pour ne pas s'arreter à cette méthode mais continuer sur les prochaines
  next();
});

// Définition de la route pour "/"
router.get('/', function(req, res) {
  res.json({
    message: "bonjour-in"
  })
})

// Définition de la route pour "/users"
router.route('/users')
  .post(function(req, res) {
    var user = req.body;

    res.json(user);

    console.dir(user);
  });

// Définition de la route pour "/user"
router.route('/user/:id')
  .get(function(req, res) {

    var reponse = {
      "nom": "LENOM",
      "prenom": "LEPRENOM",
      "mail": "LEPRENOM.LENOM@EMAIL.FR²",
      "username": "LEPRENOM.LENOM",
      "pwd": "secret123456",
      "adresse": "1 rue de l'adresse 31000 Toulouse",
      "photo": "http://buzz.io/wp-content/uploads/2014/02/worst_edited_photos_for_profiles_4.jpg" //url de la photo de profil
    };

    res.json(reponse);

    console.dir(reponse);
  });

// Définition de la route pour "/annonces"
router.route('/annonces')
  .get(function(req, res) {
    var reponse = [{
        "nom": "annonce 1",
        "description": "ceci est la première annonce et elle est bien",
        "prix_min": 0.01,
        "dateCreation": "2018-01-01 00:00:01",
        "duree": 5,
        "photo": "https://www.och.fr/sites/default/files/envoyez-nous_votre_annonce.jpg",
        "etat": "active",
        "derniereEnchere": 1000.01,
        "utilisateurEnchere": "Philippe RG"
      },
      {
        "nom": "annonce 2",
        "description": "ceci est une annonce encore mieux",
        "prix_min": 10.00,
        "dateCreation": "2018-11-01 12:35:56",
        "duree": 5,
        "photo": "https://aae-fgi.org/sites/default/files/field/annonce/annonce_2.jpg",
        "etat": "active",
        "derniereEnchere": 10.01,
        "utilisateurEnchere": "Philippe RG 2"
      }
    ]

    res.json(reponse);

    console.dir(reponse);
  })
  .post(function(req, res) {
    var annonce = req.body;
    res.json(annonce);

    console.dir(annonce);
  });

// Définition de la route pour "/annonce"
router.route('/annonce/:id')
  .get(function(req, res) {
    var reponse = {
      "nom": "annonce 1",
      "description": "ceci est la première annonce et elle est bien",
      "prix_min": 0.01,
      "dateCreation": "2018-01-01 00:00:01",
      "duree": 5,
      "photo": "https://www.och.fr/sites/default/files/envoyez-nous_votre_annonce.jpg",
      "etat": "active",
      "derniereEnchere": 1000.01,
      "utilisateurEnchere": "Philippe RG"
    };

    res.json(reponse);

    console.dir(reponse);
  })
  .put(function(req, res) {
    var annonce = req.body;

    res.json(annonce);
    console.dir(annonce);
  });


// Utilisation du routeur pour toutes les routes qui commencent pas "/"
app.use('/', router);

//// Lancement de l'application
app.listen(port);
console.log(colors.yellow('Go on localhost:' + port + ' !'));
