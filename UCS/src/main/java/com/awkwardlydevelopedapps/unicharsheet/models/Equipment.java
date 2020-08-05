package com.awkwardlydevelopedapps.unicharsheet.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "equipment")
public class Equipment {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "slot")
    private String slot;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "value")
    private String value;
    @ColumnInfo(name = "add_effect")
    private String addEffect;
    @ColumnInfo(name = "char_id")
    private int charId;

    public Equipment(String name, String slot, String type, String value, String addEffect, int charId) {
        this.name = name;
        this.slot = slot;
        this.type = type;
        this.value = value;
        this.addEffect = addEffect;
        this.charId = charId;
    }

    public String getName() {
        return name;
    }

    public String getSlot() {
        return slot;
    }

    public String getValue() {
        return value;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddEffect() {
        return addEffect;
    }

    public void setAddEffect(String addEffect) {
        this.addEffect = addEffect;
    }
}
