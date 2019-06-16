    package mo.wizardproject.controllers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import mo.capture.CaptureProvider;
import mo.core.I18n;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StageModule;
import mo.organization.StagePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mo.wizardproject.model.ConfigurationWizard;
import mo.wizardproject.model.ProjectOrganizationWizard;
import mo.wizardproject.model.StagePluginWizard;

public class CaptureController {

    private Logger log = LoggerFactory.getLogger(CaptureController.class);
    private final String CONTROLLER_KEY = "controller";


    @Inject ProjectOrganizationWizard model;
    @Inject  Injector injector;
    @FXML private ListView PluginListView;
    @FXML private ListView ConfigListView;    
    @FXML private AnchorPane anchorCapture;
    @FXML private Label labelTitle,labelConfigurations;
    ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();
    private I18n i18n; 

    
    public ObservableList<String> addObservablePlugin(){
        for(int i=0;i<model.getCaptureStage().getPlugins().size();i++){
            ObservablePlugins.add(model.getCaptureStage().getPlugins().get(i).getName());
        }
    return ObservablePlugins;    
    }
    
    public void initCaptureStage(){
        
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
         for (Plugin stagePlugin : stagePlugins) {
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            if(nodeProvider.getName().equals(model.getCaptureStage().getName())){
                model.setMOCaptureStage(nodeProvider);  //add stage     
                System.out.println(model.getMOCaptureStage());
                break;
            }       
        }
    
    }
        
    @FXML 
    private void addConfiguration(){
        ProjectOrganization PO = new ProjectOrganization("");
        String SelectedPlugin=PluginListView.getSelectionModel().getSelectedItem().toString();
        for (Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.capture.CaptureProvider")) {
            for (StagePlugin sp: model.getMOCaptureStage().getPlugins()){
                    CaptureProvider c = (CaptureProvider) plugin.getNewInstance();
                    if (c != null) {
                        if(SelectedPlugin.equals(c.getName()) && SelectedPlugin.equals(sp.getName())){
                            Configuration config = c.initNewConfiguration(PO); 
                            if (config != null) {
                                saveConfiguration(SelectedPlugin,config.getId());  
                                ConfigListView.getItems().add(sp.getName()+"("+config.getId()+")");
                                sp.getConfigurations().add(config);   

                            }
                        }              
                    }
                
            }
        }

    }
    public void saveConfiguration(String SelectedPlugin, String configId){
        for(StagePluginWizard spw: model.getCaptureStage().getPlugins()){  
            if(spw.getName().equals(SelectedPlugin)){
                    ConfigurationWizard configuration = new ConfigurationWizard(configId);
                    spw.addConfiguration(configuration);
            }                              
        }
    }
    
     @FXML 
    private void removeConfiguration(){
        String SelectedConfiguration=ConfigListView.getSelectionModel().getSelectedItem().toString();        
        String configurationString;
        String pluginName="";
        String configID="";

        for (StagePlugin sp: model.getMOCaptureStage().getPlugins()){
            System.out.println(sp.getConfigurations());
            if(sp.getConfigurations()!=null){
                for(Configuration config:sp.getConfigurations()){
                    configurationString=sp.getName()+"("+config.getId()+")";                
                    if(SelectedConfiguration.equals(configurationString)){                    
                        ConfigListView.getItems().remove(SelectedConfiguration);
                        sp.getConfigurations().remove(config);       
                        pluginName=sp.getName();
                        configID=config.getId();
                    }
                }
            }
        }
        
        for (StagePluginWizard spw: model.getCaptureStage().getPlugins()){
            if(spw.getName().equals(pluginName)){
                spw.removeConfiguration(configID);
            }
                        
        }
        
    }
    
    @FXML
    public void initialize() {
        initCaptureStage();
        i18n = new I18n(mo.wizardproject.WizardManagement.class);
        labelTitle.setText(i18n.s("CaptureController.Title"));
        labelConfigurations.setText(i18n.s("CaptureController.Configurations"));
        addObservablePlugin();
        PluginListView.setItems(ObservablePlugins);
        ConfigListView.setItems(model.getCaptureStage().getConfigPluginObservable());

    }

  

}


