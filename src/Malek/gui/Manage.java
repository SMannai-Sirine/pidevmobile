package Malek.gui;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.LocVoiture;
import com.mycompany.myapp.entities.Pays;
import com.mycompany.myapp.entities.*;
import com.mycompany.myapp.entities.services.*;
import com.mycompany.myapp.utils.AlertUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Manage extends Form {

    LocVoiture currentLocVoiture;

    TextField dureeResTF;
    Label dureeResLabel;
    PickerComponent dateResTF;
    ArrayList<Pays> listPayss;
    PickerComponent paysPC;
    Pays selectedPays = null;
    ArrayList<Voiture> listVoitures;
    PickerComponent voiturePC;
    Voiture selectedVoiture = null;

    Button manageButton;

    Form previous;

    public Manage(Form previous,Resources res) {
        super(AllReservationMalek.currentLocVoiture == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentLocVoiture = AllReservationMalek.currentLocVoiture;

        addGUIs();
        addActions(res);

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        String[] paysStrings;
        int paysIndex;
        paysPC = PickerComponent.createStrings("").label("Pays");
        listPayss = PaysService.getInstance().getAll();
        paysStrings = new String[listPayss.size()];
        paysIndex = 0;
        for (Pays pays : listPayss) {
            paysStrings[paysIndex] = pays.getNom();
            paysIndex++;
        }
        if (listPayss.size() > 0) {
            paysPC.getPicker().setStrings(paysStrings);
            paysPC.getPicker().addActionListener(l -> selectedPays = listPayss.get(paysPC.getPicker().getSelectedStringIndex()));
        } else {
            paysPC.getPicker().setStrings("");
        }

        String[] voitureStrings;
        int voitureIndex;
        voiturePC = PickerComponent.createStrings("").label("Voiture");
        listVoitures = VoitureService.getInstance().getAll();
        voitureStrings = new String[listVoitures.size()];
        voitureIndex = 0;
        for (Voiture voiture : listVoitures) {
            voitureStrings[voitureIndex] = voiture.getModel();
            voitureIndex++;
        }
        if (listVoitures.size() > 0) {
            voiturePC.getPicker().setStrings(voitureStrings);
            voiturePC.getPicker().addActionListener(l -> selectedVoiture = listVoitures.get(voiturePC.getPicker().getSelectedStringIndex()));
        } else {
            voiturePC.getPicker().setStrings("");
        }

        dateResTF = PickerComponent.createDate(null).label("DateRes");

        dureeResLabel = new Label("DureeRes : ");
        dureeResLabel.setUIID("labelDefault");
        dureeResTF = new TextField();
        dureeResTF.setHint("Tapez le dureeRes");


        if (currentLocVoiture == null) {

            manageButton = new Button("Ajouter");
        } else {

            dateResTF.getPicker().setDate(currentLocVoiture.getDateRes());
            dureeResTF.setText(String.valueOf(currentLocVoiture.getDureeRes()));

            paysPC.getPicker().setSelectedString(currentLocVoiture.getPays().getNom());
            selectedPays = currentLocVoiture.getPays();
            voiturePC.getPicker().setSelectedString(currentLocVoiture.getVoiture().getModel());
            selectedVoiture = currentLocVoiture.getVoiture();

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonWhiteCenter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                dateResTF,
                dureeResLabel, dureeResTF,
                paysPC, voiturePC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions(Resources res) {

        if (currentLocVoiture == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = LocVoitureService.getInstance().add(
                            new LocVoiture(
                                    selectedPays,
                                    selectedVoiture,
                                    dateResTF.getPicker().getDate(),
                                    (int) Float.parseFloat(dureeResTF.getText()),
                                   0,
                                    0
                            )
                    );
                    if (responseCode == 200) {
                        AlertUtils.makeNotification("LocVoiture ajouté avec succes");
                        new AllReservationMalek(res).show();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de locVoiture. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = LocVoitureService.getInstance().edit(
                            new LocVoiture(
                                    currentLocVoiture.getId(),
                                    selectedPays,
                                    selectedVoiture,
                                    dateResTF.getPicker().getDate(),
                                    (int) Float.parseFloat(dureeResTF.getText()),
                              0,
                                    0
                            )
                    );
                    if (responseCode == 200) {
                        AlertUtils.makeNotification("LocVoiture modifié avec succes");
                        new AllReservationMalek(res).show();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de locVoiture. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }


    private boolean controleDeSaisie() {

        if (dateResTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateRes", new Command("Ok"));
            return false;
        }

        if (dureeResTF.getText().equals("")) {
            Dialog.show("Avertissement", "DureeRes vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(dureeResTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", dureeResTF.getText() + " n'est pas un nombre valide (dureeRes)", new Command("Ok"));
            return false;
        }

        if (selectedPays == null) {
            Dialog.show("Avertissement", "Veuillez choisir un pays", new Command("Ok"));
            return false;
        }

        if (selectedVoiture == null) {
            Dialog.show("Avertissement", "Veuillez choisir un voiture", new Command("Ok"));
            return false;
        }

        return true;
    }
}
