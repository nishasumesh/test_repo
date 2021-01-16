package com.cm.movie.model;

import java.util.List;

public class UserModel
{
public List<Film> getAllFilmList() {
		return allFilmList;
	}
	public void setAllFilmList(List<Film> allFilmList) {
		this.allFilmList = allFilmList;
	}
public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}
	public List<Film> getFilmList() {
		return filmList;
	}
	public void setFilmList(List<Film> filmList) {
		this.filmList = filmList;
	}
private Login login;
private  List<Film> filmList;
private  List<Film> allFilmList;


}
