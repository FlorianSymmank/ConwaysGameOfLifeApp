package de.floriansymmank.conwaysgameoflife.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.apps.ASAPActivity;

import java.io.IOException;

import ConwayGameEngine.ConwayGame;
import ConwayGameEngine.ConwayGameEngineFacade;
import ConwayGameEngine.FinalScore;
import ConwayGameEngine.Score;
import ConwayGameEngine.ScoreChangedListener;
import ConwayGameEngine.UniqueStateChangedListener;
import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.asap.ConwayGameApp;
import de.floriansymmank.conwaysgameoflife.asap.ConwayGameComponent;
import de.floriansymmank.conwaysgameoflife.databinding.ActivityGameBinding;
import de.floriansymmank.conwaysgameoflife.fragments.DialogListener;
import de.floriansymmank.conwaysgameoflife.fragments.SaveDialogFragment;
import de.floriansymmank.conwaysgameoflife.fragments.ShareDialogFragment;
import de.floriansymmank.conwaysgameoflife.utils.NormalDrawer;

public class GameActivity extends ASAPActivity implements ScoreChangedListener, UniqueStateChangedListener, DialogListener {

    // get game as extra
    public static String CONWAYGAME_EXTRA = "CONWAYGAME_EXTRA";

    private ActivityGameBinding binding;
    private ConwayGameEngineFacade facade;

    private Drawer drawer;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        binding.setLifecycleOwner(this);

        facade = ConwayGameApp.getConwayGameApp().getConwayGameEngineFacade();

        ConwayGame game = null;
        if ((game = (ConwayGame) getIntent().getSerializableExtra(CONWAYGAME_EXTRA)) != null) {
            Log.println(Log.DEBUG, "GameActivity onCreate", "got Game as Extra");
        } else {
            game = facade.createGame(
                    ConwayGameApp.getConwayGameApp().getPlayerName(),
                    ConwayGameApp.getConwayGameApp().getPlayerID(),
                    ConwayGameApp.getConwayGameApp().getWidth(),
                    ConwayGameApp.getConwayGameApp().getHeight());

            Log.println(Log.DEBUG, "GameActivity onCreate", "created new Game");
        }

        binding.gameOfLife.initWorld(game);
        binding.setGame(game);

        game.setScoreChangedListener(this);
        game.setUniqueStateChangedListener(this);

        // initiate drawer and actionbar
        drawer = NormalDrawer.createNormalDrawer(this);
        actionBar = initActionBar();

        // set texts
        setText(binding.deathScore, String.valueOf(game.getDeathScore().getScore()));
        setText(binding.resScore, String.valueOf(game.getResurrectionScore().getScore()));
        setText(binding.genScore, String.valueOf(game.getGenerationScore().getScore()));

        binding.fabControl.setOnClickListener(this::controlGame);

        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
        setImage(binding.fabControl, img);
    }

    private ActionBar initActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater li = LayoutInflater.from(this);

        View toolbar = li.inflate(R.layout.game_toolbar, null);
        mActionBar.setCustomView(toolbar);
        mActionBar.setDisplayShowCustomEnabled(true);

        ImageButton btnMenu = toolbar.findViewById(R.id.menutItem);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen())
                    drawer.closeDrawer();
                else
                    drawer.openDrawer();
            }
        });

        ImageButton btnSave = toolbar.findViewById(R.id.saveItem);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveDialog();
            }
        });

        ImageButton btnReset = toolbar.findViewById(R.id.resetItem);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        return mActionBar;
    }

    private void setImage(final FloatingActionButton fab, final Drawable img) {
        runOnUiThread(() -> fab.setImageDrawable(img));
    }

    private void setText(final TextView view, final String value) {
        runOnUiThread(() -> view.setText(value));
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
        // resets the game into a neutral state
        binding.gameOfLife.reset();
        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
        setImage(binding.fabControl, img);
    }

    private void stop() {
        // halts the game in its current state
        binding.gameOfLife.stop();
        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
        setImage(binding.fabControl, img);
    }

    private void start() {
        //starts/restarts current game
        binding.gameOfLife.start();
        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_stop_24);
        setImage(binding.fabControl, img);
    }

    @Override
    public void scoreChanged(Score score) {
        // Called when ConwayGame informs about an score change
        // set according scores

        if (score.getName().equals(ConwayGame.Scores.DEATH_SCORE.toString()))
            setText(binding.deathScore, String.valueOf(score.getScore()));

        if (score.getName().equals(ConwayGame.Scores.GENERATION_SCORE.toString()))
            setText(binding.genScore, String.valueOf(score.getScore()));

        if (score.getName().equals(ConwayGame.Scores.RESURRECTION_SCORE.toString()))
            setText(binding.resScore, String.valueOf(score.getScore()));
    }

    @Override
    public void scoreCleared() {
        // reset visual scoretable

        setText(binding.resScore, String.valueOf(0));
        setText(binding.resScore, String.valueOf(0));
        setText(binding.resScore, String.valueOf(0));
    }

    @Override
    public void scoreRemoved(Score score) {
        // no need to react
    }

    @Override
    public void uniqueChanged(boolean unique) {
        if (unique) {
            Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
            setImage(binding.fabControl, img);
        } else {
            // game is not unique anymore, halt and inform user
            FinalScore score = binding.getGame().getFinalScore();
            showShareDialog(score);
            stop();
        }
    }

    private void showShareDialog(FinalScore score) {
        // show user an dialog, does user want to share the result?
        FragmentTransaction ft = getFragmentTransaction(ShareDialogFragment.SHARE_DIALOG_FRAGMENT_TAG);
        DialogFragment newFragment = ShareDialogFragment.newInstance(score, this, false);
        newFragment.show(ft, ShareDialogFragment.SHARE_DIALOG_FRAGMENT_TAG);
    }

    private void showSaveDialog() {
        // show user an save dialog to save the current game
        stop();
        FragmentTransaction ft = getFragmentTransaction(SaveDialogFragment.SAVE_DIALOG_FRAGMENT_TAG);
        DialogFragment newFragment = SaveDialogFragment.newInstance(this);
        newFragment.show(ft, SaveDialogFragment.SAVE_DIALOG_FRAGMENT_TAG);
    }

    @NonNull
    private FragmentTransaction getFragmentTransaction(String fragmentTag) {
        // setup dialog
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(fragmentTag);
        return ft;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {

        // user confirmed something

        // dialog was share dialog
        if (dialogFragment instanceof ShareDialogFragment) {
            FinalScore fs = ((ShareDialogFragment) dialogFragment).getScore();

            // Save score locally
            try {
                facade.saveScore(fs); //
            } catch (IOException ignored) {
            }

            // share score via ASAP, hopefully
            try {
                byte[] serialized = ConwayGameEngine.Util.Serialize.finalScoreSerializer(fs);
                getASAPPeer().sendASAPMessage(ConwayGameComponent.APP_NAME, ConwayGameComponent.FINAL_SCORE_URI, serialized);
            } catch (ASAPException e) {
                Log.println(Log.DEBUG, "GameActivity sendASAPMessage Exception", e.getMessage());
            }

            reset();
        } else if (dialogFragment instanceof SaveDialogFragment) {
            // dialog as save dialog
            try {
                // save game state locally, get user input as game name
                String gameName = ((SaveDialogFragment) dialogFragment).getInputText();
                binding.getGame().setName(gameName);
                facade.saveGame(binding.getGame());
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void onDialogDismiss(DialogFragment dialogFragment) {
        // even if user declined share reset current game
        if (dialogFragment instanceof ShareDialogFragment) {
            reset();
        }
    }

    @Override
    protected void onDestroy() {
        // if activity is destroyed stop conwaygameview thread!
        binding.gameOfLife.stop();
        super.onDestroy();
    }
}