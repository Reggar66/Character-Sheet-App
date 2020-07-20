package com.awkwardlydevelopedapps.unicharsheet.stat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.data.StatDao;

import java.util.ArrayList;

public class StatDialog extends DialogFragment {

    private String mAttributeValue;

    final private String DEFAULT_VALUE = "0";

    private NoticeDialogListener listener;

    public interface NoticeDialogListener {
        void onStatDialogPositiveClick(DialogFragment dialog, String value);

        void onStatDialogNegativeClick(DialogFragment dialog);
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

        final View view = inflater.inflate(R.layout.dialog_stat_value, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // On ACCEPT click
                        //get text view charId to set data
                        EditText editText = (EditText) view.findViewById(R.id.stat_value_dialog_editText);

                        String temp = editText.getText().toString();
                        if (!temp.isEmpty()) {
                            mAttributeValue = editText.getText().toString();
                        } else {
                            mAttributeValue = DEFAULT_VALUE;
                        }
                        listener.onStatDialogPositiveClick(StatDialog.this, mAttributeValue);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onStatDialogNegativeClick(StatDialog.this);
                    }
                });
        return builder.create();
    }
}
