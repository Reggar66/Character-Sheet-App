package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.MainActivity;
import com.awkwardlydevelopedapps.unicharsheet.spell.Spell;
import com.awkwardlydevelopedapps.unicharsheet.spell.SpellEditorDialog;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.SpellsViewModel;

public class SpellsFragmentDisplay extends Fragment
        implements SpellEditorDialog.NoticeDialogListener {

    private View rootView;

    private int charId;
    private int spellId;
    private SpellsViewModel viewModel;

    private TextView spellTitle;
    private TextView spellDescription;
    private TextView damageValue;
    private TextView additionalEffect;
    private TextView costValue;
    private TextView additionalCost;
    private TextView specialNotes;

    private ChangeFragmentCallback callback;

    public SpellsFragmentDisplay() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_spells_display, container, false);

        charId = ((MainActivity) requireActivity()).characterId;

        ImageView closeSpellDisplayButton = rootView.findViewById(R.id.imageView_close_spell_display);
        closeSpellDisplayButton.setOnClickListener(new CloseButtonOnClick());

        spellTitle = rootView.findViewById(R.id.textView_spell_title);

        spellDescription = rootView.findViewById(R.id.textView_spell_description);
        spellDescription.setOnClickListener(new SpellEditValuesListener(SpellEditorDialog.DESCRIPTION, spellDescription));

        damageValue = rootView.findViewById(R.id.textView_spell_damage_value);
        damageValue.setOnClickListener(new SpellEditValuesListener(SpellEditorDialog.DAMAGE_VALUE, damageValue));

        additionalEffect = rootView.findViewById(R.id.textView_spell_additional_effect);
        additionalEffect.setOnClickListener(new SpellEditValuesListener(SpellEditorDialog.DAMAGE_ADD_EFFECT, additionalEffect));

        costValue = rootView.findViewById(R.id.textView_spell_cost_value);
        costValue.setOnClickListener(new SpellEditValuesListener(SpellEditorDialog.COST_VALUE, costValue));

        additionalCost = rootView.findViewById(R.id.textView_spell_additional_cost);
        additionalCost.setOnClickListener(new SpellEditValuesListener(SpellEditorDialog.COST_ADD_VALUE, additionalCost));

        specialNotes = rootView.findViewById(R.id.textView_spell_special_notes);
        specialNotes.setOnClickListener(new SpellEditValuesListener(SpellEditorDialog.SPECIAL_NOTES, specialNotes));

        viewModel = new ViewModelProvider(requireActivity(),
                new SpellsViewModel.SpellsViewModelFactory(requireActivity().getApplication(), charId))
                .get(SpellsViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel.getSpell(spellId).observe(getViewLifecycleOwner(), new Observer<Spell>() {
            @Override
            public void onChanged(Spell spell) {
                updateSpellInfo(spell);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void updateSpellInfo(Spell spell) {
        //Spell title/name
        spellTitle.setText(spell.getSpellName());

        // Spell description
        if (spell.getDescription().isEmpty()) {
            spellDescription.setText(R.string.click_me_to_set_description);
        } else {
            spellDescription.setText(spell.getDescription());

        }

        // Damage value
        if (spell.getDmg().isEmpty()) {
            damageValue.setText("0");
        } else {
            damageValue.setText(spell.getDmg());
        }

        // Additional effects
        if (spell.getAddEffect().isEmpty()) {
            additionalEffect.setText(R.string.no_add_effect);
        } else {
            additionalEffect.setText(spell.getAddEffect());
        }

        // Cost value
        if (spell.getCost().isEmpty()) {
            costValue.setText("0");
        } else {
            costValue.setText(spell.getCost());
        }

        // Additional cost
        if (spell.getAddCost().isEmpty()) {
            additionalCost.setText(R.string.no_add_cost);
        } else {
            additionalCost.setText(spell.getAddCost());
        }

        // Special notes
        if (spell.getSpecialNotes().isEmpty()) {
            specialNotes.setText(R.string.no_special_notes);
        } else {
            specialNotes.setText(spell.getSpecialNotes());
        }
    }

    public void setChangeFragmentCallback(ChangeFragmentCallback callback) {
        this.callback = callback;
    }

    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }

    @Override
    public void onDialogPositiveClickDescription(DialogFragment dialog, String description) {
        viewModel.updateDescription(description, charId, spellId);
    }

    @Override
    public void onDialogPositiveClickDamageValue(DialogFragment dialog, String dmgValue) {
        viewModel.updateDmgValue(dmgValue, charId, spellId);
    }

    @Override
    public void onDialogPositiveClickAdditionalEffect(DialogFragment dialog, String addEffectValue) {
        viewModel.updateAddEffectValue(addEffectValue, charId, spellId);
    }

    @Override
    public void onDialogPositiveClickCostValue(DialogFragment dialog, String costValue) {
        viewModel.updateCostValue(costValue, charId, spellId);
    }

    @Override
    public void onDialogPositiveClickAdditionalCost(DialogFragment dialog, String addCostValue) {
        viewModel.updateAddCostValue(addCostValue, charId, spellId);
    }

    @Override
    public void onDialogPositiveClickSpecialNotes(DialogFragment dialog, String specialNotes) {
        viewModel.updateSpecialNotes(specialNotes, charId, spellId);
    }

    /**
     * Interfaces
     */

    public interface ChangeFragmentCallback {
        void changeToList();
    }

    /**
     * Inner classes
     */

    private class CloseButtonOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            callback.changeToList();
        }
    }

    private class SpellEditValuesListener implements View.OnClickListener {

        int contentView;
        TextView textView;

        SpellEditValuesListener(int contentView, TextView textView) {
            this.contentView = contentView;
            this.textView = textView;
        }

        @Override
        public void onClick(View view) {
            SpellEditorDialog editorDialog = new SpellEditorDialog();
            editorDialog.setTargetFragment(SpellsFragmentDisplay.this, 0);
            editorDialog.setChangeContentView(contentView);
            editorDialog.setOldValues(getOldValue());
            editorDialog.show(getParentFragmentManager(), "SPELL_EDIT_DIALOG");
        }

        private String getOldValue() {
            String oldValues = textView.getText().toString();
            if (oldValues.equals(requireContext().getResources().getString(R.string.click_me_to_set_description)) ||
                    oldValues.equals(requireContext().getResources().getString(R.string.no_add_effect)) ||
                    oldValues.equals(requireContext().getResources().getString(R.string.no_add_cost)) ||
                    oldValues.equals(requireContext().getResources().getString(R.string.no_special_notes))) {
                return "";
            } else {
                return oldValues;

            }
        }
    }
}
