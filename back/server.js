//// Initialisation serveur

//// packages

// Utilisation de colors pour la mise en couleur dans le log
var colors = require('colors/safe');

// Utilisation de express pour publier l'API
var express = require('express');
var app = express();


// utilisation de bodyParser pour récupérer les données d'un POST par exemple
var bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({
  extended: true
}));
//app.use(bodyParser.json());

app.use(bodyParser.json({limit: '50mb', type: 'application/json'}));
app.use(bodyParser());


//// Création des routes

// Appelle du routeur définissant les routes dans le fichier routes/routes.js
var router = require("./routes/routes")

// Utilisation du routeur pour toutes les routes qui commencent par "/"
app.use('/', router);

//// Lancement de l'application

// Port qui sera utilisé pour l'API REST
var port = 8080;
var addr = '0.0.0.0';
app.listen(port, addr);

console.log(colors.yellow('Go on localhost:' + port + ' !'));
