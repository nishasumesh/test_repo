package com.cm.movie.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cm.movie.mapper.LoginMapper;
import com.cm.movie.mapper.MovieMapper;
import com.cm.movie.model.Film;
import com.cm.movie.model.Login;
import com.cm.movie.model.UserModel;

@Service
public class MovieService {
	@Autowired
	private JdbcTemplate postgresTemplate;

	private static final String CHECKLOGIN = "SELECT user_id, name, username, password"
			+ "	FROM movie.user where username=? and password=?";

	private static final String getAllFilms = "SELECT language,filmid, filmname, director, actors, category, link, 'notwatched' as status FROM movie.film";

	private static final String getUserFilms = "select film.language as language, film.filmid as filmid, film.filmname as filmname, film.director as director , film.actors as actors, film.category as category, film.link as link, usrflm.status as status from movie.film film inner join  movie.userfilm usrflm on usrflm.filmid =film.filmid where  film.filmid in (select filmid from movie.userfilm where userid=? and (status='watching' or status='watched'))";

	private static final String getRecommendation = "SELECT language, filmid, filmname, director, actors, category, link, 'recommended' as status FROM movie.film where (filmid != ?) and   (actors &&(?::text[]) or category && (?::text[]) or director = ? or language = ?)";

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

	public UserModel checkUser(String username, String password) {
		UserModel userModel = new UserModel();
		LOGGER.info("postgresTemplate{}", postgresTemplate);
		LOGGER.info("username{}", username);
		LOGGER.info("password{}", password);
		Login loginObj = null;
		try {
			loginObj = postgresTemplate.queryForObject(CHECKLOGIN, new LoginMapper(), username, password);
			userModel.setLogin(loginObj);
			userModel = getAllUserRecommendations(loginObj.getUser_id(), userModel);
		} catch (EmptyResultDataAccessException | NullPointerException ex) {
			LOGGER.error("Exception occuered while login check {}", ex.getMessage());
			userModel = getAllUserRecommendations(null, userModel);
		}

		return userModel;

	}

	// Fetch all available movies

	public List<Film> getAllFilms() {

		List<Film> filmList = postgresTemplate.query(getAllFilms, new MovieMapper(null));
		return filmList;
	}

	// Fetch all user watched/watching movies
	public List<Film> getAllUserFilms(String userId) {

		List<Film> filmList = postgresTemplate.query(getUserFilms, new MovieMapper(userId), userId);
		return filmList;
	}

	// Fetch all user recommendations, avoid duplicates and watched list data
	public UserModel getAllUserRecommendations(String userId, UserModel userModel) {
		ArrayList<Film> listWithoutDuplicates = null;
		ArrayList<Film> recommendationList = null;
		ArrayList<Film> allUserWatchingFilms = null;
		if (!StringUtils.isEmpty(userId)) {
			recommendationList = new ArrayList<Film>();
			allUserWatchingFilms = (ArrayList) getAllUserFilms(userId);
			userModel.setFilmList(allUserWatchingFilms);

			for (Film userFilm : allUserWatchingFilms) {
				LOGGER.info("Fetching recommendation Movies ...{} ", userFilm.getFilmid());
				List<Film> filmRecommdList = postgresTemplate.query(getRecommendation, new MovieMapper(userId),
						userFilm.getFilmid(), userFilm.getActors(), userFilm.getCategory(), userFilm.getDirector(),
						userFilm.getLanguage());
				recommendationList.addAll(filmRecommdList);
			}

		} else {
			recommendationList = new ArrayList<Film>();
			List<Film> allFilms = getAllFilms();
			recommendationList.addAll(allFilms);

		}

		if (!recommendationList.isEmpty()) {
			listWithoutDuplicates = recommendationList.stream()
					.collect(Collectors.collectingAndThen(
							Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Film::getFilmid))),
							ArrayList::new));
		}

		if (allUserWatchingFilms != null && !allUserWatchingFilms.isEmpty()) {
			for (Film userFilm : allUserWatchingFilms) {

				Iterator<Film> iterator = listWithoutDuplicates.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().getFilmid().equalsIgnoreCase(userFilm.getFilmid())) {
						iterator.remove();
					}
				}
			}

		}
		userModel.setAllFilmList(listWithoutDuplicates);

		return userModel;
	}
}
