package pavelnazimok.uitestingfeatures.kotlin.utils

import android.view.View
import android.view.WindowManager
import android.widget.Checkable
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.Root
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleepThread
import com.schibsted.spain.barista.internal.util.resourceMatcher
import junit.framework.AssertionFailedError
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.TimeoutException
import java.util.regex.Pattern

fun waitForView(matcher: Matcher<View>, timeout: Long = 20000) {
    val endTime = System.currentTimeMillis() + timeout

    while (System.currentTimeMillis() < endTime) {
        try {
            onView(matcher).check(matches(isDisplayed()))
            return
        } catch (e: NoMatchingViewException) {
            sleepThread(500)
        } catch (e: AssertionFailedError) {
            sleepThread(500)
        }
    }

    throw TimeoutException("The View $matcher was not found within $timeout millis")
}

fun waitForView(resId: Int, timeout: Long = 20000) {
    waitForView(resId.resourceMatcher(), timeout)
}

fun waitForView(text: String, timeout: Long = 20000) {
    waitForView(withText(text), timeout)
}

fun waitForView(@IdRes viewId: Int, @StringRes stringId: Int, timeout: Long = 20000) {
    waitForView(allOf(withId(viewId), withText(stringId)), timeout)
}

fun waitForView(@IdRes viewId: Int, text: String, timeout: Long = 20000) {
    waitForView(allOf(withId(viewId), withText(text)), timeout)
}

fun waitForViewWithPattern(matcher: Matcher<View>, pattern: Pattern, timeout: Long) {
    val endTime = System.currentTimeMillis() + timeout

    while (System.currentTimeMillis() < endTime) {
        try {
            onView(matcher).check(matches(withPattern(pattern)))
            return
        } catch (e: NoMatchingViewException) {
            sleepThread(500)
        } catch (e: AssertionError) {
            sleepThread(500)
        }
    }

    onView(matcher).check(matches(withPattern(pattern)))
}

fun waitForViewWithPattern(@IdRes viewId: Int, pattern: Pattern, timeout: Long) {
    waitForViewWithPattern(withId(viewId), pattern, timeout)
}

fun withPattern(pattern: Pattern): Matcher<View> {
    return object : BoundedMatcher<View, TextView>(TextView::class.java) {
        override fun describeTo(description: Description) {
            description
                    .appendText("with regex pattern: ")
                    .appendValue(pattern.toString())
        }

        override fun matchesSafely(item: TextView): Boolean {
            return pattern.matcher(item.text).find()
        }
    }
}

fun waitForTextInView(text: String, @IdRes viewId: Int, timeout: Long) {
    val endTime = System.currentTimeMillis() + timeout
    var textInView = ""

    while (System.currentTimeMillis() < endTime) {
        textInView = getText(viewId)
        if (textInView == text) break
        sleepThread(500)
    }

    assertThat(textInView)
            .`as`("Expected text was not found in the View with ID '${resId(viewId)}' within $timeout millis")
            .isEqualTo(text)
}

fun waitForTextInView(@StringRes stringId: Int, @IdRes viewId: Int, timeout: Long) {
    waitForTextInView(resText(stringId), viewId, timeout)
}

fun waitForToast(matcher: Matcher<View>, timeout: Long = 20000) {
    val toastMatcher = object : TypeSafeMatcher<Root>() {
        override fun describeTo(description: Description) {
            description.appendText("is toast")
        }

        override fun matchesSafely(item: Root): Boolean {
            val type = item.windowLayoutParams.get().type
            @Suppress("DEPRECATION")
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                val windowToken = item.decorView.windowToken
                val appToken = item.decorView.applicationWindowToken
                return windowToken === appToken
            }
            return false
        }
    }

    val endTime = System.currentTimeMillis() + timeout

    while (System.currentTimeMillis() < endTime) {
        try {
            onView(matcher).inRoot(toastMatcher).check(matches(isDisplayed()))
            return
        } catch (e: NoMatchingViewException) {
            sleepThread(500)
        } catch (e: AssertionFailedError) {
            sleepThread(500)
        }
    }

    throw TimeoutException("The Toast $matcher was not found within $timeout millis")
}

fun setChecked(matcher: Matcher<View?>, check: Boolean) {
    onView(matcher).perform(object : ViewAction {
        override fun getConstraints(): BaseMatcher<View> {
            return object : BaseMatcher<View>() {
                override fun matches(item: Any): Boolean {
                    return CoreMatchers.isA(Checkable::class.java).matches(item)
                }

                override fun describeMismatch(item: Any, mismatchDescription: Description) {}
                override fun describeTo(description: Description) {}
            }
        }

        override fun getDescription(): String? {
            return null
        }

        override fun perform(uiController: UiController, view: View) {
            val checkableView = view as Checkable

            if (check && !checkableView.isChecked) {
                view.performClick()
            } else if (!check && checkableView.isChecked) {
                view.performClick()
            }
        }
    })
}

fun setChecked(viewId: Int, check: Boolean) {
    setChecked(withId(viewId), check)
}

fun getText(matcher: Matcher<View>): String {
    var text = ""

    onView(matcher).perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(TextView::class.java)
        }

        override fun getDescription(): String {
            return String.format("getting text from the View: $matcher")
        }

        override fun perform(uiController: UiController, view: View) {
            text = (view as TextView).text.toString()
        }
    })

    return text
}

fun getText(@IdRes viewId: Int): String {
    return getText(withId(viewId))
}

fun getListViewItemsCount(@IdRes listViewId: Int): Int {
    var count = 0

    val matcher = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {}

        override fun matchesSafely(item: View): Boolean {
            count = (item as ListView).count
            return true
        }
    }
    onView(withId(listViewId)).check(matches(matcher))

    return count
}

fun getListViewItemText(@IdRes listViewId: Int, position: Int): String {
    var text = ""

    onData(anything())
            .inAdapterView(withId(listViewId))
            .atPosition(position)
            .perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isAssignableFrom(TextView::class.java)
                }

                override fun getDescription(): String {
                    return "getting text from a ListView item"
                }

                override fun perform(uiController: UiController, view: View) {
                    text = (view as TextView).text.toString()
                }
            })

    return text
}