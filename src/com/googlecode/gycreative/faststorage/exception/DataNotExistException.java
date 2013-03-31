package com.googlecode.gycreative.faststorage.exception;

public class DataNotExistException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key = null;
	
	public DataNotExistException(String key) {
		this.key = key;
	}

	@Override
	public String getLocalizedMessage() {
		// TODO Auto-generated method stub
		return "Data for key " + key + " not exist";
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Data for key " + key + " not exist";
	}
}
