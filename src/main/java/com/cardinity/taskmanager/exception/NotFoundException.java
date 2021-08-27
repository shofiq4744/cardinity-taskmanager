package com.cardinity.taskmanager.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -413120380095594050L;

	public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
