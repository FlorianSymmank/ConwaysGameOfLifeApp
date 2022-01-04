package de.floriansymmank.conwaysgameoflife.fragments;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import ConwayGameEngine.ConwayGame;
import ConwayGameEngine.ConwayGameEngineFacade;
import ConwayGameEngine.ConwayGameEngineFacadeImpl;
import ConwayGameEngine.FinalScore;
import ConwayGameEngine.Score;
import ConwayGameEngine.ScoreChangedListener;
import ConwayGameEngine.UniqueStateChangedListener;
import de.floriansymmank.conwaysgameoflife.DialogListener;
import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.databinding.FragmentGameBinding;

public class GameFragment extends Fragment implements ScoreChangedListener, UniqueStateChangedListener, DialogListener {

    private FragmentGameBinding binding;
    private ConwayGameEngineFacade facade;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //setHasOptionsMenu(true);

        binding = FragmentGameBinding.inflate(inflater, container, false);

        facade = new ConwayGameEngineFacadeImpl(getActivity().getFilesDir().getAbsolutePath());
        ConwayGame game = facade.createGame("", 0, 50, 50);

        binding.gameOfLife.initWorld(game);
        binding.setGame(game);

        game.setScoreChangedListener(this);
        game.setUniqueStateChangedListener(this);

        setText(binding.deathScore, String.valueOf(game.getDeathScore().getScore()));
        setText(binding.resScore, String.valueOf(game.getResurrectionScore().getScore()));
        setText(binding.genScore, String.valueOf(game.getGenerationScore().getScore()));

        binding.fabControl.setOnClickListener(this::controlGame);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
        }

        return binding.getRoot();
    }


    private void setImage(final FloatingActionButton fab, final Drawable img) {
        getActivity().runOnUiThread(() -> fab.setImageDrawable(img));
    }

    private void setText(final TextView view, final String value) {
        getActivity().runOnUiThread(() -> view.setText(value));
    }

    // public because layout uses this
    public void controlGame(View view) {
        if (binding.gameOfLife.isRunning())
            stop();
        else if (binding.getGame().isUnique()) {
            start();
        } else {
            reset();
        }
    }

    private void reset() {
        binding.gameOfLife.reset();
        Drawable img = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
        setImage(binding.fabControl, img);
    }

    private void stop() {
        binding.gameOfLife.stop();
        Drawable img = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
        setImage(binding.fabControl, img);
    }

    private void start() {
        binding.gameOfLife.start();
        Drawable img = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_baseline_stop_24);
        setImage(binding.fabControl, img);
    }

    @Override
    public void scoreChanged(Score score) {
        if (score.getName().equals(ConwayGame.Scores.DEATH_SCORE.toString()))
            setText(binding.deathScore, String.valueOf(score.getScore()));

        if (score.getName().equals(ConwayGame.Scores.GENERATION_SCORE.toString()))
            setText(binding.genScore, String.valueOf(score.getScore()));

        if (score.getName().equals(ConwayGame.Scores.RESURRECTION_SCORE.toString()))
            setText(binding.resScore, String.valueOf(score.getScore()));
    }

    @Override
    public void scoreCleared() {
        setText(binding.resScore, String.valueOf(0));
        setText(binding.resScore, String.valueOf(0));
        setText(binding.resScore, String.valueOf(0));
    }

    @Override
    public void scoreRemoved(Score score) {

    }

    @Override
    public void uniqueChanged(boolean b) {
        if (b) {
            Drawable img = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
            setImage(binding.fabControl, img);
        } else {

            FinalScore score = binding.getGame().getFinalScore();

            ShareDialogFragment dialog = new ShareDialogFragment(score, this);
            dialog.show(getActivity().getSupportFragmentManager(), "ShareDialogFragment");

            stop();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        // TODO: Share
    }

    @Override
    public void onDialogDismiss(DialogFragment dialogFragment) {

        try {
            facade.saveScore(((ShareDialogFragment) dialogFragment).getScore());
        } catch (IOException ignored) {
        }

        reset();


    }
}