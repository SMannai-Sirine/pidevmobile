package com.mycompany.myapp.entities;

import com.mycompany.myapp.utils.*;
import java.util.Date;

public class LocVoiture implements Comparable<LocVoiture> {
    
    private int id;
    private Pays pays;
    private Voiture voiture;
     private Date dateRes;
     private int dureeRes;
     private int remise;
     private int tauxRemise;
    
    public LocVoiture() {}

    public LocVoiture(int id, Pays pays, Voiture voiture, Date dateRes, int dureeRes, int remise, int tauxRemise) {
        this.id = id;
        this.pays = pays;
        this.voiture = voiture;
        this.dateRes = dateRes;
        this.dureeRes = dureeRes;
        this.remise = remise;
        this.tauxRemise = tauxRemise;
    }

    public LocVoiture(Pays pays, Voiture voiture, Date dateRes, int dureeRes, int remise, int tauxRemise) {
        this.pays = pays;
        this.voiture = voiture;
        this.dateRes = dateRes;
        this.dureeRes = dureeRes;
        this.remise = remise;
        this.tauxRemise = tauxRemise;
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
    
    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }
    
    public Date getDateRes() {
        return dateRes;
    }

    public void setDateRes(Date dateRes) {
        this.dateRes = dateRes;
    }
    
    public int getDureeRes() {
        return dureeRes;
    }

    public void setDureeRes(int dureeRes) {
        this.dureeRes = dureeRes;
    }
    
    public int getRemise() {
        return remise;
    }

    public void setRemise(int remise) {
        this.remise = remise;
    }
    
    public int getTauxRemise() {
        return tauxRemise;
    }

    public void setTauxRemise(int tauxRemise) {
        this.tauxRemise = tauxRemise;
    }
    
    
    @Override
    public int compareTo(LocVoiture locVoiture) {
        switch (Statics.compareVar) {
            case "Pays":
                return this.getPays().getNom().compareTo(locVoiture.getPays().getNom());
            case "Voiture":
                return this.getVoiture().getModel().compareTo(locVoiture.getVoiture().getModel());
            case "DateRes":
                DateUtils.compareDates(this.getDateRes(), locVoiture.getDateRes());
            case "DureeRes":
                return Integer.compare(this.getDureeRes(), locVoiture.getDureeRes());
            case "Remise":
                return Integer.compare(this.getRemise(), locVoiture.getRemise());
            case "TauxRemise":
                return Integer.compare(this.getTauxRemise(), locVoiture.getTauxRemise());
            
            default:
                return 0;
        }
    }
    
}