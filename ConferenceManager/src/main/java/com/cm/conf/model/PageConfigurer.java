package com.cm.conf.model;

import java.util.List;

import org.springframework.stereotype.Component;

public class PageConfigurer {

	public List<Track> getTrackList() {
		return trackList;
	}

	public void setTrackList(List<Track> trackList) {
		this.trackList = trackList;
	}

	public String getConfDays() {
		return confDays;
	}

	public void setConfDays(String confDays) {
		this.confDays = confDays;

	}

	private String confDays;

	private List<Track> trackList;

}
