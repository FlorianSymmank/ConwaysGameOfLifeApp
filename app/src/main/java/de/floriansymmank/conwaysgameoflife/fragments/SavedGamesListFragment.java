package de.floriansymmank.conwaysgameoflife.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ConwayGameEngine.ConwayGame;
import ConwayGameEngine.ConwayGameEngineFacade;
import ConwayGameEngine.ConwayGameEngineFacadeImpl;
import de.floriansymmank.conwaysgameoflife.adapter.ListAdapterListener;
import de.floriansymmank.conwaysgameoflife.adapter.SavedGamesListAdapter;
import de.floriansymmank.conwaysgameoflife.databinding.FragmentSavedGamesListBinding;

public class SavedGamesListFragment extends Fragment implements ListAdapterListener<ConwayGame> {

    private ConwayGameEngineFacade facade;
    private FragmentSavedGamesListBinding binding;
    private SavedGamesListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        facade = new ConwayGameEngineFacadeImpl(getActivity().getFilesDir().getAbsolutePath());

        List<ConwayGame> gameList = new LinkedList<>();
        try {
            gameList = facade.getAllGames();
        } catch (IOException e) {

        }

        adapter = new SavedGamesListAdapter(getContext(), this, gameList);

        binding = FragmentSavedGamesListBinding.inflate(inflater, container, false);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onRowClick(ConwayGame item) {

    }

    @Override
    public void onRowLongClick(ConwayGame item) {

    }

    @Override
    public void onButtonClick(ConwayGame item) {
//        Intent intent = new Intent(getContext(), GameFragment.class);
//        intent.putExtra(WorkoutDetailActivity.EXTRA_PLAYLIST_ID, playlist.playlist.playlistId);
//        startActivity(intent);
    }
}