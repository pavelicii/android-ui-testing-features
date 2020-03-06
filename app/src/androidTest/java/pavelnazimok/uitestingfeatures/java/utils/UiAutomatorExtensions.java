package pavelnazimok.uitestingfeatures.java.utils;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import java.util.regex.Pattern;

import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.resId;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.resText;

public class UiAutomatorExtensions {

    public static UiDevice device() {
        return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public static UiObject2 findById(String viewId, long timeout) {
        UiObject2 object = device().wait(
                Until.findObject(By.res(Pattern.compile("\\S+:id/" + viewId))),
                timeout
        );

        if (object == null) {
            throw new UiAutomatorTimeoutException(String.format(
                    "The UiObject2 with ID '%s' was not found within %d millis",
                    viewId,
                    timeout
            ));
        }

        return object;
    }

    public static UiObject2 findById(int viewId, long timeout) {
        return findById(resId(viewId), timeout);
    }

    public static UiObject2 findByText(String text, long timeout) {
        UiObject2 object = device().wait(
                Until.findObject(By.text(text)),
                timeout
        );

        if (object == null) {
            throw new UiAutomatorTimeoutException(String.format(
                    "The UiObject2 with text '%s' was not found within %d millis",
                    text,
                    timeout
            ));
        }

        return object;
    }

    public static UiObject2 findByText(int stringId, long timeout) {
        return findByText(resText(stringId), timeout);
    }

    public static UiObject2 findByTextPattern(Pattern textPattern, long timeout) {
        UiObject2 object = device().wait(
                Until.findObject(By.text(textPattern)),
                timeout
        );

        if (object == null) {
            throw new UiAutomatorTimeoutException(String.format(
                    "The UiObject2 with Pattern '%s' was not found within %d millis",
                    textPattern.toString(),
                    timeout
            ));
        }

        return object;
    }

    public static UiObject2 findByIdAndText(String viewId, String text, long timeout) {
        UiObject2 object = device().wait(
                Until.findObject(By
                        .res(Pattern.compile("\\S+:id/" + viewId))
                        .text(text)),
                timeout
        );

        if (object == null) {
            throw new UiAutomatorTimeoutException(String.format(
                    "The UiObject2 with ID '%s' and text '%s' was not found within %d millis",
                    viewId,
                    text,
                    timeout
            ));
        }

        return object;
    }

    public static UiObject2 findByIdAndText(int viewId, String text, long timeout) {
        return findByIdAndText(resId(viewId), text, timeout);
    }

    public static UiObject2 findByIdAndText(String viewId, int stringId, long timeout) {
        return findByIdAndText(viewId, resText(stringId), timeout);
    }

    public static UiObject2 findByIdAndText(int viewId, int stringId, long timeout) {
        return findByIdAndText(resId(viewId), resText(stringId), timeout);
    }

    public static UiObject2 findByIdAndTextPattern(String viewId, Pattern textPattern, long timeout) {
        UiObject2 object = device().wait(
                Until.findObject(By
                        .res(Pattern.compile("\\S+:id/" + viewId))
                        .text(textPattern)),
                timeout
        );

        if (object == null) {
            throw new UiAutomatorTimeoutException(String.format(
                    "The UiObject2 with ID '%s' and Pattern '%s' was not found within %d millis",
                    viewId,
                    textPattern.toString(),
                    timeout
            ));
        }

        return object;
    }

    public static UiObject2 findByIdAndTextPattern(int viewId, Pattern textPattern, long timeout) {
        return findByIdAndTextPattern(resId(viewId), textPattern, timeout);
    }

    public static void waitUntilNotDisplayedById(String viewId, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                findById(viewId, 1000);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (UiAutomatorTimeoutException e) {
                return;
            }
        }
        throw new UiAutomatorTimeoutException(String.format(
                "The UiObject2 with ID '%s' was displayed when it should not within %d millis",
                viewId,
                timeout
        ));
    }

    public static void waitUntilNotDisplayedById(int viewId, long timeout) {
        waitUntilNotDisplayedById(resId(viewId), timeout);
    }

    public static void waitUntilNotDisplayedByText(String text, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            findByText(text, 1000);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
        throw new UiAutomatorTimeoutException(String.format(
                "The UiObject2 with text '%s' was displayed when it should not within %d millis",
                text,
                timeout
        ));
    }

    public static void waitUntilNotDisplayedByText(int stringId, long timeout) {
        waitUntilNotDisplayedByText(resText(stringId), timeout);
    }

    public static void waitUntilNotDisplayedByIdAndText(String viewId, String text, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            findByIdAndText(viewId, text, 1000);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
        throw new UiAutomatorTimeoutException(String.format(
                "The UiObject2 with ID '%s' and text '%s' was displayed when it should not within %d millis",
                viewId,
                text,
                timeout
        ));
    }

    public static void waitUntilNotDisplayedByIdAndText(int viewId, String text, long timeout) {
        waitUntilNotDisplayedByIdAndText(resId(viewId), text, timeout);
    }

    public static void waitUntilNotDisplayedByIdAndText(String viewId, int stringId, long timeout) {
        waitUntilNotDisplayedByIdAndText(viewId, resText(stringId), timeout);
    }

    public static void waitUntilNotDisplayedByIdAndText(int viewId, int stringId, long timeout) {
        waitUntilNotDisplayedByIdAndText(resId(viewId), resText(stringId), timeout);
    }

    public static void setChecked(UiObject2 object, boolean check) {
        if (check && !object.isChecked()) {
            object.click();
        } else if (!check && object.isChecked()) {
            object.click();
        }

        waitUntilChecked(object, check, 3000);
    }

    public static void waitUntilEnabled(UiObject2 object, long timeout) {
        if (!object.wait(Until.enabled(true), timeout)) {
            throw new UiAutomatorTimeoutException(String.format(
                    "The UiObject2 with ID '%s' was not enabled within %d millis",
                    resId(object),
                    timeout
            ));
        }
    }

    public static void waitUntilChecked(UiObject2 object, boolean check, long timeout) {
        if (!object.wait(Until.checked(check), timeout)) {
            throw new UiAutomatorTimeoutException(String.format(
                    "The UiObject2 with ID '%s' was not set to %s within %d millis",
                    resId(object),
                    String.valueOf(check),
                    timeout
            ));
        }
    }

    public static boolean openNotificationShade() {
        return device().openNotification();
    }

    public static void pressBack() {
        device().pressBack();
    }
}
