// Package pour l'utilisation de mongoose
var mongoose = require('mongoose');
require('mongoose-double')(mongoose);

var SchemaTypes = mongoose.Schema.Types;

// Définition du schéma des données utilisés
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

// Association du schéma à un model et export pour utilisation
module.exports = mongoose.model('annonce', annonceSchema);
