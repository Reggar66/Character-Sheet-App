package com.awkwardlydevelopedapps.unicharsheet;

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

import com.awkwardlydevelopedapps.unicharsheet.character.Character;
import com.awkwardlydevelopedapps.unicharsheet.character.CharacterListAdapter;
import com.awkwardlydevelopedapps.unicharsheet.data.CharacterDao;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.ItemDao;
import com.awkwardlydevelopedapps.unicharsheet.data.SpellDao;
import com.awkwardlydevelopedapps.unicharsheet.data.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.BackpackAdapter;
import com.awkwardlydevelopedapps.unicharsheet.inventory.Item;
import com.awkwardlydevelopedapps.unicharsheet.spell.Spell;
import com.awkwardlydevelopedapps.unicharsheet.spell.SpellAdapter;
import com.awkwardlydevelopedapps.unicharsheet.stat.Stat;
import com.awkwardlydevelopedapps.unicharsheet.stat.StatAdapter;

import java.util.ArrayList;

public class DeleteDialog extends DialogFragment {

    private ArrayList arrayList;
    private RecyclerView.Adapter adapter;
    private int dbOption;

    private final static int CHARACTERS = 1;
    private final static int STATS = 2;
    private final static int EQUIPMENT_ITEMS = 3;
    private final static int SPELLS = 4;

    private NoticeDialogListener listener;

    public interface NoticeDialogListener {
        void onDeleteDialogPositiveClick(DialogFragment dialog);

        void onDeleteDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            //throw new ClassCastException(getActivity().toString() + " must implement NoticeDialogListener");
            try {
                listener = (NoticeDialogListener) getTargetFragment();
            } catch (Exception e2) {
                e2.printStackTrace();
                //throw new ClassCastException(getParentFragment().toString() + " must implement NoticeDialogListener");
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_delete, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // On ACCEPT click
                        listener.onDeleteDialogPositiveClick(DeleteDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDeleteDialogNegativeClick(DeleteDialog.this);
                    }
                });
        return builder.create();
    }

    public void getData(ArrayList<?> arrayList, RecyclerView.Adapter adapter, int whichDb) {
        this.arrayList = arrayList;
        this.adapter = adapter;

        dbOption = whichDb;
    }

    private void checkCharsToDelete() {
        Character character;
        CharacterDao characterDao = DbSingleton.Instance(getContext()).getCharacterDao();
        for (int i = 0; i < arrayList.size(); i++) {
            character = (Character) arrayList.get(i);
            if (character.isChecked()) {
                characterDao.delete(character);             //deleting chars
                characterDao.deleteStats(character.id);     //deleting stats
                characterDao.deleteSpells(character.id);    //deleting spells
                characterDao.deleteEquipment(character.id); //delete eq
                characterDao.deleteItems(character.id);     //delete items
                characterDao.deleteCurrencies(character.id);//delete currencies
                arrayList.remove(i);
                adapter.notifyItemRemoved(i);
                i--;
            }
        }
        ((CharacterListAdapter) adapter).setShowChecks();
    }

    private void checkStatsToDelete() {
        Stat stat;
        StatDao statDao = DbSingleton.Instance(getContext()).getStatDao();
        for (int i = 0; i < arrayList.size(); i++) {
            stat = (Stat) arrayList.get(i);
            if (stat.isChecked()) {
                statDao.delete(stat);
                arrayList.remove(i);
                adapter.notifyItemRemoved(i);
                i--;
            }
        }
        ((StatAdapter) adapter).setShowChecks();
    }

    private void checkSpellsToDelete() {
        Spell spell;
        SpellDao spellDao = DbSingleton.Instance(getContext()).getSpellDao();
        for (int i = 0; i < arrayList.size(); i++) {
            spell = (Spell) arrayList.get(i);
            if (spell.isChecked()) {
                spellDao.delete(spell);
                arrayList.remove(i);
                adapter.notifyItemRemoved(i);
                i--;
            }
        }
        ((SpellAdapter) adapter).setShowChecks();
    }

    private void checkEquipmentItemsToDelete() {
        Item item;
        ItemDao itemDao = DbSingleton.Instance(getContext()).getItemDao();
        for (int i = 0; i < arrayList.size(); i++) {
            item = (Item) arrayList.get(i);
            if (item.isChecked()) {
                itemDao.delete(item);
                arrayList.remove(i);
                adapter.notifyItemRemoved(i);
                i--;
            }
        }
        ((BackpackAdapter) adapter).setShowChecks();
    }
}
