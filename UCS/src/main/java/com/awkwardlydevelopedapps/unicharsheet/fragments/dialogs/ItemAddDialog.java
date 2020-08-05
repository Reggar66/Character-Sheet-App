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

public class ItemAddDialog extends DialogFragment {

    public interface NoticeDialogListener {
        void onItemAddDialogPositiveClick(DialogFragment dialog, String name, String quantity);

        void onItemAddDialogNegativeClick(DialogFragment dialog);
    }

    private NoticeDialogListener listener;
    private String name;
    private String itemQuantity;

    private View rootView;

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

        rootView = inflater.inflate(R.layout.dialog_items, null);

        EditText editTextName = rootView.findViewById(R.id.item_name_dialog);
        EditText editTextQuantity = rootView.findViewById(R.id.item_quantity_dialog);

        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //On ACCEPT click
                        name = editTextName.getText().toString();
                        itemQuantity = editTextQuantity.getText().toString();
                        listener.onItemAddDialogPositiveClick(ItemAddDialog.this, name, itemQuantity);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onItemAddDialogNegativeClick(ItemAddDialog.this);
                    }
                });
        return builder.create();
    }
}
