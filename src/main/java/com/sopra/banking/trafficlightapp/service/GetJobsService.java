package com.sopra.banking.trafficlightapp.service;

import java.io.IOException;
import java.util.List;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.sopra.banking.trafficlightapp.model.JobResult;
import com.sopra.banking.trafficlightapp.model.TrafficLightConfigData.JenkinsInfo;
import com.sopra.banking.trafficlightapp.util.TrafficLightUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class GetJobsService extends ScheduledService<ObservableList<JobResult>> {
	String url;
	JenkinsInfo jenkins;
	final String username;
	final String password;
	
	public GetJobsService(String url, JenkinsInfo jenkins) {
		super();
		this.url = url;
		this.jenkins = jenkins;
		this.username = null;
		this.password = null;
	}
	
	public GetJobsService(String url, JenkinsInfo jenkins, String username, String password) {
		super();
		this.url = url;
		this.jenkins = jenkins;
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected Task<ObservableList<JobResult>> createTask() {
		Task<ObservableList<JobResult>> getJobsTask = new Task<ObservableList<JobResult>>() {
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
				
				List<String> jobs = jenkins.getJobs();
		    	List<String> views = jenkins.getViews();
		    	
		    	for(String sJob : jobs) {
		    		Job job = jenkinsServer.getJob(sJob);
					jobResults.add(getJobResult(job));
				}
		    	
		    	for(String sView : views) {
					List<Job> listViewJobs = jenkinsServer.getView(sView).getJobs();
					for(Job viewJob : listViewJobs) {
						Job job = jenkinsServer.getJob(viewJob.getName());
						JobResult jobResult = getJobResult(job);
						if(!jobResults.contains(jobResult)) {
							jobResults.add(jobResult);
							System.out.println("Adding: " + jobResult.getName());
						}
					}
				}
				
		    	return jobResults;
			}
		};
		
	    return getJobsTask;
	}
	
	public JobResult getJobResult(Job job) throws IOException {
		try {
			JobWithDetails jobWithDetails = job.details();
			BuildWithDetails buildWithDetails = jobWithDetails.getLastBuild().details();
			
			return new JobResult(job.getName(), buildWithDetails.getFullDisplayName(), buildWithDetails.getResult(), buildWithDetails.isBuilding());
		}
		catch(Exception e) {
			System.out.println("Error: " + e);
			return new JobResult(job.getName(), job.getName(), BuildResult.UNKNOWN, false);
		}
	}
};