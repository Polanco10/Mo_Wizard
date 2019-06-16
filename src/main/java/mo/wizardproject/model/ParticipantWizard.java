/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.model;


import java.util.Calendar;
import java.util.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



/**
 *
 * @author Polanco
 */
@XmlRootElement(name="participant")
@XmlType(propOrder={"id","name","date","note","observed"})
public class ParticipantWizard {
    private StringProperty id = new SimpleStringProperty();      
    private StringProperty name = new SimpleStringProperty();     
    private ObjectProperty<Date> date = new SimpleObjectProperty();    
    private StringProperty note = new SimpleStringProperty();
    private BooleanProperty observed= new SimpleBooleanProperty();
         

    
    public ParticipantWizard() {
        this(null,"","");
    }
    
  public ParticipantWizard(String id, String name, String note) {
        this.id.set(id);
        this.name.set(name);
        this.note.set(note);
        
        Date today = Calendar.getInstance().getTime();
        this.date.set(today);
        this.observed.set(false);

    }    
    
    
  
    public StringProperty idProperty() {
        return id;
    }
    @XmlElement(name="id")
    public String getId(){
        return id.get();
    }
    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty nameProperty() {
        return name;
    }
    @XmlElement(name="name")
    public String getName(){
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }

    public ObjectProperty dateProperty() {
        return date;
    }
    @XmlElement(name="date")
    public Date getDate(){
        return date.get();
    }
    public void setDate(Date date) {
        this.date.set(date);
    }

    public StringProperty noteProperty() {
        return note;
    }
    @XmlElement(name="note")
    public String getNote(){
        return note.get();
    }
    public void setNote(String note) {
        this.note.set(note);
    }
    public BooleanProperty observedProperty() {
        return observed;
    }
    @XmlElement(name="observed")
    public Boolean getObserved(){
        return observed.get();
    }
    public void setObserved(Boolean observed) {
        this.observed.set(observed);
    }

  




    
}
