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

public class LocVoitureService {

    public static LocVoitureService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<LocVoiture> listLocVoitures;

    

    private LocVoitureService() {
        cr = new ConnectionRequest();
    }

    public static LocVoitureService getInstance() {
        if (instance == null) {
            instance = new LocVoitureService();
        }
        return instance;
    }
    
    public ArrayList<LocVoiture> getAll() {
        listLocVoitures = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "mobile/locVoiture");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listLocVoitures = getList();
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

        return listLocVoitures;
    }

    private ArrayList<LocVoiture> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                LocVoiture locVoiture = new LocVoiture(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        
                        makePays((Map<String, Object>) obj.get("pays")),
                        makeVoiture((Map<String, Object>) obj.get("voiture")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateRes")),
                        (int) Float.parseFloat(obj.get("dureeRes").toString()),
                        (int) Float.parseFloat(obj.get("remise").toString()),
                        (int) Float.parseFloat(obj.get("tauxRemise").toString())
                        
                );

                listLocVoitures.add(locVoiture);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listLocVoitures;
    }
    
    public Pays makePays(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Pays pays = new Pays();
        pays.setId((int) Float.parseFloat(obj.get("id").toString()));
        pays.setNom((String) obj.get("nom"));
        return pays;
    }
    
    public Voiture makeVoiture(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Voiture voiture = new Voiture();
        voiture.setId((int) Float.parseFloat(obj.get("id").toString()));
        voiture.setModel((String) obj.get("model"));
        return voiture;
    }
    
    public int add(LocVoiture locVoiture) {
        return manage(locVoiture, false);
    }

    public int edit(LocVoiture locVoiture) {
        return manage(locVoiture, true );
    }

    public int manage(LocVoiture locVoiture, boolean isEdit) {
        
        cr = new ConnectionRequest();

        
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "mobile/locVoiture/edit");
            cr.addArgument("id", String.valueOf(locVoiture.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "mobile/locVoiture/add");
        }
        
        cr.addArgument("pays", String.valueOf(locVoiture.getPays().getId()));
        cr.addArgument("voiture", String.valueOf(locVoiture.getVoiture().getId()));
        cr.addArgument("dateRes", new SimpleDateFormat("dd-MM-yyyy").format(locVoiture.getDateRes()));
        cr.addArgument("dureeRes", String.valueOf(locVoiture.getDureeRes()));
        cr.addArgument("remise", String.valueOf(locVoiture.getRemise()));
        cr.addArgument("tauxRemise", String.valueOf(locVoiture.getTauxRemise()));
        
        
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

    public int delete(int locVoitureId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "mobile/locVoiture/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(locVoitureId));

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
