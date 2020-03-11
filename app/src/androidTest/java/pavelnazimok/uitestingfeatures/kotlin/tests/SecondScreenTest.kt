package pavelnazimok.uitestingfeatures.kotlin.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import pavelnazimok.uitestingfeatures.kotlin.robots.onMenu
import pavelnazimok.uitestingfeatures.kotlin.robots.onSecondScreen

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
