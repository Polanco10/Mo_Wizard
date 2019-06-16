/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject;

import java.io.File;
import mo.core.I18n;
import mo.core.ui.dockables.DockablesRegistry;
import mo.organization.Participant;
import mo.organization.ProjectOrganization;
import mo.organization.visualization.tree.OrganizationDockable;
import mo.organization.visualization.tree.ProjectOrganizationPlugin;
import mo.wizardproject.model.ParticipantWizard;
import mo.wizardproject.model.ProjectOrganizationWizard;

/**
 *
 * @author Polanco
 */
public class ParseModel {
    private I18n i18n;
    public ParseModel(){
            i18n = new I18n(ProjectOrganizationPlugin.class);

    }

    public ProjectOrganization print(ProjectOrganizationWizard model,ProjectOrganization PO){
         for(ParticipantWizard participantW: model.getParticipants()){
                Participant participant = new Participant();
                participant.id=participantW.getId();
                participant.folder="participant-"+participantW.getId();
                participant.date=participantW.getDate();
                participant.name=participantW.getName();
                participant.notes=participantW.getNote();
                PO.addParticipant(participant);                
            }
    return PO;    
    }
    

   public void newProject(ProjectOrganization PO){                
        File treeOrgFile = new File(PO.getLocation(), "organization-visualization-tree.xml");
            if (!treeOrgFile.exists()) {                
                OrganizationDockable dock = new OrganizationDockable(PO);
                dock.setTitleText(PO.getLocation().getName() + i18n.s("ProjectOrganizationPlugin.titleSufix"));
                dock.setProjectPath(PO.getLocation().getAbsolutePath());
                DockablesRegistry.getInstance().addDockableInProjectGroup(PO.getLocation().getAbsolutePath(), dock);
                PO.store();
            }
    }
    
}
