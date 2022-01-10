package de.floriansymmank.conwaysgameoflife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;

import com.mikepenz.materialdrawer.Drawer;

import net.sharksystem.asap.android.apps.ASAPActivity;

import ConwayGameEngine.ConwayGameEngineFacade;
import de.floriansymmank.conwaysgameoflife.ConwayGameApp;
import de.floriansymmank.conwaysgameoflife.NormalDrawer;
import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.databinding.ActivitySettingsBinding;

public class SettingActivity extends ASAPActivity {
    public static final String FIRST_LAUNCH = "FIRST_LAUNCH";

    // TODO: ASAPActivity

    private ActivitySettingsBinding binding;
    private ConwayGameEngineFacade facade;
    private ConwayGameApp conwayGameApp;

    private Drawer drawer;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conwayGameApp = ConwayGameApp.getConwayGameApp();
        facade = conwayGameApp.getConwayGameEngineFacade();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        if (getIntent().getBooleanExtra(FIRST_LAUNCH, false)) {
            binding.btnResetEverything.setVisibility(View.GONE);
        } else {
            drawer = NormalDrawer.createNormalDrawer(this);
            actionBar = initActionBar();

            binding.btnResetEverything.setVisibility(View.VISIBLE);
        }


        binding.btnSave.setOnClickListener(btnSaveClick());
        binding.btnResetEverything.setOnClickListener(btnResetEverythingClick());

        binding.etHeight.setText(String.valueOf(ConwayGameApp.getConwayGameApp().getHeight()));
        binding.etWidth.setText(String.valueOf(ConwayGameApp.getConwayGameApp().getWidth()));
        binding.etName.setText(String.valueOf(ConwayGameApp.getConwayGameApp().getPlayerName()));
        binding.etInterval.setText(String.valueOf(ConwayGameApp.getConwayGameApp().getInterval()));
    }

    private ActionBar initActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater li = LayoutInflater.from(this);

        View toolbar = li.inflate(R.layout.settings_toolbar, null);
        mActionBar.setCustomView(toolbar);
        mActionBar.setDisplayShowCustomEnabled(true);

        ImageButton item1 = toolbar.findViewById(R.id.menutItem);
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen())
                    drawer.closeDrawer();
                else
                    drawer.openDrawer();
            }
        });

        return mActionBar;
    }

    private View.OnClickListener btnResetEverythingClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facade.deleteGames();
                facade.deleteScores();

                binding.etHeight.setText(String.valueOf(ConwayGameApp.DEFAULT_GAME_WIDTH));
                binding.etWidth.setText(String.valueOf(ConwayGameApp.DEFAULT_GAME_HEIGHT));
                binding.etName.setText(ConwayGameApp.DEFAULT_PLAYER_NAME);
                binding.etInterval.setText(String.valueOf(ConwayGameApp.DEFAULT_GAME_INTERVAL));

                conwayGameApp.resetSharedPreferences(SettingActivity.this);

                saveSettings();
            }
        };
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

                if (binding.etInterval.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "interval is needed", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int interval = Integer.parseInt(binding.etInterval.getText().toString());
                    if (interval <= 10 || interval > 5000) {
                        Toast.makeText(getApplicationContext(), "Ungültiger Wert, muss zwischen 10 und 5000 ms liegen", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                saveSettings();

                if (getIntent().getBooleanExtra(FIRST_LAUNCH, false)) {
                    launchFirstActivity();
                }
            }
        };
    }

    private void saveSettings() {
        conwayGameApp.setPlayerName(getApplicationContext(), binding.etName.getText().toString());
        conwayGameApp.setHeight(getApplicationContext(), Integer.parseInt(binding.etHeight.getText().toString()));
        conwayGameApp.setWidth(getApplicationContext(), Integer.parseInt(binding.etWidth.getText().toString()));
        conwayGameApp.setInterval(getApplicationContext(), Integer.parseInt(binding.etInterval.getText().toString()));
    }

    private void launchFirstActivity() {
        this.finish();
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }
}
