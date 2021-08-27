package com.cardinity.taskmanager.exception;

public class TaskAlreadyClosedException extends RuntimeException {

	private static final long serialVersionUID = -413120380095594050L;

	public TaskAlreadyClosedException(String message) {
        super(message);
    }

    public TaskAlreadyClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
