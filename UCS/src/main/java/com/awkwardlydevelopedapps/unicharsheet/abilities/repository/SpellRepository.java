package com.awkwardlydevelopedapps.unicharsheet.abilities.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.abilities.dao.SpellDao;
import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.common.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.abilities.model.Spell;

import java.util.List;

public class SpellRepository {
    private final SpellDao spellDao;

    private final LiveData<List<Spell>> allSpells;
    private final LiveData<List<Spell>> allSpellsByNameAsc;
    private final LiveData<List<Spell>> allSpellsByNameDesc;

    public SpellRepository(Application application, int charId) {
        spellDao = DbSingleton.Instance(application).getSpellDao();

        allSpells = spellDao.getLiveDataAllSpells(charId);
        allSpellsByNameAsc = spellDao.getLiveDataAllSpellsByNameAsc(charId);
        allSpellsByNameDesc = spellDao.getLiveDataAllSpellsByNameDesc(charId);
    }

    public void insert(Spell spell) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                spellDao.insert(spell);
            }
        });
    }

    public void delete(Spell spell) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                spellDao.delete(spell);
            }
        });
    }

    public LiveData<List<Spell>> getAllSpells() {
        return allSpells;
    }

    public LiveData<List<Spell>> getAllSpellsByNameAsc() {
        return allSpellsByNameAsc;
    }

    public LiveData<List<Spell>> getAllSpellsByNameDesc() {
        return allSpellsByNameDesc;
    }

    public LiveData<Spell> getSpell(int spellId) {
        return spellDao.getLiveDataSpell(spellId);
    }

    public void updateDescription(String description, int charId, int spellId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                spellDao.updateDescription(description, charId, spellId);
            }
        });
    }

    public void updateDmgValue(String dmgValue, int charId, int spellId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                spellDao.updateDmgValue(dmgValue, charId, spellId);
            }
        });
    }

    public void updateAddEffectValue(String addEffectValue, int charId, int spellId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                spellDao.updateAddEffectValue(addEffectValue, charId, spellId);
            }
        });
    }

    public void updateCostValue(String costValue, int charId, int spellId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                spellDao.updateCostValue(costValue, charId, spellId);
            }
        });
    }

    public void updateAddCostValue(String addCostValue, int charId, int spellId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                spellDao.updateAddCostValue(addCostValue, charId, spellId);
            }
        });
    }

    public void updateSpecialNotes(String specialNotesValue, int charId, int spellId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                spellDao.updateSpecialNotes(specialNotesValue, charId, spellId);
            }
        });
    }
}
