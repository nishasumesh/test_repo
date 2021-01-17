package com.cm.conf.controller;

import java.awt.print.Book;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.cm.conf.model.Login;
import com.cm.conf.model.PageConfigurer;
import com.cm.conf.model.Track;
import com.cm.conf.service.ConfService;

@Controller
public class ConfController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfController.class);

	@Value("${conf.days}")
	private String confDays;

	@Value("${report.path}")
	private String reportPth;

	@Autowired
	ConfService confService;

	@GetMapping("/login")
	public String greetingForm(Model model) {
		LOGGER.info("inside login get call ... ");
		model.addAttribute("login", new Login());
		return "login";
	}

	@PostMapping("/login")
	public String greetingSubmit(@ModelAttribute Login login, Model model) {
		LOGGER.info("inside login post call ... ");
		LOGGER.info("inside login post call ... ");
		login = confService.checkUser(login.getUsername(), login.getPassword());
		PageConfigurer pageConfigurer = new PageConfigurer();
		pageConfigurer.setConfDays(confDays);

		model.addAttribute("login", login);
		model.addAttribute("trackList_Day1", confService.getAllEvents("1"));
		model.addAttribute("trackList_Day2", confService.getAllEvents("2"));
		model.addAttribute("trackList_Day3", confService.getAllEvents("3"));
		model.addAttribute("track", new Track());
		model.addAttribute("reportPth", reportPth);

		model.addAttribute("pageConfigurer", pageConfigurer);
		return "conf";
	}

	@PostMapping("/addEvent")
	public String confSubmit(@ModelAttribute Track track, Model model) {
		LOGGER.info("inside confSubmit post call ... ");

		LOGGER.info("Received event {}", track.getEventName());
		LOGGER.info("Received day {}", track.getDayOfEvent());

		Login login = confService.checkUser("nisha", "nisha");

		if (track.getEventDuration() == 0 || StringUtils.isEmpty(track.getDayOfEvent())
				|| StringUtils.isEmpty(track.getEventDuration()) || StringUtils.isEmpty(track.getEventEndTime())
				|| StringUtils.isEmpty(track.getEventName()) || StringUtils.isEmpty(track.getEventPerformer())
				|| StringUtils.isEmpty(track.getEventStartTime()) || StringUtils.isEmpty(track.getSectionName())) {
			model.addAttribute("error", "Mandatory data is missing");
		} else {
			int saveResponse = confService.saveTrack(track); // save data to db
			if (saveResponse == 0) {
				model.addAttribute("error", "Time does not match, Please provide a valid time ");
			}
		}

		model.addAttribute("login", login);

		model.addAttribute("track", track);
		model.addAttribute("reportPth", reportPth);

		model.addAttribute("trackList_Day1", confService.getAllEvents("1"));
		model.addAttribute("trackList_Day2", confService.getAllEvents("2"));
		model.addAttribute("trackList_Day3", confService.getAllEvents("3"));

		return "conf";
	}

	@GetMapping("/conf")
	public String confGet(@ModelAttribute PageConfigurer pageConfigurer, Model model) {
		LOGGER.info("inside confSubmit post call ... ");
		model.addAttribute("pageConfigurer", pageConfigurer);
		return "conf";
	}

	@GetMapping("/download")
	public @ResponseBody ResponseEntity<String> confDownload() throws FileNotFoundException {
		LOGGER.info("Downloading all data from DB to path {}", reportPth);
		confService.writeCSVFile(reportPth);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

}
