package com.awkwardlydevelopedapps.unicharsheet.characterList.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Preset;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.PresetList;

import java.util.List;

@Dao
public interface PresetDao {
    @Insert
    void insertPresetListName(PresetList presetList);

    @Insert
    void insertPresetStats(Preset... presets);

    @Query("SELECT name FROM presetList")
    List<String> getPresetListNames();

    @Query("SELECT * FROM presetList")
    List<PresetList> getPresetList();

    @Query("SELECT * FROM presetList ORDER BY name ASC")
    List<PresetList> getPresetListByNameAsc();

    @Query("SELECT * FROM Preset WHERE presetName = :preName")
    List<Preset> getPresetStats(String preName);

    @Query("DELETE FROM Preset WHERE presetName = :preName")
    void deletePresetStats(String preName);

    @Query("DELETE FROM presetList WHERE name = :preName")
    void deletePresetListItem(String preName);
}
