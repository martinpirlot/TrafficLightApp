package com.sopra.banking.trafficlightapp.service;

import java.util.List;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.sopra.banking.trafficlightapp.model.JobResult;
import com.sopra.banking.trafficlightapp.util.TrafficLightUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class AutomaticService extends ScheduledService<ObservableList<JobResult>> {
	final String url;
	final List<JobResult> jobs;
	final String username;
	final String password;
	
	public AutomaticService(String url, List<JobResult> jobs) {
		super();
		this.url = url;
		this.jobs = jobs;
		this.username = null;
		this.password = null;
	}
	
	public AutomaticService(String url, List<JobResult> jobs, String username, String password) {
		super();
		this.url = url;
		this.jobs = jobs;
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected Task<ObservableList<JobResult>> createTask() {
		Task<ObservableList<JobResult>> automaticTask = new Task<ObservableList<JobResult>>() {
			@Override
			protected ObservableList<JobResult> call() throws Exception {
				ObservableList<JobResult> jobResults = FXCollections.observableArrayList();
				JenkinsServer jenkinsServer;
				
				if(username != null && password != null) {
					jenkinsServer = TrafficLightUtil.getJenkinsServer(url, username, password);
				}
				else {
					jenkinsServer = TrafficLightUtil.getJenkinsServer(url);
				}
				
				for(JobResult jobResult : jobs) {
					
					if (isCancelled()) {
		                break;
		            }
					
					JobWithDetails jobWithDetails = jenkinsServer.getJob(jobResult.getName());
					BuildWithDetails buildWithDetails = jobWithDetails.getLastBuild().details();

					System.out.println(jobWithDetails.getLastBuild().details().getFullDisplayName());
					System.out.println(buildWithDetails.getResult().toString());
					System.out.println(jobWithDetails.getLastBuild().details().isBuilding());
					System.out.println(jobWithDetails.isInQueue());
					System.out.println("-------------------------------------------------");
					
					jobResult.setFullName(buildWithDetails.getFullDisplayName());
					jobResult.setBuildResult(buildWithDetails.getResult());
					jobResult.setBuilding(buildWithDetails.isBuilding());
				}
				
				return jobResults;
			}
		};
		
	    return automaticTask;
	}
};