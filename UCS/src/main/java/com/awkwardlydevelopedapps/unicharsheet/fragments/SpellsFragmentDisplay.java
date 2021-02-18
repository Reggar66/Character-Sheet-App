package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;
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
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.SpellEditorBottomSheetDialog;
import com.awkwardlydevelopedapps.unicharsheet.models.Spell;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.DataHolderViewModel;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.SpellsViewModel;

import org.jetbrains.annotations.NotNull;

public class SpellsFragmentDisplay extends Fragment
        implements SpellEditorBottomSheetDialog.NoticeDialogListener {

    private View rootView;

    private int characterID;
    private int spellID;
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

        DataHolderViewModel dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);
        characterID = dataHolderViewModel.getCharacterID();

        viewModel = new ViewModelProvider(this,
                new SpellsViewModel.SpellsViewModelFactory(requireActivity().getApplication(),
                        characterID))
                .get(SpellsViewModel.class);

        spellID = dataHolderViewModel.getSelectedSpellID();

        ImageView closeSpellDisplayButton = rootView.findViewById(R.id.imageView_close_spell_display);
        closeSpellDisplayButton.setOnClickListener(new CloseButtonOnClick());

        spellTitle = rootView.findViewById(R.id.textView_spell_title);

        spellDescription = rootView.findViewById(R.id.textView_spell_description);
        spellDescription.setOnClickListener(
                new SpellEditValuesListener(SpellEditorBottomSheetDialog.DESCRIPTION,
                        spellDescription));

        damageValue = rootView.findViewById(R.id.textView_spell_damage_value);
        damageValue.setOnClickListener(
                new SpellEditValuesListener(SpellEditorBottomSheetDialog.DAMAGE_VALUE,
                        damageValue));

        additionalEffect = rootView.findViewById(R.id.textView_spell_additional_effect);
        additionalEffect.setOnClickListener(
                new SpellEditValuesListener(SpellEditorBottomSheetDialog.DAMAGE_ADD_EFFECT,
                        additionalEffect));

        costValue = rootView.findViewById(R.id.textView_spell_cost_value);
        costValue.setOnClickListener(
                new SpellEditValuesListener(SpellEditorBottomSheetDialog.COST_VALUE,
                        costValue));

        additionalCost = rootView.findViewById(R.id.textView_spell_additional_cost);
        additionalCost.setOnClickListener(
                new SpellEditValuesListener(SpellEditorBottomSheetDialog.COST_ADD_VALUE,
                        additionalCost));

        specialNotes = rootView.findViewById(R.id.textView_spell_special_notes);
        specialNotes.setOnClickListener(
                new SpellEditValuesListener(SpellEditorBottomSheetDialog.SPECIAL_NOTES,
                        specialNotes));


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel.getSpell(spellID).observe(getViewLifecycleOwner(), new Observer<Spell>() {
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

    public void setSpellID(int spellID) {
        this.spellID = spellID;
    }

    @Override
    public void onPositiveDialogListener(@NotNull DialogFragment dialog, int option, @NotNull String valueToUpdate) {
        switch (option) {
            case SpellEditorBottomSheetDialog.DESCRIPTION:
                viewModel.updateDescription(valueToUpdate, characterID, spellID);
                break;
            case SpellEditorBottomSheetDialog.DAMAGE_VALUE:
                viewModel.updateDmgValue(valueToUpdate, characterID, spellID);
                break;
            case SpellEditorBottomSheetDialog.DAMAGE_ADD_EFFECT:
                viewModel.updateAddEffectValue(valueToUpdate, characterID, spellID);
                break;
            case SpellEditorBottomSheetDialog.COST_VALUE:
                viewModel.updateCostValue(valueToUpdate, characterID, spellID);
                break;
            case SpellEditorBottomSheetDialog.COST_ADD_VALUE:
                viewModel.updateAddCostValue(valueToUpdate, characterID, spellID);
                break;
            case SpellEditorBottomSheetDialog.SPECIAL_NOTES:
                viewModel.updateSpecialNotes(valueToUpdate, characterID, spellID);
                break;
        }
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

        int option;
        TextView textView;

        SpellEditValuesListener(int contentView, TextView textView) {
            this.option = contentView;
            this.textView = textView;
        }

        @Override
        public void onClick(View view) {
            showBottomEditDialog();
        }

        private void showBottomEditDialog() {
            SpellEditorBottomSheetDialog bottomSheetDialog =
                    new SpellEditorBottomSheetDialog(option);
            bottomSheetDialog.setNoticeDialogListener(SpellsFragmentDisplay.this);
            bottomSheetDialog.setOldValue(getOldValue());
            bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_SPELL_EDITOR");
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
