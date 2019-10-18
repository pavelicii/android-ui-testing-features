# Multidex rules for UI tests on API 19 or below
# For more details, see
#   https://spin.atomicobject.com/2018/07/16/support-kitkat-multidex/

-keep public class pavelnazimok.uitestingfeatures.java.robots.** { *; }
-keep public class pavelnazimok.uitestingfeatures.java.tests.** { *; }
-keep public class pavelnazimok.uitestingfeatures.java.utils.** { *; }
-keep public class pavelnazimok.uitestingfeatures.kotlin.robots.** { *; }
-keep public class pavelnazimok.uitestingfeatures.kotlin.tests.** { *; }
-keep public class pavelnazimok.uitestingfeatures.kotlin.utils.** { *; }

-keep class androidx.test.internal.** { *; }
-keep class org.junit.** { *; }