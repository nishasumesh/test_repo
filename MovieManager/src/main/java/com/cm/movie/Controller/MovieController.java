package com.cm.movie.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cm.movie.model.Login;
import com.cm.movie.model.UserModel;
import com.cm.movie.service.MovieService;

@Controller
public class MovieController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

	@Autowired
	MovieService movieService;

	@GetMapping("/login")
	public String greetingForm(Model model) {
		LOGGER.info("inside login get call ... ");
		model.addAttribute("login", new Login());
		return "login";
	}

	@PostMapping("/login")
	public String greetingSubmit(@ModelAttribute Login login, Model model) {
		LOGGER.info("inside login post call ... ");
		UserModel userModel = movieService.checkUser(login.getUsername(), login.getPassword());
		model.addAttribute("login", userModel.getLogin());
		model.addAttribute("watchList", userModel.getFilmList());
		model.addAttribute("recommendedList", userModel.getAllFilmList());

		return "movie";
	}

}
