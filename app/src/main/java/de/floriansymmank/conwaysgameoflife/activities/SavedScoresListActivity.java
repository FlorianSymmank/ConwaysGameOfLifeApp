package de.floriansymmank.conwaysgameoflife.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.materialdrawer.Drawer;

import net.sharksystem.asap.android.apps.ASAPActivity;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ConwayGameEngine.ConwayGameEngineFacade;
import ConwayGameEngine.FinalScore;
import de.floriansymmank.conwaysgameoflife.asap.ConwayGameApp;
import de.floriansymmank.conwaysgameoflife.utils.NormalDrawer;
import de.floriansymmank.conwaysgameoflife.R;
import de.floriansymmank.conwaysgameoflife.adapter.SavedScoresListAdapter;
import de.floriansymmank.conwaysgameoflife.databinding.ActivitySavedScoresListBinding;

public class SavedScoresListActivity extends ASAPActivity {
    //TODO: ASAPActivity

    private ConwayGameEngineFacade facade;
    private ActivitySavedScoresListBinding binding;
    private SavedScoresListAdapter adapter;

    private Drawer drawer;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_scores_list);
        binding.setLifecycleOwner(this);

        facade = ConwayGameApp.getConwayGameApp().getConwayGameEngineFacade();

        drawer = NormalDrawer.createNormalDrawer(this);
        actionBar = initActionBar();

        populateList();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        binding.recyclerview.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                populateList();
                return true;
            }
        });
    }

    private void populateList() {
        List<FinalScore> scores = new LinkedList<>();
        try {
            scores = facade.getAllScores();
        } catch (IOException e) {

        }

        if (scores == null || scores.size() == 0)
            binding.tvNoItems.setVisibility(View.VISIBLE);
        else
            binding.tvNoItems.setVisibility(View.GONE);

        adapter = new SavedScoresListAdapter(getApplicationContext(), scores);
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
                facade.deleteScores();
                populateList();
            }
        });


        return mActionBar;
    }
}
