package de.floriansymmank.conwaysgameoflife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.List;

import ConwayGameEngine.ConwayGame;
import de.floriansymmank.conwaysgameoflife.R;

public class SavedGamesListAdapter extends RecyclerView.Adapter<SavedGamesListAdapter.SavedGameViewHolder> {

    private List<ConwayGame> gameList;
    private ListAdapterListener<ConwayGame> listener;

    public SavedGamesListAdapter(Context context, ListAdapterListener<ConwayGame> listener, List<ConwayGame> itemsList) {
        this.listener = listener;
        this.gameList = itemsList;
    }


    @NonNull
    @Override
    public SavedGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_saved_games_list, parent, false);
        return new SavedGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedGameViewHolder holder, int position) {
        ConwayGame current = gameList.get(position);
        holder.tvGameName.setText(current.getName()); // TODO: Make ConwayGame nameable

        String msg = MessageFormat.format("Death: {0} Gen: {1} Res: {2}",
                current.getDeathScore().getScore(),
                current.getGenerationScore().getScore(),
                current.getResurrectionScore().getScore());

        holder.tvScore.setText(msg);

        holder.row_saved_games_list.setOnClickListener(view -> listener.onRowClick(current));
        holder.row_saved_games_list.setOnLongClickListener(view -> {
            listener.onRowLongClick(current);
            return false;
        });

        holder.btnStartGame.setOnClickListener(view -> listener.onButtonClick(current));
    }

    @Override
    public int getItemCount() {
        if (gameList == null)
            return 0;
        else
            return gameList.size();
    }

    public class SavedGameViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvGameName;
        public final TextView tvScore;
        public final Button btnStartGame;
        public final ConstraintLayout row_saved_games_list;

        public SavedGameViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvScore = itemView.findViewById(R.id.tvScore);
            btnStartGame = itemView.findViewById(R.id.btnStartGame);
            row_saved_games_list = itemView.findViewById(R.id.row_saved_games_list);
        }
    }
}
