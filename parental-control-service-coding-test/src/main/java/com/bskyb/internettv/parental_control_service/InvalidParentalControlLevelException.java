package com.bskyb.internettv.parental_control_service;

/**
 * The InvalidParentalControlLevel exception is thrown when the parental control level input is invalid.
 */
public class InvalidParentalControlLevelException extends Exception {

    /**
     * Default constructor.
     */
    public InvalidParentalControlLevelException() {
        super();
    }

    /**
     * Constructor with a custom message.
     *
     * @param message custom message
     */
    public InvalidParentalControlLevelException(String message) {
        super(message);
    }
}
