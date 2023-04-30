package com.castillojuan.synchrony.exception;

public class MissingImageHashException extends RuntimeException{

	public MissingImageHashException(String message) {
        super(message);
    }

    public MissingImageHashException(String message, Throwable cause) {
        super(message, cause);
    }
}
