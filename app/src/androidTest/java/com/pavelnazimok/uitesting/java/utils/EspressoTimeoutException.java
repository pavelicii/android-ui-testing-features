package com.pavelnazimok.uitesting.java.utils;

public class EspressoTimeoutException extends RuntimeException {

    EspressoTimeoutException() {
        super();
    }

    EspressoTimeoutException(final String message) {
        super(message);
    }
}
