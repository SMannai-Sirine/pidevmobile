/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package Malek.gui;


import ReservationVol.gui.*;
import Evenement.gui.*;
import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.PickerComponent;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.mycompany.gui.BaseForm;
import com.mycompany.myapp.entities.Evenement;
import com.mycompany.myapp.entities.LocVoiture;
import com.mycompany.myapp.entities.ReservationVol;
import com.mycompany.myapp.entities.services.LocVoitureService;
import com.mycompany.myapp.entities.services.ReservationService;
import com.mycompany.myapp.entities.services.ServiceEvenement;
import com.mycompany.myapp.utils.Statics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import com.codename1.ui.plaf.UIManager;

/**
 * The newsfeed form
 *
 * @author Shai Almog
 */
public class AllReservationMalek extends BaseForm {
        Form current;
    public static LocVoiture currentLocVoiture = null;
    Button addBtn;
    PickerComponent sortPicker;
    ArrayList<Component> componentModels;

    public AllReservationMalek(Resources res) {
        super("Reservation Voiture", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Reservation Voiture");
        getContentPane().setScrollVisible(false);
        
        super.addSideMenu(res);
        tb.addSearchCommand(e -> {});
        
        Tabs swipe = new Tabs();

        Label spacer1 = new Label();
        addTab(swipe, res.getImage("news-item.jpg"), spacer1, "  ", "", " ");
                
        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();
        
        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }
                
        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if(!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });
        
        Component.setSameSize(radioContainer, spacer1);
        add(LayeredLayout.encloseIn(swipe, radioContainer));
                                    

        addGUIs(res);
        addActions(res);


    }
    
        public void refresh(Resources res) {
        this.removeAll();
        addGUIs(res);
        addActions(res);
        this.refreshTheme();
    }
    private void addGUIs(Resources res) {
        addBtn = new Button("Ajouter");
        addBtn.setUIID("buttonWhiteCenter");
        this.add(addBtn);
        

        ArrayList<LocVoiture> listLocVoitures = LocVoitureService.getInstance().getAll();
        
        componentModels = new ArrayList<>();
        
        sortPicker = PickerComponent.createStrings("Pays", "Voiture", "DateRes", "DureeRes", "Remise", "TauxRemise").label("Trier par");
        sortPicker.getPicker().setSelectedString("");
        sortPicker.getPicker().addActionListener((l) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            Statics.compareVar = sortPicker.getPicker().getSelectedString();
            Collections.sort(listLocVoitures);
            for (LocVoiture locVoiture : listLocVoitures) {
                Component model = makeLocVoitureModel(locVoiture,res);
                this.add(model);
                componentModels.add(model);
            }
            this.revalidate();
        });
        this.add(sortPicker);
        
        if (listLocVoitures.size() > 0) {
            for (LocVoiture locVoiture : listLocVoitures) {
                Component model = makeLocVoitureModel(locVoiture,res);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }
    private void addActions(Resources res) {
        addBtn.addActionListener(action -> {
            currentLocVoiture = null;
            new Manage(this, res).show();
        });
        
    }
    Label paysLabel   , voitureLabel   , dateResLabel   , dureeResLabel   , remiseLabel   , tauxRemiseLabel  ;
    

    private Container makeModelWithoutButtons(LocVoiture locVoiture) {
        Container locVoitureModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        locVoitureModel.setUIID("containerRounded");
        
        
        paysLabel = new Label("Pays : " + locVoiture.getPays());
        paysLabel.setUIID("labelDefault");
        
        voitureLabel = new Label("Voiture : " + locVoiture.getVoiture());
        voitureLabel.setUIID("labelDefault");
        
        dateResLabel = new Label("DateRes : " + new SimpleDateFormat("dd-MM-yyyy").format(locVoiture.getDateRes()));
        dateResLabel.setUIID("labelDefault");
        
        dureeResLabel = new Label("DureeRes : " + locVoiture.getDureeRes());
        dureeResLabel.setUIID("labelDefault");
        
        remiseLabel = new Label("Remise : " + (locVoiture.getRemise() == 1 ?  "True" : "False"));
        remiseLabel.setUIID("labelDefault");
        
        tauxRemiseLabel = new Label("TauxRemise : " + locVoiture.getTauxRemise());
        tauxRemiseLabel.setUIID("labelDefault");
        
        paysLabel = new Label("Pays : " + locVoiture.getPays().getNom());
        paysLabel.setUIID("labelDefault");
        
        voitureLabel = new Label("Voiture : " + locVoiture.getVoiture().getModel());
        voitureLabel.setUIID("labelDefault");
        

        locVoitureModel.addAll(
                
                paysLabel, voitureLabel, dateResLabel, dureeResLabel, remiseLabel, tauxRemiseLabel
        );

        return locVoitureModel;
    }
    
    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeLocVoitureModel(LocVoiture locVoiture,Resources res) {

        Container locVoitureModel = makeModelWithoutButtons(locVoiture);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        
        editBtn = new Button("Modifier");
        editBtn.setUIID("buttonWhiteCenter");
        editBtn.addActionListener(action -> {
            currentLocVoiture = locVoiture;
            new Manage(this,res).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce locVoiture ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = LocVoitureService.getInstance().delete(locVoiture.getId());

                if (responseCode == 200) {
                    currentLocVoiture = null;
                    dlg.dispose();
                    locVoitureModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du locVoiture. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);
        
        
        locVoitureModel.add(btnsContainer);

        return locVoitureModel;
    }
    
        
    private void addTab(Tabs swipe, Image img, Label spacer, String likesStr, String commentsStr, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if(img.getHeight() < size) {
            img = img.scaledHeight(size);
        }
        Label likes = new Label(likesStr);
        Style heartStyle = new Style(likes.getUnselectedStyle());
        heartStyle.setFgColor(0xff2d55);
        FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, heartStyle);
        likes.setIcon(heartImage);
        likes.setTextPosition(RIGHT);

        Label comments = new Label(commentsStr);
        FontImage.setMaterialIcon(comments, FontImage.MATERIAL_CHAT);
        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");
        
        Container page1 = 
            LayeredLayout.encloseIn(
                image,
                overlay,
                BorderLayout.south(
                    BoxLayout.encloseY(
                            new SpanLabel(text, "LargeWhiteText"),
                            spacer
                        )
                )
            );

        swipe.addTab("", page1);
    }
    
    }
