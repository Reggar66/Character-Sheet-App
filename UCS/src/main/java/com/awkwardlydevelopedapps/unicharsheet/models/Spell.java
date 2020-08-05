package com.awkwardlydevelopedapps.unicharsheet.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract;

@Entity(tableName = "spells")
public class Spell {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    private String spellName;
    @ColumnInfo(name = "cost")
    private String cost;
    @ColumnInfo(name = "add_cost")
    private String addCost;
    @ColumnInfo(name = "damage")
    private String dmg;
    @ColumnInfo(name = "add_effect")
    private String addEffect;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "special_notes")
    private String specialNotes;
    @ColumnInfo(name = "image_id")
    private String imageResourceTag;
    @ColumnInfo(name = "char_id")
    private int charId;

    @Ignore
    private boolean hasImage;
    @Ignore
    private boolean checked;

    public Spell(String spellName, String description, String cost, String addCost, String dmg, String addEffect, String specialNotes, String imageResourceTag, int charId) {
        this.spellName = spellName;
        this.description = description;
        this.cost = cost;
        this.addCost = addCost;
        this.dmg = dmg;
        this.addEffect = addEffect;
        this.specialNotes = specialNotes;
        this.imageResourceTag = imageResourceTag;
        this.charId = charId;
        if (imageResourceTag.isEmpty()) {
            hasImage = false;
        } else {
            hasImage = true;
        }

    }

    public String getSpellName() {
        return spellName;
    }

    public String getDescription() {
        return description;
    }

    public String getCost() {
        return cost;
    }

    public String getDmg() {
        return dmg;
    }

    public String getImageResourceTag() {
        return imageResourceTag;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public int getImageResourceId() {
        switch (imageResourceTag) {
            case ImageContract.Spell.FIRE:
                return ImageContract.Spell.FIRE_ID;
            case ImageContract.Spell.AIR:
                return ImageContract.Spell.AIR_ID;
            case ImageContract.Spell.WATER:
                return ImageContract.Spell.WATER_ID;
            case ImageContract.Spell.EARTH:
                return ImageContract.Spell.EARTH_ID;
            case ImageContract.Spell.NATURE:
                return ImageContract.Spell.NATURE_ID;
            case ImageContract.Spell.ESSENCE:
                return ImageContract.Spell.ESSENCE_ID;
            case ImageContract.Spell.MIND:
                return ImageContract.Spell.MIND_ID;
            default:
                return ImageContract.Spell.FIRE_ID;
        }
    }

    public void setChecked(boolean check) {
        checked = check;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getAddCost() {
        return addCost;
    }

    public void setAddCost(String addCost) {
        this.addCost = addCost;
    }

    public String getAddEffect() {
        return addEffect;
    }

    public void setAddEffect(String addEffect) {
        this.addEffect = addEffect;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }
}
