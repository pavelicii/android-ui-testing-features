package pavelnazimok.uitestingfeatures.java.tests;

import android.Manifest;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import pavelnazimok.uitestingfeatures.MainActivity;
import pavelnazimok.uitestingfeatures.java.robots.DeviceRobot;
import pavelnazimok.uitestingfeatures.java.robots.FirstScreenRobot;
import pavelnazimok.uitestingfeatures.java.robots.MenuRobot;
import pavelnazimok.uitestingfeatures.java.robots.SecondScreenRobot;
import pavelnazimok.uitestingfeatures.java.robots.ThirdScreenRobot;
import pavelnazimok.uitestingfeatures.java.utils.CustomAnnotation;

import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.isNetworkConnected;

public abstract class BaseTest {

    final FirstScreenRobot firstScreenRobot = new FirstScreenRobot();
    final SecondScreenRobot secondScreenRobot = new SecondScreenRobot();
    final ThirdScreenRobot thirdScreenRobot = new ThirdScreenRobot();
    final MenuRobot menuRobot = new MenuRobot();
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
