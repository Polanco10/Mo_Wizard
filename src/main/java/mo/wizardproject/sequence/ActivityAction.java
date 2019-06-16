/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.sequence;


import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import mo.wizardproject.model.ParticipantWizard;
import mo.wizardproject.model.ProjectOrganizationWizard;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBException;
import mo.wizardproject.ObservationsEmbedFx;
import mo.wizardproject.model.ActivityWizard;
import mo.wizardproject.persistence.ProtocolWrite;
/**
 *
 * @author Polanco
 */
public class ActivityAction  {
    ProjectOrganizationWizard model;
        ProjectOrganizationWizard PO;

    @FXML private TableView<ParticipantWizard>participants_table;
    @FXML private TableColumn<ParticipantWizard,String>id_column;    
    @FXML private TableColumn<ParticipantWizard,String>name_column; 
    @FXML private TableColumn<ParticipantWizard,Boolean>observed_column; 
    @FXML private AnchorPane anchorObservations;
    @FXML private Label nameProject,activities,durationActivities,totalParticipants,participantsObserved;

    int millis=1000;//equals one second
    
    @FXML
    public void runActivity() throws InterruptedException{
    ParticipantWizard selected = participants_table.getSelectionModel().getSelectedItem();
    if(!wasObserved(selected)){
        ActivitiesProcess();
        selected.setObserved(Boolean.TRUE);
        setLabels();
        participants_table.refresh();
        showingWindow();
    }

    
    }
    public void showMessageDialog(String TypeMessage,String ActivityName,String MessageContent){

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(TypeMessage);
        alert.setHeaderText(ActivityName);
        alert.setContentText(MessageContent); 
        alert.showAndWait();
    
    
    }
   public void hidingWindow(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ObservationsEmbedFx.frame.setVisible(false);
            }
        });
   }
       public void showingWindow(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ObservationsEmbedFx.frame.setVisible(true);
            }
        });
   }
    public void ActivitiesProcess() throws InterruptedException{
        for(ActivityWizard activity:model.getActivities()){
 
                showMessageDialog("Mensaje de inicio",activity.getName(),activity.getStartMessage());
                execute(activity.getPath());
                hidingWindow();
               sleepAtLeast(activity.getTimeExecution()*millis);
                showMessageDialog("Mensaje de fin",activity.getName(),activity.getEndMessage()); 
                if(activity.getCloseActivity()){
                    killprocess(activity.getProcessName());
                }
                
                   
        }        
    }

    
    public void sleepAtLeast(long millis) throws InterruptedException {
        long t0 = System.currentTimeMillis();
        long millisLeft = millis;
         while (millisLeft > 0) {
             
        Thread.sleep(millisLeft);
        long t1 = System.currentTimeMillis();
        millisLeft = millis - (t1 - t0);
  }
}

    public boolean wasObserved(ParticipantWizard participant){
        return participant.getObserved();
    }
    public void execute(String path){
        String os = System.getProperty("os.name").toLowerCase();
        Runtime app = Runtime.getRuntime();

    try{
        if (os.indexOf( "win" ) >= 0) {
        app.exec("cmd /c "+path);        
        }else if (os.indexOf( "mac" ) >= 0) {
          app.exec(path);

        } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {
        app.exec(new String[] {"xdg-open", path });
        }

    }catch(Exception e){
        System.out.println(e);}
    }
    
    public void killprocess(String path){
        String os = System.getProperty("os.name").toLowerCase();
        Runtime app = Runtime.getRuntime();

    try{
        if (os.indexOf( "win" ) >= 0) {
            app.exec("tskill " +path);        
        }else if (os.indexOf( "mac" ) >= 0) {
          app.exec("killall " +path);

        } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {
        app.exec("killall " +path);
        }

    }catch(Exception e){
        System.out.println(e);}
    
    
    }
    
    @FXML
    public void closeDialog() throws IOException, JAXBException{
        SwingUtilities.invokeLater(new Runnable() {                                    
        @Override
        public void run() {
            ObservationsEmbedFx.frame.dispose();
            }
        });    
        ProtocolWrite pw = new ProtocolWrite();
        String newPath=model.getProject().getPath()+"/"+model.getProject().getName();
        pw.WriteXml(model,model.getProject().getName(),newPath);
    }
    public void setLabels(){
        String nameActivities="";
        int duration=0;
        int countObserved=0;
        for(int i=0;i<model.getActivities().size();i++){
            duration+=model.getActivities().get(i).getTimeExecution();
            if(i!=model.getActivities().size()-1){
            nameActivities+=model.getActivities().get(i).getName()+",";
            }
            else{
            nameActivities+=model.getActivities().get(i).getName()+".";
            }            
        }
        for(int i=0;i<model.getParticipants().size();i++){
            if(model.getParticipants().get(i).getObserved()){
            countObserved+=1;
            }            
        }
        
        
    nameProject.setText(model.getProject().getName());
    activities.setText(nameActivities);
    durationActivities.setText(Integer.toString(duration)+"(s)");
    totalParticipants.setText(Integer.toHexString(model.getParticipants().size()));
    participantsObserved.setText(Integer.toString(countObserved));
    }
        
    public void setProjectOrganization(ProjectOrganizationWizard ProjectOrganization){
        this.model = ProjectOrganization;
        model.setObservableParticipant(model.getParticipants());
        participants_table.getItems().addAll(model.getObservableParticipant());
        setLabels();

        
    }
    @FXML
    public void initialize() {

        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));    
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        observed_column.setCellValueFactory(new PropertyValueFactory<>("observed"));
        observed_column.setCellFactory(column -> new CheckBoxTableCell());         
    }
}
