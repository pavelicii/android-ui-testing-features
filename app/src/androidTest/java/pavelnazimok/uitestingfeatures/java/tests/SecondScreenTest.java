package pavelnazimok.uitestingfeatures.java.tests;

import org.junit.Test;

public class SecondScreenTest extends BaseTest {

    @Test
    public void alertDialog_afterClickOk_shouldNotBeDisplayed() {
        menuRobot
                .clickSecondScreen();

        secondScreenRobot
                .clickAlertDialog()
                .clickOkInAlertDialog()
                .assertAlertDialogIsNotDisplayed();
    }
}
