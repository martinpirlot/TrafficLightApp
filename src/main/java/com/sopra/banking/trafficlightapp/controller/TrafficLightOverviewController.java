package com.sopra.banking.trafficlightapp.controller;

import java.util.List;

import com.offbytwo.jenkins.model.BuildResult;
import com.sopra.banking.trafficlightapp.Main;
import com.sopra.banking.trafficlightapp.model.JobResult;
import com.sopra.banking.trafficlightapp.model.TrafficLightConfigData.JenkinsInfo;
import com.sopra.banking.trafficlightapp.model.TrafficLightConfigData.TrafficLightConfig;
import com.sopra.banking.trafficlightapp.service.AutomaticService;
import com.sopra.banking.trafficlightapp.util.TrafficLightUtil;

import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.util.Duration;

public class TrafficLightOverviewController {
	@FXML
	private Tab overviewTab;
	@FXML
	private Tab manualTab;
	@FXML
	private Tab automaticTab;
	
	@FXML
    private Button startButton;
	
	@FXML
    private Label urlLabel;
    @FXML
    private Label portLabel;
    @FXML
    private Label jobsLabel;
    @FXML
    private Label viewsLabel;
    
    @FXML
    private Label refreshLabel;
    @FXML
    private Label blinkLabel;
    
    private Main main;
    
    String jenkinsUrl;
    String usbSwitchCmdPath;
    List<String> jenkinsJobs;
    AutomaticService automaticService = null;
    int refresh;
    
    public TrafficLightOverviewController() {
    }
    
    @FXML
    private void initialize() {
    }
    
    public void setMain(Main main) {
        this.main = main;

        usbSwitchCmdPath = "C:/TrafficLightApp/USBswitchCmd.exe";
        TrafficLightConfig trafficLightConfig = this.main.getTrafficLightConfigData().getTrafficLightConfig();
        JenkinsInfo jenkins = trafficLightConfig.getJenkins();
        
        jenkinsUrl = jenkins.getHost() + ":" + jenkins.getPort() + jenkins.getPath();
        jenkinsJobs = TrafficLightUtil.getJobs(jenkins, jenkinsUrl);
        refresh = trafficLightConfig.getRefresh();
        
        // Add observable list data to the table
        urlLabel.setText(jenkins.getHost());
        portLabel.setText(Integer.toString(jenkins.getPort()));
        jobsLabel.setText(jenkins.getJobs().toString());
        viewsLabel.setText(jenkins.getViews().toString());
        
        refreshLabel.setText(Integer.toString(trafficLightConfig.getRefresh()) + "ms");
        if(trafficLightConfig.isBlink()) {
        	blinkLabel.setText(Integer.toString(trafficLightConfig.getBlinkDelay()) + "ms");
        }
        else {
        	blinkLabel.setText("No");
        }
        
        createAutomaticService();
    }
    
    private void createAutomaticService() {
    	automaticService = new AutomaticService(jenkinsUrl, jenkinsJobs);
		automaticService.setPeriod(Duration.millis(refresh));
		automaticService.setRestartOnFailure(true);
		
		automaticService.setOnSucceeded((WorkerStateEvent event) -> {
			final List<JobResult> jobResults = automaticService.getValue();
			
			int position = 2;
			boolean blinking = false;
			
			for(JobResult jobResult : jobResults) {
				BuildResult result = jobResult.getBuildResult();
				if(result.equals(BuildResult.UNSTABLE)) {
					position = 1;
				}
				else if(!result.equals(BuildResult.SUCCESS) && !result.equals(BuildResult.ABORTED)) {
					position = 0;
				}
				if(jobResult.isBuilding()) {
					blinking = true;
				}
			}
			
			// TODO Update UI with Job list (name + result)
			
			// TODO Put in another service (TrafficLightService)
			TrafficLightUtil.switchLight(usbSwitchCmdPath, position, 1);
		});
		
		automaticService.setOnFailed((WorkerStateEvent event) -> {
			// TODO add a label into UI with a message to explain that it failed.
			System.out.println(automaticService.getException().getMessage());
		});
    }
    
    @FXML
    private void handleStartButton() {
    	if(automaticService == null) {
    		createAutomaticService();
    	}
    	
    	if(automaticService.isRunning()) {
    		startButton.setText("Start");
    		automaticService.cancel();
    		automaticService.reset();
    	}
    	else {
    		startButton.setText("Stop");
    		automaticService.start();
    	}
    }
    
    @FXML
    private void handleGreenButton() {
    	TrafficLightUtil.switchLight(usbSwitchCmdPath, 2, 1);
    }
    
    @FXML
    private void handleOrangeButton() {
    	TrafficLightUtil.switchLight(usbSwitchCmdPath, 1, 1);
    }
    
    @FXML
    private void handleRedButton() {
    	TrafficLightUtil.switchLight(usbSwitchCmdPath, 0, 1);
    }
}
