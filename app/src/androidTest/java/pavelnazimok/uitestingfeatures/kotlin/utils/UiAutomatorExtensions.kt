package pavelnazimok.uitestingfeatures.kotlin.utils

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions
import java.util.concurrent.TimeoutException
import java.util.regex.Pattern

val device: UiDevice
    get() = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

fun findById(viewId: String, timeout: Long = 20000): UiObject2 {
    return device.wait(
        Until.findObject(By.res(Pattern.compile("\\S+:id/$viewId"))),
        timeout
    ) ?: throw TimeoutException(
        "The UiObject2 with ID '$viewId' was not found within $timeout millis"
    )
}

fun findById(@IdRes viewId: Int, timeout: Long = 20000): UiObject2 {
    return findById(resId(viewId), timeout)
}

fun findByText(text: String, timeout: Long = 20000): UiObject2 {
    return device.wait(
        Until.findObject(By.text(text)),
        timeout
    ) ?: throw TimeoutException(
        "The UiObject2 with text '$text' was not found within $timeout millis"
    )
}

fun findByText(@StringRes stringId: Int, timeout: Long = 20000): UiObject2 {
    return findByText(resText(stringId), timeout)
}

fun findByTextPattern(textPattern: Pattern, timeout: Long = 20000): UiObject2 {
    return UiAutomatorExtensions.device().wait(
            Until.findObject(By.text(textPattern)),
            timeout
    )
            ?: throw TimeoutException(
                    "The UiObject2 with Pattern '$textPattern' was not found within $timeout millis"
            )
}

fun findByIdAndText(viewId: String, text: String, timeout: Long = 20000): UiObject2 {
    return device.wait(
        Until.findObject(By
            .res(Pattern.compile("\\S+:id/$viewId"))
            .text(text)),
        timeout
    ) ?: throw TimeoutException(
        "The UiObject2 with ID '$viewId' and text '$text' was not found within $timeout millis"
    )
}

fun findByIdAndText(@IdRes viewId: Int, text: String, timeout: Long = 20000): UiObject2 {
    return findByIdAndText(resId(viewId), text, timeout)
}

fun findByIdAndText(
    viewId: String,
    @StringRes stringId: Int,
    timeout: Long = 20000
): UiObject2 {
    return findByIdAndText(viewId, resText(stringId), timeout)
}

fun findByIdAndText(
    @IdRes viewId: Int,
    @StringRes stringId: Int,
    timeout: Long = 20000
): UiObject2 {
    return findByIdAndText(resId(viewId), resText(stringId), timeout)
}

fun findByIdAndTextPattern(
    viewId: String,
    textPattern: Pattern,
    timeout: Long = 20000
): UiObject2 {

    return device.wait(
        Until.findObject(By
            .res(Pattern.compile("\\S+:id/$viewId"))
            .text(textPattern)),
        timeout
    ) ?: throw TimeoutException("The UiObject2 with ID '$viewId' and Pattern '$textPattern' " +
        "was not found within $timeout millis"
    )
}

fun findByIdAndTextPattern(
    @IdRes viewId: Int,
    textPattern: Pattern,
    timeout: Long = 20000
): UiObject2 {
    return findByIdAndTextPattern(resId(viewId), textPattern, timeout)
}

fun waitUntilNotDisplayedById(viewId: String, timeout: Long = 20000) {
    val endTime = System.currentTimeMillis() + timeout
    while (System.currentTimeMillis() < endTime) {
        try {
            findById(viewId, 3000)
            Thread.sleep(1000)
        } catch (e: TimeoutException) {
            return
        }
    }
    throw TimeoutException("The UiObject2 with ID '$viewId' " +
        "was displayed when it should not within $timeout millis"
    )
}

fun waitUntilNotDisplayedById(@IdRes viewId: Int, timeout: Long = 20000) {
    waitUntilNotDisplayedById(resId(viewId), timeout)
}

fun waitUntilNotDisplayedByText(text: String, timeout: Long = 20000) {
    val endTime = System.currentTimeMillis() + timeout
    while (System.currentTimeMillis() < endTime) {
        try {
            findByText(text, 3000)
            Thread.sleep(1000)
        } catch (e: TimeoutException) {
            return
        }
    }
    throw TimeoutException("The UiObject2 with text '$text' " +
        "was displayed when it should not within $timeout millis"
    )
}

fun waitUntilNotDisplayedByText(@StringRes stringId: Int, timeout: Long = 20000) {
    waitUntilNotDisplayedByText(resText(stringId), timeout)
}

fun waitUntilNotDisplayedByIdAndText(viewId: String, text: String, timeout: Long = 20000) {
    val endTime = System.currentTimeMillis() + timeout
    while (System.currentTimeMillis() < endTime) {
        try {
            findByIdAndText(viewId, text, 3000)
            Thread.sleep(1000)
        } catch (e: TimeoutException) {
            return
        }
    }
    throw TimeoutException("The UiObject2 with ID '$viewId' and text '$text' " +
        "was displayed when it should not within $timeout millis")
}

fun waitUntilNotDisplayedByIdAndText(@IdRes viewId: Int, text: String, timeout: Long = 20000) {
    waitUntilNotDisplayedByIdAndText(resId(viewId), text, timeout)
}

fun waitUntilNotDisplayedByIdAndText(
    viewId: String,
    @StringRes stringId: Int,
    timeout: Long = 20000
) {
    waitUntilNotDisplayedByIdAndText(viewId, resText(stringId), timeout)
}

fun waitUntilNotDisplayedByIdAndText(
    @IdRes viewId: Int,
    @StringRes stringId: Int,
    timeout: Long = 20000
) {
    waitUntilNotDisplayedByIdAndText(resId(viewId), resText(stringId), timeout)
}

fun UiObject2.setChecked(check: Boolean) {
    if (check && !this.isChecked) {
        this.click()
    } else if (!check && this.isChecked) {
        this.click()
    }
    this.waitUntilChecked(check)
}

fun UiObject2.waitUntilEnabled(timeout: Long = 3000) {
    if (!this.wait(Until.enabled(true), timeout)) {
        throw TimeoutException("The UiObject2 with ID '${resId(this)}' " +
            "was not enabled within $timeout millis"
        )
    }
}

fun UiObject2.waitUntilChecked(check: Boolean, timeout: Long = 3000) {
    if (!this.wait(Until.checked(check), timeout)) {
        throw TimeoutException("The UiObject2 with ID '${resId(this)}' " +
            "was not set to $check within $timeout millis"
        )
    }
}

fun openNotification(): Boolean {
    return device.openNotification()
}

fun pressBack() {
    device.pressBack()
}
