package com.mycompany.myapp.entities;

import java.util.Date;

public class Voiture {
    
    private int id;
    private Pays pays;
     private String model;
     private String type;
     private float prix;
    
    public Voiture() {}

    public Voiture(int id, Pays pays, String model, String type, float prix) {
        this.id = id;
        this.pays = pays;
        this.model = model;
        this.type = type;
        this.prix = prix;
    }

    public Voiture(Pays pays, String model, String type, float prix) {
        this.pays = pays;
        this.model = model;
        this.type = type;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }
    
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
    
    
    
}