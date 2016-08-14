package com.sopra.banking.trafficlightapp.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import com.sopra.banking.trafficlightapp.model.TrafficLightConfigData.JenkinsInfo;

public class TrafficLightUtil {
	public static void switchLight(String path, int position, int state) {
    	try {
	    	ProcessBuilder processBuilder = new ProcessBuilder(path, "" + state, "-#", "" + position);
	    	processBuilder.redirectErrorStream(true);
	    	
	    	Process process = processBuilder.start();
	    	
	    	BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	String line;
	
	    	while ((line = br.readLine()) != null) {
	    		System.out.println(line);
	    	}
    	} catch (Exception e) {
    		System.err.println("Error: " + e);
    	}
    }
	
	public static List<String> getJobs(JenkinsInfo jenkins, String jenkinsUrl) {
    	List<String> jobs = jenkins.getJobs();
    	
    	List<String> views = jenkins.getViews();
    	try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), "x2sdemo", "x2sdemo");
			
			for(String sView : views) {
				List<Job> listTmpJobs = jenkinsServer.getView(sView).getJobs();
				for(Job tmpJob : listTmpJobs) {
					String sJob = tmpJob.getName();
					if(!jobs.contains(sJob)) {
						jobs.add(sJob);
					}
				}
				
			}
		}
		catch(Exception e) {
			System.out.println("Error: " + e);
		}
    	
    	return jobs;
    }
}
