package com.cm.movie.mapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import com.cm.movie.model.Film;

public class MovieMapper implements RowMapper<Film> {

	public String userId;

	public MovieMapper(String userId) {
		this.userId = userId;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieMapper.class);

	@Override
	public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
		Film film = new Film();
		if (!StringUtils.isEmpty(userId)) {
			film.setUserid(userId);
		}
		try
		{
		film.setFilmid(rs.getString("filmid"));
		film.setFilmname(rs.getString("filmname"));
		film.setDirector(rs.getString("director"));
		if (rs.findColumn("status") > 0 && rs.getString("status") != null) {
			film.setStatus(rs.getString("status"));
		}

		Array sqlActorArr = rs.getArray("actors");
		String[] actorArr = (String[]) sqlActorArr.getArray();
		film.setActors(actorArr);

		Array sqlCatArr = rs.getArray("actors");
		String[] catArr = (String[]) sqlCatArr.getArray();
		film.setCategory(catArr);

		film.setLink(rs.getString("link"));
		film.setLanguage(rs.getString("language"));
		}catch(NullPointerException npe)
		{
			LOGGER.error("Nullpointer {}",npe.getMessage());
		}
		return film;

	}
}
