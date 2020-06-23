package com.pavelnazimok.uitesting.java.tests;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.pavelnazimok.uitesting.java.robots.MenuRobot;
import com.pavelnazimok.uitesting.java.robots.SecondScreenRobot;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SecondScreenTest extends BaseTest {

    final SecondScreenRobot secondScreenRobot = new SecondScreenRobot();
    final MenuRobot menuRobot = new MenuRobot();

    @Test
    public void alertDialog_afterClickOk_shouldNotBeDisplayed() {
        menuRobot
                .clickSecondScreen();

        secondScreenRobot
                .clickAlertDialog()
                .clickOkInAlertDialog()
                .assertAlertDialogIsNotDisplayed();
    }
}
