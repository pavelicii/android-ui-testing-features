package pavelnazimok.uitestingfeatures.java.utils;

public class EspressoTimeoutException extends RuntimeException {

    EspressoTimeoutException() {
        super();
    }

    EspressoTimeoutException(String message) {
        super(message);
    }
}
