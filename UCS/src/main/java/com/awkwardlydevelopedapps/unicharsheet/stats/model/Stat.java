package com.awkwardlydevelopedapps.unicharsheet.stats.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "stats")
public class Stat {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "value")
    private String value;
    @ColumnInfo(name = "char_id")
    private int charId;
    @ColumnInfo(name = "page")
    private int page;

    @Ignore
    private boolean checked;

    public Stat(String name, String value, int charId, int page) {
        this.name = name;
        this.value = value;
        this.charId = charId;
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setStatValue(String value) {
        this.value = value;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }

    public void setChecked(boolean check) {
        checked = check;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
