package com.sopra.banking.trafficlightapp.model;

import java.util.List;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class AutomaticService extends ScheduledService<Void> {

	String usbSwitchCmdPath;
	String url;
	List<String> jobs;
	
	public AutomaticService(String url, List<String> jobs, String usbSwitchCmdPath) {
		super();
		this.url = url;
		this.jobs = jobs;
		this.usbSwitchCmdPath = usbSwitchCmdPath;
	}
	
	@Override
	protected Task<Void> createTask() {
	    return new AutomaticTask(url, jobs, usbSwitchCmdPath);
	}
};