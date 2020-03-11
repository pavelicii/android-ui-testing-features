package pavelnazimok.uitestingfeatures.java.tests;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class ThirdScreenTest extends BaseTest {

    @Test
    public void itemText_afterSelectInListView_shouldBeDisplayed() {
        menuRobot
                .clickThirdScreen();

        final String selectedItemText = thirdScreenRobot
                .clickShowListView()
                .clickListViewItem(new Random().nextInt(thirdScreenRobot.getListViewCount()));

        thirdScreenRobot
                .assertSelectedItemIsDisplayed(selectedItemText);
    }
}
