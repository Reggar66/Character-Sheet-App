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

public class CharEqDialog extends DialogFragment {

    private NoticeDialogListener listener;
    private String oldName;
    private String oldType;
    private String oldValue;
    private String oldAdditionalEffects;
    private EditText editTextName;
    private EditText editTextType;
    private EditText editTextValue;
    private EditText editTextAdditionalEffects;

    public interface NoticeDialogListener {
        void onEqDialogPositiveClick(DialogFragment dialog, String name, String type, String value, String additionalEffects);

        void onEqDialogNegativeClick(DialogFragment dialog);
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

        final View view = inflater.inflate(R.layout.dialog_equipment, null);

        editTextName = view.findViewById(R.id.editText_name);
        editTextName.setText(oldName);

        editTextType = view.findViewById(R.id.editText_type);
        editTextType.setText(oldType);

        editTextValue = view.findViewById(R.id.editText_value);
        editTextValue.setText(oldValue);

        editTextAdditionalEffects = view.findViewById(R.id.editText_additionalEffects);
        editTextAdditionalEffects.setText(oldAdditionalEffects);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //On ACCEPT click
                        listener.onEqDialogPositiveClick(CharEqDialog.this,
                                editTextName.getText().toString(),
                                editTextType.getText().toString(),
                                editTextValue.getText().toString(),
                                editTextAdditionalEffects.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //On cancel
                        listener.onEqDialogNegativeClick(CharEqDialog.this);
                    }
                });
        return builder.create();
    }

    public void setOldNameAndValue(String oldName, String oldType, String oldValue, String oldAdditionalEffects) {
        this.oldName = oldName;
        this.oldType = oldType;
        this.oldValue = oldValue;
        this.oldAdditionalEffects = oldAdditionalEffects;
    }
}
