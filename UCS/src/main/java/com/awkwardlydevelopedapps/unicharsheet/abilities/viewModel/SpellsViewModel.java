package com.awkwardlydevelopedapps.unicharsheet.abilities.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.abilities.adapters.SpellAdapter;
import com.awkwardlydevelopedapps.unicharsheet.abilities.model.Spell;
import com.awkwardlydevelopedapps.unicharsheet.abilities.repository.SpellRepository;
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;

import java.util.ArrayList;
import java.util.List;

public class SpellsViewModel extends ViewModel {
    private final SpellRepository spellRepository;
    private final MediatorLiveData<List<Spell>> allSpells = new MediatorLiveData<>();
    private List<Spell> spellList;

    public SpellsViewModel(Application application, int charId) {
        spellRepository = new SpellRepository(application, charId);

        allSpells.addSource(getAllSpellsByNameAsc(),
                spells -> allSpells.setValue(spells));
    }

    public MediatorLiveData<List<Spell>> getAllSpells() {
        return allSpells;
    }


    public LiveData<List<Spell>> getAllSpellsByNameAsc() {
        return spellRepository.getAllSpellsByNameAsc();
    }

    public LiveData<List<Spell>> getAllSpellsByNameDesc() {
        return spellRepository.getAllSpellsByNameDesc();
    }

    public void orderBy(int order) {
        allSpells.removeSource(getAllSpellsByNameAsc());
        allSpells.removeSource(getAllSpellsByNameDesc());

        switch (order) {
            default:
            case Sort.BY_NAME_ASC:
                allSpells.addSource(getAllSpellsByNameAsc(), spells -> allSpells.setValue(spells));
                break;
            case Sort.BY_NAME_DESC:
                allSpells.addSource(getAllSpellsByNameDesc(), spells -> allSpells.setValue(spells));
                break;
        }
    }

    public void insert(Spell spell) {
        spellRepository.insert(spell);
    }

    public void delete(Spell spell) {
        spellRepository.delete(spell);
    }

    public void checkSpellsAndDelete(SpellAdapter adapter) {
        List<Spell> temp = new ArrayList<>(this.spellList);
        for (Spell spell : temp) {
            if (spell.isChecked()) {
                spellRepository.delete(spell);
            }
        }
        adapter.setShowChecks();
    }

    public List<Spell> getSpellList() {
        return spellList;
    }

    public void setSpellList(List<Spell> spellList) {
        this.spellList = spellList;
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
