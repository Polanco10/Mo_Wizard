package mo.wizardproject.controllers;

import com.google.inject.Inject;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import mo.core.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mo.wizardproject.model.ProjectOrganizationWizard;

public class PathController {

    private Logger log = LoggerFactory.getLogger(PathController.class);
    
    @FXML private TextField tfNameProject;
    @FXML private TextField tfFolderProject;
    @Inject ProjectOrganizationWizard model;
    @FXML public Label warning,labelPath,labelName,labelTitle;
    @FXML private Button searchButton;
    private I18n i18n;    
    
    @FXML 
    private void DirectoryChooser(ActionEvent event){
        final DirectoryChooser dirchooser = new DirectoryChooser();
        File file = dirchooser.showDialog(null);
        if (file!=null){
            tfFolderProject.setText(file.getAbsolutePath());
        }
      } 
    
    @FXML
    public void initialize() {
       i18n = new I18n(mo.wizardproject.WizardManagement.class);
       labelTitle.setText(i18n.s("PathController.Title"));
       labelPath.setText(i18n.s("PathController.Path"));
       labelName.setText(i18n.s("PathController.ProjectName"));
       searchButton.setText(i18n.s("PathController.Search"));
       tfNameProject.textProperty().bindBidirectional( model.getProject().nameProperty() );
       tfFolderProject.textProperty().bindBidirectional( model.getProject().pathProperty());
    }

  
}
