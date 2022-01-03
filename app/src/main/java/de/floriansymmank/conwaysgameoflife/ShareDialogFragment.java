package de.floriansymmank.conwaysgameoflife;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ConwayGameEngine.FinalScore;

public class ShareDialogFragment extends DialogFragment {
    private FinalScore score;
    private String message = "";
    private DialogListener listener;

    public ShareDialogFragment(FinalScore score) {
        this.score = score;
        message = "gen: " + score.getGenerationScore() + " res: " + score.getResurrectionScore() + " deaths: " + score.getDeathScore();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Share this Result? " + message)
                .setPositiveButton("Share!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        Log.println(Log.DEBUG, "Share", "Yes");
                        listener.onDialogPositiveClick(ShareDialogFragment.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.println(Log.DEBUG, "Share", "No");
                        listener.onDialogNegativeClick(ShareDialogFragment.this);

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString() + " must implement DialogListener");
        }
    }

    public FinalScore getScore() {
        return score;
    }
}
