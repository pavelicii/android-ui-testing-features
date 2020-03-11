package pavelnazimok.uitestingfeatures.kotlin.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import pavelnazimok.uitestingfeatures.kotlin.robots.onMenu
import pavelnazimok.uitestingfeatures.kotlin.robots.onThirdScreen
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
