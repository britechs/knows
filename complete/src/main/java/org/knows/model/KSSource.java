package org.knows.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

public class KSSource implements Serializable {
	@Size(min=2, max=30)
	private String location;
	private String name;
	private boolean isPersonal=false;
	private Date sourceDt;
	private int version=0;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isPersonal() {
		return isPersonal;
	}
	public void setPersonal(boolean isPersonal) {
		this.isPersonal = isPersonal;
	}
	public Date getSourceDt() {
		return sourceDt;
	}
	public void setSourceDt(Date sourceDt) {
		this.sourceDt = sourceDt;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
}
