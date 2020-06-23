package com.pavelnazimok.uitesting.kotlin.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pavelnazimok.uitesting.kotlin.robots.onMenu
import com.pavelnazimok.uitesting.kotlin.robots.onSecondScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecondScreenTest : BaseTest() {

    @Test
    fun alertDialog_afterClickOk_shouldNotBeDisplayed() {
        onMenu {
            clickSecondScreen()
        }

        onSecondScreen {
            clickAlertDialog()
            clickOkInAlertDialog()
            assertAlertDialogIsNotDisplayed()
        }
    }
}
