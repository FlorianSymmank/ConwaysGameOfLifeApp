package de.floriansymmank.conwaysgameoflife.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ConwayGameEngine.FinalScore;
import de.floriansymmank.conwaysgameoflife.DialogListener;

public class ShareDialogFragment extends DialogFragment {

    private FinalScore score;
    private String message = "";
    private DialogListener listener;

    public ShareDialogFragment(FinalScore score, DialogListener listener) {
        this.score = score;
        this.listener = listener;
        message = "gen: " + score.getGenerationScore() + " res: " + score.getResurrectionScore() + " deaths: " + score.getDeathScore();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Share this Result? " + message)
                .setPositiveButton("Share!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(ShareDialogFragment.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(ShareDialogFragment.this);
                    }
                });

        return builder.create();
    }

    public FinalScore getScore() {
        return score;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        listener.onDialogCancel(this);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onDialogDismiss(this);
    }
}
