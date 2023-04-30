package com.castillojuan.synchrony.exception;

public class MissingUserIdException extends RuntimeException{

	public MissingUserIdException(String message) {
        super(message);
    }

    public MissingUserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
