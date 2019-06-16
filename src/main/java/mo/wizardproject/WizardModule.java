package mo.wizardproject;

import mo.wizardproject.model.ProjectOrganizationWizard;
import com.google.inject.AbstractModule;
import java.util.ArrayList;
import java.util.List;
import mo.capture.CaptureProvider;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.wizardproject.model.ActivityWizard;
import mo.wizardproject.model.ConfigurationWizard;
import mo.wizardproject.model.ParticipantWizard;
import mo.wizardproject.model.ProjectWizard;
import mo.wizardproject.model.StageModuleWizard;
import mo.wizardproject.model.StagePluginWizard;

/**
 * Created by carl on 4/30/16.
 */
public class WizardModule extends AbstractModule {

    @Override
    protected void configure() {
        
        ProjectWizard project = new ProjectWizard("","");
        List <ParticipantWizard> participants=new ArrayList<>();
        List <StageModuleWizard> stages=new ArrayList<>();
        List<StagePluginWizard> plugins = new ArrayList<>();
        addCapturePlugins(plugins); 
        StageModuleWizard StageCapture = new StageModuleWizard("Captura",plugins);
        stages.add(StageCapture);        
        List <ActivityWizard> activities = new ArrayList<>();
        ProjectOrganizationWizard model = new ProjectOrganizationWizard(project,participants,stages,activities);        
        bind(ProjectOrganizationWizard.class).toInstance(model);
        
    }
    

    public List<StagePluginWizard> addCapturePlugins(List<StagePluginWizard> plugins){
        
        for (Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.capture.CaptureProvider")) {
            List<ConfigurationWizard> configurations = new ArrayList<>();
            CaptureProvider c = (CaptureProvider) plugin.getNewInstance();
            plugins.add(new StagePluginWizard(c.getName(),configurations));
        }
    return plugins;
    }
    
}
