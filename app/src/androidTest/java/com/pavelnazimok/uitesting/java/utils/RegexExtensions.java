package com.pavelnazimok.uitesting.java.utils;

import android.view.View;

import androidx.annotation.NonNull;

import org.hamcrest.Matcher;

import java.util.regex.Pattern;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.pavelnazimok.uitesting.java.utils.EspressoExtensions.getText;
import static org.assertj.core.api.Assertions.assertThat;

public class RegexExtensions {

    @NonNull
    public static java.util.regex.Matcher match(@NonNull final String input, @NonNull final Pattern pattern) {
        assertThat(input).matches(pattern);
        final java.util.regex.Matcher matcher = pattern.matcher(input);
        //noinspection ResultOfMethodCallIgnored
        matcher.matches();
        return matcher;
    }

    @NonNull
    public static java.util.regex.Matcher match(@NonNull final Matcher<View> viewMatcher, @NonNull final Pattern pattern) {
        return match(getText(viewMatcher), pattern);
    }

    @NonNull
    public static java.util.regex.Matcher match(final int viewId, @NonNull final Pattern pattern) {
        return match(withId(viewId), pattern);
    }

    @NonNull
    public static java.util.regex.Matcher find(@NonNull final String input, @NonNull final Pattern pattern) {
        assertThat(input).containsPattern(pattern);
        final java.util.regex.Matcher matcher = pattern.matcher(input);
        //noinspection ResultOfMethodCallIgnored
        matcher.find();
        return matcher;
    }

    @NonNull
    public static java.util.regex.Matcher find(@NonNull final Matcher<View> viewMatcher, @NonNull final Pattern pattern) {
        return find(getText(viewMatcher), pattern);
    }

    @NonNull
    public static java.util.regex.Matcher find(final int viewId, @NonNull final Pattern pattern) {
        return find(withId(viewId), pattern);
    }
}
