package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.SpellDao;
import com.awkwardlydevelopedapps.unicharsheet.models.Spell;

import java.util.List;

public class SpellRepository {
    private SpellDao spellDao;
    private LiveData<List<Spell>> allSpells;

    public SpellRepository(Application application, int charId) {
        spellDao = DbSingleton.Instance(application).getSpellDao();
        allSpells = spellDao.getLiveDataAllSpells(charId);
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
