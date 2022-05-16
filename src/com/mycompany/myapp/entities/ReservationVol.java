/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;

/**
 *
 * @author Sémia
 */
public class ReservationVol {
    private int idreservationvol;
    private int idu;
    private int idvol;

    public ReservationVol() {
    }

    public ReservationVol(int idreservationvol, int idu, int idvol) {
        this.idreservationvol = idreservationvol;
        this.idu = idu;
        this.idvol = idvol;
    }
    
    

    public int getIdreservationvol() {
        return idreservationvol;
    }

    public int getIdu() {
        return idu;
    }

    public int getIdvol() {
        return idvol;
    }

    public void setIdreservationvol(int idreservationvol) {
        this.idreservationvol = idreservationvol;
    }

    public void setIdu(int idu) {
        this.idu = idu;
    }

    public void setIdvol(int idvol) {
        this.idvol = idvol;
    }

    @Override
    public String toString() {
        return "ReservationVol{" + "idreservationvol=" + idreservationvol + ", idu=" + idu + ", idvol=" + idvol + '}';
    }
    
    
    
}
