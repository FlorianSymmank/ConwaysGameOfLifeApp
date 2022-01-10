package de.floriansymmank.conwaysgameoflife.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import de.floriansymmank.conwaysgameoflife.asap.ConwayGameApp;
import de.floriansymmank.conwaysgameoflife.databinding.ActivitySettingsBinding;

public class InitialActivity extends Activity {

    private ActivitySettingsBinding binding;
    private ConwayGameApp conwayGameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize ConwayGameApp
        conwayGameApp = ConwayGameApp.initializeConwayGameApp(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
        }

        // if PLAYER_NAME is in sharePreferences, app was started beforehand, no need to send user to settings menu
        if (getSharedPreferences(ConwayGameApp.PREFERENCES_FILE, MODE_PRIVATE).contains(ConwayGameApp.PLAYER_NAME)) {
            launchFirstActivity();
        } else {
            Log.println(Log.DEBUG, "InitialActivity onCreate", "App opened for the first time");
            launchSettingsActivity();
        }
    }

    private void launchFirstActivity() {
        // send user to game
        this.finish();
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }

    private void launchSettingsActivity() {
        // send user to settings, first time user started this app
        this.finish();
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra(SettingActivity.FIRST_LAUNCH, true);
        this.startActivity(intent);
    }
}