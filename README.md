# Android UI Testing Features

I use this repository as storage of Android UI testing practices from my experience: test patterns, naming conventions, wrapper methods for test and device actions, etc.
Or as a part of my programming portfolio.

Some of these come from single projects, others — from common test codebases.
They are written both in Java and Kotlin.

:cactus: It's not a separate framework, it's just helpers and aliases for personal reuse in the future.

## App&Tests

In this repository, there are a simple Android app and Instrumentation tests.

The app has only 3 screens:

![First Screen](/docres/screen_first.png) ![Second Screen](/docres/screen_second.png) ![Third Screen](/docres/screen_third.png)
 
There is a test class for each screen. Tests are written based on concepts and tools which are described below.

## Tools

All testing stuff is made with the help of the following frameworks:

* [Espresso](https://developer.android.com/training/testing/espresso/) — Instrumentation testing framework by Google
* [Barista](https://github.com/SchibstedSpain/Barista) — built on top of Espresso and provides more simple API
* [UI Automator](https://developer.android.com/training/testing/ui-automator) — allows you to interact with system apps and dialogs
* [AssertJ](https://github.com/joel-costigliola/assertj-core) — provides a rich set of assertions

>*Everything in this repository was done before the release of a very promising [Kaspresso](https://github.com/KasperskyLab/Kaspresso) framework. 
>Some of the wrappers presented here (e.g. waiters) could now be well-replaced by their Kaspresso implementation.*

## Test Design Pattern

UI tests are written with the **Testing Robots** design pattern, which is very similar to Page Object.

The idea is that each app screen should have its own Robot where you describe test steps which you can apply to the screen.
You then chain these steps in a test.

See the examples:
* Java: [`ScreenRobot`](app/src/androidTest/java/com.pavelnazimok/uitesting/java/robots/FirstScreenRobot.java), [`ScreenTest`](app/src/androidTest/java/com.pavelnazimok/uitesting/java/tests/FirstScreenTest.java)
* Kotlin: [`ScreenRobot`](app/src/androidTest/java/com.pavelnazimok/uitesting/kotlin/robots/FirstScreenRobot.kt), [`ScreenTest`](app/src/androidTest/java/com.pavelnazimok/uitesting/kotlin/tests/FirstScreenTest.kt)

You can figure out the base concepts of the pattern from this [talk](https://jakewharton.com/testing-robots/) by Jake Wharton.

## Naming convention

Proper naming is very important:

* When you see *a test* name, you should understand exactly what it **verifies**, under what **conditions**, and what is the expected **result**
* When you see *a test step* name, you should understand exactly what is being done in the app during this step execution 
**without a need** to read the step's source code

For test names here I used an old but relevant [convention](https://osherove.com/blog/2005/4/3/naming-standards-for-unit-tests.html) by Roy Osherove:

```Java
@Test 
public void unitOfWork_stateUnderTest_expectedBehavior() {}
```

* `unitOfWork` — main *object* or *action* under test
* `stateUnderTest` — system state when *object* or *action* is being tested (usually starts with `when`, `after`, `on`)
* `expectedBehavior` — what we expect to see (always starts with `should`)

## Utils

These mainly contain method wrappers for frequently used and complicated UI or device-specific actions.
It has waiters, view property getters, improved assert messages, syntactic sugar, etc.

* [`EspressoExtensions.java`](app/src/androidTest/java/com.pavelnazimok/uitesting/java/utils/EspressoExtensions.java) | [`EspressoExtensions.kt`](app/src/androidTest/java/com.pavelnazimok/uitesting/kotlin/utils/EspressoExtensions.kt) — custom Espresso-like methods/functions (also, consider Kaspresso)
* [`UiAutomatorExtensions.java`](app/src/androidTest/java/com.pavelnazimok/uitesting/java/utils/UiAutomatorExtensions.java) | [`UiAutomatorExtensions.kt`](app/src/androidTest/java/com.pavelnazimok/uitesting/kotlin/utils/UiAutomatorExtensions.kt) — custom methods/functions to work with
UI Automator's API, mostly with [`UiObject2`](https://developer.android.com/reference/androidx/test/uiautomator/UiObject2) UI elements
* [`AndroidExtensions.java`](app/src/androidTest/java/com.pavelnazimok/uitesting/java/utils/AndroidExtensions.java) | [`AndroidExtensions.kt`](app/src/androidTest/java/com.pavelnazimok/uitesting/kotlin/utils/AndroidExtensions.kt) — device-specific actions

## TMS Integration

One possible way to integrate your tests with Test Management System is to report test results during test execution.
For this one can use [JUnit RunListener API](https://junit.org/junit4/javadoc/4.12/org/junit/runner/notification/RunListener.html).

There is an implementation of such Listener to report to TestRail: [`ReporterRunListener.java`](app/src/androidTest/java/com.pavelnazimok/uitesting/testrail/ReporterRunListener.java).

To make it work you need to:

* Add [TestRail API bindings](http://docs.gurock.com/testrail-api2/start) to your project (unfortunately, you can't add it as a dependency yet)
* Create [`AndroidManifest.xml`](app/src/androidTest/AndroidManifest.xml) for a test app 
* Annotate your tests with Test Case ID from TestRail (you need a custom [Annotation](app/src/androidTest/java/com.pavelnazimok/uitesting/testrail/CaseId.java) for this):

```Java
@Test
@CaseId(1)
public void your_test_name() { }
```

## Lint checks

There are configured linters for both languages:

* Java: [checkstyle](https://github.com/checkstyle/checkstyle). To run, execute `gradlew checkstyle` command in Terminal
* Kotlin: [ktlint](https://github.com/pinterest/ktlint). To run, execute `gradlew ktlint` command in Terminal