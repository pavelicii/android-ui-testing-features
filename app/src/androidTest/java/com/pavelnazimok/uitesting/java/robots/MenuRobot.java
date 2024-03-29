package com.pavelnazimok.uitesting.java.robots;

import com.pavelnazimok.uitesting.R;

import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;

@SuppressWarnings("UnusedReturnValue")
public class MenuRobot {

    public MenuRobot clickFirstScreen() {
        clickOn(R.id.nav_first);
        return this;
    }

    public MenuRobot clickSecondScreen() {
        clickOn(R.id.nav_second);
        return this;
    }

    public MenuRobot clickThirdScreen() {
        clickOn(R.id.nav_third);
        return this;
    }
}
