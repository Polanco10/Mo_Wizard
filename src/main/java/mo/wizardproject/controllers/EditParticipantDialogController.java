/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.controllers;


import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.wizardproject.model.ParticipantWizard;

/**
 *
 * @author Polanco
 */
public class EditParticipantDialogController implements Initializable{
    @FXML private TextField id_field;
    @FXML private TextField name_field;
    @FXML private DatePicker date;
    @FXML private TextArea note_field;
    @FXML private Label warning;

    
    private ParticipantWizard participant;
    private List<ParticipantWizard> participants;        
    private boolean okClicked = false;
    private Stage dialogStage;
    
 /* public static final LocalDate NOW_LOCAL_DATE (){
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date , formatter);
        return localDate;
    }*/

    public boolean isInputValid(List<ParticipantWizard> participants){
        this.participants=participants;
        if(participants!=null){
        for(int i=0; i<participants.size();i++){
            if(participants.get(i).getId().equals(id_field.getText()) && participant!=participants.get(i)){
            warning.setText("Ese ID ya está en uso");
            warning.setVisible(true);
                return false;
            }    
        }
        }
        return true;
    }
    public boolean isNotNull(){
        if(id_field.getText()==null){
            warning.setText("Complete los campos obligatorios (*)");
            warning.setVisible(true);
        return false;
        }
        return true;
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public boolean isOkClicked() {
        return okClicked;
    }
    public void setParticipant(ParticipantWizard participant){
        this.participant = participant;
        Date DateParticipant = participant.getDate();
        LocalDate localDate = DateParticipant.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        id_field.setText(participant.getId());
        name_field.setText(participant.getName());
        date.setValue(localDate);
        note_field.setText(participant.getNote());
        }
    


    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    @FXML
    private void handleOk() {
        if (isInputValid(participants)&& isNotNull()) {
           LocalDate localDate = date.getValue();
           Date PersonDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            participant.setId(id_field.getText()); 
            participant.setName(name_field.getText());
            participant.setDate(PersonDate);
            participant.setNote(note_field.getText());
            okClicked = true;
            dialogStage.close();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        warning.setVisible(false);
       
    }    
    
}
