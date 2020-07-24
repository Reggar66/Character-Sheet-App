package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.awkwardlydevelopedapps.unicharsheet.stat.Stat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CharacterCreationFragment extends Fragment {

    private View rootView;

    private FloatingActionButton floatingActionButton;

    private ImageView icon;
    private BottomSheetBehavior bottomSheetBehavior;
    private ArrayList<Icon> icons;

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


        floatingActionButton = rootView.findViewById(R.id.add_button);
        floatingActionButton.setImageResource(R.drawable.ic_done_black_24dp);
        floatingActionButton.setOnClickListener(new FABOnClick());


        View bottomSheetIcons = rootView.findViewById(R.id.bottomSheet_iconSelection);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetIcons);

        icon = rootView.findViewById(R.id.icon_characterCreation);
        icon.setImageResource(ImageContract.Character.COWLED_ID);
        icon.setContentDescription(ImageContract.Character.COWLED);
        icon.setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        });


        RecyclerView recyclerViewBottomSheet = rootView.findViewById(R.id.recyclerView_icons_grid);
        icons = Icon.Companion.populateIcons();
        IconsAdapter iconsAdapter = new IconsAdapter(icons, icon, bottomSheetBehavior);
        recyclerViewBottomSheet.setAdapter(iconsAdapter);
        recyclerViewBottomSheet.setLayoutManager(new GridLayoutManager(requireContext(), 3));

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

    private Runnable taskLoadPresetList(Handler handler) {
        return new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                List<String> tempPresetList = getPresetList();
                msg.what = 1;
                msg.obj = tempPresetList;
                handler.sendMessage(msg);
            }
        };
    }

    private List<String> getPresetList() {
        List<String> presets = new ArrayList<>();
        presets.add("None");
        presets.add("Blade");
        presets.addAll(presetDao.getPresetList());

        return presets;
    }

    private Runnable taskCreateAndAddCharacter() {
        return new Runnable() {
            @Override
            public void run() {
                createAndAddCharacter();
            }
        };
    }

    private void createAndAddCharacter() {
        EditText editTextName = rootView.findViewById(R.id.edit_character_name);
        EditText editTextClass = rootView.findViewById(R.id.edit_character_class);
        EditText editTextRace = rootView.findViewById(R.id.edit_character_race);

        String name = editTextName.getText().toString();
        String className = editTextClass.getText().toString();
        String race = editTextRace.getText().toString();


        //create and add new character
        //Check if given character already exist, if so - quit adding
        Character newCharacter = new Character(name, className, race, imageResourceTag());

        //Insert new character to DB and store its ID.
        charId = (int) characterDao.insert(newCharacter);

        //Go back to CharacterList
        NavHostFragment
                .findNavController(this)
                .navigate(CharacterCreationFragmentDirections.actionCharacterCreationFragmentToCharacterListFragment());
    }

    private void bladePreset(String charName) {
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

    private void loadCustomPreset(String presetName, String charName) {
        List<Preset> statList = new ArrayList<>(presetDao.getPresetStats(presetName));

        for (Preset preset : statList) {
            statDao.insert(new Stat(preset.getName(), "0", charId, preset.getPage()));
        }

    }

    private String imageResourceTag() {
        return icon.getContentDescription().toString();
    }

    private void showToast(String msg) {
        ((AppCompatActivity) requireActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
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

    private class OnIconChooserClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Snackbar.make(rootView, "Icon click!", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }
}
