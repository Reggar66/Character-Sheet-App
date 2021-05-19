package com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "currency")
public class Currency {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String value;
    @ColumnInfo(name = "max_value")
    private String maxValue;
    private String type; //gold, silver, copper
    @ColumnInfo(name = "char_id")
    private int charId;

    @Ignore
    public static final String TYPE_GOLD = "Gold";
    @Ignore
    public static final String TYPE_SILVER = "Silver";
    @Ignore
    public static final String TYPE_COPPER = "Copper";


    public Currency(String value, String maxValue, String type, int charId) {
        this.value = value;
        this.type = type;
        this.charId = charId;
        this.maxValue = maxValue;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public int getCharId() {
        return charId;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }
}
