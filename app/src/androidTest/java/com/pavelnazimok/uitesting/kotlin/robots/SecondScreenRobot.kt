package com.pavelnazimok.uitesting.kotlin.robots

import com.pavelnazimok.uitesting.R
import com.pavelnazimok.uitesting.kotlin.utils.findById
import com.pavelnazimok.uitesting.kotlin.utils.waitForView
import com.pavelnazimok.uitesting.kotlin.utils.waitUntilNotDisplayedById
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

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
