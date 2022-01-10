package de.floriansymmank.conwaysgameoflife.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ConwayGameEngine.FinalScore;
import de.floriansymmank.conwaysgameoflife.R;

public class ShareDialogFragment extends DialogFragment {

    // The tag for this fragment
    public static final String SHARE_DIALOG_FRAGMENT_TAG = "SHARE_DIALOG_FRAGMENT_TAG";

    private final FinalScore score;
    private final DialogListener listener;
    private final boolean hasTextInput;
    private EditText edInput;

    public ShareDialogFragment(FinalScore score, DialogListener listener, boolean hasTextInput) {
        this.score = score;
        this.listener = listener;
        this.hasTextInput = hasTextInput;
    }

    public static DialogFragment newInstance(FinalScore score, DialogListener listener, boolean hasTextInput) {
        return new ShareDialogFragment(score, listener, hasTextInput);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        if (hasTextInput) {
            View view = inflater.inflate(R.layout.name_input_dialog, null);
            edInput = (EditText) view.findViewById(R.id.et_name);
            builder.setView(view);
        }

        String message = "gen: " + score.getGenerationScore() + " res: " + score.getResurrectionScore() + " deaths: " + score.getDeathScore();

        builder.setTitle("Share this Result?")
                .setMessage(message)
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

    public String getInputText() {
        return edInput.getText().toString();
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
