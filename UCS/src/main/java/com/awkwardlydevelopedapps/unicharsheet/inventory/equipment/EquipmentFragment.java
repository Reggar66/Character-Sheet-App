package com.awkwardlydevelopedapps.unicharsheet.inventory.equipment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.model.Equipment;
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.viewModel.EquipmentViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class EquipmentFragment extends Fragment
        implements EquipmentBottomSheetDialog.NoticeDialogListener {

    private View rootView;
    private int characterID;

    private EquipmentViewModel viewModel;

    private String slotCurrentlySelected;
    private String lastSlotSelected;
    private final HashMap<String, ImageView> buttonMap = new HashMap<>();

    // Item details texts.
    private TextView textViewSlotItemArmorOrDamage;
    private TextView textViewItemName;
    private TextView textViewItemArmorType;
    private TextView textViewItemArmorValue;
    private TextView textViewItemAdditionalEffectsValue;

    // KEYs
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

    private final static String KEY_SLOT_SELECTED = "SLOT_SELECTED";


    public EquipmentFragment() {

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            slotCurrentlySelected = SLOT_HEAD;
        } else {
            slotCurrentlySelected = savedInstanceState.getString(KEY_SLOT_SELECTED);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_equipment, container, false);

        DataHolderViewModel dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);

        characterID = dataHolderViewModel.getCharacterID();

        // Head
        // Slot buttons
        ImageView imageViewHead = rootView.findViewById(R.id.imageView_slot_head);
        imageViewHead.setOnClickListener(new SlotOnClick(SLOT_HEAD));
        buttonMap.put(SLOT_HEAD, imageViewHead);

        // Neck
        ImageView imageViewNeck = rootView.findViewById(R.id.imageView_slot_neck);
        imageViewNeck.setOnClickListener(new SlotOnClick(SLOT_NECK));
        buttonMap.put(SLOT_NECK, imageViewNeck);

        // Chest
        ImageView imageViewChest = rootView.findViewById(R.id.imageView_slot_chest);
        imageViewChest.setOnClickListener(new SlotOnClick(SLOT_CHEST));
        buttonMap.put(SLOT_CHEST, imageViewChest);

        // Right arm
        ImageView imageViewRight = rootView.findViewById(R.id.imageView_slot_right_arm);
        imageViewRight.setOnClickListener(new SlotOnClick(SLOT_RIGHT_ARM));
        buttonMap.put(SLOT_RIGHT_ARM, imageViewRight);

        // Left arm
        ImageView imageViewLeft = rootView.findViewById(R.id.imageView_slot_left_arm);
        imageViewLeft.setOnClickListener(new SlotOnClick(SLOT_LEFT_ARM));
        buttonMap.put(SLOT_LEFT_ARM, imageViewLeft);

        // Legs
        ImageView imageViewLegs = rootView.findViewById(R.id.imageView_slot_legs);
        imageViewLegs.setOnClickListener(new SlotOnClick(SLOT_LEGS));
        buttonMap.put(SLOT_LEGS, imageViewLegs);

        // Feet
        ImageView imageViewFeet = rootView.findViewById(R.id.imageView_slot_feet);
        imageViewFeet.setOnClickListener(new SlotOnClick(SLOT_FEET));
        buttonMap.put(SLOT_FEET, imageViewFeet);

        // Weapon
        ImageView imageViewWeapon = rootView.findViewById(R.id.imageView_slot_weapon);
        imageViewWeapon.setOnClickListener(new SlotOnClick(SLOT_WEAPON));
        buttonMap.put(SLOT_WEAPON, imageViewWeapon);

        // Off hand
        ImageView imageViewOffHand = rootView.findViewById(R.id.imageView_slot_off_hand);
        imageViewOffHand.setOnClickListener(new SlotOnClick(SLOT_OFF_HAND));
        buttonMap.put(SLOT_OFF_HAND, imageViewOffHand);

        // Ears
        ImageView imageViewEars = rootView.findViewById(R.id.imageView_slot_ears);
        imageViewEars.setOnClickListener(new SlotOnClick(SLOT_EARS));
        buttonMap.put(SLOT_EARS, imageViewEars);

        //Finger
        ImageView imageViewFinger = rootView.findViewById(R.id.imageView_slot_finger);
        imageViewFinger.setOnClickListener(new SlotOnClick(SLOT_FINGER));
        buttonMap.put(SLOT_FINGER, imageViewFinger);

        // Set selected state for current button
        slotChangeState(slotCurrentlySelected);

        // Item details edit button
        ImageView imageViewEditButton = rootView.findViewById(R.id.imageView_editDetails);
        imageViewEditButton.setOnClickListener(new ItemDetailOnClick());

        // Item descriptions texts
        textViewItemName = rootView.findViewById(R.id.textView_slot_item_name);
        textViewItemArmorType = rootView.findViewById(R.id.textView_slot_item_type_value);
        textViewItemArmorValue = rootView.findViewById(R.id.textView_slot_item_armor_value);
        textViewItemAdditionalEffectsValue = rootView.findViewById(R.id.textView_slot_item_additionalEffects_value);
        textViewSlotItemArmorOrDamage = rootView.findViewById(R.id.textView_slot_item_armor);

        viewModel = new ViewModelProvider(this,
                new EquipmentViewModel.EquipmentViewModelFactory(requireActivity().getApplication(), characterID))
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
                            characterID));
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

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SLOT_SELECTED, slotCurrentlySelected);
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

    private void slotChangeState(String slotKey) {
        ImageView lastButton = buttonMap.get(lastSlotSelected);
        if (lastButton != null)
            lastButton.setBackground(null);

        ImageView currentButton = buttonMap.get(slotKey);
        if (currentButton != null) {
            currentButton.setBackground(ContextCompat.getDrawable(requireContext(),
                    R.drawable.slot_selected_drawable));

            lastSlotSelected = slotKey;
        }
    }

    @Override
    public void onPositiveClickListener(@NotNull String name, @NotNull String type, @NotNull String value, @NotNull String additionalEffect, @NotNull String slot) {
        viewModel.updateEquipment(
                name,
                type,
                value,
                additionalEffect,
                slot,
                characterID
        );
    }

    /**
     * Inner classes
     */

    private class SlotOnClick implements View.OnClickListener {

        private final String slotKey;

        SlotOnClick(String slotKey) {
            this.slotKey = slotKey;
        }

        @Override
        public void onClick(View view) {
            enterSlotState();
            for (Equipment e : viewModel.getEquipmentList()) {
                if (e.getSlot().equals(slotKey)) {
                    updateTextDetails(e);
                }
            }
        }

        private void enterSlotState() {
            slotCurrentlySelected = slotKey;

            if (!checkForSlot(slotCurrentlySelected)) {
                viewModel.insertEq(new Equipment(getResources().getString(R.string.eq_no_item_name),
                        slotCurrentlySelected,
                        getResources().getString(R.string.eq_type_none),
                        "0",
                        getResources().getString(R.string.eq_no_additional_effects),
                        characterID));
            }

            if (slotKey.equals(SLOT_WEAPON) || slotKey.equals(SLOT_OFF_HAND)) {
                textViewSlotItemArmorOrDamage.setText(R.string.eq_value);
            } else {
                textViewSlotItemArmorOrDamage.setText(R.string.eq_armor);
            }

            slotChangeState(slotKey);
        }
    }

    private class ItemDetailOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showEditBottomDialog();
        }


        private void showEditBottomDialog() {
            EquipmentBottomSheetDialog bottomSheetDialog =
                    EquipmentBottomSheetDialog
                            .Companion
                            .newInstance(
                                    slotCurrentlySelected,
                                    textViewItemName.getText().toString(),
                                    textViewItemArmorType.getText().toString(),
                                    textViewItemArmorValue.getText().toString(),
                                    textViewItemAdditionalEffectsValue.getText().toString()
                            );
            bottomSheetDialog.show(getChildFragmentManager(), "BOTTOM_DIALOG_EQ_EDIT");
        }
    }
}

