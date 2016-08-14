package com.sopra.banking.trafficlightapp.service;

import java.util.List;

import com.sopra.banking.trafficlightapp.model.JobResult;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class AutomaticService extends ScheduledService<List<JobResult>> {

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
	protected Task<List<JobResult>> createTask() {
	    return new AutomaticTask(url, jobs, usbSwitchCmdPath);
	}
};