package org.address.book.exception;

/**
 * @author rishi
 *	Custom exception that will be thrown to each request.
 */
public class ContactException extends Exception {

    public ContactException() {
        super();
    }

    public ContactException(String message) {
        super(message);
    }

}
