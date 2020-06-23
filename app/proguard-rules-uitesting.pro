# Multidex rules for UI tests on API 19 or below
# For more details, see
#   https://spin.atomicobject.com/2018/07/16/support-kitkat-multidex/

-keep public class com.pavelnazimok.uitesting.java.robots.** { *; }
-keep public class com.pavelnazimok.uitesting.java.tests.** { *; }
-keep public class com.pavelnazimok.uitesting.java.utils.** { *; }
-keep public class com.pavelnazimok.uitesting.kotlin.robots.** { *; }
-keep public class com.pavelnazimok.uitesting.kotlin.tests.** { *; }
-keep public class com.pavelnazimok.uitesting.kotlin.utils.** { *; }

-keep class androidx.test.internal.** { *; }
-keep class org.junit.** { *; }