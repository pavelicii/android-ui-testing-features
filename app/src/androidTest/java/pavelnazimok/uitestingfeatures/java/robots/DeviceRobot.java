package pavelnazimok.uitestingfeatures.java.robots;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions;
import pavelnazimok.uitestingfeatures.java.utils.UiAutomatorTimeoutException;

import static com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleepThread;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.getNetworkType;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.getRunningActivityOfAppUnderTest;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.isAirplaneEnabled;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.isEmulator;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.isMobileDataEnabled;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.isNetworkConnected;
import static pavelnazimok.uitestingfeatures.java.utils.AndroidExtensions.resText;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.device;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.findById;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.findByIdAndText;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.findByIdAndTextPattern;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.findByText;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.findByTextPattern;
import static pavelnazimok.uitestingfeatures.java.utils.UiAutomatorExtensions.setChecked;

@SuppressWarnings({ "UnusedReturnValue", "WeakerAccess" })
public class DeviceRobot {
    public static final Pattern BUTTON_VPN_SETTINGS_PATTERN = Pattern.compile("\\S+:id/settings_button");

    private Context targetContext() {
        return ApplicationProvider.getApplicationContext();
    }

    private int networkTypeBeforeDisabling = -1;
    private boolean networkWasDisabledDuringTest = false;

    public Response getHttpResponse(final String url) throws IOException {
        final int retryAttempts = 3;

        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();

        IOException requestException = null;
        Response response = null;

        for (int i = 0; i < retryAttempts; ++i) {
            try {
                final Request request = new Request.Builder()
                        .url(url)
                        .build();

                if (response != null) {
                    response.close();
                }

                response = httpClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    break;
                }
            } catch (IOException e) {
                Log.d("UI Test: getHttpResponse", "Error accessing " + url);

                if (i == retryAttempts - 1) {
                    requestException = e;
                }
            } finally {
                httpClient.connectionPool().evictAll();
            }

            sleepThread(1000);
        }

        httpClient.dispatcher().executorService().shutdown();
        if (httpClient.cache() != null) {
            //noinspection ConstantConditions
            httpClient.cache().close();
        }

        if (requestException != null) {
            throw requestException;
        }

        return response;
    }

    /**
     * Enable or disable network on the device. Works with 2 types: Wi-Fi and Mobile data.
     * If there is no initial network on the device, it will try to enable Wi-Fi on real devices
     * and Mobile data on emulators.
     */
    public DeviceRobot setNetworkEnabled(final boolean enabled) {
        final long networkTimeout = 20000;

        if (enabled && !isNetworkConnected()) {
            if (networkTypeBeforeDisabling == ConnectivityManager.TYPE_WIFI) {
                setWifiEnabled(true);
            } else if (networkTypeBeforeDisabling == ConnectivityManager.TYPE_MOBILE || isEmulator()) {
                setMobileDataEnabled(true);
            } else {
                setWifiEnabled(true);
            }

            final long endTime = System.currentTimeMillis() + networkTimeout;
            while (!isNetworkConnected() && System.currentTimeMillis() < endTime) {
                sleepThread(1000);
            }
            assumeTrue(
                    String.format("Did not connect to any network within %d millis\nSkipping this test", networkTimeout),
                    isNetworkConnected()
            );
        } else if (!enabled && isNetworkConnected()) {
            networkTypeBeforeDisabling = getNetworkType();

            if (networkTypeBeforeDisabling == ConnectivityManager.TYPE_WIFI) {
                setWifiEnabled(false);
            } else if (networkTypeBeforeDisabling == ConnectivityManager.TYPE_MOBILE) {
                setMobileDataEnabled(false);
            }

            final long endTime = System.currentTimeMillis() + networkTimeout;
            while (isNetworkConnected() && System.currentTimeMillis() < endTime) {
                sleepThread(1000);
            }
            assumeFalse(
                    String.format("Did not disconnect from a network within %d millis\nSkipping this test", networkTimeout),
                    isNetworkConnected()
            );

            networkWasDisabledDuringTest = true;
        }

        return this;
    }

    public DeviceRobot setAirplaneEnabled(final boolean enabled) {
        if ((enabled && !isAirplaneEnabled()) || (!enabled && isAirplaneEnabled())) {
            final Activity appActivity = getRunningActivityOfAppUnderTest();

            try {
                final Intent airplaneIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                airplaneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                targetContext().startActivity(airplaneIntent);
            } catch (ActivityNotFoundException e) {
                final Intent wirelessSettingsIntent = new Intent("android.settings.WIRELESS_SETTINGS");
                wirelessSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                targetContext().startActivity(wirelessSettingsIntent);
            }

            UiObject2 airplaneSwitch;
            final Pattern airplaneTextPattern = Pattern.compile("Airplane mode|Aeroplane mode");

            try {
                airplaneSwitch = findByIdAndTextPattern(android.R.id.title, airplaneTextPattern, 5000);
            } catch (UiAutomatorTimeoutException e) {
                findByIdAndText(android.R.id.title, "Advanced", 500).click();
                airplaneSwitch = findByIdAndTextPattern(android.R.id.title, airplaneTextPattern, 3000);
            }

            airplaneSwitch.click();

            if (appActivity != null) {
                openActivity(appActivity);
            }
        }

        return this;
    }

    public DeviceRobot setWifiEnabled(final boolean enabled) {
        if (isEmulator() && Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            //noinspection ResultOfMethodCallIgnored
            fail("Simulated Wi-Fi is not available on emulators with API 24 (Android 7.0) or lower");
        }

        final WifiManager wifiManager = (WifiManager) targetContext().getSystemService(Context.WIFI_SERVICE);
        assertThat(wifiManager).isNotNull();

        wifiManager.setWifiEnabled(enabled);

        return this;
    }

    public DeviceRobot setMobileDataEnabled(final boolean enabled) {
        assumeTrue(
                "Mobile data switching is not implemented for API 22 (Android 5.1) or lower",
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        );

        if ((enabled && !isMobileDataEnabled()) || (!enabled && isMobileDataEnabled())) {
            final Activity appActivity = getRunningActivityOfAppUnderTest();

            final Intent dataUsageIntent = new Intent();
            dataUsageIntent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
            dataUsageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            targetContext().startActivity(dataUsageIntent);

            findByIdAndTextPattern(android.R.id.title, Pattern.compile("Cellular data|Mobile data"), 5000).click();

            if (appActivity != null) {
                openActivity(appActivity);
            }
        }

        return this;
    }

    public DeviceRobot setAlwaysOnVpnEnabled(final boolean enabled, final int appName) {
        assumeTrue(
                "Always-on VPN settings are only available on API 24 (Android 7.0) or higher",
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        );

        final Activity appActivity = getRunningActivityOfAppUnderTest();

        openVpnSettings();

        final UiObject2 appMenuItemWithGear = device().wait(
                Until.findObject(By
                        .hasDescendant(By.text(resText(appName)))
                        .hasDescendant(By.res(BUTTON_VPN_SETTINGS_PATTERN))),
                10000
        );

        assumeTrue(
                "Always-on VPN settings are not available. Note that some manufacturers (e.g. Huawei, Sony, LG, and others) may provide no access to them",
                appMenuItemWithGear != null
        );

        appMenuItemWithGear.findObject(By.res(BUTTON_VPN_SETTINGS_PATTERN)).click();

        final UiObject2 alwaysOnSwitch = findById("switch_widget", 10000);
        if (enabled) {
            setChecked(alwaysOnSwitch, false);
            setChecked(alwaysOnSwitch, true);
        } else {
            setChecked(alwaysOnSwitch, true);
            setChecked(alwaysOnSwitch, false);
        }

        if (appActivity != null) {
            openActivity(appActivity);
        }

        return this;
    }

    public DeviceRobot forgetVpnProfile(final int appName) {
        assumeTrue(
                "VPN could be forgotten manually only on API 24 (Android 7.0) or higher",
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        );

        final Activity appActivity = getRunningActivityOfAppUnderTest();

        openVpnSettings();

        final UiObject2 appMenuItemWithGear = device().wait(
                Until.findObject(By
                        .hasDescendant(By.text(resText(appName)))
                        .hasDescendant(By.res(BUTTON_VPN_SETTINGS_PATTERN))),
                10000
        );

        assumeTrue(
                "VPN could not be forgotten on this device. Note that some manufacturers " +
                        "(e.g. Huawei) may provide no such option", appMenuItemWithGear != null
        );

        appMenuItemWithGear.findObject(By.res(BUTTON_VPN_SETTINGS_PATTERN)).click();

        findByTextPattern(Pattern.compile("Forget VPN|Delete VPN profile"), 5000).click();
        findById(android.R.id.button2, 3000).click();

        if (appActivity != null) {
            openActivity(appActivity);
        }

        return this;
    }

    public DeviceRobot enableNetworkAfterTest() {
        if (networkWasDisabledDuringTest) {
            setNetworkEnabled(true);
            networkWasDisabledDuringTest = false;
        }

        return this;
    }

    public DeviceRobot openActivity(final Class<? extends Activity> activity) {
        final Intent activityIntent = new Intent(targetContext(), activity);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        targetContext().startActivity(activityIntent);
        sleepThread(2000);
        return this;
    }

    public DeviceRobot openActivity(final Activity activity) {
        return openActivity(activity.getClass());
    }

    public DeviceRobot openVpnSettings() {
        final Intent vpnSettingsIntent = new Intent("android.net.vpn.SETTINGS");
        vpnSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        targetContext().startActivity(vpnSettingsIntent);
        return assertVpnSettingsScreenIsDisplayed();
    }

    public DeviceRobot openNotificationShade() {
        assertThat(UiAutomatorExtensions.openNotificationShade())
                .withFailMessage("Failed to open Notification Shade")
                .isTrue();
        return this;
    }

    public DeviceRobot assertVpnSettingsScreenIsDisplayed() {
        assertThat(findByText("VPN", 10000).getApplicationPackage())
                .as("Check if Android VPN Settings screen was opened")
                .isEqualTo("com.android.settings");
        return this;
    }

    public DeviceRobot assertNotificationIsDisplayed(final String title, final String message) {
        findByText(title, 10000);
        findByText(message, 10000);
        return this;
    }

    public String getClipboardText() {
        final AtomicReference<String> clipboardText = new AtomicReference<>("");
        final CountDownLatch latch = new CountDownLatch(1);
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        final ClipboardManager clipboard = (ClipboardManager) targetContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = null;
                        ClipData.Item item = null;

                        if (clipboard != null) {
                            clip = clipboard.getPrimaryClip();
                        }
                        if (clip != null) {
                            item = clip.getItemAt(0);
                        }
                        if (item != null) {
                            clipboardText.set(item.getText().toString());
                        }
                        latch.countDown();
                    }
                }
        );
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return clipboardText.get();
    }

    public DeviceRobot wakeUp() {
        try {
            device().wakeUp();
        } catch (RemoteException e) {
            throw new RuntimeException("Could not wake device up");
        }
        return this;
    }

    public DeviceRobot pressBack() {
        UiAutomatorExtensions.pressBack();
        return this;
    }

    public DeviceRobot sleep(final long timeout) {
        sleepThread(timeout);
        return this;
    }
}
