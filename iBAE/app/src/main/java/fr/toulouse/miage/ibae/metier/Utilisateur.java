package fr.toulouse.miage.ibae.metier;

import android.util.Base64;

public class Utilisateur {
    private int id;
    private String login;
    private String nom;
    private String prenom;
    private String mail;
    private String adresse;
    private Base64 photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Base64 getPhoto() {
        return photo;
    }

    public void setPhoto(Base64 photo) {
        this.photo = photo;
    }

}
