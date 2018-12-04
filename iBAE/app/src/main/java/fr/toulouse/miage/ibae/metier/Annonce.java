package fr.toulouse.miage.ibae.metier;

import android.util.Base64;

import java.sql.Timestamp;

public class Annonce {
    private int id;
    private String nom;
    private String desciption;
    private double prixMin;
    private Timestamp dateCreation;
    private String creePar;
    private int duree;
    private String photo;
    private String etat;
    private double derniereEnchere;
    private String utilisateurEnchere;

    public int getId() {
        return id;
    }

    public void setId(int id){ this.id = id;}

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

    public double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(double prixMin) {
        this.prixMin = prixMin;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getCreePar() {
        return creePar;
    }

    public void setCreePar(String creePar) {
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

    public double getDerniereEnchere() {
        return derniereEnchere;
    }

    public void setDerniereEnchere(double derniereEnchere) {
        this.derniereEnchere = derniereEnchere;
    }

    public String getUtilisateurEnchere() {
        return utilisateurEnchere;
    }

    public void setUtilisateurEnchere(String utilisateurEnchere) {
        this.utilisateurEnchere = utilisateurEnchere;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
