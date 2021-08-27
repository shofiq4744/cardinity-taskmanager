package com.cardinity.taskmanager.exception;

public class InvalidAccessException extends RuntimeException {

	private static final long serialVersionUID = 9165347995308484727L;

	public InvalidAccessException(String message) {
        super(message);
    }

    public InvalidAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
