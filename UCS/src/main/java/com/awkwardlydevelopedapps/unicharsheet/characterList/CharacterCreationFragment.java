package com.awkwardlydevelopedapps.unicharsheet.characterList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awkwardlydevelopedapps.unicharsheet.characterList.adapters.CharacterIconsAdapter;
import com.awkwardlydevelopedapps.unicharsheet.characterList.adapters.PresetListAdapter;
import com.awkwardlydevelopedapps.unicharsheet.characterList.dao.CharacterDao;
import com.awkwardlydevelopedapps.unicharsheet.characterList.dao.PresetDao;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Character;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Preset;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.PresetList;
import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.model.Icon;
import com.awkwardlydevelopedapps.unicharsheet.common.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.common.data.ImageContract;
import com.awkwardlydevelopedapps.unicharsheet.stats.dao.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.stats.model.Stat;
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
        ArrayList<Icon> icons = Icon.Companion.populateCharacterIcons();
        CharacterIconsAdapter characterIconsAdapter = new CharacterIconsAdapter(icons, icon, bottomSheetBehaviorIcons);
        recyclerViewBottomSheet.setAdapter(characterIconsAdapter);
        recyclerViewBottomSheet.setLayoutManager(new GridLayoutManager(requireContext(), 3));


        // Bottom Sheet with presets
        View bottomSheetPreset = rootView.findViewById(R.id.bottomSheet_presetSelection);
        BottomSheetBehavior<View> bottomSheetBehaviorPreset = BottomSheetBehavior.from(bottomSheetPreset);
        bottomSheetBehaviorPreset.setState(BottomSheetBehavior.STATE_HIDDEN);
        textViewPreset = rootView.findViewById(R.id.textView_preset_characterCreation);

        RecyclerView recyclerViewPresetList = rootView.findViewById(R.id.recyclerView_presetList);
        ArrayList<PresetList> presetList = PresetList.presetListBuiltinPresets(requireContext());
        PresetListAdapter presetListAdapter = new PresetListAdapter(presetList,
                textViewPreset,
                bottomSheetBehaviorPreset,
                presetDao,
                requireActivity());
        recyclerViewPresetList.setAdapter(presetListAdapter);
        recyclerViewPresetList.setLayoutManager(new LinearLayoutManager(requireContext()));

        ExecSingleton.getInstance().execute(() -> {
            presetList.addAll(presetDao.getPresetListByNameAsc());
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
        return this::createAndAddCharacter;
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
    }

    private void checkPresetToAdd() {
        String preNone = getString(R.string.none);
        String preBlade = getString(R.string.blade);

        String presetName = textViewPreset.getText().toString();
        if (!presetName.equals(preBlade) && !presetName.equals(preNone)) {
            presetName = "Custom";
        }

        if (presetName.equals(preNone)) {
            showToast("Character created without preset.");
        } else if (presetName.equals(preBlade)) {
            showToast("Character created with 'Blade' preset.");
            bladePreset();
        } else if (presetName.equals("Custom")) {
            // For some reason IDE shows warning that above statement is always true. It's not.
            // We check preset name based on localization string,
            // if it doesn't match 'None' && 'Blade' THEN we set it to 'Custom'.
            showToast("Character created with custom preset.");
            loadCustomPreset(textViewPreset.getText().toString());
        } else {
            showToast("Something wrong with preset.");
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

            //Go back to CharacterList
            NavHostFragment
                    .findNavController(CharacterCreationFragment.this)
                    .navigate(CharacterCreationFragmentDirections.actionCharacterCreationFragmentToCharacterListFragment());
        }
    }
}
