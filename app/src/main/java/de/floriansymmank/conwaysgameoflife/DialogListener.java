package de.floriansymmank.conwaysgameoflife;

import androidx.fragment.app.DialogFragment;

public interface DialogListener {
    void onDialogPositiveClick(DialogFragment dialogFragment);

    void onDialogNegativeClick(DialogFragment dialogFragment);
}
