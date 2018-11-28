package fr.toulouse.miage.ibae.metier;

import android.util.Base64;

import java.sql.Timestamp;

public class Annonce {
    private int id;
    private String nom;
    private String desciption;
    private float prixMin;
    private Timestamp dateCreation;
    private Utilisateur creePar;
    private int duree;
    private String photo;
    private float derniereEnchere;
    private Utilisateur utilisateurEnchere;

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public float getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(float prixMin) {
        this.prixMin = prixMin;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Utilisateur getCreePar() {
        return creePar;
    }

    public void setCreePar(Utilisateur creePar) {
        this.creePar = creePar;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public float getDerniereEnchere() {
        return derniereEnchere;
    }

    public void setDerniereEnchere(float derniereEnchere) {
        this.derniereEnchere = derniereEnchere;
    }

    public Utilisateur getUtilisateurEnchere() {
        return utilisateurEnchere;
    }

    public void setUtilisateurEnchere(Utilisateur utilisateurEnchere) {
        this.utilisateurEnchere = utilisateurEnchere;
    }

}
