package mo.wizardproject.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mo.wizardproject.model.ParticipantWizard;
import mo.wizardproject.model.ProjectOrganizationWizard;
import javafx.stage.Window;
import mo.core.I18n;


public class ParticipantsController {

    private Logger log = LoggerFactory.getLogger(ParticipantsController.class);
    private final String CONTROLLER_KEY = "controller";
    
    @Inject  ProjectOrganizationWizard model;
    @Inject  Injector injector;
    @FXML private AnchorPane anchorParticipant;
    @FXML private TableView <ParticipantWizard>participants_table;
    @FXML private TableColumn <ParticipantWizard,String>id_column;    
    @FXML private TableColumn <ParticipantWizard,String>name_column;    
    @FXML private TableColumn <ParticipantWizard,Date>date_column;
    @FXML private TableColumn<ParticipantWizard, String> note_column;
    @FXML private Label labelTitle;
    @FXML private JFXButton addParticipantButton,removeParticipantButton,editParticipantButton;
    private I18n i18n; 
    
   
        @FXML 
    private void handleEditParticipant(ActionEvent event) {         
       ParticipantWizard selectedParticipant = participants_table.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {            
           boolean okClicked = showParticipantEditDialog(selectedParticipant ,"Editar Participante");
           if(okClicked){
               participants_table.refresh();
           }
                            
        } else {
                System.out.println("dialog error");
        }
    }
    @FXML
    private void handleDeleteParticipant() {
        ParticipantWizard selectedIndex = participants_table.getSelectionModel().getSelectedItem();
        participants_table.getItems().remove(selectedIndex);
        model.removeParticipant(selectedIndex);
        
    }
    @FXML 
    private void handleNewParticipant(ActionEvent event) {               
        int order = model.getActivities().size()+1;
        ParticipantWizard participant = new ParticipantWizard();        
        boolean okClicked = showParticipantEditDialog(participant,"Crear participante");
        if (okClicked){
            model.addParticipant(participant);
            participants_table.getItems().clear();
            participants_table.getItems().addAll(model.getObservableParticipant());
        }
    }
    
  
    public boolean showParticipantEditDialog(ParticipantWizard participant,String Title){
        try{
   
        
        Window primaryStage = anchorParticipant.getScene().getWindow(); 
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WizardController.class.getResource("/fxml/wizard/ui/EditParticipantDialog.fxml"));
        BorderPane page = (BorderPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle(Title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        EditParticipantDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setParticipant(participant);
        controller.isInputValid(model.getParticipants());

        dialogStage.showAndWait();

        return controller.isOkClicked();


    } catch (IOException e) {
        e.printStackTrace();
        return false;
        }
    }
    
    
    
    
    
    
    
    
    
    @FXML
    public void initialize() {
        i18n = new I18n(mo.wizardproject.WizardManagement.class);
        labelTitle.setText(i18n.s("ParticipantsController.Title"));
        addParticipantButton.setText(i18n.s("ParticipantsController.Button"));
        removeParticipantButton.setText(i18n.s("ParticipantsController.Button"));
        editParticipantButton.setText(i18n.s("ParticipantsController.Button"));
        name_column.setText(i18n.s("ParticipantsController.NameColumn"));
        note_column.setText(i18n.s("ParticipantsController.NoteColumn"));
        date_column.setText(i18n.s("ParticipantsController.DateColumn"));
        
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));    
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));      
        note_column.setCellValueFactory(new PropertyValueFactory<>("note"));
        date_column.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        participants_table.getItems().addAll(model.getObservableParticipant());
    }
}
