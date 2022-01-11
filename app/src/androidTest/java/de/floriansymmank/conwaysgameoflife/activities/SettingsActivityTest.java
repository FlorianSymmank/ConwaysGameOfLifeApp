package de.floriansymmank.conwaysgameoflife.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.activities.InitialActivity;
import de.floriansymmank.conwaysgameoflife.activities.SettingActivity;
import de.floriansymmank.conwaysgameoflife.asap.ConwayGameApp;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SettingsActivityTest {
    static {
        // SettingActivity ist ASAPActivity -> kein Virtual Device
        // SettingActivity braucht initializiertes ConwayGameApp mit ASAP Funktionalitäten
        ActivityScenario.launch(new Intent(ApplicationProvider.getApplicationContext(), InitialActivity.class));
    }

    @Rule
    public ActivityScenarioRule<SettingActivity> mSettingsActivityTestRule = new ActivityScenarioRule<SettingActivity>(SettingActivity.class);

    @Test
    public void showsResetEverything() {
        onView(ViewMatchers.withId(R.id.btnResetEverything)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void showsToolbar() {
        // menutItem is usually the drawer open imageButton id, if its there, toolbar is there
        onView(withId(R.id.menutItem)).check(matches(ViewMatchers.isDisplayed()));
    }
}