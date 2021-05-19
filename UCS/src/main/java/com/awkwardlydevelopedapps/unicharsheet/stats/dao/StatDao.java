package com.awkwardlydevelopedapps.unicharsheet.stats.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.stats.model.Stat;

import java.util.List;

@Dao
public interface StatDao {

    @Insert
    void insert(Stat... stats);

    @Insert
    long insert(Stat stat);

    @Delete
    void delete(Stat stat);

    @Query("SELECT * FROM stats WHERE char_id = :id")
    List<Stat> getAllStats(int id);

    @Query("SELECT * FROM stats WHERE char_id = :id")
    LiveData<List<Stat>> getLiveDataAllStats(int id);

    @Query("SELECT  * FROM stats WHERE char_id = :id AND page = :page")
    List<Stat> getAllPageStats(int id, int page);

    @Query("SELECT  * FROM stats WHERE char_id = :id AND page = :page")
    LiveData<List<Stat>> getLiveDataAllPageStats(int id, int page);

    @Query("SELECT  * FROM stats WHERE char_id = :id AND page = :page ORDER BY name ASC")
    LiveData<List<Stat>> getLiveDataAllPageStatsByNameAsc(int id, int page);

    @Query("SELECT  * FROM stats WHERE char_id = :id AND page = :page ORDER BY name DESC")
    LiveData<List<Stat>> getLiveDataAllPageStatsByNameDesc(int id, int page);

    @Query("SELECT  * FROM stats WHERE char_id = :id AND page = :page ORDER BY CAST(value AS INTEGER) ASC")
    LiveData<List<Stat>> getLiveDataAllPageStatsByValueAsc(int id, int page);

    @Query("SELECT  * FROM stats WHERE char_id = :id AND page = :page ORDER BY CAST(value AS INTEGER) DESC")
    LiveData<List<Stat>> getLiveDataAllPageStatsByValueDesc(int id, int page);

    @Query("UPDATE stats SET value = :newValue, name=:statName WHERE char_id = :charId AND id = :statId")
    void updateStatValue(String statName, String newValue, int charId, int statId);

    @Query("DELETE FROM stats WHERE char_id = :id AND name = :statName")
    void deleteSingleStat(int id, String statName);
}
