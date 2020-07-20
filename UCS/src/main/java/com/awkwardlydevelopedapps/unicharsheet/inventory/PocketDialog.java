package com.awkwardlydevelopedapps.unicharsheet.inventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.awkwardlydevelopedapps.unicharsheet.R;

import java.util.Objects;

public class PocketDialog extends DialogFragment {

    private int option;
    private String currencyType;
    private NoticeDialogListener listener;

    public static final int CURRENCY = 0;
    public static final int EXPERIENCE = 1;
    public static final int LEVEL = 2;

    public interface NoticeDialogListener {
        void onCurrencyDialogPositiveClick(DialogFragment dialog, String currencyType, int value, int maxValue);

        void onExperienceDialogPositiveClick(DialogFragment dialog, int value, int maxValue);

        void onLevelDialogPositiveClick(DialogFragment dialog, int value);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (NoticeDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString() + "must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_pocket_fragment, null);
        EditText editTextValue = view.findViewById(R.id.editTextNumber_pocketDialog_value);
        EditText editTextMaxValue = view.findViewById(R.id.editTextNumber_pocketDialog_maxValue);
        TextView textViewMaxValue = view.findViewById(R.id.textView_maxValue);
        if (option == LEVEL) {
            editTextMaxValue.setVisibility(View.GONE);
            textViewMaxValue.setVisibility(View.GONE);
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //On ACCEPT click
                        String valueText = editTextValue.getText().toString();
                        String maxValueText = editTextMaxValue.getText().toString();

                        // Check for value
                        int value;
                        int maxValue;
                        if (valueText.isEmpty())
                            value = 0;
                        else
                            value = Integer.parseInt(valueText);

                        if (maxValueText.isEmpty())
                            maxValue = 100;
                        else
                            maxValue = Integer.parseInt(maxValueText);

                        switch (option) {
                            case CURRENCY:
                                listener.onCurrencyDialogPositiveClick(PocketDialog.this, currencyType, value, maxValue);
                                break;
                            case EXPERIENCE:
                                listener.onExperienceDialogPositiveClick(PocketDialog.this, value, maxValue);
                                break;
                            case LEVEL:
                                listener.onLevelDialogPositiveClick(PocketDialog.this, value);
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Objects.requireNonNull(PocketDialog.this.getDialog()).cancel();
                    }
                });
        return builder.create();
    }

    public void setOption(int option, String currencyType) {
        this.option = option;
        this.currencyType = currencyType;
    }
}
