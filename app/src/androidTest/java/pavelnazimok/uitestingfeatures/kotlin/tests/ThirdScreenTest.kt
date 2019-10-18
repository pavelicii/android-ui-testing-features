package pavelnazimok.uitestingfeatures.kotlin.tests

import org.junit.Test
import pavelnazimok.uitestingfeatures.kotlin.robots.onMenu
import pavelnazimok.uitestingfeatures.kotlin.robots.onThirdScreen
import kotlin.random.Random

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