package com.pavelnazimok.uitesting.java.utils;

public class UiAutomatorTimeoutException extends RuntimeException {

    UiAutomatorTimeoutException() {
        super();
    }

    UiAutomatorTimeoutException(final String message) {
        super(message);
    }
}
