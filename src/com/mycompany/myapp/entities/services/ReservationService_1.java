package com.mycompany.myapp.entities.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.*;
import com.mycompany.myapp.utils.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationService_1 {

    public static ReservationService_1 instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Reservation> listReservations;

    

    private ReservationService_1() {
        cr = new ConnectionRequest();
    }

    public static ReservationService_1 getInstance() {
        if (instance == null) {
            instance = new ReservationService_1();
        }
        return instance;
    }
    
    public ArrayList<Reservation> getAll() {
        listReservations = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "mobile/reservation");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listReservations = getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listReservations;
    }

    private ArrayList<Reservation> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Reservation reservation = new Reservation(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        
                        (int) Float.parseFloat(obj.get("idRes").toString()),
                        (int) Float.parseFloat(obj.get("type").toString()),
                        (String) obj.get("etat")
                        
                );

                listReservations.add(reservation);
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
        return listReservations;
    }
    
    public int add(Reservation reservation) {
        return manage(reservation, false);
    }

    public int edit(Reservation reservation) {
        return manage(reservation, true );
    }

    public int manage(Reservation reservation, boolean isEdit) {
        
        cr = new ConnectionRequest();

        
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "mobile/reservation/edit");
            cr.addArgument("id", String.valueOf(reservation.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "mobile/reservation/add");
        }
        
        cr.addArgument("idRes", String.valueOf(reservation.getIdRes()));
        cr.addArgument("type", String.valueOf(reservation.getType()));
        cr.addArgument("etat", reservation.getEtat());
        
        
        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int delete(int reservationId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "mobile/reservation/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(reservationId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
}
