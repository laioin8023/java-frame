package com.laioin.java.frame.activemq.exception;

public class ActivemqException extends RuntimeException {

	private static final long serialVersionUID = 2685143772736353091L;

	public ActivemqException() {
		super();
	}

	public ActivemqException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActivemqException(String message) {
		super(message);
	}

	public ActivemqException(Throwable cause) {
		super(cause);
	}

}
