package de.floriansymmank.conwaysgameoflife;

import androidx.fragment.app.DialogFragment;

public interface DialogListener {
    void onDialogPositiveClick(DialogFragment dialogFragment);

    default void onDialogNegativeClick(DialogFragment dialogFragment) {
    }

    void onDialogDismiss(DialogFragment dialogFragment);

    default void onDialogCancel(DialogFragment dialogFragment) {
    }

    ;
}
