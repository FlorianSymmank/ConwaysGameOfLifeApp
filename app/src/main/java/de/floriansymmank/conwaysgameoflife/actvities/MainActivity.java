package de.floriansymmank.conwaysgameoflife.actvities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void resumeGame(View view) {
        binding.gameOfLife.start();
    }

    public void stopGame(View view) {
        binding.gameOfLife.stop();
    }
}