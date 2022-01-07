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

import de.floriansymmank.conwaysgameoflife.DialogListener;
import de.floriansymmank.conwaysgameoflife.R;

public class SaveDialogFragment extends DialogFragment {

    // The tag for this fragment
    public static final String SAVE_DIALOG_FRAGMENT_TAG = "SAVE_DIALOG_FRAGMENT_TAG";

    private final DialogListener listener;
    private EditText edInput;

    public SaveDialogFragment(DialogListener listener) {
        this.listener = listener;
    }

    public static DialogFragment newInstance(DialogListener listener) {
        return new SaveDialogFragment(listener);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.name_input_dialog, null);
        edInput = (EditText) view.findViewById(R.id.et_name);
        edInput.setHint("Name des Spiels");
        builder.setView(view);

        String message = "Wollen Sie den akutellen Zustand speichern?";

        builder.setTitle("Speichern?")
                .setMessage(message)
                .setPositiveButton("Speichern!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(SaveDialogFragment.this);
                    }
                })
                .setNegativeButton("Nö", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(SaveDialogFragment.this);
                    }
                });

        return builder.create();
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