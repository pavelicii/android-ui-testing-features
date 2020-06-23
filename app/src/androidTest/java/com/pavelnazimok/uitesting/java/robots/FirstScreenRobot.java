package com.pavelnazimok.uitesting.java.robots;

import com.pavelnazimok.uitesting.R;

import static com.pavelnazimok.uitesting.java.utils.EspressoExtensions.getText;
import static com.pavelnazimok.uitesting.java.utils.EspressoExtensions.waitForView;
import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed;
import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;

@SuppressWarnings("UnusedReturnValue")
public class FirstScreenRobot {

    public int getCoffeePrice() {
        return Integer.valueOf(getText(R.id.label_coffee_price));
    }

    public FirstScreenRobot clickCoffeeIncrement() {
        clickOn(R.id.button_coffee_increment);
        return this;
    }

    public FirstScreenRobot clickCoffeeDecrement() {
        clickOn(R.id.button_coffee_decrement);
        return this;
    }

    public FirstScreenRobot assertFirstScreenIsDisplayed() {
        waitForView(R.id.screen_first, 5000);
        return this;
    }

    public FirstScreenRobot assertCoffeeCountEquals(final int count) {
        assertDisplayed(R.id.label_coffee_count, String.valueOf(count));
        return this;
    }

    public FirstScreenRobot assertCoffeePriceEquals(final int price) {
        assertDisplayed(R.id.label_coffee_price, String.valueOf(price));
        return this;
    }

    public FirstScreenRobot assertTotalPriceEquals(final int price) {
        assertDisplayed(R.id.label_total_price, String.valueOf(price));
        return this;
    }
}
