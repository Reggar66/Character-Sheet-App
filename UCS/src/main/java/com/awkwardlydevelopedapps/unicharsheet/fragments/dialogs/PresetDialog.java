package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.awkwardlydevelopedapps.unicharsheet.R;

public class PresetDialog extends DialogFragment {

    private NoticeDialogListener listener;

    public interface NoticeDialogListener {
        void onPresetAddDialogPositiveClick(DialogFragment dialog, String presetName);

        void onPresetAddDialogNegativeClick(DialogFragment dialog);
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

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_preset, null);

        builder.setView(view).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //On Accept
                EditText editText = view.findViewById(R.id.editText_preset);
                String name = editText.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getContext(), "Name your preset!", Toast.LENGTH_LONG).show();
                } else {
                    listener.onPresetAddDialogPositiveClick(PresetDialog.this, name);
                }

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //On cancel
                listener.onPresetAddDialogNegativeClick(PresetDialog.this);
            }
        });


        return builder.create();
    }
}
