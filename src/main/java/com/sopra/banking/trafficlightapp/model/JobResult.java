package com.sopra.banking.trafficlightapp.model;

import com.offbytwo.jenkins.model.BuildResult;

public class JobResult {
	private String jobName;
	private BuildResult buildResult;
	private boolean building;

	public JobResult(String jobName, BuildResult buildResult, boolean building) {
		super();
		this.jobName = jobName;
		this.buildResult = buildResult;
		this.building = building;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public BuildResult getBuildResult() {
		return buildResult;
	}

	public void setBuildResult(BuildResult buildResult) {
		this.buildResult = buildResult;
	}

	public boolean isBuilding() {
		return building;
	}

	public void setBuilding(boolean building) {
		this.building = building;
	}
}
