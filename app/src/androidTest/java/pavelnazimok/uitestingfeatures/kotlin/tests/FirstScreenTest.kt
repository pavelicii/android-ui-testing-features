package pavelnazimok.uitestingfeatures.kotlin.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import pavelnazimok.uitestingfeatures.kotlin.robots.onFirstScreen
import pavelnazimok.uitestingfeatures.kotlin.utils.CustomAnnotation

@RunWith(AndroidJUnit4::class)
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
