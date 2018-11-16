// Initialisation serveur

// packages
var express = require('express');
var app = express();

// utilisation de bodyParser pour récupérer les données d'un POST par exemple
var bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// Port qui sera utilisé pour l'API REST
var port = 8080;


// Création des routes

var router = express.Router();

router.get('/',function(req,res){
  res.json({message: "bonjour-in"})
})

app.use('/api', router);

app.listen(port);
console.log('Go on localhost:' + port + ' !');
