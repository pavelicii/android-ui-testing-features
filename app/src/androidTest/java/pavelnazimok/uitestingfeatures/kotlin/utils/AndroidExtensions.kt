package pavelnazimok.uitestingfeatures.kotlin.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import androidx.test.uiautomator.UiObject2

val targetContext: Context
    get() = ApplicationProvider.getApplicationContext()

val networkType: Int
    get() {
        val cm = targetContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager?

        if (cm != null) {
            val ni = cm.activeNetworkInfo

            if (ni != null) {
                if (ni.isConnected) {
                    @Suppress("DEPRECATION")
                    if (ni.type == ConnectivityManager.TYPE_WIFI) {
                        return ConnectivityManager.TYPE_WIFI
                    } else if (ni.type == ConnectivityManager.TYPE_MOBILE) {
                        return ConnectivityManager.TYPE_MOBILE
                    }
                }
            }
        }

        return -1
    }

val isNetworkConnected: Boolean
    get() {
        val cm = targetContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager?

        if (cm != null) {
            val ni = cm.activeNetworkInfo

            if (ni != null) {
                @Suppress("DEPRECATION")
                return ni.isConnected
                        && (ni.type == ConnectivityManager.TYPE_WIFI
                        || ni.type == ConnectivityManager.TYPE_MOBILE)
            }
        }

        return false
    }

val isAirplaneEnabled: Boolean
    @SuppressLint("InlinedApi")
    get() {
        return Settings.System.getInt(targetContext.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

val isMobileDataEnabled: Boolean
    get() {
        val tm = targetContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm.simState == TelephonyManager.SIM_STATE_READY) {
            Settings.Secure.getInt(targetContext.contentResolver, "mobile_data", 1) == 1
        } else {
            false
        }
    }

val isEmulator: Boolean
    get() {
        return (
            Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
            "google_sdk" == Build.PRODUCT
            )
    }

val runningActivityOfAppUnderTest: Activity?
    @SuppressLint("PrivateApi")
    get() {
        try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val activityThread = activityThreadClass.getMethod("currentActivityThread")
                .invoke(null)
            val activitiesField = activityThreadClass.getDeclaredField("mActivities")
            activitiesField.isAccessible = true
            val activities = activitiesField.get(activityThread) as Map<*, *>
            for (activityRecord in activities.values) {
                val activityRecordClass = activityRecord!!.javaClass
                val pausedField = activityRecordClass.getDeclaredField("paused")
                pausedField.isAccessible = true
                if (!pausedField.getBoolean(activityRecord)) {
                    val activityField = activityRecordClass.getDeclaredField("activity")
                    activityField.isAccessible = true
                    return activityField.get(activityRecord) as Activity
                }
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        return null
    }

fun resId(@IdRes viewId: Int): String {
    return targetContext.resources.getResourceEntryName(viewId)
}

fun resId(viewId: String): Int {
    return targetContext.resources.getIdentifier(
            viewId,
            null,
            targetContext.packageName
    )
}

fun resId(uiObject2: UiObject2): String {
    return uiObject2.resourceName.split(":id/")[1]
}

fun resText(@StringRes textId: Int): String {
    return targetContext.resources.getString(textId)
}