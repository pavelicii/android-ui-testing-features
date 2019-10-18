package pavelnazimok.uitestingfeatures.java.robots;

import pavelnazimok.uitestingfeatures.R;

import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.findById;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.waitUntilNotDisplayedById;

@SuppressWarnings("UnusedReturnValue")
public class SecondScreenRobot {

    public SecondScreenRobot clickAlertDialog() {
        clickOn(R.id.button_alert_dialog);
        return this;
    }

    public SecondScreenRobot clickOkInAlertDialog() {
        findById(android.R.id.button1, 10000).click();
        return this;
    }

    public SecondScreenRobot clickCancelInAlertDialog() {
        findById(android.R.id.button2, 10000).click();
        return this;
    }

    public SecondScreenRobot assertAlertDialogIsNotDisplayed() {
        waitUntilNotDisplayedById("alertTitle", 5000);
        return this;
    }
}
