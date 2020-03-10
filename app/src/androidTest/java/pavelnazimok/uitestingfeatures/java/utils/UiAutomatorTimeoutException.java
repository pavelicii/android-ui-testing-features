package pavelnazimok.uitestingfeatures.java.utils;

public class UiAutomatorTimeoutException extends RuntimeException {

    UiAutomatorTimeoutException() {
        super();
    }

    UiAutomatorTimeoutException(final String message) {
        super(message);
    }
}
