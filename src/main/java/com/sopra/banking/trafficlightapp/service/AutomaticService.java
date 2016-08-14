package com.sopra.banking.trafficlightapp.service;

import java.util.List;

import com.sopra.banking.trafficlightapp.model.JobResult;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class AutomaticService extends ScheduledService<List<JobResult>> {

	String url;
	List<String> jobs;
	
	public AutomaticService(String url, List<String> jobs) {
		super();
		this.url = url;
		this.jobs = jobs;
	}
	
	@Override
	protected Task<List<JobResult>> createTask() {
	    return new AutomaticTask(url, jobs);
	}
};