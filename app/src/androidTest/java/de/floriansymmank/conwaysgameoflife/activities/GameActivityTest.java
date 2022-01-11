package de.floriansymmank.conwaysgameoflife.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.floriansymmank.conwaysgameoflife.R;

@RunWith(AndroidJUnit4ClassRunner.class)
public class GameActivityTest {
    static {
        // SettingActivity ist ASAPActivity -> kein Virtual Device
        // SettingActivity braucht initializiertes ConwayGameApp mit ASAP Funktionalitäten
        ActivityScenario.launch(new Intent(ApplicationProvider.getApplicationContext(), InitialActivity.class));
    }

    @Rule
    public ActivityTestRule<GameActivity> mGameActivityTestRule = new ActivityTestRule<GameActivity>(GameActivity.class, false, false);
    @Rule
    public ActivityScenarioRule<GameActivity> mGameActivityScenarioRule = new ActivityScenarioRule<GameActivity>(GameActivity.class);

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    @Test
    public void fabClickStartsGame() {
        mGameActivityTestRule.launchActivity(null);

        onView(withId(R.id.fabControl)).perform(click());

        assertTrue(mGameActivityTestRule.getActivity().getGameOfLifeView().isRunning());
    }

    @Test
    public void fabDoubleClickStopsGame() {
        mGameActivityTestRule.launchActivity(null);

        onView(withId(R.id.fabControl)).perform(click());

        assertTrue(mGameActivityTestRule.getActivity().getGameOfLifeView().isRunning());

        onView(withId(R.id.fabControl)).perform(click());

        assertFalse(mGameActivityTestRule.getActivity().getGameOfLifeView().isRunning());
    }
}