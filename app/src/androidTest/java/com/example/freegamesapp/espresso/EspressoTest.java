package com.example.freegamesapp.espresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.freegamesapp.R;
import com.example.freegamesapp.activities.auth.AuthActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    @Rule
    public ActivityScenarioRule<AuthActivity> authActivityActivityScenarioRule
            = new ActivityScenarioRule<>(AuthActivity.class);

    private View decorView;

    @Before
    public void setUp() {
        authActivityActivityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<AuthActivity>() {
            @Override
            public void perform(AuthActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    public void tryToLoginWithEmptyEditTextTest(){
        final String expectedWarning = "Please, fill the data.";
        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withText(expectedWarning))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tryToRegisterWithEmptyEditTextTest(){
        final String expectedWarning = "Please, fill the data.";
        onView(withId(R.id.btn_register))
                .perform(click());

        onView(withText(expectedWarning))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tryRegisterAccountTest(){

        final String email = "usuarioTestPruebas@pruebas.com";
        final String password = "123456";

        onView(withId(R.id.et_email))
                .perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.et_password))
                .perform(typeText(password), closeSoftKeyboard());

        Intents.init();
        onView(withId(R.id.btn_register))
                .perform(click());
        Intents.release();
    }
}
