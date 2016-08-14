package com.sopra.banking.trafficlightapp.model;

import java.net.URI;
import java.util.List;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.JobWithDetails;

import javafx.concurrent.Task;

public class AutomaticTask extends Task<Void> {

	final private String usbSwitchCmdPath;
	final private String url;
	final private List<String> jobs;
	
	public AutomaticTask(String url, List<String> jobs, String usbSwitchCmdPath) {
		super();
		this.url = url;
		this.jobs = jobs;
		this.usbSwitchCmdPath = usbSwitchCmdPath;
	}
	
	@Override
	protected Void call() throws Exception {
		int position = 2;
		boolean blink = false;
		
		try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(url), "x2sdemo", "x2sdemo");
			
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
				
				if(result.equals(BuildResult.UNSTABLE)) {
					position = 1;
				}
				else if(!result.equals(BuildResult.SUCCESS) && !result.equals(BuildResult.ABORTED)) {
					position = 0;
				}
				
				if(job.getLastBuild().details().isBuilding()) {
					blink = true;
				}
			}
		}
		catch(Exception e) {
			System.err.println("Error: " + e);
			position = 0;
		}
		
		TrafficLightUtil.switchLight(usbSwitchCmdPath, position, 1);
		
		return null;
	}
}