/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myapp.entities.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.codename1.components.FloatingHint;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.ReservationVol;
import com.mycompany.myapp.entities.Task;
import com.mycompany.myapp.utils.Statics;
import java.util.Date;
/*
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.smtp.SMTPTransport;
*/
/**
 *
 * @author remo
 */
public class ReservationService {

    private ConnectionRequest request;
    private boolean responseOk;
    private List<Task> tasks;
    private List<ReservationVol> reservationVols;
    public static ReservationService instance=null;

    public ReservationService() {
        tasks = new ArrayList<>();
    }
    
        public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }
    


    public boolean addTask(Task task) {
        this.request = new ConnectionRequest();
        String url = Statics.BASE_URL + "create?name=" + task.getName() + "&status=" + task.getStatus();
        this.request.setUrl(url);
        this.request.setPost(false);

        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                responseOk = request.getResponseCode() == 200;
                request.removeResponseListener(this);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseOk;
    }

    public boolean addReservation(int idVol, String email) {
        this.request = new ConnectionRequest();
        String url = "http://127.0.0.1:8000/reservationvol/new/" + idVol;
        this.request.setUrl(url);
        this.request.setPost(false);

        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                responseOk = request.getResponseCode() == 200;
                request.removeResponseListener(this);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(request);
        //sendMail(email, idVol);
        return responseOk;
    }

    public boolean deleteReservation(int idVol) {
        this.request = new ConnectionRequest();
        String url = "http://127.0.0.1:8000/reservationvol/" + idVol;
        this.request.setUrl(url);
        this.request.setPost(true);

        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                responseOk = request.getResponseCode() == 200;
                request.removeResponseListener(this);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseOk;
    }

    public List<Task> getAllTasks() {
        String url = "http://127.0.0.1:8000/reservationvol";
        this.request = new ConnectionRequest();
        //  String url = Statics.BASE_URL + "get";
        this.request.setUrl(url);
        /*  this.request.addResponseListener(new ActionListener<NetworkEvent>(){
        @Override
        public void actionPerformed(NetworkEvent evt){
        resultOk = request.getResponseData()}
    });
         */
        this.request.setPost(false);
        System.out.println("heeeeeeeeeeeeeeeeeeeeeeere" + request.getUrl() + " ---" + request.getHttpMethod());

        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String response = new String(request.getResponseData());
                int code = request.getResponseCode();
                System.out.println(response);
//parseTasks(response);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(request);
        return this.tasks;
    }

    public List<ReservationVol> getReservationVol() {
        String url = "http://127.0.0.1:8000/reservationvol/mobile/aff";
        this.request = new ConnectionRequest();
        //  String url = Statics.BASE_URL + "get";
        this.request.setUrl(url);
        /*  this.request.addResponseListener(new ActionListener<NetworkEvent>(){
        @Override
        public void actionPerformed(NetworkEvent evt){
        resultOk = request.getResponseData()}
    });
         */
        this.request.setPost(false);

        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String response = new String(request.getResponseData());
                System.out.println(response);
                reservationVols = parseReservationVol(response);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(request);
        return this.reservationVols;
    }

    private List<ReservationVol> parseReservationVol(String jsonText) {
        reservationVols = new ArrayList<>();
        try {
            JSONParser jsonParser = new JSONParser();
            Map<String, Object> taskListJson = jsonParser.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) taskListJson.get("root");

            for (Map<String, Object> obj : list) {
                ReservationVol reservationVol = new ReservationVol();
                float idreservationvol = Float.parseFloat(obj.get("idreservationvol").toString());
                float idu = Float.parseFloat(obj.get("idu").toString());
                float idvol = Float.parseFloat(obj.get("idvol").toString());
                reservationVol.setIdreservationvol((int) idreservationvol);
                reservationVol.setIdu((int) idu);
                reservationVol.setIdvol((int) idvol);
                reservationVols.add(reservationVol);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return reservationVols;
    }
/*
    public void sendMail(String emailTo, int idVol) {
        try {
            String email = "semia.ezzaouia@esprit.tn";
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp"); //SMTP protocol
            props.put("mail.smtps.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtps.auth", "true"); //enable authentication

            Session session = Session.getInstance(props, null); // aleh 9rahach 5ater mazlna masabinach javax.mail .jar

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress("Confirmation de la reservation du vol <monEmail@domaine.com>"));
            msg.setRecipients(Message.RecipientType.TO, emailTo);
            msg.setSubject("TouToDo  : Confirmation Vol ");
            msg.setSentDate(new Date(System.currentTimeMillis()));

            String txt = "Votre reservation du vol : " + idVol + " a ete effectuee avec succes";

            msg.setText(txt);

            SMTPTransport st = (SMTPTransport) session.getTransport("smtps");

            st.connect("smtp.gmail.com", 465, "semia.ezzaouia@esprit.tn", "fillegarcon");

            st.sendMessage(msg, msg.getAllRecipients());

            System.out.println("server response : " + st.getLastServerResponse());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

}
