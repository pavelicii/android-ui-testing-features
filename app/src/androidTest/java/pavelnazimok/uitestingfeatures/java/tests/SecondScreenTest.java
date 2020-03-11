package pavelnazimok.uitestingfeatures.java.tests;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
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
