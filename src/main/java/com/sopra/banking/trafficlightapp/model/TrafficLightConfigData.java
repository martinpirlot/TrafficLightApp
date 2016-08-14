package com.sopra.banking.trafficlightapp.model;

import java.util.List;

public class TrafficLightConfigData {
	
	private TrafficLightConfig trafficLightConfig;
	
	public TrafficLightConfigData(TrafficLightConfig trafficLightConfig) {
		super();
		this.trafficLightConfig = trafficLightConfig;
	}

	public static class TrafficLightConfig {
		private int refresh;
		private boolean blink;
		private int blinkDelay;
		private String clewareExecutable;
		private JenkinsInfo jenkins;
		
		public TrafficLightConfig(int refresh, boolean blink, int blinkDelay, String clewareExecutable,
				JenkinsInfo jenkins) {
			super();
			this.refresh = refresh;
			this.blink = blink;
			this.blinkDelay = blinkDelay;
			this.clewareExecutable = clewareExecutable;
			this.jenkins = jenkins;
		}

		public int getRefresh() {
			return refresh;
		}
		
		public void setRefresh(int refresh) {
			this.refresh = refresh;
		}
		
		public boolean isBlink() {
			return blink;
		}
		
		public void setBlink(boolean blink) {
			this.blink = blink;
		}
		
		public int getBlinkDelay() {
			return blinkDelay;
		}
		
		public void setBlinkDelay(int blinkDelay) {
			this.blinkDelay = blinkDelay;
		}
		
		public String getClewareExecutable() {
			return clewareExecutable;
		}
		
		public void setClewareExecutable(String clewareExecutable) {
			this.clewareExecutable = clewareExecutable;
		}
		
		public JenkinsInfo getJenkins() {
			return jenkins;
		}
		
		public void setJenkins(JenkinsInfo jenkins) {
			this.jenkins = jenkins;
		}
	}
	
	public static class JenkinsInfo {
		private String host;
		private int port;
		private String path;
		private List<String> jobs;
		private List<String> views;
		
		public JenkinsInfo(String host, int port, String path, List<String> jobs, List<String> views) {
			super();
			this.host = host;
			this.port = port;
			this.path = path;
			this.jobs = jobs;
			this.views = views;
		}

		public String getHost() {
			return host;
		}
		
		public void setHost(String host) {
			this.host = host;
		}
		
		public int getPort() {
			return port;
		}
		
		public void setPort(int port) {
			this.port = port;
		}
		
		public String getPath() {
			return path;
		}
		
		public void setPath(String path) {
			this.path = path;
		}
		
		public List<String> getJobs() {
			return jobs;
		}
		
		public void setJobs(List<String> jobs) {
			this.jobs = jobs;
		}
		
		public List<String> getViews() {
			return views;
		}
		
		public void setViews(List<String> views) {
			this.views = views;
		}
	}

	public TrafficLightConfig getTrafficLightConfig() {
		return trafficLightConfig;
	}

	public void setTrafficLightConfig(TrafficLightConfig trafficLightConfig) {
		this.trafficLightConfig = trafficLightConfig;
	}
}
