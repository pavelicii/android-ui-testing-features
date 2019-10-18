# Multidex rules for UI tests on API 19 or below
# For more details, see
#   https://spin.atomicobject.com/2018/07/16/support-kitkat-multidex/

-keep public class pavelnazimok.uitestingfeatures.** { *; }

-keep class androidx.test.internal.** { *; }
-keep class org.junit.** { *; }