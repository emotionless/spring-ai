package com.eazybytes.springai.exception;

public class InvalidException extends RuntimeException {
    public InvalidException(String message, String response) {
        super("Answer check failed: The answer \"" + response + "\" " +
                "is not correct for the question \"" + message + "\".");
    }
}
