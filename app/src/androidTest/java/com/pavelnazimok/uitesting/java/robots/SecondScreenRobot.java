package com.pavelnazimok.uitesting.java.robots;

import com.pavelnazimok.uitesting.R;

import static com.pavelnazimok.uitesting.java.utils.UiAutomatorExtensions.findById;
import static com.pavelnazimok.uitesting.java.utils.UiAutomatorExtensions.waitUntilNotDisplayedById;
import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;

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
