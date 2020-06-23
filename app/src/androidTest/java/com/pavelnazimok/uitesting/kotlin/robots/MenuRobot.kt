package com.pavelnazimok.uitesting.kotlin.robots

import com.pavelnazimok.uitesting.R
import com.pavelnazimok.uitesting.kotlin.utils.waitForView
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

fun onMenu(func: MenuRobot.() -> Unit): MenuRobot {
    return MenuRobot().apply {
        assertMenuIsDisplayed()
        func()
    }
}

class MenuRobot {

    fun clickFirstScreen() {
        clickOn(R.id.nav_first)
    }

    fun clickSecondScreen() {
        clickOn(R.id.nav_second)
    }

    fun clickThirdScreen() {
        clickOn(R.id.nav_third)
    }

    fun assertMenuIsDisplayed() {
        waitForView(R.id.nav_bottom_bar)
    }
}
