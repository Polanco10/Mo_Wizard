/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBException;
import mo.wizardproject.controllers.WizardController;
import mo.wizardproject.model.ProjectOrganizationWizard;
import mo.wizardproject.persistence.ProtocolRead;
import mo.wizardproject.sequence.ActivityAction;

/**
 *
 * @author Polanco
 */
public class ObservationsEmbedFx {
    public static JFrame frame;
    public static int widthWizard=675;
    public static int heightWizard=505;
    public static ProjectOrganizationWizard PO;
    private static String XmlFile;
    @Inject Injector injector;
    @Inject ProjectOrganizationWizard model;
    
    private static void initWizard(JFXPanel fxPanel) {
            final Parent p;		
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation( WizardController.class.getResource("/fxml/wizard/ui/Observations.fxml"));
            p= loader.load();
            Scene scene = new Scene(p, widthWizard, heightWizard);
            ActivityAction observations = loader.getController();
            observations.setProjectOrganization(PO);
            fxPanel.setScene(scene);
            
        } catch (IOException ex) {
            Logger.getLogger(ObservationsEmbedFx.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private static void initWindowEmbedded() {
        frame = new JFrame("Observations");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(widthWizard, heightWizard);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initWizard(fxPanel);
            }
       });
    }
    public static void getXmlData(String path) throws JAXBException{
        ProtocolRead pr = new ProtocolRead();            
        PO = pr.ReadXml(PO, path);
    }
    
    public static void runWizard(String path) throws JAXBException {
        getXmlData(path);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initWindowEmbedded();
            }
        });
    }
    
    
}
