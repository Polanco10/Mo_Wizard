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
import mo.core.I18n;
import mo.wizardproject.controllers.WizardController;
import mo.wizardproject.model.ProjectOrganizationWizard;

/**
 *
 * @author Polanco
 */
public class WizardEmbedFx {
    public static JFrame frame;    
    public static int widthWizard=920;
    public static int heightWizard=620;
    public static ProjectOrganizationWizard PO;
    @Inject Injector injector;
    @Inject ProjectOrganizationWizard model;
    private static I18n inter;

    
    private static void initWizard(JFXPanel fxPanel) {
        
            final Injector injector = Guice.createInjector( new WizardModule() );
            final Parent p;		
        try {
            p = FXMLLoader.load( WizardController.class.getResource("/fxml/wizard/ui/Layout.fxml"),null, new JavaFXBuilderFactory(),(ac) -> injector.getInstance(ac));
            Scene scene = new Scene(p, widthWizard, heightWizard);
            fxPanel.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(WizardEmbedFx.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private static void initWindowEmbedded() {
        inter = new I18n(mo.wizardproject.WizardManagement.class);
        frame = new JFrame(inter.s("NewProjectWizardPanel.newProjectWizardTitle"));
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
    
    public static void runWizard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initWindowEmbedded();
            }
        });
    }
    
    
}
