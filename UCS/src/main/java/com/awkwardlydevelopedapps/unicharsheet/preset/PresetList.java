package com.awkwardlydevelopedapps.unicharsheet.preset;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

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

    public static ArrayList<PresetList> presetList() {
        ArrayList<PresetList> presetList = new ArrayList<>();
        presetList.add(new PresetList("None"));
        presetList.add(new PresetList("Blade"));

        return presetList;
    }
}
