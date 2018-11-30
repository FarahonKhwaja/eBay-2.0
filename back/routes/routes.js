
var express = require('express');
var colors = require('colors/safe');

// Utilisation de mongoDB pour le stockage des informations
var mongoose = require('mongoose');
require('mongoose-double')(mongoose);

// Définition des schémas pour utiliser mongo
var Schema = mongoose.Schema;
var SchemaTypes = mongoose.Schema.Types;

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
  id: Number,
  nom: String,
  description: String,
  prix_min: SchemaTypes.Double,
  dateCreation: String,
  utilisateurCreation: String,
  duree: Number,
  photo: String,
  etat: String,
  derniereEnchere: SchemaTypes.Double,
  utilisateurEnchere: String
});

var userSchema = mongoose.Schema({
  id: Number,
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

    userModel.findOne({
      username: user.username
    }).exec(function(err, usercheck) {

      if (!usercheck) {

        userModel.aggregate([{
          $group: {
            _id: null,
            max: {
              $max: "$id"
            }
          }
        }]).exec(function(err, reponse) {
          if (!reponse.length) {
            user.id = 0
          } else {
            user.id = reponse[0].max + 1;
          }

          userModel.create(user, function(err, userInsere) {
            console.log(userInsere);
            res.json(userInsere);
          });
        });

      } else {
        res.status(409);
        res.json({
          message: "l'utilisateur existe déjà"
        });
        console.log("l'utilisateur existe déjà");
      }
    });
  });

router.route('/usercheck')
  .post(function(req, res) {
    var user = req.body;

    console.log(user);

    userModel.findOne({
      username: user.username,
      pwd: user.pwd
    }).exec(function(err, usercheck) {
      if (!usercheck){
        usercheck = {};
      }
      res.json(usercheck);
      console.log(usercheck);
    });

  });

// Définition de la route pour "/user"
router.route('/user/:id')
  .get(function(req, res) {

    userModel.findOne({
      id: req.params.id
    }, {
      _id: 0
    }).exec(function(err, utilisateur) {
      res.json(utilisateur);

      console.log(utilisateur);
    })
  });

// Définition de la route pour "/annonces"
router.route('/annonces')
  .get(function(req, res) {
    annonceModel.find({}, {
      _id: 0
    }).exec(function(err, annonces) {
      res.json(annonces);

      console.log(annonces);
    })
  })
  .post(function(req, res) {
    var annonce = req.body;
    annonceModel.aggregate([{
      $group: {
        _id: null,
        max: {
          $max: "$id"
        }
      }
    }]).exec(function(err, reponse) {
      if (!reponse.length) {
        annonce.id = 0
      } else {
        annonce.id = reponse[0].max + 1;
      }

      annonceModel.create(annonce, function(err, annonceInsere) {
        console.log(annonceInsere);
        res.json(annonceInsere);
      });
    });

  });

// Définition de la route pour "/annonce"
router.route('/annonce/:id')
  .get(function(req, res) {
    annonceModel.findOne({
      id: req.params.id
    }, {
      _id: 0
    }).exec(function(err, annonce) {
      res.json(annonce);

      console.log(annonce);
    })
  })
  .put(function(req, res) {
    var annonceAMAJ = req.body;

    var condition = {
      "id": annonceAMAJ.id
    };

    var updates = {
      "derniereEnchere": annonceAMAJ.derniereEnchere,
      "utilisateurEnchere": annonceAMAJ.utilisateurEnchere
    };

    annonceModel.update(condition, updates, function(err, resultat) {
      res.json(resultat);
      console.log(resultat);
    })
  });

  module.exports = router;
