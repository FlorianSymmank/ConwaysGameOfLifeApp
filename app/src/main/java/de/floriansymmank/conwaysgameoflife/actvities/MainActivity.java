package de.floriansymmank.conwaysgameoflife.actvities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setConwayWorld(binding.gameOfLife.getConwayWorld());
        binding.getConwayWorld().addOnPropertyChangedCallback(resetCallback);
        setContentView(binding.getRoot());
    }

    private Observable.OnPropertyChangedCallback resetCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if(propertyId == BR.unique && !binding.getConwayWorld().isUnique()){
                binding.fabControl.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_settings_backup_restore_24));
            }
        }
    };

    public void controlGame(View view) {

        if (binding.getConwayWorld().isUnique()) {
            if (binding.gameOfLife.isRunning()) {
                binding.gameOfLife.stop();
                binding.fabControl.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_arrow_24));
            } else {
                binding.gameOfLife.start();
                binding.fabControl.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_stop_24));
            }
        } else {
            binding.getConwayWorld().resetWorld();
            binding.fabControl.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_arrow_24));
        }
    }
}