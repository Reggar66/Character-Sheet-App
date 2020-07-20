package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.MainActivity;
import com.awkwardlydevelopedapps.unicharsheet.inventory.CharEqDialog;
import com.awkwardlydevelopedapps.unicharsheet.inventory.Equipment;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.EquipmentViewModel;

import java.util.List;
import java.util.Objects;

public class EquipmentFragment extends Fragment
        implements CharEqDialog.NoticeDialogListener {

    private View rootView;
    private int charId;

    private EquipmentViewModel viewModel;

    private String slotCurrentlySelected = SLOT_HEAD;

    // Slot buttons
    private ImageView imageViewHead;
    private ImageView imageViewNeck;
    private ImageView imageViewChest;
    private ImageView imageViewRight;
    private ImageView imageViewLeft;
    private ImageView imageViewLegs;
    private ImageView imageViewFeet;
    private ImageView imageViewWeapon;
    private ImageView imageViewOffHand;
    private ImageView imageViewEars;
    private ImageView imageViewFinger;

    // Item details texts.
    private TextView textViewSlotItemArmorOrDamage;
    private TextView textViewItemName;
    private TextView textViewItemArmorType;
    private TextView textViewItemArmorValue;
    private TextView textViewItemAdditionalEffectsValue;

    private ImageView imageViewEditButton;

    private final static String SLOT_HEAD = "Head";
    private final static String SLOT_NECK = "Neck";
    private final static String SLOT_CHEST = "Chest";
    private final static String SLOT_LEFT_ARM = "Left arm";
    private final static String SLOT_RIGHT_ARM = "Right arm";
    private final static String SLOT_LEGS = "Legs";
    private final static String SLOT_FEET = "Feet";
    private final static String SLOT_WEAPON = "Weapon";
    private final static String SLOT_OFF_HAND = "Off hand";
    private final static String SLOT_EARS = "Ears";
    private final static String SLOT_FINGER = "FINGER";


    public EquipmentFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_equipment, container, false);

        charId = ((MainActivity) requireActivity()).characterId;

        // Head
        imageViewHead = rootView.findViewById(R.id.imageView_slot_head);
        imageViewHead.setOnClickListener(new SlotOnClick(SLOT_HEAD));

        // Neck
        imageViewNeck = rootView.findViewById(R.id.imageView_slot_neck);
        imageViewNeck.setOnClickListener(new SlotOnClick(SLOT_NECK));

        // Chest
        imageViewChest = rootView.findViewById(R.id.imageView_slot_chest);
        imageViewChest.setOnClickListener(new SlotOnClick(SLOT_CHEST));

        // Right arm
        imageViewRight = rootView.findViewById(R.id.imageView_slot_right_arm);
        imageViewRight.setOnClickListener(new SlotOnClick(SLOT_RIGHT_ARM));

        // Left arm
        imageViewLeft = rootView.findViewById(R.id.imageView_slot_left_arm);
        imageViewLeft.setOnClickListener(new SlotOnClick(SLOT_LEFT_ARM));

        // Legs
        imageViewLegs = rootView.findViewById(R.id.imageView_slot_legs);
        imageViewLegs.setOnClickListener(new SlotOnClick(SLOT_LEGS));
        // Feet
        imageViewFeet = rootView.findViewById(R.id.imageView_slot_feet);
        imageViewFeet.setOnClickListener(new SlotOnClick(SLOT_FEET));

        // Weapon
        imageViewWeapon = rootView.findViewById(R.id.imageView_slot_weapon);
        imageViewWeapon.setOnClickListener(new SlotOnClick(SLOT_WEAPON));

        // Off hand
        imageViewOffHand = rootView.findViewById(R.id.imageView_slot_off_hand);
        imageViewOffHand.setOnClickListener(new SlotOnClick(SLOT_OFF_HAND));

        // Ears
        imageViewEars = rootView.findViewById(R.id.imageView_slot_ears);
        imageViewEars.setOnClickListener(new SlotOnClick(SLOT_EARS));

        //Finger
        imageViewFinger = rootView.findViewById(R.id.imageView_slot_finger);
        imageViewFinger.setOnClickListener(new SlotOnClick(SLOT_FINGER));

        // Item details edit button
        imageViewEditButton = rootView.findViewById(R.id.imageView_editDetails);
        imageViewEditButton.setOnClickListener(new ItemDetailOnClick());

        // Item descriptions texts
        textViewItemName = rootView.findViewById(R.id.textView_slot_item_name);
        textViewItemArmorType = rootView.findViewById(R.id.textView_slot_item_type_value);
        textViewItemArmorValue = rootView.findViewById(R.id.textView_slot_item_armor_value);
        textViewItemAdditionalEffectsValue = rootView.findViewById(R.id.textView_slot_item_additionalEffects_value);
        textViewSlotItemArmorOrDamage = rootView.findViewById(R.id.textView_slot_item_armor);

        viewModel = new ViewModelProvider(this,
                new EquipmentViewModel.EquipmentViewModelFactory(requireActivity().getApplication(), charId))
                .get(EquipmentViewModel.class);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllEquipment().observe(getViewLifecycleOwner(), new Observer<List<Equipment>>() {
            @Override
            public void onChanged(List<Equipment> equipment) {
                // Saves the most fresh list of EQ to use on changing slots
                viewModel.setEquipmentList(equipment);

                // Checks if slot exists in DB, if not - creates them
                if (!checkForSlot(slotCurrentlySelected)) {
                    viewModel.insertEq(new Equipment(getResources().getString(R.string.eq_no_item_name),
                            slotCurrentlySelected,
                            getResources().getString(R.string.eq_type_none),
                            "0",
                            getResources().getString(R.string.eq_no_additional_effects),
                            charId));
                }

                // Updates texts for currently selected slots
                for (Equipment e : equipment) {
                    if (e.getSlot().equals(slotCurrentlySelected)) {
                        updateTextDetails(e);
                    }
                }
            }
        });
    }

    private void updateTextDetails(Equipment e) {
        // Name
        if (e.getName() == null || e.getName().isEmpty()) {
            textViewItemName.setText(R.string.eq_no_item_name);
        } else {
            textViewItemName.setText(e.getName());
        }

        // Type
        if (e.getType() == null || e.getType().isEmpty()) {
            textViewItemArmorType.setText(R.string.eq_type_none);
        } else {
            textViewItemArmorType.setText(e.getType());
        }

        // Value
        if (e.getValue() == null || e.getValue().isEmpty()) {
            textViewItemArmorValue.setText("0");
        } else {
            textViewItemArmorValue.setText(e.getValue());
        }

        // Additional effects
        if (e.getAddEffect() == null || e.getAddEffect().isEmpty()) {
            textViewItemAdditionalEffectsValue.setText(R.string.eq_no_additional_effects);
        } else {
            textViewItemAdditionalEffectsValue.setText(e.getAddEffect());
        }
    }

    private boolean checkForSlot(String slot) {
        for (Equipment e : viewModel.getEquipmentList()) {
            // If given slot exists -> return true
            if (e.getSlot().equals(slot)) {
                return true;
            }
        }
        // else -> return false
        return false;
    }

    private void slotChangeState(String slot) {
        imageViewHead.setBackground(null);
        imageViewNeck.setBackground(null);
        imageViewChest.setBackground(null);
        imageViewRight.setBackground(null);
        imageViewLeft.setBackground(null);
        imageViewLegs.setBackground(null);
        imageViewFeet.setBackground(null);
        imageViewWeapon.setBackground(null);
        imageViewOffHand.setBackground(null);
        imageViewEars.setBackground(null);
        imageViewFinger.setBackground(null);

        switch (slot) {
            case SLOT_HEAD:
                imageViewHead.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_CHEST:
                imageViewChest.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_RIGHT_ARM:
                imageViewRight.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_LEFT_ARM:
                imageViewLeft.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_FEET:
                imageViewFeet.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_NECK:
                imageViewNeck.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_LEGS:
                imageViewLegs.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_WEAPON:
                imageViewWeapon.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_OFF_HAND:
                imageViewOffHand.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_EARS:
                imageViewEars.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
            case SLOT_FINGER:
                imageViewFinger.setBackground(requireContext().getDrawable(R.drawable.slot_selected_drawable));
                break;
        }
    }

    @Override
    public void onEqDialogPositiveClick(DialogFragment dialog, String name, String type, String value, String additionalEffects) {
        viewModel.updateEquipment(name, type, value, additionalEffects, slotCurrentlySelected, charId);
    }

    @Override
    public void onEqDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }


    /**
     * Inner classes
     */

    private class SlotOnClick implements View.OnClickListener {

        private String slot;

        SlotOnClick(String slot) {
            this.slot = slot;
        }

        @Override
        public void onClick(View view) {
            enterSlotState();
            for (Equipment e : viewModel.getEquipmentList()) {
                if (e.getSlot().equals(slot)) {
                    updateTextDetails(e);
                }
            }
        }

        private void enterSlotState() {

            slotCurrentlySelected = slot;

            if (!checkForSlot(slotCurrentlySelected)) {
                viewModel.insertEq(new Equipment(getResources().getString(R.string.eq_no_item_name),
                        slotCurrentlySelected,
                        getResources().getString(R.string.eq_type_none),
                        "0",
                        getResources().getString(R.string.eq_no_additional_effects),
                        charId));
            }

            if (slot.equals(SLOT_WEAPON) || slot.equals(SLOT_OFF_HAND)) {
                textViewSlotItemArmorOrDamage.setText(R.string.eq_value);
            } else {
                textViewSlotItemArmorOrDamage.setText(R.string.eq_armor);
            }

            slotChangeState(slot);
        }
    }

    private class ItemDetailOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            CharEqDialog dialog = new CharEqDialog();
            dialog.setTargetFragment(EquipmentFragment.this, 0);
            dialog.setOldNameAndValue(textViewItemName.getText().toString(),
                    textViewItemArmorType.getText().toString(),
                    textViewItemArmorValue.getText().toString(),
                    textViewItemAdditionalEffectsValue.getText().toString());
            dialog.show(getParentFragmentManager(), "ITEM_DETAIL_EDIT_DIALOG");
        }
    }
}

