package com.pavelnazimok.uitesting.java.tests;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.pavelnazimok.uitesting.java.robots.FirstScreenRobot;
import com.pavelnazimok.uitesting.java.utils.CustomAnnotation;
import com.pavelnazimok.uitesting.testrail.CaseId;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FirstScreenTest extends BaseTest {

    final FirstScreenRobot firstScreenRobot = new FirstScreenRobot();

    @Test
    @CustomAnnotation
    @CaseId(1)
    public void coffeeCountAndTotalPrice_afterClickIncrement_shouldIncrease() {
        final int coffeePrice = firstScreenRobot.getCoffeePrice();

        firstScreenRobot
                .assertFirstScreenIsDisplayed()
                .clickCoffeeIncrement()
                .assertCoffeeCountEquals(1)
                .assertTotalPriceEquals(coffeePrice)
                .clickCoffeeIncrement()
                .assertCoffeeCountEquals(2)
                .assertTotalPriceEquals(coffeePrice * 2)
                .clickCoffeeIncrement()
                .assertCoffeeCountEquals(3)
                .assertTotalPriceEquals(coffeePrice * 3);
    }

    // This ↓ test should fail!
    @Test
    public void coffeeCountAndTotalPrice_afterClickDecrement_shouldDecreaseNotLessThanZero() {
        firstScreenRobot
                .assertFirstScreenIsDisplayed()
                .clickCoffeeIncrement()
                .clickCoffeeDecrement()
                .assertCoffeeCountEquals(0)
                .assertTotalPriceEquals(0)
                .clickCoffeeDecrement()
                .assertCoffeeCountEquals(0)
                .assertTotalPriceEquals(0);
    }
}
