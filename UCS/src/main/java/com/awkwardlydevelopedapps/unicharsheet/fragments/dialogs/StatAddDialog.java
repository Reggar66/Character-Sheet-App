package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.awkwardlydevelopedapps.unicharsheet.R;

public class StatAddDialog extends DialogFragment {

    private String mStatName;
    private String mStatValue;

    final private String DEFAULT_VALUE = "0";

    private NoticeDialogListener listener;

    public interface NoticeDialogListener {
        void onStatAddDialogPositiveClick(DialogFragment dialog, String name, String value);

        void onStatAddDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (NoticeDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_stat_add, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // On ACCEPT click

                        EditText editText = (EditText) view.findViewById(R.id.stat_name_dialog_editText);
                        mStatName = editText.getText().toString();
                        editText = view.findViewById(R.id.stat_value_dialog_editText);

                        String temp = editText.getText().toString();
                        if (!temp.isEmpty()) {
                            mStatValue = editText.getText().toString();
                        } else {
                            mStatValue = DEFAULT_VALUE;
                        }

                        listener.onStatAddDialogPositiveClick(StatAddDialog.this, mStatName, mStatValue);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onStatAddDialogNegativeClick(StatAddDialog.this);
                    }
                });
        return builder.create();
    }
}
