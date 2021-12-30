package de.floriansymmank.conwaysgameoflife.actvities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ConwayGameEngine.ConwayGame;
import ConwayGameEngine.ConwayGameImpl;
import ConwayGameEngine.Score;
import ConwayGameEngine.ScoreChangedListener;
import ConwayGameEngine.UniqueStateChangedListener;
import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConwayGame game = new ConwayGameImpl("", 1, 50, 50);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.gameOfLife.initWorld(game);
        binding.setGame(game);

        game.setScoreChangedListener(new ScoreChangedListener() {
            @Override
            public void changed(Score score) {
                if (score.getName().equals(ConwayGame.Scores.DEATH_SCORE.toString()))
                    setText(binding.deathScore, String.valueOf(score.getScore()));

                if (score.getName().equals(ConwayGame.Scores.GENERATION_SCORE.toString()))
                    setText(binding.genScore, String.valueOf(score.getScore()));

                if (score.getName().equals(ConwayGame.Scores.RESURRECTION_SCORE.toString()))
                    setText(binding.resScore, String.valueOf(score.getScore()));
            }

            @Override
            public void cleared() {
                setText(binding.resScore, String.valueOf(0));
                setText(binding.resScore, String.valueOf(0));
                setText(binding.resScore, String.valueOf(0));
            }

            @Override
            public void removed(Score score) {

            }
        });

        game.setUniqueStateChangedListener(new UniqueStateChangedListener() {
            @Override
            public void changed(boolean b) {

                Log.println(Log.DEBUG, "Hannes", "changed: " + b);

                Drawable img;

                if (b) {
                    img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
                    setImage(binding.fabControl, img);
                } else {
                    img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_settings_backup_restore_24);
                    setImage(binding.fabControl, img);
                    binding.gameOfLife.stop();
                    // TODO: Add Dialog/Save and reset
                    // binding.gameOfLife.getConwaGame().reset();
                    // Then
                    // img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
                }
            }
        });

        setContentView(binding.getRoot());
    }

    private void setImage(final FloatingActionButton fab, final Drawable img) {
        runOnUiThread(() -> fab.setImageDrawable(img));
    }

    private void setText(final TextView view, final String value) {
        runOnUiThread(() -> view.setText(value));
    }

    public void controlGame(View view) {
        Drawable img;

        if (binding.gameOfLife.isRunning()) {
            binding.gameOfLife.stop();
            img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
        } else {
            binding.gameOfLife.start();
            img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_stop_24);
        }

        setImage(binding.fabControl, img);
    }
}