/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.model;

import java.util.List;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Polanco
 */
@XmlRootElement(name="plugin")
@XmlType(propOrder={"name","configurations"})
public class StagePluginWizard {
    public final static Logger LOGGER = Logger.getLogger(StagePluginWizard.class.getName());

  
    String name;
    List<ConfigurationWizard> configurations;
   
    public StagePluginWizard(String name,List<ConfigurationWizard> configurations){
        this.name=name;
        this.configurations=configurations; 
    }
    public StagePluginWizard(){
    this(null,null);
    }

    public void setName(String PluginName) {
        this.name = PluginName;
    }


    public void setConfigurations(List<ConfigurationWizard> configurations) {
        this.configurations = configurations;
    }

    @XmlElement(name="name")
    public String getName() {
        return name;
    }

    @XmlElementWrapper(name="configurations")
    @XmlElement(name="configuration")
    public List<ConfigurationWizard> getConfigurations() {
        return configurations;
    }
    public List<ConfigurationWizard> addConfiguration(ConfigurationWizard configuration){
        configurations.add(configuration);
        return configurations;
    
    }
    public List<ConfigurationWizard> removeConfiguration(String ConfigurationName){
        for(ConfigurationWizard configuration: configurations){
            if(configuration.getName().equals(ConfigurationName)){
                configurations.remove(configuration);
            }
        }
        return configurations;
    
    }

    

}
