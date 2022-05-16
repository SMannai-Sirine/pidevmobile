/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.messaging.Message;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Evenement;
import com.mycompany.myapp.entities.Evenement;

import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author msi
 */
public class ServiceEvenement {

    public ArrayList<Evenement> evenements;
    
    public static ServiceEvenement instance=null;
    public boolean resultOK;
    private ConnectionRequest req;
    public ServiceEvenement() {
         req = new ConnectionRequest();
    }

    public static ServiceEvenement getInstance() {
        if (instance == null) {
            instance = new ServiceEvenement();
        }
        return instance;
    }
    


    public ArrayList<Evenement> parseEvenements(String jsonText){
                try {

                    System.out.println(jsonText);
            evenements=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                Evenement a = new Evenement();
                                
                float id = Float.parseFloat(obj.get("idevent").toString());
                a.setId((int) id);
                a.setIntitule(obj.get("intitule").toString());
                a.setPays(obj.get("paysevent").toString());
                a.setAdresse(obj.get("adresse").toString());
                a.setType(obj.get("typeevent").toString());
                a.setPhoto(obj.get("photo").toString());
                a.setPrix(Float.parseFloat(obj.get("prix").toString()));


                try {  
                            Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(obj.get("dateevent").toString());
                            a.setDateev(date1);

                        } catch (ParseException ex) {
                            System.out.println(ex);
                        }
                               
                evenements.add(a);


            }
        } catch (IOException ex) {
            
        }
        return evenements;
    }
    public ArrayList<Evenement> getAllEvenements(){
        String url = Statics.BASE_URL+"evenement/mobile/aff";
        req.setUrl(url);
        req.addResponseListener(new com.codename1.ui.events.ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                evenements = parseEvenements(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        com.codename1.io.NetworkManager.getInstance().addToQueueAndWait(req);
        return evenements;
    }


    public boolean addEvenement(Evenement u) {
        String url = Statics.BASE_URL + "evenement/mobile/new?dateev="+u.getDateev()+"&prix="+u.getPrix()+"&intitule="+u.getIntitule()+"&adresse="+u.getAdresse()+"&type="+u.getType()+"&photo="+u.getPhoto()+"&pays="+u.getPays();//création de l'URL
               req.setUrl(url);
               System.out.println(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

        public boolean editEvenement(Evenement u) {
        String url = Statics.BASE_URL + "evenement/mobile/editevent?id="+u.getId()+"&dateev="+u.getDateev()+"&prix="+u.getPrix()+"&intitule="+u.getIntitule()+"&adresse="+u.getAdresse()+"&type="+u.getType()+"&photo="+u.getPhoto()+"&pays="+u.getPays();//création de l'URL
               req.setUrl(url);
               System.out.println(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public boolean deleteEvenement(int id) {
        String url = Statics.BASE_URL + "evenement/mobile/del?id=" + id;
               req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

}
