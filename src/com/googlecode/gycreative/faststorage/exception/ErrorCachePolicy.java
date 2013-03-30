package com.googlecode.gycreative.faststorage.exception;

public class ErrorCachePolicy extends Exception {
	
	int errorPolicy;
	
	public ErrorCachePolicy(int errorPolicy) {
		this.errorPolicy = errorPolicy;
	}
	
	@Override
	public String getLocalizedMessage() {
		// TODO Auto-generated method stub
		return "Error cache policy: " + this.errorPolicy;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Error cache policy: " + this.errorPolicy;
	}
}
