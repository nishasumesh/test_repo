package com.cm.conf.service;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.cm.conf.mapper.LoginMapper;
import com.cm.conf.mapper.TrackMapper;
import com.cm.conf.model.Login;
import com.cm.conf.model.Track;

@Service
public class ConfService {
	@Autowired
	private JdbcTemplate postgresTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfService.class);

	private static final String CHECKLOGIN = "SELECT user_id, name, username, password"
			+ "	FROM movie.user where username=? and password=?";

	private static final String getDayEvents = "SELECT confid, section, eventname, eventstart, eventend, eventduration, dayofevent, eventprformer " + 
			"	FROM movie.conference where dayofevent=?";

	private static final String saveDayEvents = "INSERT INTO movie.conference (confid, section, eventname, eventstart, eventend, eventduration, dayofevent, eventprformer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public List<Track> getAllEvents(String day) {

		return postgresTemplate.query(getDayEvents, new TrackMapper(),day);
	}
	
	public int saveTrack(Track trackNew) {

		//Check for time availability
		//duration calculator
		
		return postgresTemplate.update(saveDayEvents, UUID.randomUUID().toString(),trackNew.getSectionName(),trackNew.getEventName(),trackNew.getEventStartTime(),trackNew.getEventEndTime(),""+trackNew.getEventDuration(),trackNew.getDayOfEvent(),trackNew.getEventPerformer());
	
	}

	public Login checkUser(String username, String password) {
		LOGGER.info("postgresTemplate{}", postgresTemplate);
		LOGGER.info("username{}", username);
		LOGGER.info("password{}", password);
		Login loginObj = null;
		try {
			loginObj = postgresTemplate.queryForObject(CHECKLOGIN, new LoginMapper(), username, password);
		} catch (EmptyResultDataAccessException | NullPointerException ex) {
			LOGGER.error("Exception occuered while login check {}", ex.getMessage());
		}

		return loginObj;

	}

	public static Timestamp convertStringToTimestamp(String strDate) {
	    try {
	      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	       // you can change format of date
	      Date date = formatter.parse(strDate);
	      Timestamp timeStampDate = new Timestamp(date.getTime());

	      return timeStampDate;
	    } catch (ParseException e) {
	      System.out.println("Exception :" + e);
	      return null;
	    }
	  }

	public void writeCSVFile(String reportPth) {
		 ICsvBeanWriter beanWriter = null;
		    CellProcessor[] processors = new CellProcessor[] {
		            new NotNull(), 
		            new NotNull(), 
		            new NotNull(), 
		            new NotNull(), 
		            new NotNull(), 
		            new NotNull(), 
		            new NotNull(), 
		            new NotNull() 
		    };
		 
		    try {
		        beanWriter = new CsvBeanWriter(new FileWriter(reportPth),
		                CsvPreference.STANDARD_PREFERENCE);
		     
		        String[] header = {"confid", "sectionName", "eventName", "eventStartTime", "eventEndTime", "eventDuration","dayOfEvent","eventPerformer"};
		        beanWriter.writeHeader(header);
		 
			    java.util.List<Track> trackList =getAllEvents("1");

		        for (Track trackData : trackList) {
		            beanWriter.write(trackData, header, processors);
		        }
		        trackList =getAllEvents("2");

		        for (Track trackData : trackList) {
		            beanWriter.write(trackData, header, processors);
		        }
		        trackList =getAllEvents("3");

		        for (Track trackData : trackList) {
		            beanWriter.write(trackData, header, processors);
		        }
		 
		    } catch (IOException ex) {
		        LOGGER.error("Error writing the CSV file: " + ex);
		    } finally {
		        if (beanWriter != null) {
		            try {
		                beanWriter.close();
		            } catch (IOException ex) {
		                System.err.println("Error closing the writer: " + ex);
		            }
		        }
		    }		
	}
	
}
