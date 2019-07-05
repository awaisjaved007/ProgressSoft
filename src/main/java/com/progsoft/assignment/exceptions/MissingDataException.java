package com.progsoft.assignment.exceptions;

/**
 * The Class MissingDataException.
 *
 * @author Ibraheem Faiq
 * @since Jan 16, 2019
 */
public class MissingDataException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 7518828110205383015L;

    /**
     * Instantiates a new missing data exception.
     *
     * @param message the message
     */
    public MissingDataException(final String message) {
        super(message);
    }

}