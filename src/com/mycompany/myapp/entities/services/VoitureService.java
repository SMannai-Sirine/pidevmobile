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

public class VoitureService {

    public static VoitureService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Voiture> listVoitures;

    

    private VoitureService() {
        cr = new ConnectionRequest();
    }

    public static VoitureService getInstance() {
        if (instance == null) {
            instance = new VoitureService();
        }
        return instance;
    }
    
    public ArrayList<Voiture> getAll() {
        listVoitures = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "mobile/voiture");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listVoitures = getList();
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

        return listVoitures;
    }

    private ArrayList<Voiture> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Voiture voiture = new Voiture(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        
                        makePays((Map<String, Object>) obj.get("pays")),
                        (String) obj.get("model"),
                        (String) obj.get("type"),
                        Float.parseFloat(obj.get("prix").toString())
                        
                );

                listVoitures.add(voiture);
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
        return listVoitures;
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
    
    public int add(Voiture voiture) {
        return manage(voiture, false);
    }

    public int edit(Voiture voiture) {
        return manage(voiture, true );
    }

    public int manage(Voiture voiture, boolean isEdit) {
        
        cr = new ConnectionRequest();

        
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "mobile/voiture/edit");
            cr.addArgument("id", String.valueOf(voiture.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "mobile/voiture/add");
        }
        
        cr.addArgument("pays", String.valueOf(voiture.getPays().getId()));
        cr.addArgument("model", voiture.getModel());
        cr.addArgument("type", voiture.getType());
        cr.addArgument("prix", String.valueOf(voiture.getPrix()));
        
        
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

    public int delete(int voitureId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "mobile/voiture/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(voitureId));

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
