package com.cm.movie.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cm.movie.mapper.LoginMapper;
import com.cm.movie.model.Login;
import com.cm.movie.model.UserModel;
import com.cm.movie.model.Film;
import com.cm.movie.mapper.*;

@Service
public class MovieService {
	@Autowired
	private JdbcTemplate postgresTemplate;

	private static final String CHECKLOGIN = "SELECT user_id, name, username, password"
			+ "	FROM movie.user where username=? and password=?";

	private static final String getAllFilms = "SELECT language,filmid, filmname, director, actors, category, link, 'notwatched' as status FROM movie.film";

	private static final String getUserFilms = "select film.language as language, film.filmid as filmid, film.filmname as filmname, film.director as director , film.actors as actors, film.category as category, film.link as link, usrflm.status as status from movie.film film inner join  movie.userfilm usrflm on usrflm.filmid =film.filmid where  film.filmid in (select filmid from movie.userfilm where userid=? and status='watching')";

	private static final String getRecommendation = "SELECT language, filmid, filmname, director, actors, category, link, 'recommended' as status FROM movie.film where  actors &&(?::text[]) or category && (?::text[]) or director = ? or language = ?";

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

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		final Set<Object> seen = new HashSet<>();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public List<Film> getAllFilms() {

		List<Film> filmList = postgresTemplate.query(getAllFilms, new MovieMapper(null));
		return filmList;
	}

	public List<Film> getAllUserFilms(String userId) {

		List<Film> filmList = postgresTemplate.query(getUserFilms, new MovieMapper(userId), userId);
		return filmList;
	}

	public UserModel getAllUserRecommendations(String userId, UserModel userModel) {
		List<Film> listWithoutDuplicates = null;
		List<Film> recommendationList = new ArrayList<Film>();
		if (!StringUtils.isEmpty(userId)) {
			List<Film> allUserWatchingFilms = getAllUserFilms(userId);
			userModel.setFilmList(allUserWatchingFilms);
			for (Film userFilm : allUserWatchingFilms) {
				LOGGER.info("Fetching recommendation Movies ... ");

				List<Film> filmRecommdList = postgresTemplate.query(getRecommendation, new MovieMapper(userId),
						userFilm.getActors(), userFilm.getCategory(), userFilm.getDirector(), userFilm.getLanguage());
				recommendationList.addAll(filmRecommdList);
			}
		} else {
			List<Film> allFilms = getAllFilms();
			recommendationList.addAll(allFilms);

		}
		if (!recommendationList.isEmpty()) {
			listWithoutDuplicates = recommendationList.stream()
					.collect(Collectors.collectingAndThen(
							Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Film::getFilmid))),
							ArrayList::new));
		}
		userModel.setAllFilmList(listWithoutDuplicates);

		return userModel;
	}
}
