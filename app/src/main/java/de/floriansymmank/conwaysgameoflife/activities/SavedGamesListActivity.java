package de.floriansymmank.conwaysgameoflife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

public class SavedGamesListActivity extends AppCompatActivity implements ListAdapterListener<ConwayGame> {

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
        List<ConwayGame> gameList = new LinkedList<>();
        try {
            gameList = facade.getAllGames();
        } catch (IOException e) {

        }

        drawer = initDrawer();
        actionBar = initActionBar();

        adapter = new SavedGamesListAdapter(getApplicationContext(), this, gameList);

        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private ActionBar initActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater li = LayoutInflater.from(this);

        View toolbar = li.inflate(R.layout.saved_games_list_toolbar, null);
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

        ImageButton item2 = toolbar.findViewById(R.id.deleteItem);
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "DELETE ALL", Toast.LENGTH_SHORT).show();
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
                                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
//                                Intent intent = new Intent(getApplicationContext(), SavedGamesListActivity.class);
//                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }

    @Override
    public void onRowClick(ConwayGame item) {

    }

    @Override
    public void onRowLongClick(ConwayGame item) {

    }

    @Override
    public void onButtonClick(ConwayGame item) {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra(GameActivity.CONWAYGAME_EXTRA, item);
        startActivity(intent);
    }
}