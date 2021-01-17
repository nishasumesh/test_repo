package com.cm.conf.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.cm.conf.model.Login;

public class LoginMapper implements RowMapper<Login> {

		@Override
		public Login mapRow(ResultSet rs, int rowNum) throws SQLException {
			Login login = new Login();
			login.setName(rs.getString("name"));
			login.setUser_id(rs.getString("user_id"));
			login.setUsername(rs.getString("username"));
			login.setPassword(rs.getString("password"));
			return login;

		}
	}


