package com.awkwardlydevelopedapps.unicharsheet.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "level")
public class Level {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "value")
    private int value;
    @ColumnInfo(name = "char_id")
    private int charId;

    public Level(int value, int charId) {
        this.value = value;
        this.charId = charId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }
}
