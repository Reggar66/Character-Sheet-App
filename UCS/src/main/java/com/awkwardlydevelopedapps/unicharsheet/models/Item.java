package com.awkwardlydevelopedapps.unicharsheet.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "quantity")
    private String quantity;
    @ColumnInfo(name = "char_id")
    private int charId;

    @Ignore
    private boolean checked;

    public Item(String name, String quantity, int charId) {
        this.name = name;
        this.quantity = quantity;
        this.charId = charId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean check) {
        checked = check;
    }
}
