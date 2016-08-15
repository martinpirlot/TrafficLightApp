package com.sopra.banking.trafficlightapp.model;

import com.offbytwo.jenkins.model.BuildResult;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JobResult {
	private StringProperty name;
	private StringProperty fullName;
	private ObjectProperty<BuildResult> buildResult;
	private BooleanProperty building;

	public JobResult(String name, String fullName, BuildResult buildResult, boolean building) {
		super();
		this.name = new SimpleStringProperty(name);
		this.fullName = new SimpleStringProperty(fullName);
		this.buildResult = new SimpleObjectProperty<BuildResult>(buildResult);
		this.building = new SimpleBooleanProperty(building);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getFullName() {
		return fullName.get();
	}

	public void setFullName(String fullName) {
		this.fullName.set(fullName);
	}

	public BuildResult getBuildResult() {
		return buildResult.get();
	}

	public void setBuildResult(BuildResult buildResult) {
		this.buildResult.set(buildResult);
	}

	public boolean isBuilding() {
		return building.get();
	}

	public void setBuilding(boolean building) {
		this.building.set(building);
	}
	
	public StringProperty nameProperty() {
        return name;
    }
	
	public StringProperty fullNameProperty() {
        return fullName;
    }
	
	public ObjectProperty<BuildResult> buildResultProperty() {
        return buildResult;
    }
	
	public BooleanProperty buildingProperty() {
        return building;
    }
	
	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof JobResult)) {
	        return false;
	    }

	    JobResult otherJobResult = (JobResult) other;

	    return this.name.get().equals(otherJobResult.name.get());
	}
	
	@Override
	public int hashCode() {
	    int hashCode = 1;

	    hashCode = hashCode * 37 + this.name.hashCode();

	    return hashCode;
	}
}
