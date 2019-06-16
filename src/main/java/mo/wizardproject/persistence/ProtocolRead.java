/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.persistence;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import mo.wizardproject.model.ProjectOrganizationWizard;


/**
 *
 * @author Polanco
 */
public class ProtocolRead {
    public  ProjectOrganizationWizard ReadXml(ProjectOrganizationWizard ProjectOrganization,String File) throws JAXBException{
       JAXBContext context= JAXBContext.newInstance(ProjectOrganizationWizard.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ProjectOrganization = (ProjectOrganizationWizard) unmarshaller.unmarshal(new File(File));
    return ProjectOrganization;
    }

}
