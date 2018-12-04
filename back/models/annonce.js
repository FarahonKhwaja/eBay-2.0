var mongoose = require('mongoose');
require('mongoose-double')(mongoose);

var SchemaTypes = mongoose.Schema.Types;

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

module.exports = mongoose.model('annonce', annonceSchema);
