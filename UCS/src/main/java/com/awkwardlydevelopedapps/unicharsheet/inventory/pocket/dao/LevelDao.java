package com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Level;

import java.util.List;

@Dao
public interface LevelDao {
    @Insert
    void insert(Level level);

    @Delete
    void delete(Level level);

    @Query("SELECT * FROM level WHERE char_id = :charId")
    LiveData<List<Level>> getLiveDataAllLvl(int charId);

    @Query("SELECT * FROM level WHERE char_id = :charId")
    LiveData<Level> getLevel(int charId);

    @Query("UPDATE level SET value = :value WHERE char_id = :charId")
    void update(int value, int charId);
}
