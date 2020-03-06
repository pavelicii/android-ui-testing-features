package pavelnazimok.uitestingfeatures.java.utils;

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
import static com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleepThread;
import static com.schibsted.spain.barista.internal.util.ResourceTypeKt.resourceMatcher;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.resId;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.resText;

public class EspressoExtensions {

    public static void waitForView(Matcher<View> matcher, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;

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

    public static void waitForView(int resId, long timeout) {
        waitForView(resourceMatcher(resId), timeout);
    }

    public static void waitForView(String text, long timeout) {
        waitForView(withText(text), timeout);
    }

    public static void waitForView(int viewId, int stringId, long timeout) {
        waitForView(allOf(withId(viewId), withText(stringId)), timeout);
    }

    public static void waitForView(int viewId, String text, long timeout) {
        waitForView(allOf(withId(viewId), withText(text)), timeout);
    }

    public static void waitForViewWithPattern(Matcher<View> matcher, Pattern pattern, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;

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

    public static void waitForViewWithPattern(int viewId, Pattern pattern, long timeout) {
        waitForViewWithPattern(withId(viewId), pattern, timeout);
    }

    public static Matcher<View> withPattern(Pattern pattern) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description
                        .appendText("with regex pattern: ")
                        .appendValue(pattern.toString());
            }

            @Override
            protected boolean matchesSafely(TextView item) {
                return pattern.matcher(item.getText()).find();
            }
        };
    }

    public static void waitForTextInView(String text, int viewId, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        String textInView = "";

        while (System.currentTimeMillis() < endTime) {
            textInView = getText(viewId);
            if (textInView.equals(text)) break;
            sleepThread(500);
        }

        assertThat(textInView)
                .as(String.format("Expected text was not found in the View with ID '%s' within %d millis", resId(viewId), timeout))
                .isEqualTo(text);
    }

    public static void waitForTextInView(int stringId, int viewId, long timeout) {
        waitForTextInView(resText(stringId), viewId, timeout);
    }

    public static void waitForToast(String text, long timeout) {
        Matcher<Root> matcher = new TypeSafeMatcher<Root>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is Toast");
            }

            @Override
            protected boolean matchesSafely(Root item) {
                int type = item.getWindowLayoutParams().get().type;
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    IBinder windowToken = item.getDecorView().getWindowToken();
                    IBinder appToken = item.getDecorView().getApplicationWindowToken();
                    return (windowToken == appToken);
                }
                return false;
            }
        };

        long endTime = System.currentTimeMillis() + timeout;
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

    public static void setChecked(Matcher<View> matcher, boolean check) {
        onView(matcher).perform(new ViewAction() {
            @Override
            public BaseMatcher<View> getConstraints() {
                return new BaseMatcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {}

                    @Override
                    public void describeTo(Description description) {}
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;

                if (check && !checkableView.isChecked()) {
                    view.performClick();
                } else if (!check && checkableView.isChecked()) {
                    view.performClick();
                }
            }
        });
    }

    public static void setChecked(int viewId, boolean check) {
        setChecked(withId(viewId), check);
    }

    public static String getText(Matcher<View> matcher) {
        AtomicReference<String> text = new AtomicReference<>();

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
            public void perform(UiController uiController, View view) {
                text.set(((TextView) view).getText().toString());
            }
        });

        return text.get();
    }

    public static String getText(int viewId) {
        return getText(withId(viewId));
    }

    public static int getListViewItemsCount(int listViewId) {
        AtomicInteger count = new AtomicInteger(0);

        Matcher<View> matcher = new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            protected boolean matchesSafely(View item) {
                count.set(((ListView) item).getCount());
                return true;
            }
        };
        onView(withId(listViewId)).check(matches(matcher));

        return count.get();
    }

    public static String getListViewItemText(int listViewId, int position) {
        AtomicReference<String> text = new AtomicReference<>();

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
                    public void perform(UiController uiController, View view) {
                        text.set(((TextView) view).getText().toString());
                    }
                });

        return text.get();
    }
}
