package com.pavelnazimok.uitesting.kotlin.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pavelnazimok.uitesting.kotlin.robots.onMenu
import com.pavelnazimok.uitesting.kotlin.robots.onThirdScreen
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class ThirdScreenTest : BaseTest() {

    @Test
    fun itemText_afterSelectInListView_shouldBeDisplayed() {
        onMenu {
            clickThirdScreen()
        }

        onThirdScreen {
            clickShowListView()
            val selectedItemText = clickListViewItem(Random.nextInt(getListViewCount()))
            assertSelectedItemIsDisplayed(selectedItemText)
        }
    }
}
