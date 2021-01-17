package com.cm.conf.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class Track 
{


	public String getConfid() {
		return confid;
	}

	public void setConfid(String confid) {
		this.confid = confid;
	}

	public String getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(String eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public String getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(String eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public String getDayOfEvent() {
		return dayOfEvent;
	}

	public void setDayOfEvent(String dayOfEvent) {
		this.dayOfEvent = dayOfEvent;
	}

	public double getEventDuration() {
		return eventDuration;
	}

	public void setEventDuration(double eventDuration) {
		this.eventDuration = eventDuration;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}



	public String getEventPerformer() {
		return eventPerformer;
	}

	public void setEventPerformer(String eventPerformer) {
		this.eventPerformer = eventPerformer;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	private double eventDuration;
	
	private String eventName;
	
	private String eventStartTime;
	
	private String eventEndTime;
	
	private String eventPerformer;
	
	private String sectionName;
	
	private String dayOfEvent;
	private String confid;

}
