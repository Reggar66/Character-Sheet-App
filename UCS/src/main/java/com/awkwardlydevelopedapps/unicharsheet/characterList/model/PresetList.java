package com.awkwardlydevelopedapps.unicharsheet.characterList.model;

import android.content.Context;
import android.content.res.Resources;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.awkwardlydevelopedapps.unicharsheet.R;

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

    public static ArrayList<PresetList> presetList(Context context) {
        ArrayList<PresetList> presetList = new ArrayList<>();
        presetList.add(new PresetList(context.getString(R.string.none)));
        presetList.add(new PresetList(context.getString(R.string.blade)));

        return presetList;
    }
}
