package mo.wizardproject.controllers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBException;
import mo.core.I18n;
import mo.core.MultimodalObserver;
import mo.core.filemanagement.FileRegistry;
import mo.core.filemanagement.project.Project;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.core.preferences.AppPreferencesWrapper;
import mo.core.preferences.PreferencesManager;
import mo.organization.ProjectOrganization;
import mo.wizardproject.ParseModel;
import static mo.wizardproject.WizardEmbedFx.frame;
import mo.wizardproject.WizardProvider;
import mo.wizardproject.model.ProjectOrganizationWizard;
import mo.wizardproject.persistence.ProtocolWrite;

@Extension(
    xtends = {
        @Extends(extensionPointId = "mo.wizardproject.WizardProvider")
    }
)

public class WizardController implements WizardProvider{


	private final String CONTROLLER_KEY = "controller";

	@FXML VBox contentPanel;
	@FXML HBox hboxIndicators;
	@FXML Button btnNext, btnBack, btnCancel, btnFinish;
        @FXML private FontAwesomeIconView checkPath, checkParticipant, checkCapture, checkActivity;
        @FXML private JFXButton locationButton, participantsButton, captureButton,activitiesButton;
	@Inject Injector injector;
	@Inject ProjectOrganizationWizard model;
	private final List<Parent> steps = new ArrayList<>();
	private final IntegerProperty currentStep = new SimpleIntegerProperty(-1);
        PathController pathController;
        private I18n i18n;

	@FXML
	public void initialize() throws Exception {
                initLanguage();
		buildSteps();
		initButtons();
		setInitialContent();
                
	}
        @Override
        public void initLanguage(){
            i18n = new I18n(mo.wizardproject.WizardManagement.class);
            locationButton.setText(i18n.s("WizardController.Location"));
            participantsButton.setText(i18n.s("WizardController.Participants"));
            captureButton.setText(i18n.s("WizardController.Capture"));
            activitiesButton.setText(i18n.s("WizardController.Activities"));
            btnNext.setText(i18n.s("WizardController.Next"));
            btnBack.setText(i18n.s("WizardController.Back"));
            btnCancel.setText(i18n.s("WizardController.Cancel"));
            btnFinish.setText(i18n.s("WizardController.Finish"));
            
        }
	private void initButtons() {
		btnBack.disableProperty().bind( currentStep.lessThanOrEqualTo(0) );
		btnNext.disableProperty().bind( currentStep.greaterThanOrEqualTo(steps.size()-1) );
                btnFinish.visibleProperty().bind(currentStep.greaterThanOrEqualTo(steps.size()-1));

	}

	private void setInitialContent() {
		currentStep.set( 0 );  // first element
		contentPanel.getChildren().add( steps.get( currentStep.get() ));
	}


        @Override
	public void buildSteps(){

            try {
                final JavaFXBuilderFactory bf = new JavaFXBuilderFactory();
                
                final Callback<Class<?>, Object> cb = (clazz) -> injector.getInstance(clazz);
                FXMLLoader fxmlLoaderPath = new FXMLLoader( WizardController.class.getResource("/fxml/wizard/ui/Path.fxml"), null, bf, cb);
                Parent path = fxmlLoaderPath.load( );
                path.getProperties().put( CONTROLLER_KEY, fxmlLoaderPath.getController() );
                
                FXMLLoader fxmlLoaderParticipants = new FXMLLoader( WizardController.class.getResource("/fxml/wizard/ui/Participants.fxml"), null, bf, cb );
                Parent participants = fxmlLoaderParticipants.load();
                participants.getProperties().put( CONTROLLER_KEY, fxmlLoaderParticipants.getController() );
                
                FXMLLoader fxmlLoaderCapture = new FXMLLoader(WizardController.class.getResource("/fxml/wizard/ui/Capture.fxml"), null, bf, cb );
                Parent capture = fxmlLoaderCapture.load( );
                capture.getProperties().put( CONTROLLER_KEY, fxmlLoaderCapture.getController() );
                
                FXMLLoader fxmlLoaderProtocol = new FXMLLoader(WizardController.class.getResource("/fxml/wizard/ui/Activities.fxml"), null, bf, cb );
                Parent activity = fxmlLoaderProtocol.load( );
                activity.getProperties().put( CONTROLLER_KEY, fxmlLoaderProtocol.getController() );
                
                pathController = (PathController)fxmlLoaderPath.getController();
                steps.addAll( Arrays.asList(path, participants, capture,activity));
            } catch (IOException ex) {
                Logger.getLogger(WizardController.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        @Override
        public boolean updateState(){
            File folder = new File(model.getProject().getPath());
            File newFolder = new File(model.getProject().getPath()+"/"+model.getProject().getName());
            if (model.getProject().getName().isEmpty() || model.getProject().getName()==null){
                pathController.warning.setVisible(true);
                pathController.warning.setText(i18n.s("PathController.warningName"));
                return false;
            } else if (model.getProject().getPath().isEmpty() || model.getProject().getPath()==null) {
                pathController.warning.setVisible(true);
                pathController.warning.setText(i18n.s("PathController.warningLocation"));
                return false;
            } else if (!folder.isDirectory()) {
                pathController.warning.setVisible(true);
                pathController.warning.setText(i18n.s("PathController.warningLocationExistence"));
                return false;
            } else if (newFolder.exists()){
                pathController.warning.setVisible(true);
                pathController.warning.setText(i18n.s("PathController.warningLocationExists"));
                return false;
            } else {
                pathController.warning.setVisible(false);
                return true;
            }
    }

	@FXML
        @Override
	public void next() {
        	Parent p = steps.get(currentStep.get());
                
                if(updateState()){
                    if( currentStep.get() < (steps.size()-1) ) {
                            contentPanel.getChildren().remove( steps.get(currentStep.get()) );
                            currentStep.set( currentStep.get() + 1 );
                            contentPanel.getChildren().add( steps.get(currentStep.get()) );
                            checkPosition(currentStep);
                    }
                    else{
                    }
                    
                }
	}

	@FXML
        @Override
	public void back() {
		if( currentStep.get() > 0 ) {
			contentPanel.getChildren().remove( steps.get(currentStep.get()) );
			currentStep.set( currentStep.get() - 1 );
			contentPanel.getChildren().add( steps.get(currentStep.get()) );
                        checkPosition(currentStep);

		}
	}

	@FXML
        @Override
	public void finish(){
            try {
                String newPath=model.getProject().getPath()+"/"+model.getProject().getName();
                ProtocolWrite pw = new ProtocolWrite();
                createFolder();
                pw.WriteXml(model,model.getProject().getName(),newPath);
                ParseModel parse = new ParseModel();
                ProjectOrganization PO = new ProjectOrganization(newPath);
                PO.addStage(model.getMOCaptureStage());
                parse.print(model, PO);
                Platform.setImplicitExit(true);                                    
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        parse.newProject(PO);
                        frame.dispose();
                        
                    }
                });            
            } catch (IOException | JAXBException ex) {
                Logger.getLogger(WizardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @FXML
        @Override
        public void cancel(){
            Platform.setImplicitExit(true);
            SwingUtilities.invokeLater(new Runnable() {                                    
            @Override
            public void run() {
                frame.dispose();
                }
            });
        }
        
        public void checkPosition(IntegerProperty currentStep){

            switch(currentStep.getValue()){
                case 0: checkPath.setVisible(true);
                        checkParticipant.setVisible(false);
                break;
                case 1: checkParticipant.setVisible(true);
                        checkCapture.setVisible(false);
                        checkPath.setVisible(false);
                break;
                case 2: checkCapture.setVisible(true);
                        checkActivity.setVisible(false);
                        checkParticipant.setVisible(false);
                break;
                case 3: checkActivity.setVisible(true);  
                        checkCapture.setVisible(false);
                break;                

            }      
        }
    
        @Override
        public void createFolder(){
            String folder = model.getProject().getPath()+"/"+model.getProject().getName();
            File project = new File((String) folder);
            if (project.mkdir()) {
                Project p = new Project((String) folder);
                saveProjectInAppPreferences(p);

                FileRegistry.getInstance().addOpenedProject(p);   
            }
        }
        @Override
        public void saveProjectInAppPreferences(Project project) {
        PreferencesManager pm = new PreferencesManager();
        AppPreferencesWrapper app = (AppPreferencesWrapper) pm.loadOrCreate(AppPreferencesWrapper.class, new File(MultimodalObserver.APP_PREFERENCES_FILE));
        app.addOpenedProject(project.getFolder().getAbsolutePath());
        pm.save(app, new File(MultimodalObserver.APP_PREFERENCES_FILE));
        }
       
     



}
