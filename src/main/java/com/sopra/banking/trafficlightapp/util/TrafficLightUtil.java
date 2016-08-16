package com.sopra.banking.trafficlightapp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import com.offbytwo.jenkins.JenkinsServer;

public class TrafficLightUtil {
	public static void switchLight(String path, int state, int position) {
    	callTrafficLightExecutable(path, "" + state, "-#", "" + position);
    }
	
	public static void switchAllLights(String path, int state) {
    	callTrafficLightExecutable(path, "" + state, "-#", "1,2,3");
    }
	
	public static void callTrafficLightExecutable(String... command) {
    	try {
	    	ProcessBuilder processBuilder = new ProcessBuilder(command);
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

	public static JenkinsServer getJenkinsServer(String url) throws URISyntaxException {
		return new JenkinsServer(new URI(url));
	}

	public static JenkinsServer getJenkinsServer(String url, String username, String password) throws URISyntaxException {
		return new JenkinsServer(new URI(url), username, password);
	}
}
