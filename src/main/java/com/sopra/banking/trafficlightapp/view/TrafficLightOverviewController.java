package com.sopra.banking.trafficlightapp.view;

import java.util.List;

import com.sopra.banking.trafficlightapp.Main;
import com.sopra.banking.trafficlightapp.model.AutomaticService;
import com.sopra.banking.trafficlightapp.model.TrafficLightConfigData.JenkinsInfo;
import com.sopra.banking.trafficlightapp.model.TrafficLightConfigData.TrafficLightConfig;
import com.sopra.banking.trafficlightapp.model.TrafficLightUtil;

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
    }
    
    @FXML
    private void handleStartButton() {
    	if(automaticService == null) {
    		automaticService = new AutomaticService(jenkinsUrl, jenkinsJobs, usbSwitchCmdPath);
    		automaticService.setPeriod(Duration.millis(refresh));
    		automaticService.setRestartOnFailure(true);
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
