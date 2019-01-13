package models;

import java.util.Date;

import database.DataDAO;

public class Data {

	private Date date;
	
	private int value;
	
	private String origin;

	public Data() {}

	public Data(Date iDate, int iValue, String iOrigin) {
		this.setDate(iDate);
		this.setValue(iValue);
		this.setOrigin(iOrigin);
	}
	
	public void save() {
		DataDAO lDao = new DataDAO();
		lDao.saveData(this);
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	
}
