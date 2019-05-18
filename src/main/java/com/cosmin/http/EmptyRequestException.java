package com.cosmin.http;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class EmptyRequestException extends Exception{

	private static final long serialVersionUID = 1L;

	EmptyRequestException() {
		super();
	}

	public EmptyRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EmptyRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyRequestException(String message) {
		super(message);
	}

	public EmptyRequestException(Throwable cause) {
		super(cause);
	}
	
}
