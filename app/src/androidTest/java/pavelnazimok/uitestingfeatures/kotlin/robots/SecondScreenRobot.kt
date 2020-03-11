package pavelnazimok.uitestingfeatures.kotlin.robots

import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import pavelnazimok.uitestingfeatures.R
import pavelnazimok.uitestingfeatures.kotlin.utils.findById
import pavelnazimok.uitestingfeatures.kotlin.utils.waitForView
import pavelnazimok.uitestingfeatures.kotlin.utils.waitUntilNotDisplayedById

fun onSecondScreen(func: SecondScreenRobot.() -> Unit): SecondScreenRobot {
    return SecondScreenRobot().apply {
        assertSecondScreenIsDisplayed()
        func()
    }
}

class SecondScreenRobot {

    fun clickAlertDialog() {
        clickOn(R.id.button_alert_dialog)
    }

    fun clickOkInAlertDialog() {
        findById(android.R.id.button1).click()
    }

    fun clickCancelInAlertDialog() {
        findById(android.R.id.button2).click()
    }

    fun assertSecondScreenIsDisplayed() {
        waitForView(R.id.screen_second)
    }

    fun assertAlertDialogIsNotDisplayed() {
        waitUntilNotDisplayedById("alertTitle")
    }
}
