package com.mycompany.myapp.entities;

import java.util.Date;

public class Reservation {
    
    private int id;
     private int idRes;
     private int type;
     private String etat;
    
    public Reservation() {}

    public Reservation(int id, int idRes, int type, String etat) {
        this.id = id;
        this.idRes = idRes;
        this.type = type;
        this.etat = etat;
    }

    public Reservation(int idRes, int type, String etat) {
        this.idRes = idRes;
        this.type = type;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdRes() {
        return idRes;
    }

    public void setIdRes(int idRes) {
        this.idRes = idRes;
    }
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
    
    
    
}