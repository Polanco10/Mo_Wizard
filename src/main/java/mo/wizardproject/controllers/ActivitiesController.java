package mo.wizardproject.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import mo.core.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mo.wizardproject.model.ActivityWizard;
import mo.wizardproject.model.ProjectOrganizationWizard;

public class ActivitiesController  {

    private Logger log = LoggerFactory.getLogger(ActivitiesController.class);
    
    @FXML private AnchorPane anchorActivity;
    @FXML private TableView<ActivityWizard> activityTable;
    @FXML private TableColumn<ActivityWizard,String> orderColumn;
    @FXML private TableColumn<ActivityWizard, String> activityNameColumn;
    @FXML private Label orderLabel,activityTime,activityNameLabel,pathLabel,startMessageLabel,endMessageLabel,closeWhenFinishedLabel,processNameLabel;
    @FXML private Label labelTitle,order,activity,path,startMessage,endMessage, executionTime,closeWhenFinished,processName;//Static Labels
    @FXML private JFXButton addActivityButton,editActivityButton,removeActivityButton;
    @Inject  ProjectOrganizationWizard model;
    @Inject  Injector injector;
    private I18n i18n;  


    @FXML 
    private void handleEditActivity(ActionEvent event) {         
       ActivityWizard selectedActivity = activityTable.getSelectionModel().getSelectedItem();
        if (selectedActivity != null) {            
           boolean okClicked = showActivityEditDialog(selectedActivity ,"Editar actividad");
           if(okClicked){
               showActivityDetails(selectedActivity);
               activityTable.refresh();
           }
                            
        } else {
                System.out.println("dialog error");
        }
    }
    @FXML
    private void handleDeleteActivity() {
        ActivityWizard selectedIndex = activityTable.getSelectionModel().getSelectedItem();       
        activityTable.getItems().remove(selectedIndex);
        model.removeActivity(selectedIndex);
        setPositions(model.getActivities());
        
    }
    @FXML 
    private void handleNewActivity(ActionEvent event) {               
        int order = model.getActivities().size()+1;
        ActivityWizard activity = new ActivityWizard("","",order,1);        
        boolean okClicked = showActivityEditDialog(activity,"Crear actividad");
        if (okClicked){
            model.addActivity(activity);
            activityTable.getItems().clear();
            activityTable.getItems().addAll(model.getObservableActivity());
        }
    }
    
    public void showActivityDetails(ActivityWizard activity){
    if (activity != null) {
        orderLabel.setText(Integer.toString(activity.getOrder()));
        activityNameLabel.setText(activity.getName());
        pathLabel.setText(activity.getPath());
        startMessageLabel.setText(activity.getStartMessage());
        endMessageLabel.setText(activity.getEndMessage());
        activityTime.setText(Integer.toString(activity.getTimeExecution()));
        closeWhenFinishedLabel.setText(String.valueOf(activity.getCloseActivity()));
        processNameLabel.setText(activity.getProcessName());

        
    } else {
        orderLabel.setText("");
        activityNameLabel.setText("");
        pathLabel.setText("");
        startMessageLabel.setText("");
        endMessageLabel.setText("");
        activityTime.setText("");
        closeWhenFinishedLabel.setText("");
        processNameLabel.setText("");
    }
}
    public void setPositions(List<ActivityWizard> activities){
     int position=1;
    for(ActivityWizard activity:activities){
        activity.setOrder(position);
        position++;
    }
    
    }
    public boolean showActivityEditDialog(ActivityWizard activity,String Title){
        try{
   
        
        Window primaryStage =  anchorActivity.getScene().getWindow(); 
         FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WizardController.class.getResource("/fxml/wizard/ui/EditActivityDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle(Title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        EditActivityDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setActivity(activity);

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
        labelTitle.setText(i18n.s("ActivitiesController.Title"));
        order.setText(i18n.s("ActivitiesController.Order"));
        activity.setText(i18n.s("ActivitiesController.Activity"));
        path.setText(i18n.s("ActivitiesController.Path"));
        startMessage.setText(i18n.s("ActivitiesController.StartMessage"));
        endMessage.setText(i18n.s("ActivitiesController.EndMessage"));
        executionTime.setText(i18n.s("ActivitiesController.ExecutionTime"));
        addActivityButton.setText(i18n.s("ActivitiesController.Activity"));
        editActivityButton.setText(i18n.s("ActivitiesController.Activity"));
        removeActivityButton.setText(i18n.s("ActivitiesController.Activity"));
        orderColumn.setText(i18n.s("ActivitiesController.Order"));
        activityNameColumn.setText(i18n.s("ActivitiesController.ActivityColumn"));
        closeWhenFinished.setText(i18n.s("ActivitiesController.CloseWhenFinished"));
        processName.setText(i18n.s("ActivitiesController.ProcessName"));
        
        
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("order"));
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        showActivityDetails(null);
        activityTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showActivityDetails(newValue));
        activityTable.getItems().addAll(model.getObservableActivity());

    }
}

