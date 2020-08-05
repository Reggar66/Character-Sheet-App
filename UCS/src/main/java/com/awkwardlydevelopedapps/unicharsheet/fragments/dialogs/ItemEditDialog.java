package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.models.Item;

public class ItemEditDialog extends DialogFragment {

    private View view;
    private String oldName;
    private String oldQuantity;
    private int itemId;

    private NoticeDialogListener listener;


    public interface NoticeDialogListener {
        void onEditDialogPositiveClick(DialogFragment dialog, String name, String quantity, int id);

        void onEditDialogNegativeClick(DialogFragment dialog);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_items, null);
        EditText editTextName = view.findViewById(R.id.item_name_dialog);
        editTextName.setText(oldName);
        EditText editTextQuantity = view.findViewById(R.id.item_quantity_dialog);
        editTextQuantity.setText(oldQuantity);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //On ACCEPT click
                        listener.onEditDialogPositiveClick(ItemEditDialog.this, editTextName.getText().toString(), editTextQuantity.getText().toString(), itemId);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onEditDialogNegativeClick(ItemEditDialog.this);
                    }
                });
        return builder.create();
    }

    public void setOldItem(Item item) {
        oldName = item.getName();
        oldQuantity = item.getQuantity();
        itemId = item.id;
    }
}
