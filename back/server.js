//// Initialisation serveur

//// packages

// Utilisation de express pour publier l'API
var express = require('express');
var app = express();

// Utilisation de mongoDB pour le stockage des informations
var mongoose = require('mongoose');

// Définition des schémas pour utiliser mongo
var Schema = mongoose.Schema;

// utilisation de bodyParser pour récupérer les données d'un POST par exemple
var bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());

//// connexion à la base de données mongoDB

var url = 'mongodb://localhost:27017/android';
var options = {
  useNewUrlParser: true
};

mongoose.connect(url, options, function(error) {
  if (error) {
    console.log(error);
  }
  console.log("Connexion réussie !");
});
var db = mongoose.connection;

//// Port qui sera utilisé pour l'API REST
var port = 8080;


//// Création des routes

// Récupération d'un routeur
var router = express.Router();

// Fonction de log des routes pour info ou debug
router.use(function(req, res, next) {
  console.log(req.method + " " + req.url);

  // Pour ne pas s'arreter à cette méthode mais continuer sur les prochaines
  next();
});

// Définition de la route pour "/"
router.get('/', function(req, res) {
  res.json({
    message: "bonjour-in"
  })
})


// Utilisation du routeur pour toutes les routes qui commencent pas "/"
app.use('/', router);

//// Lancement de l'application
app.listen(port);
console.log('Go on localhost:' + port + ' !');
