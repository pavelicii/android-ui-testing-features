package pavelnazimok.uitestingfeatures.java.tests;

import org.junit.Test;

import pavelnazimok.uitestingfeatures.java.utils.CustomAnnotation;
import pavelnazimok.uitestingfeatures.testrail.CaseId;


public class FirstScreenTest extends BaseTest {

    @Test
    @CustomAnnotation
    @CaseId(1)
    public void coffeeCountAndTotalPrice_afterClickIncrement_shouldIncrease() {
        int coffeePrice = firstScreenRobot.getCoffeePrice();

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
