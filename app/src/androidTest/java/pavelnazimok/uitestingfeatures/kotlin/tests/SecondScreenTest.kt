package pavelnazimok.uitestingfeatures.kotlin.tests

import org.junit.Test
import pavelnazimok.uitestingfeatures.kotlin.robots.onMenu
import pavelnazimok.uitestingfeatures.kotlin.robots.onSecondScreen

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
