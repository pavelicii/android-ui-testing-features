package pavelnazimok.uitestingfeatures.java.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.UiObject2;

import java.lang.reflect.Field;

public final class AndroidExtensions {

    private AndroidExtensions() {
    }

    public static String resId(final int viewId) {
        return ApplicationProvider.getApplicationContext().getResources().getResourceEntryName(viewId);
    }

    public static int resId(final String viewId) {
        return ApplicationProvider.getApplicationContext().getResources().getIdentifier(
                viewId,
                null,
                ApplicationProvider.getApplicationContext().getPackageName()
        );
    }

    public static String resId(final UiObject2 object) {
        return object.getResourceName().split(":id/")[1];
    }

    public static String resText(final int stringId) {
        return ApplicationProvider.getApplicationContext().getResources().getString(stringId);
    }

    public static boolean isNetworkConnected() {
        final ConnectivityManager cm = (ConnectivityManager) ApplicationProvider.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            final NetworkInfo ni = cm.getActiveNetworkInfo();

            if (ni != null) {
                return ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE);
            }
        }

        return false;
    }

    public static boolean isAirplaneEnabled() {
        return Settings.System.getInt(ApplicationProvider.getApplicationContext().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    public static boolean isMobileDataEnabled() {
        final TelephonyManager tm = (TelephonyManager) ApplicationProvider.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return Settings.Secure.getInt(ApplicationProvider.getApplicationContext().getContentResolver(), "mobile_data", 1) == 1;
        }
        return false;
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static int getNetworkType() {
        final ConnectivityManager cm = (ConnectivityManager) ApplicationProvider.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            final NetworkInfo ni = cm.getActiveNetworkInfo();

            if (ni != null) {
                if (ni.isConnected()) {
                    if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
                        return ConnectivityManager.TYPE_WIFI;
                    } else if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
                        return ConnectivityManager.TYPE_MOBILE;
                    }
                }
            }
        }

        return -1;
    }

    public static Activity getRunningActivityOfAppUnderTest() {
        try {
            final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            final Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            final Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            final ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
            if (activities != null) {
                for (Object activityRecord : activities.values()) {
                    final Class activityRecordClass = activityRecord.getClass();
                    final Field pausedField = activityRecordClass.getDeclaredField("paused");
                    pausedField.setAccessible(true);
                    if (!pausedField.getBoolean(activityRecord)) {
                        final Field activityField = activityRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);
                        return (Activity) activityField.get(activityRecord);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
