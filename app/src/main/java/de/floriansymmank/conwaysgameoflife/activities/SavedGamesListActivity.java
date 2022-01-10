package de.floriansymmank.conwaysgameoflife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnFlingListener;

import com.mikepenz.materialdrawer.Drawer;

import net.sharksystem.asap.android.apps.ASAPActivity;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ConwayGameEngine.ConwayGame;
import ConwayGameEngine.ConwayGameEngineFacade;
import ConwayGameEngine.ConwayGameEngineFacadeImpl;
import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.adapter.ListAdapterListener;
import de.floriansymmank.conwaysgameoflife.adapter.SavedGamesListAdapter;
import de.floriansymmank.conwaysgameoflife.databinding.ActivitySavedGamesListBinding;
import de.floriansymmank.conwaysgameoflife.utils.NormalDrawer;

public class SavedGamesListActivity extends ASAPActivity implements ListAdapterListener<ConwayGame> {

    private ConwayGameEngineFacade facade;
    private ActivitySavedGamesListBinding binding;
    private SavedGamesListAdapter adapter;

    private Drawer drawer;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_games_list);
        binding.setLifecycleOwner(this);

        facade = new ConwayGameEngineFacadeImpl(getFilesDir().getAbsolutePath());

        // initiate drawer and actionbar
        drawer = NormalDrawer.createNormalDrawer(this);
        actionBar = initActionBar();

        populateList();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // fling = pull down recyclerview
        binding.recyclerview.setOnFlingListener(new OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                populateList();
                return true;
            }
        });
    }

    private void populateList() {
        Log.println(Log.DEBUG, "SavedGamesListActivity populateList", "List populated");

        // get all currently available games, and display them
        List<ConwayGame> gameList = new LinkedList<>();
        try {
            gameList = facade.getAllGames();
        } catch (IOException ignore) {
        }

        if (gameList == null || gameList.size() == 0)
            binding.tvNoItems.setVisibility(View.VISIBLE);
        else
            binding.tvNoItems.setVisibility(View.GONE); // there wasnt any saved games

        adapter = new SavedGamesListAdapter(getApplicationContext(), this, gameList);
        binding.recyclerview.setAdapter(adapter);
    }

    private ActionBar initActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater li = LayoutInflater.from(this);

        View toolbar = li.inflate(R.layout.saved_scores_list_toolbar, null);
        mActionBar.setCustomView(toolbar);
        mActionBar.setDisplayShowCustomEnabled(true);

        ImageButton btn1 = toolbar.findViewById(R.id.menutItem);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen())
                    drawer.closeDrawer();
                else
                    drawer.openDrawer();
            }
        });

        // delete all saved games
        ImageButton btn2 = toolbar.findViewById(R.id.deleteItem);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facade.deleteGames();
                populateList();
            }
        });


        return mActionBar;
    }

    @Override
    public void onRowClick(ConwayGame item) {
        // no interaction needed
    }

    @Override
    public void onRowLongClick(ConwayGame item) {
        // no interaction needed
        // TODO: Maybe delete clicked game (dialog)
    }

    @Override
    public void onButtonClick(ConwayGame item) {
        // open clicked game
        Log.println(Log.DEBUG, "SavedGamesListActivity onButtonClick", "Saved ConwayGame chosen");
        finish();
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra(GameActivity.CONWAYGAME_EXTRA, item);
        startActivity(intent);
    }
}