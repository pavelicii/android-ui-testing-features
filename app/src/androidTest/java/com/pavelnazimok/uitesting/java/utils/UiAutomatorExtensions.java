package com.pavelnazimok.uitesting.java.utils;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import java.util.regex.Pattern;

import static com.pavelnazimok.uitesting.java.utils.AndroidExtensions.resId;
import static com.pavelnazimok.uitesting.java.utils.AndroidExtensions.resText;

public class UiAutomatorExtensions {

    public static UiDevice device() {
        return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public static UiObject2 findById(final String viewId, final long timeout) {
        final UiObject2 object = device().wait(
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

    public static UiObject2 findById(final int viewId, final long timeout) {
        return findById(resId(viewId), timeout);
    }

    public static UiObject2 findByText(final String text, final long timeout) {
        final UiObject2 object = device().wait(
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

    public static UiObject2 findByText(final int stringId, final long timeout) {
        return findByText(resText(stringId), timeout);
    }

    public static UiObject2 findByTextPattern(final Pattern textPattern, final long timeout) {
        final UiObject2 object = device().wait(
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

    public static UiObject2 findByIdAndText(final String viewId, final String text, final long timeout) {
        final UiObject2 object = device().wait(
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

    public static UiObject2 findByIdAndText(final int viewId, final String text, final long timeout) {
        return findByIdAndText(resId(viewId), text, timeout);
    }

    public static UiObject2 findByIdAndText(final String viewId, final int stringId, final long timeout) {
        return findByIdAndText(viewId, resText(stringId), timeout);
    }

    public static UiObject2 findByIdAndText(final int viewId, final int stringId, final long timeout) {
        return findByIdAndText(resId(viewId), resText(stringId), timeout);
    }

    public static UiObject2 findByIdAndTextPattern(final String viewId, final Pattern textPattern, final long timeout) {
        final UiObject2 object = device().wait(
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

    public static UiObject2 findByIdAndTextPattern(final int viewId, final Pattern textPattern, final long timeout) {
        return findByIdAndTextPattern(resId(viewId), textPattern, timeout);
    }

    public static void waitUntilNotDisplayedById(final String viewId, final long timeout) {
        final long endTime = System.currentTimeMillis() + timeout;
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

    public static void waitUntilNotDisplayedById(final int viewId, final long timeout) {
        waitUntilNotDisplayedById(resId(viewId), timeout);
    }

    public static void waitUntilNotDisplayedByText(final String text, final long timeout) {
        final long endTime = System.currentTimeMillis() + timeout;
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

    public static void waitUntilNotDisplayedByText(final int stringId, final long timeout) {
        waitUntilNotDisplayedByText(resText(stringId), timeout);
    }

    public static void waitUntilNotDisplayedByIdAndText(final String viewId, final String text, final long timeout) {
        final long endTime = System.currentTimeMillis() + timeout;
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

    public static void waitUntilNotDisplayedByIdAndText(final int viewId, final String text, final long timeout) {
        waitUntilNotDisplayedByIdAndText(resId(viewId), text, timeout);
    }

    public static void waitUntilNotDisplayedByIdAndText(final String viewId, final int stringId, final long timeout) {
        waitUntilNotDisplayedByIdAndText(viewId, resText(stringId), timeout);
    }

    public static void waitUntilNotDisplayedByIdAndText(final int viewId, final int stringId, final long timeout) {
        waitUntilNotDisplayedByIdAndText(resId(viewId), resText(stringId), timeout);
    }

    public static void setChecked(final UiObject2 object, final boolean check) {
        if (check && !object.isChecked()) {
            object.click();
        } else if (!check && object.isChecked()) {
            object.click();
        }

        waitUntilChecked(object, check, 3000);
    }

    public static void waitUntilEnabled(final UiObject2 object, final long timeout) {
        if (!object.wait(Until.enabled(true), timeout)) {
            throw new UiAutomatorTimeoutException(String.format(
                    "The UiObject2 with ID '%s' was not enabled within %d millis",
                    resId(object),
                    timeout
            ));
        }
    }

    public static void waitUntilChecked(final UiObject2 object, final boolean check, final long timeout) {
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
