package com.pavelnazimok.uitesting.kotlin.robots

import com.pavelnazimok.uitesting.R
import com.pavelnazimok.uitesting.kotlin.utils.getText
import com.pavelnazimok.uitesting.kotlin.utils.waitForView
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

fun onFirstScreen(func: FirstScreenRobot.() -> Unit): FirstScreenRobot {
    return FirstScreenRobot().apply {
        assertFirstScreenIsDisplayed()
        func()
    }
}

class FirstScreenRobot {

    fun getCoffeePrice(): Int {
        return Integer.valueOf(getText(R.id.label_coffee_price))
    }

    fun clickCoffeeIncrement() {
        clickOn(R.id.button_coffee_increment)
    }

    fun clickCoffeeDecrement() {
        clickOn(R.id.button_coffee_decrement)
    }

    fun assertFirstScreenIsDisplayed() {
        waitForView(R.id.screen_first)
    }

    fun assertCoffeeCountEquals(count: Int) {
        assertDisplayed(R.id.label_coffee_count, count.toString())
    }

    fun assertCoffeePriceEquals(price: Int) {
        assertDisplayed(R.id.label_coffee_price, price.toString())
    }

    fun assertTotalPriceEquals(price: Int) {
        assertDisplayed(R.id.label_total_price, price.toString())
    }
}
