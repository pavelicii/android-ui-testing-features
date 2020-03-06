package pavelnazimok.uitestingfeatures.kotlin.robots

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleepThread
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Assume.assumeFalse
import org.junit.Assume.assumeTrue
import pavelnazimok.uitestingfeatures.kotlin.utils.device
import pavelnazimok.uitestingfeatures.kotlin.utils.findById
import pavelnazimok.uitestingfeatures.kotlin.utils.findByIdAndText
import pavelnazimok.uitestingfeatures.kotlin.utils.findByIdAndTextPattern
import pavelnazimok.uitestingfeatures.kotlin.utils.findByText
import pavelnazimok.uitestingfeatures.kotlin.utils.findByTextPattern
import pavelnazimok.uitestingfeatures.kotlin.utils.isAirplaneEnabled
import pavelnazimok.uitestingfeatures.kotlin.utils.isEmulator
import pavelnazimok.uitestingfeatures.kotlin.utils.isMobileDataEnabled
import pavelnazimok.uitestingfeatures.kotlin.utils.isNetworkConnected
import pavelnazimok.uitestingfeatures.kotlin.utils.networkType
import pavelnazimok.uitestingfeatures.kotlin.utils.openNotification
import pavelnazimok.uitestingfeatures.kotlin.utils.pressBack
import pavelnazimok.uitestingfeatures.kotlin.utils.resText
import pavelnazimok.uitestingfeatures.kotlin.utils.runningActivityOfAppUnderTest
import pavelnazimok.uitestingfeatures.kotlin.utils.targetContext
import pavelnazimok.uitestingfeatures.kotlin.utils.waitUntilChecked
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.regex.Pattern

val BUTTON_VPN_SETTINGS_PATTERN: Pattern = Pattern.compile("\\S+:id/settings_button")

fun onDevice(func: DeviceRobot.() -> Unit) = DeviceRobot().apply { func() }

@SuppressLint("WifiManagerPotentialLeak")
class DeviceRobot {

    private var networkTypeBeforeDisabling = -1
    private var networkWasDisabledDuringTest = false

    val currentIp: String
        get() {
            val ip = getHttpResponseBody("https://ipapi.co/ip")
            assumeTrue("Could not get current IP, check logs", ip != null)

            val ipRegex = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b".toRegex()
            assumeTrue("Expected to get current IP, but got: $ip", ip!!.matches(ipRegex))

            return ip
        }

    fun getHttpResponseBody(url: String, retryAttempts: Int = 3): String? {
        sleepThread(2000)

        var responseBody: String? = null

        val httpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build()

        var response: Response? = null

        for (i in 1..retryAttempts) {
            try {
                val request = Request.Builder()
                        .url(url)
                        .build()

                response = httpClient.newCall(request).execute()

                if (response.isSuccessful) {
                    responseBody = response.body()!!.string()
                    Log.d("okhttp", "Response body: $responseBody")
                    break
                }
            } catch (e: IOException) {
                Log.d("okhttp", e.toString())
            } finally {
                response?.close()
                httpClient.connectionPool().evictAll()
            }

            sleepThread(1000)
        }

        httpClient.dispatcher().executorService().shutdown()
        httpClient.connectionPool().evictAll()
        httpClient.cache()?.close()

        return responseBody
    }

    /**
     * Enable or disable network on the device. Works with 2 types: Wi-Fi and Mobile data.
     * If there is no initial network on the device, it will try to enable Wi-Fi on real devices
     * and Mobile data on emulators.
     */
    @Suppress("DEPRECATION")
    fun setNetworkEnabled(enabled: Boolean, timeout: Long = 20000) {
        if (enabled && !isNetworkConnected) {
            if (networkTypeBeforeDisabling == ConnectivityManager.TYPE_WIFI) {
                setWifiEnabled(true)
            } else if (
                    networkTypeBeforeDisabling == ConnectivityManager.TYPE_MOBILE || isEmulator
            ) {
                setMobileDataEnabled(true)
            } else {
                setWifiEnabled(true)
            }

            val endTime = System.currentTimeMillis() + timeout
            while (!isNetworkConnected && System.currentTimeMillis() < endTime) {
                sleepThread(1000)
            }
            assumeTrue(
                    "Did not connect to any network within $timeout millis\nSkipping this test",
                    isNetworkConnected
            )
        } else if (!enabled && isNetworkConnected) {
            networkTypeBeforeDisabling = networkType

            if (networkTypeBeforeDisabling == ConnectivityManager.TYPE_WIFI) {
                setWifiEnabled(false)
            } else if (networkTypeBeforeDisabling == ConnectivityManager.TYPE_MOBILE) {
                setMobileDataEnabled(false)
            }

            val endTime = System.currentTimeMillis() + timeout
            while (isNetworkConnected && System.currentTimeMillis() < endTime) {
                sleepThread(1000)
            }
            assumeFalse(
                    "Did not disconnect from a network within $timeout millis\nSkipping this test",
                    isNetworkConnected
            )

            networkWasDisabledDuringTest = true
        }
    }

    fun setAirplaneEnabled(enabled: Boolean) {
        if (enabled && !isAirplaneEnabled || !enabled && isAirplaneEnabled) {
            val appActivity = runningActivityOfAppUnderTest

            try {
                val airplaneIntent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
                airplaneIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                targetContext.startActivity(airplaneIntent)
            } catch (e: ActivityNotFoundException) {
                val wirelessSettingsIntent = Intent("android.settings.WIRELESS_SETTINGS")
                wirelessSettingsIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                targetContext.startActivity(wirelessSettingsIntent)
            }

            var airplaneSwitch: UiObject2
            val airplaneTextPattern = Pattern.compile("Airplane mode|Aeroplane mode")

            try {
                airplaneSwitch =
                        findByIdAndTextPattern(android.R.id.title, airplaneTextPattern)
            } catch (e: TimeoutException) {
                findByIdAndText(android.R.id.title, "Advanced").click()
                airplaneSwitch =
                        findByIdAndTextPattern(android.R.id.title, airplaneTextPattern)
            }

            airplaneSwitch.click()

            if (appActivity != null) {
                openActivity(appActivity)
            }
        }
    }

    fun setWifiEnabled(enabled: Boolean): DeviceRobot {
        if (isEmulator && Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            fail<Any>("Simulated Wi-Fi is not available on emulators with API 24 " +
                    "(Android 7.0) or lower")
        }

        val wifiManager = targetContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        assertThat(wifiManager).isNotNull()

        wifiManager.isWifiEnabled = enabled

        return this
    }

    fun setMobileDataEnabled(enabled: Boolean) {
        assumeTrue(
                "Mobile data switching is not implemented for API 22 (Android 5.1) or lower",
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        )

        if (enabled && !isMobileDataEnabled || !enabled && isMobileDataEnabled) {
            val appActivity = runningActivityOfAppUnderTest

            val dataUsageIntent = Intent()
            dataUsageIntent.component = ComponentName("com.android.settings",
                    "com.android.settings.Settings\$DataUsageSummaryActivity")
            dataUsageIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            targetContext.startActivity(dataUsageIntent)

            findByIdAndTextPattern(
                    android.R.id.title,
                    Pattern.compile("Cellular data|Mobile data")
            ).click()

            if (appActivity != null) {
                openActivity(appActivity)
            }
        }
    }

    fun setAlwaysOnVpnEnabled(enabled: Boolean, appName: Int) {
        assumeTrue(
                "Always-on VPN settings are only available on API 24 (Android 7.0) or higher",
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        )

        val appActivity = runningActivityOfAppUnderTest

        openVpnSettings()

        val appMenuItemWithGear = device.wait(
                Until.findObject(By
                        .hasDescendant(By.text(resText(appName)))
                        .hasDescendant(By.res(BUTTON_VPN_SETTINGS_PATTERN))),
                10000
        )

        assumeTrue(
                "Always-on VPN settings are not available. Note that some manufacturers " +
                        "(e.g. Huawei) may provide no access to them",
                appMenuItemWithGear != null
        )

        appMenuItemWithGear.findObject(By.res(BUTTON_VPN_SETTINGS_PATTERN)).click()

        val alwaysOnSwitch = findById("switch_widget")
        if (enabled) {
            if (alwaysOnSwitch.isChecked) {
                alwaysOnSwitch.click()
                alwaysOnSwitch.waitUntilChecked(false)
            }
            alwaysOnSwitch.click()
            alwaysOnSwitch.waitUntilChecked(true)
        } else {
            if (!alwaysOnSwitch.isChecked) {
                alwaysOnSwitch.click()
                alwaysOnSwitch.waitUntilChecked(true)
            }
            alwaysOnSwitch.click()
            alwaysOnSwitch.waitUntilChecked(false)
        }

        if (appActivity != null) {
            openActivity(appActivity)
        }
    }

    fun forgetVpn(appName: Int) {
        assumeTrue(
                "VPN could be forgotten manually only on API 24 (Android 7.0) or higher",
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        )

        val appActivity = runningActivityOfAppUnderTest

        openVpnSettings()

        val appMenuItemWithGear = device.wait(
                Until.findObject(By
                        .hasDescendant(By.text(resText(appName)))
                        .hasDescendant(By.res(BUTTON_VPN_SETTINGS_PATTERN))),
                10000
        )

        assumeTrue(
                "VPN could not be forgotten on this device. Note that some manufacturers " +
                        "(e.g. Huawei) may provide no such option", appMenuItemWithGear != null
        )

        appMenuItemWithGear.findObject(By.res(BUTTON_VPN_SETTINGS_PATTERN)).click()

        findByTextPattern(Pattern.compile("Forget VPN|Delete VPN profile")).click()
        findById(android.R.id.button2).click()

        if (appActivity != null) {
            openActivity(appActivity)
        }
    }

    fun enableNetworkAfterTest() {
        if (networkWasDisabledDuringTest) {
            setNetworkEnabled(true)
            networkWasDisabledDuringTest = false
        }
    }

    fun openActivity(activity: Activity) {
        val activityIntent = Intent(targetContext, activity.javaClass)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        targetContext.startActivity(activityIntent)
        sleepThread(2000)
    }

    fun openNotificationShade() {
        assertThat(openNotification())
                .withFailMessage("Failed to open Notification Shade")
                .isTrue()
    }

    fun openVpnSettings() {
        val vpnSettingsIntent = Intent("android.net.vpn.SETTINGS")
        vpnSettingsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        targetContext.startActivity(vpnSettingsIntent)
        assertVpnSettingsScreenIsDisplayed()
    }

    fun getClipboardText():String {
        var clipboardText = ""
        val latch = CountDownLatch(1)

        Handler(Looper.getMainLooper()).post {
            val clipboard = targetContext.getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager
            var clip: ClipData? = null
            var item: ClipData.Item? = null

            clip = clipboard.primaryClip

            if (clip != null) {
                item = clip.getItemAt(0)
            }
            if (item != null) {
                clipboardText = item.text.toString()
            }
            latch.countDown()
        }

        latch.await(10, TimeUnit.SECONDS)

        return clipboardText
    }

    fun assertVpnSettingsScreenIsDisplayed() {
        assertThat(findByText("VPN").applicationPackage)
                .`as`("Check if Android VPN Settings screen was opened")
                .isEqualTo("com.android.settings")
    }

    fun wakeUp() {
        if (!device.isScreenOn) {
            device.wakeUp()
        }
    }

    fun back() {
        pressBack()
    }

    fun sleep(timeout: Long) {
        sleepThread(timeout)
    }
}