package de.floriansymmank.conwaysgameoflife.activities;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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
import de.floriansymmank.conwaysgameoflife.databinding.ActivityGameBinding;
import de.floriansymmank.conwaysgameoflife.fragments.SaveDialogFragment;
import de.floriansymmank.conwaysgameoflife.fragments.ShareDialogFragment;

public class GameActivity extends AppCompatActivity implements ScoreChangedListener, UniqueStateChangedListener, DialogListener {

    private ActivityGameBinding binding;
    private ConwayGameEngineFacade facade;

    private Drawer drawer;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_game);

        binding.setLifecycleOwner(this);

        facade = new ConwayGameEngineFacadeImpl(getFilesDir().getAbsolutePath());

        ConwayGame game = facade.createGame("", 0, 50, 50);

        binding.gameOfLife.initWorld(game);
        binding.setGame(game);

        game.setScoreChangedListener(this);
        game.setUniqueStateChangedListener(this);

        drawer = initDrawer();
        actionBar = initActionBar();

        setText(binding.deathScore, String.valueOf(game.getDeathScore().getScore()));
        setText(binding.resScore, String.valueOf(game.getResurrectionScore().getScore()));
        setText(binding.genScore, String.valueOf(game.getGenerationScore().getScore()));

        binding.fabControl.setOnClickListener(this::controlGame);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
        }

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

        ImageButton item2 = toolbar.findViewById(R.id.saveItem);
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveDialog();
            }
        });

        ImageButton item3 = toolbar.findViewById(R.id.resetItem);
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });


        return mActionBar;
    }

    private Drawer initDrawer() {

        PrimaryDrawerItem i = new PrimaryDrawerItem();
        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName("New Conway's Game of Life");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Saved Games");

        return new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(i, item1, item2)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
//                                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
//                                startActivity(intent);

                                reset();
                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), "Saved Games", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .build();
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
        binding.gameOfLife.reset();
        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
        setImage(binding.fabControl, img);
    }

    private void stop() {
        binding.gameOfLife.stop();
        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
        setImage(binding.fabControl, img);
    }

    private void start() {
        binding.gameOfLife.start();
        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_stop_24);
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
            Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
            setImage(binding.fabControl, img);
        } else {
            FinalScore score = binding.getGame().getFinalScore();
            showShareDialog(score);
            stop();
        }
    }

    private void showShareDialog(FinalScore score) {
        FragmentTransaction ft = getFragmentTransaction(ShareDialogFragment.SHARE_DIALOG_FRAGMENT_TAG);
        DialogFragment newFragment = ShareDialogFragment.newInstance(score, this, false);
        newFragment.show(ft, ShareDialogFragment.SHARE_DIALOG_FRAGMENT_TAG);
    }

    private void showSaveDialog() {
        FragmentTransaction ft = getFragmentTransaction(SaveDialogFragment.SAVE_DIALOG_FRAGMENT_TAG);
        DialogFragment newFragment = SaveDialogFragment.newInstance(this);
        newFragment.show(ft, SaveDialogFragment.SAVE_DIALOG_FRAGMENT_TAG);
    }

    @NonNull
    private FragmentTransaction getFragmentTransaction(String fragmentTag) {
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

        if (dialogFragment instanceof ShareDialogFragment) {
            // TODO: Share
            reset();
        } else if (dialogFragment instanceof SaveDialogFragment) {
            try {
                String gameName = ((SaveDialogFragment) dialogFragment).getInputText();
                binding.getGame().setName(gameName);
                facade.saveGame(binding.getGame());
                Toast.makeText(this, "Game Saved!", Toast.LENGTH_SHORT).show();
            } catch (IOException ignored) {
                Toast.makeText(this, "GAME NOT SAVED", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDialogDismiss(DialogFragment dialogFragment) {
        if (dialogFragment instanceof ShareDialogFragment) {
            reset();
        } else if (dialogFragment instanceof SaveDialogFragment) {

        }
    }
}