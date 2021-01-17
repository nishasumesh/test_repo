package com.cm.conf.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.cm.conf.model.Track;

public class TrackMapper implements RowMapper<Track> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrackMapper.class);

	@Override
	public Track mapRow(ResultSet rs, int rowNum) throws SQLException {
		Track track = new Track();
		track.setConfid(rs.getString("confid"));
		track.setSectionName(rs.getString("section"));
		track.setEventName(rs.getString("eventname"));
		track.setEventStartTime(rs.getString("eventstart"));
		track.setEventEndTime(rs.getString("eventend"));
		track.setEventDuration(Double.parseDouble(rs.getString("eventduration")));
		track.setDayOfEvent(rs.getString("dayofevent"));
		track.setEventPerformer(rs.getString("eventprformer"));

		return track;
	}
}