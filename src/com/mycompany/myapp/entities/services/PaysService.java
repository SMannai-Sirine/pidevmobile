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

public class PaysService {

    public static PaysService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Pays> listPayss;

    

    private PaysService() {
        cr = new ConnectionRequest();
    }

    public static PaysService getInstance() {
        if (instance == null) {
            instance = new PaysService();
        }
        return instance;
    }
    
    public ArrayList<Pays> getAll() {
        listPayss = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "mobile/pays");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listPayss = getList();
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

        return listPayss;
    }

    private ArrayList<Pays> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Pays pays = new Pays(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        
                        (String) obj.get("nom")
                        
                );

                listPayss.add(pays);
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
        return listPayss;
    }
    
    public int add(Pays pays) {
        return manage(pays, false);
    }

    public int edit(Pays pays) {
        return manage(pays, true );
    }

    public int manage(Pays pays, boolean isEdit) {
        
        cr = new ConnectionRequest();

        
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "mobile/pays/edit");
            cr.addArgument("id", String.valueOf(pays.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "mobile/pays/add");
        }
        
        cr.addArgument("nom", pays.getNom());
        
        
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

    public int delete(int paysId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "mobile/pays/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(paysId));

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
