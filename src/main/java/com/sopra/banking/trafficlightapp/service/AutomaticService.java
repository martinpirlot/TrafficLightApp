package com.sopra.banking.trafficlightapp.service;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.sopra.banking.trafficlightapp.model.JobResult;
import com.sopra.banking.trafficlightapp.util.TrafficLightUtil;

import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class AutomaticService extends ScheduledService<ObservableList<JobResult>> {
	final String url;
	final ObservableList<JobResult> jobs;
	final String username;
	final String password;

	public AutomaticService(String url, ObservableList<JobResult> jobs) {
		super();
		this.url = url;
		this.jobs = jobs;
		this.username = null;
		this.password = null;
	}

	public AutomaticService(String url, ObservableList<JobResult> jobs, String username, String password) {
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

					jobResult.setFullName(buildWithDetails.getFullDisplayName());
					jobResult.setBuilding(buildWithDetails.isBuilding());
					if(buildWithDetails.isBuilding()) {
						jobResult.setBuildResult(BuildResult.BUILDING);
					}
					else {
						jobResult.setBuildResult(buildWithDetails.getResult());
					}
				}

				return jobs;
			}
		};

	    return automaticTask;
	}
};