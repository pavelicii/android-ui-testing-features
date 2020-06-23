package com.pavelnazimok.uitesting.java.utils;

import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pavelnazimok.uitesting.java.utils.AndroidExtensions.resId;
import static com.pavelnazimok.uitesting.java.utils.AndroidExtensions.resText;
import static com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleepThread;
import static com.schibsted.spain.barista.internal.util.ResourceTypeKt.resourceMatcher;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;

public class EspressoExtensions {

    public static void waitForView(final Matcher<View> matcher, final long timeout) {
        final long endTime = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis() < endTime) {
            try {
                onView(matcher).check(matches(isDisplayed()));
                return;
            } catch (NoMatchingViewException e) {
                sleepThread(500);
            }
        }

        throw new EspressoTimeoutException(String.format(
                "The View '%s' was not found within %d millis",
                matcher.toString(),
                timeout
        ));
    }

    public static void waitForView(final int resId, final long timeout) {
        waitForView(resourceMatcher(resId), timeout);
    }

    public static void waitForView(final String text, final long timeout) {
        waitForView(withText(text), timeout);
    }

    public static void waitForView(final int viewId, final int stringId, final long timeout) {
        waitForView(allOf(withId(viewId), withText(stringId)), timeout);
    }

    public static void waitForView(final int viewId, final String text, final long timeout) {
        waitForView(allOf(withId(viewId), withText(text)), timeout);
    }

    public static void waitForViewWithPattern(final Matcher<View> matcher, final Pattern pattern, final long timeout) {
        final long endTime = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis() < endTime) {
            try {
                onView(matcher).check(matches(withPattern(pattern)));
                return;
            } catch (NoMatchingViewException | AssertionError e) {
                sleepThread(500);
            }
        }

        onView(matcher).check(matches(withPattern(pattern)));
    }

    public static void waitForViewWithPattern(final int viewId, final Pattern pattern, final long timeout) {
        waitForViewWithPattern(withId(viewId), pattern, timeout);
    }

    public static Matcher<View> withPattern(final Pattern pattern) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(final Description description) {
                description
                        .appendText("with regex pattern: ")
                        .appendValue(pattern.toString());
            }

            @Override
            protected boolean matchesSafely(final TextView item) {
                return pattern.matcher(item.getText()).find();
            }
        };
    }

    public static void waitForTextInView(final String text, final int viewId, final long timeout) {
        final long endTime = System.currentTimeMillis() + timeout;
        String textInView = "";

        while (System.currentTimeMillis() < endTime) {
            textInView = getText(viewId);
            if (textInView.equals(text)) {
                break;
            }
            sleepThread(500);
        }

        assertThat(textInView)
                .as(String.format("Expected text was not found in the View with ID '%s' within %d millis", resId(viewId), timeout))
                .isEqualTo(text);
    }

    public static void waitForTextInView(final int stringId, final int viewId, final long timeout) {
        waitForTextInView(resText(stringId), viewId, timeout);
    }

    public static void waitForToast(final String text, final long timeout) {
        final Matcher<Root> matcher = new TypeSafeMatcher<Root>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("is Toast");
            }

            @Override
            protected boolean matchesSafely(final Root item) {
                final int type = item.getWindowLayoutParams().get().type;
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    final IBinder windowToken = item.getDecorView().getWindowToken();
                    final IBinder appToken = item.getDecorView().getApplicationWindowToken();
                    return windowToken == appToken;
                }
                return false;
            }
        };

        final long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withText(text)).inRoot(matcher).check(matches(isDisplayed()));
                return;
            } catch (NoMatchingViewException e) {
                sleepThread(500);
            }
        }

        throw new EspressoTimeoutException(String.format("The Toast with text '%s' was not found within %d millis", text, timeout));
    }

    public static void setChecked(final Matcher<View> matcher, final boolean check) {
        onView(matcher).perform(new ViewAction() {
            @Override
            public BaseMatcher<View> getConstraints() {
                return new BaseMatcher<View>() {
                    @Override
                    public boolean matches(final Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(final Object item, final Description mismatchDescription) { }

                    @Override
                    public void describeTo(final Description description) { }
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                final Checkable checkableView = (Checkable) view;

                if (check && !checkableView.isChecked()) {
                    view.performClick();
                } else if (!check && checkableView.isChecked()) {
                    view.performClick();
                }
            }
        });
    }

    public static void setChecked(final int viewId, final boolean check) {
        setChecked(withId(viewId), check);
    }

    public static String getText(final Matcher<View> matcher) {
        final AtomicReference<String> text = new AtomicReference<>();

        onView(allOf(matcher, not(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                text.set(((TextView) view).getText().toString());
            }
        });

        return text.get();
    }

    public static String getText(final int viewId) {
        return getText(withId(viewId));
    }

    public static int getListViewItemsCount(final int listViewId) {
        final AtomicInteger count = new AtomicInteger(0);

        final Matcher<View> matcher = new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(final Description description) {
            }

            @Override
            protected boolean matchesSafely(final View item) {
                count.set(((ListView) item).getCount());
                return true;
            }
        };
        onView(withId(listViewId)).check(matches(matcher));

        return count.get();
    }

    public static String getListViewItemText(final int listViewId, final int position) {
        final AtomicReference<String> text = new AtomicReference<>();

        onData(anything())
                .inAdapterView(withId(listViewId))
                .atPosition(position)
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isAssignableFrom(TextView.class);
                    }

                    @Override
                    public String getDescription() {
                        return "getting text from a ListView item";
                    }

                    @Override
                    public void perform(final UiController uiController, final View view) {
                        text.set(((TextView) view).getText().toString());
                    }
                });

        return text.get();
    }
}
