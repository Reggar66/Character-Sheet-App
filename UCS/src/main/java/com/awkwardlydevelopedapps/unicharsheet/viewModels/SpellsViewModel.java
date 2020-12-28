package com.awkwardlydevelopedapps.unicharsheet.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.repositories.SpellRepository;
import com.awkwardlydevelopedapps.unicharsheet.models.Spell;
import com.awkwardlydevelopedapps.unicharsheet.adapters.SpellAdapter;

import java.util.ArrayList;
import java.util.List;

public class SpellsViewModel extends ViewModel {
    private SpellRepository spellRepository;
    private LiveData<List<Spell>> allSpells;
    private List<Spell> spells;

    private int selectedSpellID = -1;

    public final static int SPELL_ID_NOT_SET = -1;


    public SpellsViewModel(Application application, int charId) {
        spellRepository = new SpellRepository(application, charId);
        allSpells = spellRepository.getAllSpells();
    }

    public LiveData<List<Spell>> getAllSpells() {
        return allSpells;
    }

    public void insert(Spell spell) {
        spellRepository.insert(spell);
    }

    public void delete(Spell spell) {
        spellRepository.delete(spell);
    }

    public void checkSpellsAndDelete(SpellAdapter adapter) {
        List<Spell> temp = new ArrayList<>(this.spells);
        for (Spell spell : temp) {
            if (spell.isChecked()) {
                spellRepository.delete(spell);
            }
        }
        adapter.setShowChecks();
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public void setSpells(List<Spell> spells) {
        this.spells = spells;
    }

    public LiveData<Spell> getSpell(int spellId) {
        return spellRepository.getSpell(spellId);
    }

    public void updateDescription(String description, int charId, int spellId) {
        spellRepository.updateDescription(description, charId, spellId);
    }

    public void updateDmgValue(String dmgValue, int charId, int spellId) {
        spellRepository.updateDmgValue(dmgValue, charId, spellId);
    }

    public void updateAddEffectValue(String addEffectValue, int charId, int spellId) {
        spellRepository.updateAddEffectValue(addEffectValue, charId, spellId);
    }

    public void updateCostValue(String costValue, int charId, int spellId) {
        spellRepository.updateCostValue(costValue, charId, spellId);
    }

    public void updateAddCostValue(String addCostValue, int charId, int spellId) {
        spellRepository.updateAddCostValue(addCostValue, charId, spellId);
    }

    public void updateSpecialNotes(String specialNotesValue, int charId, int spellId) {
        spellRepository.updateSpecialNotes(specialNotesValue, charId, spellId);
    }

    public int getSelectedSpellID() {
        return selectedSpellID;
    }

    public void setSelectedSpellID(int selectedSpellID) {
        this.selectedSpellID = selectedSpellID;
    }

    /**
     * Factory
     */

    public static class SpellsViewModelFactory implements ViewModelProvider.Factory {

        private Application application;
        private int charId;

        public SpellsViewModelFactory(Application application, int charId) {
            this.application = application;
            this.charId = charId;
        }


        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SpellsViewModel(application, charId);
        }
    }
}
