var mongoose = require('mongoose');

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

module.exports = mongoose.model('user', userSchema);
