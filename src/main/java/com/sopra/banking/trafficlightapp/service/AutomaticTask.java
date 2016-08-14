package com.sopra.banking.trafficlightapp.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.sopra.banking.trafficlightapp.model.JobResult;

import javafx.concurrent.Task;

public class AutomaticTask extends Task<List<JobResult>> {

	final private String url;
	final private List<String> jobs;
	
	public AutomaticTask(String url, List<String> jobs) {
		super();
		this.url = url;
		this.jobs = jobs;
	}
	
	@Override
	protected List<JobResult> call() throws Exception {
		List<JobResult> jobResults = new ArrayList<JobResult>();

		//TODO add credentials possibilities
		//JenkinsServer jenkinsServer = new JenkinsServer(new URI(url), "username", "password");
		JenkinsServer jenkinsServer = new JenkinsServer(new URI(url));
		
		for(String sJob : jobs) {
			
			if (isCancelled()) {
                break;
            }
			
			JobWithDetails job = jenkinsServer.getJob(sJob).details();
			BuildResult result = job.getLastBuild().details().getResult();
			System.out.println(job.getLastBuild().details().getFullDisplayName());
			System.out.println(result.toString());
			System.out.println(job.getLastBuild().details().isBuilding());
			System.out.println(job.isInQueue());
			System.out.println("-------------------------------------------------");

			jobResults.add(new JobResult(job.getLastBuild().details().getFullDisplayName(), result, job.getLastBuild().details().isBuilding()));
		}
		
		return jobResults;
	}
}