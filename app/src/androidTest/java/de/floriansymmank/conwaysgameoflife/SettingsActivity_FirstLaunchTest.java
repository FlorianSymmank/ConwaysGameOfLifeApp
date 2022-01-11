package de.floriansymmank.conwaysgameoflife;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.fail;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.floriansymmank.conwaysgameoflife.activities.GameActivity;
import de.floriansymmank.conwaysgameoflife.activities.InitialActivity;
import de.floriansymmank.conwaysgameoflife.activities.SettingActivity;
import de.floriansymmank.conwaysgameoflife.asap.ConwayGameApp;

@RunWith(AndroidJUnit4.class)
public class SettingsActivity_FirstLaunchTest {

    static Intent intentFirstLaunch;

    static {
        intentFirstLaunch = new Intent(ApplicationProvider.getApplicationContext(), SettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(SettingActivity.FIRST_LAUNCH, true);
        intentFirstLaunch.putExtras(bundle);

        // fragt mich nicht wie ich darauf gekommen bin .....
        // SettingActivity ist ASAPActivity -> kein Virtual Device
        // SettingActivity braucht initializiertes ConwayGameApp mit ASAP Funktionalitäten
        // works ig
        ConwayGameApp.initializeConwayGameApp(
                InstrumentationRegistry.getInstrumentation().startActivitySync(
                        new Intent(ApplicationProvider.getApplicationContext(), InitialActivity.class)
                )
        );
    }

    @Rule
    public ActivityScenarioRule<SettingActivity> mSettingsActivityTestRule = new ActivityScenarioRule<SettingActivity>(intentFirstLaunch);

    @Test
    public void firstLaunchHidesResetEverythingButton() {
        onView(withId(R.id.btnResetEverything)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void firstLaunchHidesToolbar() {
        try {
            // menutItem is usually the drawer open imageButton id, if its there, toolbar is there
            onView(withId(R.id.menutItem)).check(matches(ViewMatchers.isDisplayed()));
            fail();
        } catch (NoMatchingViewException e) {
            // menutItem was not there ->success
        }
    }

    @Test
    public void clickSaveButton_launchesGameActivity() {
        // implies default values are set in their textEdits, otherwise invalidation would prevent saving and lauching

        Intents.init();
        onView(withId(R.id.btnSave)).perform(click());

        intended(hasComponent(GameActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void clickSaveButtonValuesMissing_doesNotLaunchGameActivity() {
        // activity is still visible
        onView(withId(R.id.etName)).perform(clearText());
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.etName)).check(matches(isDisplayed()));
        // visiable in logs (de.floriansymmank.conwaysgameoflife D/SettingActivity btnSaveClick: etName text is empty)
    }
}
