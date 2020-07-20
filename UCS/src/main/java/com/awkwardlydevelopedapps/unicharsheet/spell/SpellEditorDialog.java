package com.awkwardlydevelopedapps.unicharsheet.spell;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.awkwardlydevelopedapps.unicharsheet.R;

import java.util.Objects;

public class SpellEditorDialog extends DialogFragment {

    private NoticeDialogListener listener;

    private View layoutView;
    private int contentView;
    private String oldValues;
    private EditText editTextValues;

    public final static int DESCRIPTION = 0;
    public final static int DAMAGE_VALUE = 1;
    public final static int DAMAGE_ADD_EFFECT = 2;
    public final static int COST_VALUE = 3;
    public final static int COST_ADD_VALUE = 4;
    public final static int SPECIAL_NOTES = 5;

    public interface NoticeDialogListener {
        void onDialogPositiveClickDescription(DialogFragment dialog, String description);

        void onDialogPositiveClickDamageValue(DialogFragment dialog, String dmgValue);

        void onDialogPositiveClickAdditionalEffect(DialogFragment dialog, String addEffectValue);

        void onDialogPositiveClickCostValue(DialogFragment dialog, String costValue);

        void onDialogPositiveClickAdditionalCost(DialogFragment dialog, String addCostValue);

        void onDialogPositiveClickSpecialNotes(DialogFragment dialog, String specialNotes);
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
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setTitle(R.string.ability_editor);

        //Choosing content based on chose value to edit
        switch (contentView) {
            case DESCRIPTION:
                layoutView = inflater.inflate(R.layout.dialog_spell_description_change, null);
                editTextValues = layoutView.findViewById(R.id.editText_description);
                editTextValues.setText(oldValues);
                break;
            case DAMAGE_VALUE:
            case COST_VALUE:
                layoutView = inflater.inflate(R.layout.dialog_spell_damage_change, null);
                editTextValues = layoutView.findViewById(R.id.editText_spell_damage_value);
                editTextValues.setText(oldValues);
                break;
            case DAMAGE_ADD_EFFECT:
                layoutView = inflater.inflate(R.layout.dialog_spell_description_change, null);
                TextView textView = layoutView.findViewById(R.id.textView_desc_hint);
                textView.setText(R.string.enter_additional_effect);
                editTextValues = layoutView.findViewById(R.id.editText_description);
                editTextValues.setText(oldValues);
                break;
            case COST_ADD_VALUE:
                layoutView = inflater.inflate(R.layout.dialog_spell_description_change, null);
                TextView textView2 = layoutView.findViewById(R.id.textView_desc_hint);
                textView2.setText(R.string.enter_additional_cost);
                editTextValues = layoutView.findViewById(R.id.editText_description);
                editTextValues.setText(oldValues);
                break;
            case SPECIAL_NOTES:
                layoutView = inflater.inflate(R.layout.dialog_spell_description_change, null);
                TextView textView3 = layoutView.findViewById(R.id.textView_desc_hint);
                textView3.setText(R.string.enter_special_notes);
                editTextValues = layoutView.findViewById(R.id.editText_description);
                editTextValues.setText(oldValues);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + contentView);
        }

        builder.setView(layoutView);

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (contentView) {
                    case DESCRIPTION:
                        listener.onDialogPositiveClickDescription(SpellEditorDialog.this, getDescriptionText());
                        break;
                    case DAMAGE_VALUE:
                        listener.onDialogPositiveClickDamageValue(SpellEditorDialog.this, getDamageValue());
                        break;
                    case DAMAGE_ADD_EFFECT:
                        listener.onDialogPositiveClickAdditionalEffect(SpellEditorDialog.this, getAddEffectValue());
                        break;
                    case COST_VALUE:
                        listener.onDialogPositiveClickCostValue(SpellEditorDialog.this, getDamageValue());
                        break;
                    case COST_ADD_VALUE:
                        listener.onDialogPositiveClickAdditionalCost(SpellEditorDialog.this, getAddCostValue());
                        break;
                    case SPECIAL_NOTES:
                        listener.onDialogPositiveClickSpecialNotes(SpellEditorDialog.this, getSpecialNotes());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + contentView);
                }

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Objects.requireNonNull(SpellEditorDialog.this.getDialog()).cancel();
            }
        });


        return builder.create();
    }


    private String getDescriptionText() {
        EditText editText = layoutView.findViewById(R.id.editText_description);
        if (editText.getText().toString().isEmpty()) {
            return getString(R.string.click_me_to_set_description);
        } else {
            return editText.getText().toString();
        }
    }

    private String getDamageValue() {
        EditText editText = layoutView.findViewById(R.id.editText_spell_damage_value);
        if (editText.getText().toString().isEmpty()) {
            return "0";
        } else {
            return editText.getText().toString();
        }
    }

    private String getAddEffectValue() {
        EditText editText = layoutView.findViewById(R.id.editText_description);
        if (editText.getText().toString().isEmpty()) {
            return getString(R.string.no_add_effect);
        } else {
            return editText.getText().toString();
        }
    }

    private String getAddCostValue() {
        EditText editText = layoutView.findViewById(R.id.editText_description);
        if (editText.getText().toString().isEmpty()) {
            return getString(R.string.no_add_cost);
        } else {
            return editText.getText().toString();
        }
    }

    private String getSpecialNotes() {
        EditText editText = layoutView.findViewById(R.id.editText_description);
        if (editText.getText().toString().isEmpty()) {
            return getString(R.string.no_special_notes);
        } else {
            return editText.getText().toString();
        }
    }

    public void setChangeContentView(int contentView) {
        this.contentView = contentView;
    }

    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }
}
