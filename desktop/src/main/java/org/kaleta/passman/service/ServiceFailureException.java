package org.kaleta.passman.service;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class ServiceFailureException extends Exception {

    public ServiceFailureException() {
    }

    public ServiceFailureException(Throwable cause) {
        super(cause);
    }

    public ServiceFailureException(String message) {
        super(message);
    }

    public ServiceFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
