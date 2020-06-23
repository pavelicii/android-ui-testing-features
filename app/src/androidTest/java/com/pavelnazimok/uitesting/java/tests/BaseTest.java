package com.pavelnazimok.uitesting.java.tests;

import android.Manifest;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.pavelnazimok.uitesting.app.MainActivity;
import com.pavelnazimok.uitesting.java.robots.DeviceRobot;
import com.pavelnazimok.uitesting.java.utils.CustomAnnotation;

import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static com.pavelnazimok.uitesting.java.utils.AndroidExtensions.isNetworkConnected;

public abstract class BaseTest {

    final DeviceRobot deviceRobot = new DeviceRobot();

    private GrantPermissionRule runtimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_COARSE_LOCATION
    );

    private ExternalResource setUpRule = new ExternalResource() {
        @Override
        public Statement apply(final Statement base, final Description description) {
            if (description.getAnnotation(CustomAnnotation.class) != null) {
                System.out.println("This test is annotated with CustomAnnotation");
            }

            return super.apply(base, description);
        }

        @Override
        protected void before() {
            if (!isNetworkConnected()) {
                deviceRobot.setNetworkEnabled(true);
            }

            activityTestRule.launchActivity(null);

            deviceRobot.wakeUp();
        }
    };

    private ExternalResource tearDownRule = new ExternalResource() {
        @Override
        protected void after() {
            deviceRobot.enableNetworkAfterTest();
        }
    };

    private ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public RuleChain ruleChain = RuleChain.emptyRuleChain()
            .around(runtimePermissionRule)
            .around(setUpRule)
            .around(activityTestRule)
            .around(tearDownRule);
}
