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

import java.util.List;

import ConwayGameEngine.ConwayGame;
import de.floriansymmank.conwaysgameoflife.R;

public class SavedGamesListAdapter extends RecyclerView.Adapter<SavedGamesListAdapter.SavedGameViewHolder> {

    private List<ConwayGame> gameList;
    private Context context;
    private ListAdapterListener<ConwayGame> listener;

    public SavedGamesListAdapter(Context context, ListAdapterListener<ConwayGame> listener, List<ConwayGame> itemsList) {
        this.context = context;
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
        holder.tvGameName.setText(current.getName());

        holder.tvDeathScore.setText(String.format("%s: %d",
                context.getString(R.string.DeathScore),
                current.getDeathScore().getScore()));

        holder.tvGenScore.setText(String.format("%s: %d",
                context.getString(R.string.GenScore),
                current.getGenerationScore().getScore()));

        holder.tvResScore.setText(String.format("%s: %d",
                context.getString(R.string.ResScore),
                current.getResurrectionScore().getScore()));

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
        public final TextView tvGenScore;
        public final TextView tvResScore;
        public final TextView tvDeathScore;

        public final Button btnStartGame;
        public final ConstraintLayout row_saved_games_list;

        public SavedGameViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvGenScore = itemView.findViewById(R.id.tvGenScore);
            tvResScore = itemView.findViewById(R.id.tvResScore);
            tvDeathScore = itemView.findViewById(R.id.tvDeathScore);
            btnStartGame = itemView.findViewById(R.id.btnStartGame);
            row_saved_games_list = itemView.findViewById(R.id.row_saved_games_list);
        }
    }
}
