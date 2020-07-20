package com.awkwardlydevelopedapps.unicharsheet.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.character.Character;

import java.util.List;

@Dao
public interface CharacterDao {
    @Insert
    long insert(Character character);

    @Query("SELECT * FROM characters WHERE id = :id")
    Character get(int id);

    @Delete
    void delete(Character character);

    @Query("SELECT * FROM characters")
    List<Character> getAllCharacters();

    @Query("SELECT * FROM characters")
    LiveData<List<Character>> getLiveDataAllCharacters();

    //Delete all stuff
    @Query("DELETE FROM stats WHERE char_id = :id")
    void deleteStats(int id);

    @Query("DELETE FROM spells WHERE char_id = :id")
    void deleteSpells(int id);

    @Query("DELETE FROM equipment WHERE char_id = :id")
    void deleteEquipment(int id);

    @Query("DELETE FROM items WHERE char_id = :id")
    void deleteItems(int id);

    @Query("DELETE FROM currency WHERE char_id = :id")
    void deleteCurrencies(int id);

    @Query("DELETE FROM experience WHERE char_id = :id")
    void deleteExperience(int id);
}
