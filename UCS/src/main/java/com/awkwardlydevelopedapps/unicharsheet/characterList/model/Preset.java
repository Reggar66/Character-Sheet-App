package com.awkwardlydevelopedapps.unicharsheet.characterList.model;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.stats.model.Stat;

import java.util.ArrayList;

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

    public static ArrayList<Stat> getBladePreset(int charId, Context context) {
        ArrayList<Stat> preset = new ArrayList<>();

        preset.add(new Stat(context.getResources().getString(R.string.strength),
                "0",
                charId,
                1));
        preset.add(new Stat(context.getResources().getString(R.string.agility),
                "0",
                charId, 1));
        preset.add(new Stat(context.getResources().getString(R.string.endurance),
                "0",
                charId, 1));
        preset.add(new Stat(context.getResources().getString(R.string.intuition),
                "0",
                charId, 1));
        preset.add(new Stat(context.getResources().getString(R.string.will),
                "0",
                charId, 1));
        preset.add(new Stat(context.getResources().getString(R.string.wisdom),
                "0",
                charId, 1));
        preset.add(new Stat(context.getResources().getString(R.string.charisma),
                "0",
                charId, 1));
        preset.add(new Stat(context.getResources().getString(R.string.reflex),
                "0",
                charId, 2));
        preset.add(new Stat(context.getResources().getString(R.string.encumbrance),
                "0",
                charId, 2));
        preset.add(new Stat(context.getResources().getString(R.string.toughness),
                "0",
                charId, 2));
        preset.add(new Stat(context.getResources().getString(R.string.speed),
                "0",
                charId, 2));
        preset.add(new Stat(context.getResources().getString(R.string.reaction),
                "0",
                charId, 2));
        preset.add(new Stat(context.getResources().getString(R.string.potential),
                "0",
                charId, 2));
        preset.add(new Stat(context.getResources().getString(R.string.ether),
                "0",
                charId, 2));

        return preset;
    }
}
