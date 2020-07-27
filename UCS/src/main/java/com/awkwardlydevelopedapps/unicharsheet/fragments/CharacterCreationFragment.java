package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.Icon;
import com.awkwardlydevelopedapps.unicharsheet.IconsAdapter;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.character.Character;
import com.awkwardlydevelopedapps.unicharsheet.data.CharacterDao;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract;
import com.awkwardlydevelopedapps.unicharsheet.data.PresetDao;
import com.awkwardlydevelopedapps.unicharsheet.data.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.preset.Preset;
import com.awkwardlydevelopedapps.unicharsheet.preset.PresetList;
import com.awkwardlydevelopedapps.unicharsheet.preset.PresetListAdapter;
import com.awkwardlydevelopedapps.unicharsheet.stat.Stat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CharacterCreationFragment extends Fragment {

    private View rootView;

    private ImageView icon;
    private BottomSheetBehavior<View> bottomSheetBehaviorIcons;
    TextView textViewPreset;

    private CharacterDao characterDao;
    private StatDao statDao;
    private PresetDao presetDao;
    private int charId;

    public CharacterCreationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_character_creation, container, false);

        characterDao = DbSingleton.Instance(requireContext()).getCharacterDao();
        statDao = DbSingleton.Instance(requireContext()).getStatDao();
        presetDao = DbSingleton.Instance(requireContext()).getPresetDao();


        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.add_button);
        floatingActionButton.setImageResource(R.drawable.ic_done_black_24dp);
        floatingActionButton.setOnClickListener(new FABOnClick());

        // Bottom Sheet with Icons
        View bottomSheetIcons = rootView.findViewById(R.id.bottomSheet_iconSelection);
        bottomSheetBehaviorIcons = BottomSheetBehavior.from(bottomSheetIcons);
        bottomSheetBehaviorIcons.setState(BottomSheetBehavior.STATE_HIDDEN);

        icon = rootView.findViewById(R.id.icon_characterCreation);
        icon.setImageResource(ImageContract.Character.COWLED_ID);
        icon.setContentDescription(ImageContract.Character.COWLED);

        RecyclerView recyclerViewBottomSheet = rootView.findViewById(R.id.recyclerView_icons_grid);
        ArrayList<Icon> icons = Icon.Companion.populateIcons();
        IconsAdapter iconsAdapter = new IconsAdapter(icons, icon, bottomSheetBehaviorIcons);
        recyclerViewBottomSheet.setAdapter(iconsAdapter);
        recyclerViewBottomSheet.setLayoutManager(new GridLayoutManager(requireContext(), 3));


        // Bottom Sheet with presets
        View bottomSheetPreset = rootView.findViewById(R.id.bottomSheet_presetSelection);
        BottomSheetBehavior<View> bottomSheetBehaviorPreset = BottomSheetBehavior.from(bottomSheetPreset);
        bottomSheetBehaviorPreset.setState(BottomSheetBehavior.STATE_HIDDEN);
        textViewPreset = rootView.findViewById(R.id.textView_preset_characterCreation);

        RecyclerView recyclerViewPresetList = rootView.findViewById(R.id.recyclerView_presetList);
        ArrayList<PresetList> presetList = PresetList.presetList();
        PresetListAdapter presetListAdapter = new PresetListAdapter(presetList, textViewPreset, bottomSheetBehaviorPreset, presetDao);
        recyclerViewPresetList.setAdapter(presetListAdapter);
        recyclerViewPresetList.setLayoutManager(new LinearLayoutManager(requireContext()));

        ExecSingleton.getInstance().execute(() -> {
            presetList.addAll(presetDao.getPresetList());
            presetListAdapter.notifyDataSetChanged();
        });

        // Taking care of showing bottom sheets
        icon.setOnClickListener(view -> {
            bottomSheetBehaviorPreset.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomSheetBehaviorIcons.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

        textViewPreset.setOnClickListener(view -> {
            bottomSheetBehaviorIcons.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomSheetBehaviorPreset.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private Runnable taskCreateAndAddCharacter() {
        return () -> createAndAddCharacter();
    }

    private void createAndAddCharacter() {
        EditText editTextName = rootView.findViewById(R.id.edit_character_name);
        EditText editTextClass = rootView.findViewById(R.id.edit_character_class);
        EditText editTextRace = rootView.findViewById(R.id.edit_character_race);

        String name = editTextName.getText().toString();
        String className = editTextClass.getText().toString();
        String race = editTextRace.getText().toString();


        // Create and add new character
        Character newCharacter = new Character(name, className, race, imageResourceTag());

        // Insert new character to DB and store its ID.
        charId = (int) characterDao.insert(newCharacter);

        // Preset check
        checkPresetToAdd();


        //Go back to CharacterList
        NavHostFragment
                .findNavController(this)
                .navigate(CharacterCreationFragmentDirections.actionCharacterCreationFragmentToCharacterListFragment());
    }

    private void checkPresetToAdd() {
        String presetName = textViewPreset.getText().toString();
        if (!presetName.equals("Blade") && !presetName.equals("None")) {
            presetName = "Custom";
        }
        switch (presetName) {
            case "None":
                showToast("Character created without Blade preset.");
                break;
            case "Blade":
                Snackbar.make(rootView, "Character created with Blade preset.", BaseTransientBottomBar.LENGTH_SHORT).show();
                bladePreset();
            case "Custom":
                showToast("Custom preset.");
                loadCustomPreset(textViewPreset.getText().toString());
                break;
            default:
                showToast("Something wrong with preset.");
                break;
        }
    }

    private void bladePreset() {
        statDao.insert(
                new Stat(getString(R.string.strength), "0", charId, 1),
                new Stat(getString(R.string.agility), "0", charId, 1),
                new Stat(getString(R.string.endurance), "0", charId, 1),
                new Stat(getString(R.string.intuition), "0", charId, 1),
                new Stat(getString(R.string.will), "0", charId, 1),
                new Stat(getString(R.string.wisdom), "0", charId, 1),
                new Stat(getString(R.string.charisma), "0", charId, 1),
                new Stat(getString(R.string.reflex), "0", charId, 2),
                new Stat(getString(R.string.encumbrance), "0", charId, 2),
                new Stat(getString(R.string.toughness), "0", charId, 2),
                new Stat(getString(R.string.speed), "0", charId, 2),
                new Stat(getString(R.string.reaction), "0", charId, 2),
                new Stat(getString(R.string.potential), "0", charId, 2),
                new Stat(getString(R.string.ether), "0", charId, 2)
        );
    }

    private void loadCustomPreset(String presetName) {
        List<Preset> presetStatList = new ArrayList<>(presetDao.getPresetStats(presetName));

        for (Preset preset : presetStatList) {
            statDao.insert(new Stat(preset.getName(), "0", charId, preset.getPage()));
        }

    }

    private String imageResourceTag() {
        return icon.getContentDescription().toString();
    }

    private void showToast(String msg) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show());
    }

    private void showSnackbar(String msg) {
        requireActivity().runOnUiThread(() -> Snackbar.make(rootView, msg, BaseTransientBottomBar.LENGTH_SHORT).show());
    }

    /**
     * Inner classes
     */

    private class FABOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ExecSingleton.getInstance().execute(taskCreateAndAddCharacter());
        }
    }
}
