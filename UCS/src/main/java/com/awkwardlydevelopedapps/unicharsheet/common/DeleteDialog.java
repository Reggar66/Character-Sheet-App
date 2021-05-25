package com.awkwardlydevelopedapps.unicharsheet.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.utils.LogWrapper;

import org.jetbrains.annotations.NotNull;

public class DeleteDialog extends DialogFragment {

    private NoticeDialogListener listener;

    public interface NoticeDialogListener {
        void onDeleteDialogPositiveClick(DialogFragment dialog);

        void onDeleteDialogNegativeClick(DialogFragment dialog);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_delete, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, (dialog, id) -> {
                    // On ACCEPT click
                    listener.onDeleteDialogPositiveClick(DeleteDialog.this);
                })
                .setNegativeButton(R.string.cancel,
                        (dialog, id) -> listener.onDeleteDialogNegativeClick(DeleteDialog.this));
        return builder.create();
    }

    public DeleteDialog setNoticeDialogListener(NoticeDialogListener noticeDialogListener) {
        this.listener = noticeDialogListener;

        LogWrapper.Companion.v("SETTER", "NoticeDialogListener set: " + listener.toString());
        return this;
    }
}
