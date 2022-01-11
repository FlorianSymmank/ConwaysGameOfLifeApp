package de.floriansymmank.conwaysgameoflife.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
    private final boolean hasTextInput; // can turn on/off extra text input
    private EditText edInput;

    public ShareDialogFragment(FinalScore score, DialogListener listener, boolean hasTextInput) {
        this.score = score;
        this.listener = listener;
        this.hasTextInput = hasTextInput;
    }

    public static DialogFragment newInstance(FinalScore score, DialogListener listener, boolean hasTextInput) {
        Log.println(Log.DEBUG, "ShareDialogFragment newInstance", "creating new share Dialog instance");
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

        String message = String.format("%s:%d\n %s: %d\n %s: %d",
                getString(R.string.GenScore), score.getGenerationScore(),
                getString(R.string.ResScore), score.getResurrectionScore(),
                getString(R.string.DeathScore), score.getDeathScore());

        builder.setTitle(getString(R.string.ShareThisResult))
                .setMessage(message)
                .setPositiveButton(getString(R.string.Share) + "!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(ShareDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
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
