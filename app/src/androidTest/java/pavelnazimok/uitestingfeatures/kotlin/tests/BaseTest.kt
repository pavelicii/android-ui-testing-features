package pavelnazimok.uitestingfeatures.kotlin.tests

import android.Manifest
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.model.Statement
import pavelnazimok.uitestingfeatures.MainActivity
import pavelnazimok.uitestingfeatures.kotlin.robots.NETWORK_WAS_DISABLED_DURING_TEST
import pavelnazimok.uitestingfeatures.kotlin.robots.onDevice
import pavelnazimok.uitestingfeatures.kotlin.utils.CustomAnnotation
import pavelnazimok.uitestingfeatures.kotlin.utils.isNetworkConnected
import pavelnazimok.uitestingfeatures.kotlin.utils.targetContext

@RunWith(AndroidJUnit4::class)
abstract class BaseTest {

    private val runtimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val setUpRule = object : ExternalResource() {
        override fun apply(base: Statement, description: Description): Statement {
            if (description.getAnnotation(CustomAnnotation::class.java) != null) {
                println("This test is annotated with CustomAnnotation")
            }

            return super.apply(base, description)
        }

        override fun before() {
            if (!isNetworkConnected) {
                onDevice {
                    setNetworkEnabled(true)
                }
            }

            activityTestRule.launchActivity(null)
        }
    }

    private val tearDownRule = object : ExternalResource() {
        override fun after() {
            if (NETWORK_WAS_DISABLED_DURING_TEST) {
                onDevice {
                    setNetworkEnabled(true)
                }
            }
        }
    }

    private val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @get:Rule
    var ruleChain: RuleChain = RuleChain.emptyRuleChain()
            .around(runtimePermissionRule)
            .around(setUpRule)
            .around(activityTestRule)
            .around(tearDownRule)

    internal fun openMainActivity() {
        val mainActivityIntent = Intent(targetContext, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        targetContext.startActivity(mainActivityIntent)
    }
}