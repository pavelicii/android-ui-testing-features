package pavelnazimok.uitestingfeatures.kotlin.robots

import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import pavelnazimok.uitestingfeatures.R
import pavelnazimok.uitestingfeatures.kotlin.utils.getListViewItemText
import pavelnazimok.uitestingfeatures.kotlin.utils.getListViewItemsCount
import pavelnazimok.uitestingfeatures.kotlin.utils.resId
import pavelnazimok.uitestingfeatures.kotlin.utils.waitForView

fun onThirdScreen(func: ThirdScreenRobot.() -> Unit): ThirdScreenRobot {
    return ThirdScreenRobot().apply {
        assertThirdScreenIsDisplayed()
        func()
    }
}

class ThirdScreenRobot {

    fun getListViewCount(): Int {
        return getListViewItemsCount(resId("android:id/select_dialog_listview"))
    }

    fun clickShowListView() {
        clickOn(R.id.button_listview)
    }

    fun clickListViewItem(position: Int): String {
        val listViewId = resId("android:id/select_dialog_listview")
        waitForView(listViewId)
        scrollListToPosition(listViewId, position)
        val listViewItemText = getListViewItemText(listViewId, position)
        clickListItem(listViewId, position)
        return listViewItemText
    }

    fun assertThirdScreenIsDisplayed() {
        waitForView(R.id.screen_third)
    }

    fun assertSelectedItemIsDisplayed(itemText: String) {
        waitForView(R.id.label_item_selected, itemText)
    }
}
