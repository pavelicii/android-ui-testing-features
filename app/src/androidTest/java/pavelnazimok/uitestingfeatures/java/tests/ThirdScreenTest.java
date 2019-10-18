package pavelnazimok.uitestingfeatures.java.tests;

import org.junit.Test;

import java.util.Random;

public class ThirdScreenTest extends BaseTest {

    @Test
    public void itemText_afterSelectInListView_shouldBeDisplayed() {
        menuRobot
                .clickThirdScreen();

        String selectedItemText = thirdScreenRobot
                .clickShowListView()
                .clickListViewItem(new Random().nextInt(thirdScreenRobot.getListViewCount()));

        thirdScreenRobot
                .assertSelectedItemIsDisplayed(selectedItemText);
    }
}
