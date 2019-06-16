/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.controllers;




import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mo.wizardproject.model.ActivityWizard;


public class EditActivityDialogController {

    @FXML private TextField startMessageField, endMessageField,pathField,nameField,processNameField;
    @FXML private Label warning,labelOrder,processNameLabel;
    @FXML private Spinner<Integer> SpinnerTime;    
    @FXML private RadioButton closeFalse,closeTrue;
    SpinnerValueFactory<Integer> timeValueFactory;


    private ActivityWizard activity;
        
    private boolean okClicked = false;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public boolean isOkClicked() {
        return okClicked;
    }
    public void setActivity(ActivityWizard activity){
        this.activity = activity;        
        timeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3600,activity.getTimeExecution());
        SpinnerTime.setValueFactory(timeValueFactory);
        SpinnerTime.setEditable(true);
        TextFormatter formatter = new TextFormatter(timeValueFactory.getConverter(), timeValueFactory.getValue());
        SpinnerTime.getEditor().setTextFormatter(formatter);
        timeValueFactory.valueProperty().bindBidirectional(formatter.valueProperty());  
        labelOrder.setText(Integer.toString(activity.getOrder()));
        nameField.setText(activity.getName());
        startMessageField.setText(activity.getStartMessage());
        endMessageField.setText(activity.getEndMessage());
        pathField.setText(activity.getPath());
        processNameField.setText(activity.getProcessName());
        if(activity.getCloseActivity()){
        closeTrue.setSelected(true);
        processNameField.setVisible(true);
        processNameLabel.setVisible(true);
        }else{
        closeFalse.setSelected(true);
        }

    
    }
    private boolean isInputValid() {
        if(nameField.getText().isEmpty()|| pathField.getText().isEmpty()){
            warning.setVisible(true);
            return false;
        }
        
        return true;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML 
    private void setVisibleProcessName(){
        if(closeTrue.isSelected()){
            processNameField.setVisible(true);
            processNameLabel.setVisible(true);
            
        }
        else if(closeFalse.isSelected()){
            processNameField.setVisible(false);
            processNameLabel.setVisible(false);
        }
        
    }
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            activity.setName(nameField.getText());
            activity.setPath(pathField.getText());
            activity.setProcessName(processNameField.getText());
            activity.setTimeExecution(SpinnerTime.getValue());
            if (startMessageField.getText()==null) {
                activity.setStartMessage("");
            }else{activity.setStartMessage(startMessageField.getText());}
            if (endMessageField.getText()==null) {            
                activity.setEndMessage("");
            }else{
                activity.setEndMessage(endMessageField.getText());
            }
            if(closeTrue.isSelected()){
                activity.setCloseActivity(true);
            }

            okClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML 
    private void FileChooser(ActionEvent event){
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Actividades externas a MO");
        File file = filechooser.showOpenDialog(null);
        if(file!=null){
            pathField.setText(file.getAbsolutePath());
        }  
    } 
    
    @FXML
    private void initialize() {
        warning.setVisible(false);
        
    }


}