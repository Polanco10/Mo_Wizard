/*
 * To change this license header, choose License Headers in ProjectWizard Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.model;

import com.google.inject.Inject;
import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import mo.organization.ProjectOrganization;
import mo.organization.StageAction;
import mo.organization.StageModule;
import mo.organization.StagePlugin;

/**
 *
 * @author Polanco
 */
@XmlRootElement(name="protocol")
@XmlType(propOrder={"project","participants","stages","activities"})

public class ProjectOrganizationWizard {
    ProjectWizard project;
    List<ParticipantWizard> participants ;
    List<StageModuleWizard> stages;
    List<ActivityWizard> activities;
    StageModule MOCaptureStage ;


    ObservableList<ActivityWizard> activitiesObservable = FXCollections.observableArrayList();
    ObservableList<ParticipantWizard> participantsObservable = FXCollections.observableArrayList();


    
    @Inject
    public ProjectOrganizationWizard( ProjectWizard project, List<ParticipantWizard> participants, List<StageModuleWizard> stages, List<ActivityWizard> activities){
    this.project=project;
    this.participants=participants;
    this.stages=stages;
    this.activities=activities;
    }
    public ProjectOrganizationWizard(){
    this(null,null,null,null);
    }
   
    public void setMOCaptureStage(StageModule CaptureStage) {
        this.MOCaptureStage = CaptureStage;
    }
    @XmlTransient
    public StageModule getMOCaptureStage(){
        return MOCaptureStage;
    }
    
    
    public void addParticipant(ParticipantWizard participant){
      participants.add(participant);   
      participantsObservable.add(participant);
    }
    public List<ParticipantWizard> removeParticipant(ParticipantWizard participant){
      participants.remove(participant);
      participantsObservable.remove(participant);
      return participants;
      }
    public ObservableList<ParticipantWizard> getObservableParticipant(){        
    return participantsObservable;
    }
    public ObservableList<ParticipantWizard> setObservableParticipant(List<ParticipantWizard> participants){  
           participantsObservable.addAll(participants);
    return participantsObservable;
    }
    
    public ParticipantWizard getParticipant(int index){
     return participants.get(index);     
    }
    public ObservableList<ActivityWizard> getObservableActivity(){        
    return activitiesObservable;
    }
    
    public void addActivity(ActivityWizard activity){ 
      activitiesObservable.add(activity);        
      activities.add(activity);  
      
    }
    
    public List<ActivityWizard> removeActivity(ActivityWizard activity){
        activities.remove(activity);
        activitiesObservable.remove(activity);     
      return activities;       
    }
    @XmlElementWrapper(name="activities")
    @XmlElement(name="activity")
    public List<ActivityWizard> getActivities(){
    return activities;
    }
    public ActivityWizard getActivity(int index){
     return activities.get(index);     
    }

    @XmlElement(name="project")
    public ProjectWizard getProject() {
        return project;
    }

    public void setProject(ProjectWizard project) {
        this.project = project;
    }
    @XmlElementWrapper(name="participants")
    @XmlElement(name="participant")
    public List<ParticipantWizard> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantWizard> participants) {
        this.participants = participants;
    }
    @XmlElementWrapper(name="stages")
    @XmlElement(name="stage")
    public List<StageModuleWizard> getStages() {
        return stages;
    }

    public StageModuleWizard getCaptureStage(){
        return stages.get(0);
    }

    public void setStages(List<StageModuleWizard> stages) {
        this.stages = stages;
    }

    public void setActivities(List<ActivityWizard> activities) {
        this.activities = activities;
    }
    
    
}
