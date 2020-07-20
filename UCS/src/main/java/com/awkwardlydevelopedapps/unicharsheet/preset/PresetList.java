package com.awkwardlydevelopedapps.unicharsheet.preset;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "presetList")
public class PresetList {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public PresetList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
