/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject;

import mo.core.filemanagement.project.Project;
import mo.core.plugin.ExtensionPoint;


/**
 *
 * @author Polanco
 */
@ExtensionPoint
public interface WizardProvider {
    void initLanguage();
    void saveProjectInAppPreferences(Project project);
    void createFolder();
    boolean updateState();
    void buildSteps();
    void next();
    void cancel();
    void finish();
    void back();
    
}
