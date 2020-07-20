package com.awkwardlydevelopedapps.unicharsheet.preset;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "preset")
public class Preset {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String name;
    private String presetName;
    private int page;

    public Preset(String name, int page, String presetName) {
        this.name = name;
        this.page = page;
        this.presetName = presetName;
    }

    public String getName() {
        return name;
    }

    public String getPresetName() {
        return presetName;
    }

    public int getPage() {
        return page;
    }
}
