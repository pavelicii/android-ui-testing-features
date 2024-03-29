package com.pavelnazimok.uitesting.kotlin.tests

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.pavelnazimok.uitesting.app.MainActivity
import com.pavelnazimok.uitesting.kotlin.robots.onDevice
import com.pavelnazimok.uitesting.kotlin.utils.CustomAnnotation
import com.pavelnazimok.uitesting.kotlin.utils.isNetworkConnected
import org.junit.Rule
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.runner.Description
import org.junit.runners.model.Statement

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

            onDevice {
                wakeUp()
            }
        }
    }

    private val tearDownRule = object : ExternalResource() {
        override fun after() {
            onDevice {
                enableNetworkAfterTest()
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
}
