package com.awkwardlydevelopedapps.unicharsheet.spell;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.SpellDao;

import java.util.ArrayList;

public class SpellsDialog extends DialogFragment {
    private String mSpellName;
    private String mImageResId;

    private NoticeDialogListener listener;

    private RadioButton radioButtonFire;
    private RadioButton radioButtonAir;
    private RadioButton radioButtonWater;
    private RadioButton radioButtonEarth;
    private RadioButton radioButtonNature;
    private RadioButton radioButtonEssence;
    private RadioButton radioButtonMind;

    public interface NoticeDialogListener {
        void onSpellCreateDialogPositiveClick(DialogFragment dialog, String spellName, String imageResId);

        void onSpellCreateDialogNegativeClick(DialogFragment dialog);
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

    public SpellsDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_spells, null);

        radioButtonFire = view.findViewById(R.id.radioButton1_fire);
        radioButtonAir = view.findViewById(R.id.radioButton2_air);
        radioButtonWater = view.findViewById(R.id.radioButton3_water);
        radioButtonEarth = view.findViewById(R.id.radioButton4_earth);
        radioButtonNature = view.findViewById(R.id.radioButton5_nature);
        radioButtonEssence = view.findViewById(R.id.radioButton6_essence);
        radioButtonMind = view.findViewById(R.id.radioButton7_mind);

        radioCheckHandler();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle("New ability creation").setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // On ACCEPT click
                        EditText spellName = view.findViewById(R.id.spell_name_dialog);
                        mSpellName = spellName.getText().toString();
                        //If no name entered then abort (return)
                        if (mSpellName.isEmpty()) {
                            Toast.makeText(getContext(), "Enter a name for your ability!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String iconIdTemp;
                        if (radioButtonFire.isChecked()) {
                            iconIdTemp = ImageContract.Spell.FIRE;
                        } else if (radioButtonAir.isChecked()) {
                            iconIdTemp = ImageContract.Spell.AIR;
                        } else if (radioButtonWater.isChecked()) {
                            iconIdTemp = ImageContract.Spell.WATER;
                        } else if (radioButtonEarth.isChecked()) {
                            iconIdTemp = ImageContract.Spell.EARTH;
                        } else if (radioButtonNature.isChecked()) {
                            iconIdTemp = ImageContract.Spell.NATURE;
                        } else if (radioButtonEssence.isChecked()) {
                            iconIdTemp = ImageContract.Spell.ESSENCE;
                        } else if (radioButtonMind.isChecked()) {
                            iconIdTemp = ImageContract.Spell.MIND;
                        } else {
                            iconIdTemp = "";
                        }
                        mImageResId = iconIdTemp;

                        listener.onSpellCreateDialogPositiveClick(SpellsDialog.this, mSpellName, mImageResId);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onSpellCreateDialogNegativeClick(SpellsDialog.this);
                    }
                });
        return builder.create();
    }

    private void radioCheckHandler() {
        radioButtonFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonAir.setChecked(false);
                radioButtonWater.setChecked(false);
                radioButtonEarth.setChecked(false);
                radioButtonNature.setChecked(false);
                radioButtonEssence.setChecked(false);
                radioButtonMind.setChecked(false);
            }
        });

        radioButtonAir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonFire.setChecked(false);
                radioButtonWater.setChecked(false);
                radioButtonEarth.setChecked(false);
                radioButtonNature.setChecked(false);
                radioButtonEssence.setChecked(false);
                radioButtonMind.setChecked(false);
            }
        });

        radioButtonWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonFire.setChecked(false);
                radioButtonAir.setChecked(false);
                radioButtonEarth.setChecked(false);
                radioButtonNature.setChecked(false);
                radioButtonEssence.setChecked(false);
                radioButtonMind.setChecked(false);
            }
        });

        radioButtonEarth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonFire.setChecked(false);
                radioButtonAir.setChecked(false);
                radioButtonWater.setChecked(false);
                radioButtonNature.setChecked(false);
                radioButtonEssence.setChecked(false);
                radioButtonMind.setChecked(false);
            }
        });

        radioButtonNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonFire.setChecked(false);
                radioButtonAir.setChecked(false);
                radioButtonWater.setChecked(false);
                radioButtonEarth.setChecked(false);
                radioButtonEssence.setChecked(false);
                radioButtonMind.setChecked(false);
            }
        });

        radioButtonEssence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonFire.setChecked(false);
                radioButtonAir.setChecked(false);
                radioButtonWater.setChecked(false);
                radioButtonEarth.setChecked(false);
                radioButtonNature.setChecked(false);
                radioButtonMind.setChecked(false);
            }
        });

        radioButtonMind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonFire.setChecked(false);
                radioButtonAir.setChecked(false);
                radioButtonWater.setChecked(false);
                radioButtonEarth.setChecked(false);
                radioButtonNature.setChecked(false);
                radioButtonEssence.setChecked(false);
            }
        });
    }

}

