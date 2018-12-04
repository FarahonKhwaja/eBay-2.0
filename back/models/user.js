// Package pour l'utilisation de mongoose
var mongoose = require('mongoose');

// Définition du schéma des données utilisés
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

// Association du schéma à un model et export pour utilisation
module.exports = mongoose.model('user', userSchema);
