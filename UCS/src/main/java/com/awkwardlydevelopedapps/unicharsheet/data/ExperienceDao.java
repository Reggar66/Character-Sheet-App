package com.awkwardlydevelopedapps.unicharsheet.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.inventory.Experience;

import java.util.List;

@Dao
public interface ExperienceDao {

    @Insert
    void insert(Experience experience);

    @Delete
    void delete(Experience experience);

    @Query("SELECT * FROM experience WHERE char_id = :charId")
    LiveData<List<Experience>> getLiveDataAllExp(int charId);

    @Query("SELECT * FROM experience WHERE char_id = :charId")
    LiveData<Experience> getExperience(int charId);

    @Query("UPDATE experience SET value = :value WHERE char_id = :charId")
    void update(int value, int charId);

    @Query("UPDATE experience SET value = :value, max_value = :maxValue WHERE char_id = :charId")
    void updateWithMaxValue(int value, int maxValue, int charId);
}
