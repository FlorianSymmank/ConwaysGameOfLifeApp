package de.floriansymmank.conwaysgameoflife.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import de.floriansymmank.conwaysgameoflife.ConwayGameApp;
import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.databinding.ActivityInitialBinding;

public class InitialActivity extends Activity {

    private ActivityInitialBinding binding;
    private ConwayGameApp conwayGameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conwayGameApp = ConwayGameApp.initializeConwayGameApp(this);

        if (getSharedPreferences(ConwayGameApp.PREFERENCES_FILE, MODE_PRIVATE).contains(ConwayGameApp.PLAYER_NAME)) {
            lauchFirstActivity();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_initial);
        binding.btnSave.setOnClickListener(btnSaveClick());
        binding.etHeight.setText(String.valueOf(ConwayGameApp.DEFAULT_GAME_HEIGHT));
        binding.etWidth.setText(String.valueOf(ConwayGameApp.DEFAULT_GAME_WIDTH));
    }

    private void lauchFirstActivity() {
        Class realFirstActivity = GameActivity.class;
        this.finish();
        Intent intent = new Intent(this, realFirstActivity);
        this.startActivity(intent);
    }

    private View.OnClickListener btnSaveClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Player Name is needed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (binding.etHeight.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "height is needed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (binding.etWidth.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Width is needed", Toast.LENGTH_SHORT).show();
                    return;
                }

                conwayGameApp.setPlayerName(getApplicationContext(), binding.etName.getText().toString());
                conwayGameApp.setHeight(getApplicationContext(), Integer.parseInt(binding.etHeight.getText().toString()));
                conwayGameApp.setHeight(getApplicationContext(), Integer.parseInt(binding.etWidth.getText().toString()));

                lauchFirstActivity();
            }
        };
    }


    // SharedPreferences.Editor.clear()
    // SharedPreferences.Editor.commit()
}