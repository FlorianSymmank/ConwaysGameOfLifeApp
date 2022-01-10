package de.floriansymmank.conwaysgameoflife.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import de.floriansymmank.conwaysgameoflife.ConwayGameApp;
import de.floriansymmank.conwaysgameoflife.databinding.ActivitySettingsBinding;

public class InitialActivity extends Activity {

    private ActivitySettingsBinding binding;
    private ConwayGameApp conwayGameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conwayGameApp = ConwayGameApp.initializeConwayGameApp(this);

        if (getSharedPreferences(ConwayGameApp.PREFERENCES_FILE, MODE_PRIVATE).contains(ConwayGameApp.PLAYER_NAME)) {
            launchFirstActivity();
        } else
            launchSettingsActivity();
    }

    private void launchFirstActivity() {
        this.finish();
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }

    private void launchSettingsActivity() {
        this.finish();
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra(SettingActivity.FIRST_LAUNCH, true);
        this.startActivity(intent);
    }
}