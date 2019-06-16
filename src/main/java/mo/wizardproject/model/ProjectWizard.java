/*
 * To change this license header, choose License Headers in ProjectWizard Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.model;

import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Polanco
 */    
@XmlRootElement(name="project")
@XmlType(propOrder={"name","path"})
public class ProjectWizard {
    private static final Logger LOGGER = Logger.getLogger(ProjectWizard.class.getName());
    private StringProperty name = new SimpleStringProperty();
    private StringProperty path = new SimpleStringProperty();

    

    public ProjectWizard(String name,String path){
        this.name.set(name);
        this.path.set(path);
    }
    public ProjectWizard(){
    this(null,null);
    }
    

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String nameProject) {
        this.name.set(nameProject);
    }
    @XmlElement(name="name")
    public String getName(){
        return name.get();
    
    }
    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }
    @XmlElement(name="path")
    public String getPath(){
        return path.get();
    }

}
