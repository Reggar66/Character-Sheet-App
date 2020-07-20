package com.awkwardlydevelopedapps.unicharsheet.stat;

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

public class StatTabNameChangeDialog extends DialogFragment {

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String tabName);

        void onDialogNegativeClick(DialogFragment dialog);
    }


    private NoticeDialogListener listener;

    private View layoutView;

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
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        layoutView = inflater.inflate(R.layout.dialog_stat_tab_name_change, null);

        builder.setTitle("Tab name change");

        builder.setView(layoutView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do stuff on OK.
                EditText editText = layoutView.findViewById(R.id.editText_tabName);
                String tabName = editText.getText().toString();
                listener.onDialogPositiveClick(StatTabNameChangeDialog.this, tabName);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel on CANCEL
                listener.onDialogNegativeClick(StatTabNameChangeDialog.this);
            }
        });


        return builder.create();
    }
}
