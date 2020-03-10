package pavelnazimok.uitestingfeatures.java.robots;

import pavelnazimok.uitestingfeatures.R;

import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;
import static com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem;
import static com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.resId;
import static pavelnazimok.uitestingfeatures.java.utils.EspressoExtensions.getListViewItemText;
import static pavelnazimok.uitestingfeatures.java.utils.EspressoExtensions.getListViewItemsCount;
import static pavelnazimok.uitestingfeatures.java.utils.EspressoExtensions.waitForView;

@SuppressWarnings("UnusedReturnValue")
public class ThirdScreenRobot {

    public int getListViewCount() {
        return getListViewItemsCount(resId("android:id/select_dialog_listview"));
    }

    public ThirdScreenRobot clickShowListView() {
        clickOn(R.id.button_listview);
        return this;
    }

    public String clickListViewItem(final int position) {
        final int listViewId = resId("android:id/select_dialog_listview");
        waitForView(listViewId, 5000);
        scrollListToPosition(listViewId, position);
        final String listViewItemText = getListViewItemText(listViewId, position);
        clickListItem(listViewId, position);
        return listViewItemText;
    }

    public ThirdScreenRobot assertSelectedItemIsDisplayed(final String itemText) {
        waitForView(R.id.label_item_selected, itemText, 10000);
        return this;
    }
}
