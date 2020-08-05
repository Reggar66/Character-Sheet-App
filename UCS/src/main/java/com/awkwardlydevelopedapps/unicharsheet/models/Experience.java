package com.awkwardlydevelopedapps.unicharsheet.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "experience")
public class Experience {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "value")
    private int value;
    @ColumnInfo(name = "max_value")
    private int maxValue;
    @ColumnInfo(name = "char_id")
    private int charId;

    public Experience(int value, int maxValue, int charId) {
        this.value = value;
        this.maxValue = maxValue;
        this.charId = charId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }
}
