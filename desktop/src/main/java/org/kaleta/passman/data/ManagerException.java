package org.kaleta.passman.data;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class ManagerException extends Exception {

    public ManagerException() {
    }

    public ManagerException(Throwable cause) {
        super(cause);
    }

    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
