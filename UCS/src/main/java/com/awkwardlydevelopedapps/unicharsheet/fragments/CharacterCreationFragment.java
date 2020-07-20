package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.MainActivity;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.character.Character;
import com.awkwardlydevelopedapps.unicharsheet.data.CharacterDao;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract;
import com.awkwardlydevelopedapps.unicharsheet.data.PresetDao;
import com.awkwardlydevelopedapps.unicharsheet.data.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.preset.Preset;
import com.awkwardlydevelopedapps.unicharsheet.stat.Stat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CharacterCreationFragment extends Fragment
        implements AdapterView.OnItemSelectedListener {

    private View rootView;

    private ArrayAdapter<String> adapter;
    private ImageView deletePresetButton;
    private Spinner spinner;

    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rb6;

    private FloatingActionButton floatingActionButton;

    private CharacterDao characterDao;
    private StatDao statDao;
    private PresetDao presetDao;
    private int charId;

    private Handler handler;
    private final static int UPDATE_PRESET_LIST = 1;

    public CharacterCreationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_character_creation, container, false);


        characterDao = DbSingleton.Instance(requireContext()).getCharacterDao();
        statDao = DbSingleton.Instance(requireContext()).getStatDao();
        presetDao = DbSingleton.Instance(requireContext()).getPresetDao();

        rb1 = rootView.findViewById(R.id.radioButton_icon1);
        rb2 = rootView.findViewById(R.id.radioButton2_icon2);
        rb3 = rootView.findViewById(R.id.radioButton3_icon3);
        rb4 = rootView.findViewById(R.id.radioButton4_icon4);
        rb5 = rootView.findViewById(R.id.radioButton5_icon5);
        rb6 = rootView.findViewById(R.id.radioButton6_icon6);
        radioCheckHandler();

        spinner = rootView.findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //providing listener
        spinner.setOnItemSelectedListener(this);

        //Deleting presets - button
        deletePresetButton = rootView.findViewById(R.id.imageView_deleteButton);
        deletePresetButton.setOnClickListener(new deleteButtonOnClick());

        floatingActionButton = rootView.findViewById(R.id.add_button);
        floatingActionButton.setImageResource(R.drawable.ic_done_black_24dp);
        floatingActionButton.setOnClickListener(new FABOnClick());

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == UPDATE_PRESET_LIST) {
                    adapter.addAll((List<String>) msg.obj);
                    adapter.notifyDataSetChanged();
                }
                super.handleMessage(msg);
            }
        };
        ExecSingleton.getInstance().execute(taskLoadPresetList(handler));

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

        String spinnerName = spinner.getSelectedItem().toString();
        if (!spinnerName.equals("Blade") && !spinnerName.equals("None")) {
            spinnerName = "Custom";
        }

        if (!name.isEmpty()) {
            //create and add new character
            //Check if given character already exist, if so - quit adding
            Character newCharacter = new Character(name, className, race, imageIdValue());

            //Insert new character to DB and store its ID.
            charId = (int) characterDao.insert(newCharacter);

            //Preset selection handling.
            switch (spinnerName) {
                case "Blade":
                    bladePreset(name);
                    showToast("Created character with Blade preset!");
                    break;
                case "None":
                    showToast("Created character without preset!");
                    break;
                case "Custom":
                    loadCustomPreset(spinner.getSelectedItem().toString(), name);
                    showToast("Created character with custom preset!");
                    break;

                default:
                    showToast("Something went wrong...");
                    break;
            }

            //Go back to CharacterList
            NavHostFragment
                    .findNavController(this)
                    .navigate(CharacterCreationFragmentDirections.actionCharacterCreationFragmentToCharacterListFragment());
        } else {
            showToast("You need to enter a name!");
        }
    }

    private void radioCheckHandler() {
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb5.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb6.setChecked(false);
            }
        });

        rb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
            }
        });
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

    private String imageIdValue() {
        String iconIdTemp;

        if (rb1.isChecked()) {
            iconIdTemp = ImageContract.Character.COWLED;
        } else if (rb2.isChecked()) {
            iconIdTemp = ImageContract.Character.CULTIST;
        } else if (rb3.isChecked()) {
            iconIdTemp = ImageContract.Character.VIKING;
        } else if (rb4.isChecked()) {
            iconIdTemp = ImageContract.Character.WIZARD;
        } else if (rb5.isChecked()) {
            iconIdTemp = ImageContract.Character.VISORED;
        } else if (rb6.isChecked()) {
            iconIdTemp = ImageContract.Character.KENAKU;
        } else {
            iconIdTemp = "";
        }

        return iconIdTemp;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinner.getSelectedItem().toString().equals("Blade") || spinner.getSelectedItem().toString().equals("None")) {
            deletePresetButton.setVisibility(View.GONE);
        } else {
            deletePresetButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(requireContext(), "Nothing selected.", Toast.LENGTH_SHORT).show();
    }

    private void showToast(String msg) {
        ((AppCompatActivity) requireActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSpinner(String object) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.remove(object);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Inner classes
     */

    private class deleteButtonOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ExecSingleton.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    presetDao.deletePresetStats(spinner.getSelectedItem().toString());
                    presetDao.deletePresetListItem(spinner.getSelectedItem().toString());
                    showToast("Preset has been deleted.");
                    updateSpinner(spinner.getSelectedItem().toString());
                }
            });
        }
    }

    private class FABOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ExecSingleton.getInstance().execute(taskCreateAndAddCharacter());
        }
    }
}
