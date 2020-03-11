package pavelnazimok.uitestingfeatures.kotlin.tests

import org.junit.Test
import pavelnazimok.uitestingfeatures.kotlin.robots.onFirstScreen
import pavelnazimok.uitestingfeatures.kotlin.utils.CustomAnnotation

class FirstScreenTest : BaseTest() {

    @Test
    @CustomAnnotation
    fun coffeeCountAndTotalPrice_afterClickIncrement_shouldIncrease() {
        onFirstScreen {
            val coffeePrice = getCoffeePrice()

            assertFirstScreenIsDisplayed()
            clickCoffeeIncrement()
            assertCoffeeCountEquals(1)
            assertTotalPriceEquals(coffeePrice)
            clickCoffeeIncrement()
            assertCoffeeCountEquals(2)
            assertTotalPriceEquals(coffeePrice * 2)
            clickCoffeeIncrement()
            assertCoffeeCountEquals(3)
            assertTotalPriceEquals(coffeePrice * 3)
        }
    }

    // This ↓ test should fail!
    @Test
    fun coffeeCountAndTotalPrice_afterClickDecrement_shouldDecreaseNotLessThanZero() {
        onFirstScreen {
            assertFirstScreenIsDisplayed()
            clickCoffeeIncrement()
            clickCoffeeDecrement()
            assertCoffeeCountEquals(0)
            assertTotalPriceEquals(0)
            clickCoffeeDecrement()
            assertCoffeeCountEquals(0)
            assertTotalPriceEquals(0)
        }
    }
}
