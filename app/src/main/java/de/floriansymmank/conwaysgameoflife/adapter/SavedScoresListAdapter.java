package de.floriansymmank.conwaysgameoflife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.List;

import ConwayGameEngine.FinalScore;
import de.floriansymmank.conwaysgameoflife.R;

public class SavedScoresListAdapter extends RecyclerView.Adapter<SavedScoresListAdapter.SavedScoresViewHolder> {

    private List<FinalScore> scoresList;

    public SavedScoresListAdapter(Context context, List<FinalScore> itemsList) {
        this.scoresList = itemsList;
    }

    @NonNull
    @Override
    public SavedScoresListAdapter.SavedScoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_saved_scores_list, parent, false);
        return new SavedScoresListAdapter.SavedScoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedScoresListAdapter.SavedScoresViewHolder holder, int position) {
        FinalScore current = scoresList.get(position);
        holder.tvPlayerName.setText(current.getPlayerName());

        holder.tvGenScore.setText("GenScore: " + current.getGenerationScore());
        holder.tvDeathScore.setText("DeathScore: " + current.getDeathScore());
        holder.tvResScore.setText("ResScore: " + current.getResurrectionScore());
        holder.tvDate.setText("Date: " + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(current.getDate()));
    }

    @Override
    public int getItemCount() {
        if (scoresList == null)
            return 0;
        else
            return scoresList.size();
    }

    public class SavedScoresViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvPlayerName;
        public final TextView tvResScore;
        public final TextView tvDeathScore;
        public final TextView tvGenScore;
        public final TextView tvDate;

        public final ConstraintLayout row_saved_scores_list;

        public SavedScoresViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvResScore = itemView.findViewById(R.id.tvResScore);
            tvDeathScore = itemView.findViewById(R.id.tvDeathScore);
            tvGenScore = itemView.findViewById(R.id.tvGenScore);
            tvDate = itemView.findViewById(R.id.tvDate);
            row_saved_scores_list = itemView.findViewById(R.id.row_saved_scores_list);
        }
    }
}
