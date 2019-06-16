/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import mo.core.I18n;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.core.ui.menubar.IMenuBarItemProvider;
import static mo.core.ui.menubar.MenuItemLocations.UNDER;




/**
 *
 * @author Polanco
 */
@Extension(
        xtends = {
            @Extends(
                    extensionPointId = "mo.core.ui.menubar.IMenuBarItemProvider"
            )
        }
)
public class WizardManagement implements IMenuBarItemProvider{
    private JMenu projectMenu;
    private JMenuItem newProject, openProject, closeProject;
    private I18n inter;

    
    public WizardManagement(){
    
        inter = new I18n(mo.wizardproject.WizardManagement.class);
        
        projectMenu = new JMenu();
        projectMenu.setName("Wizard");
        projectMenu.setText(inter.s("WizardManagement.WizardMenu"));
        
        newProject = new JMenuItem();
        newProject.setName("New Project");
        newProject.setText(inter.s("WizardManagement.newProjectMenuItem"));
        
        openProject = new JMenuItem();
        openProject.setName("Start Protocol");
        openProject.setText(inter.s("WizardManagement.StartProtocolMenuItem"));

        newProject.addActionListener((ActionEvent e) -> {
            newProject();
        });

        openProject.addActionListener((ActionEvent e) -> {
            openProject();
        });
        projectMenu.add(newProject);
        projectMenu.add(openProject);
    }
    
    private void openProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        FileFilter filter = new FileNameExtensionFilter("Xml File","xml");
        chooser.addChoosableFileFilter(filter);

        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selected = chooser.getSelectedFile();
                ObservationsEmbedFx.runWizard(selected.getAbsolutePath());
            } catch (JAXBException ex) {
                Logger.getLogger(WizardManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }

    }
    
  

    private void newProject() {
        WizardEmbedFx.runWizard();

        
    }


    @Override
    public JMenuItem getItem() {
        return projectMenu;
    }

    @Override
    public int getRelativePosition() {
        return UNDER;
    }

    @Override
    public String getRelativeTo() {
        return "file";
    }
}
