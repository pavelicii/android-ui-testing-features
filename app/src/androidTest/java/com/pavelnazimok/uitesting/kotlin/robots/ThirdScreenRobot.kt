package com.pavelnazimok.uitesting.kotlin.robots

import com.pavelnazimok.uitesting.R
import com.pavelnazimok.uitesting.kotlin.utils.getListViewItemText
import com.pavelnazimok.uitesting.kotlin.utils.getListViewItemsCount
import com.pavelnazimok.uitesting.kotlin.utils.resId
import com.pavelnazimok.uitesting.kotlin.utils.waitForView
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition

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
