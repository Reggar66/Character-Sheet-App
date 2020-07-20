package com.awkwardlydevelopedapps.unicharsheet.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.preset.PresetList;
import com.awkwardlydevelopedapps.unicharsheet.preset.Preset;

import java.util.List;

@Dao
public interface PresetDao {
    @Insert
    void insertPresetListName(PresetList presetList);

    @Insert
    void insertPresetStats(Preset... presets);

    @Query("SELECT name FROM presetList")
    List<String> getPresetList();

    @Query("SELECT * FROM Preset WHERE presetName = :preName")
    List<Preset> getPresetStats(String preName);

    @Query("DELETE FROM Preset WHERE presetName = :preName")
    void deletePresetStats(String preName);

    @Query("DELETE FROM presetList WHERE name = :preName")
    void deletePresetListItem(String preName);
}
