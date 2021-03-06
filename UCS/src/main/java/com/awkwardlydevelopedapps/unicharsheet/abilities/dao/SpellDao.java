package com.awkwardlydevelopedapps.unicharsheet.abilities.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.abilities.model.Spell;

import java.util.List;

@Dao
public interface SpellDao {
    @Insert
    long insert(Spell spell);

    @Delete
    void delete(Spell spell);

    @Query("SELECT * FROM spells WHERE char_id = :id")
    List<Spell> getAllSpells(int id);

    @Query("SELECT * FROM spells WHERE char_id = :charId")
    LiveData<List<Spell>> getLiveDataAllSpells(int charId);

    @Query("SELECT * FROM spells WHERE char_id = :charId ORDER BY name ASC")
    LiveData<List<Spell>> getLiveDataAllSpellsByNameAsc(int charId);

    @Query("SELECT * FROM spells WHERE char_id = :charId ORDER BY name DESC")
    LiveData<List<Spell>> getLiveDataAllSpellsByNameDesc(int charId);

    @Query("SELECT * FROM spells WHERE id = :id")
    Spell getSpell(int id);

    @Query("SELECT * FROM spells WHERE id = :id")
    LiveData<Spell> getLiveDataSpell(int id);

    @Query("UPDATE spells SET description = :desc WHERE char_id = :charId AND id = :spellId")
    void updateDescription(String desc, int charId, int spellId);

    @Query("UPDATE spells SET damage = :dmgValue WHERE char_id = :charId AND id = :spellId")
    void updateDmgValue(String dmgValue, int charId, int spellId);

    @Query("UPDATE spells SET add_effect = :addEffectValue WHERE char_id = :charId AND id = :spellId")
    void updateAddEffectValue(String addEffectValue, int charId, int spellId);

    @Query("UPDATE spells SET cost = :costValue WHERE char_id = :charId AND id = :spellId")
    void updateCostValue(String costValue, int charId, int spellId);

    @Query("UPDATE spells SET add_cost = :addCostValue WHERE char_id = :charId AND id = :spellId")
    void updateAddCostValue(String addCostValue, int charId, int spellId);

    @Query("UPDATE spells SET special_notes = :specialNotes WHERE char_id = :charId AND id = :spellId")
    void updateSpecialNotes(String specialNotes, int charId, int spellId);
}
