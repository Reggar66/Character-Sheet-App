package com.awkwardlydevelopedapps.unicharsheet.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract;

@Entity(tableName = "characters")
public class Character {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    private String mCharacterName;
    @ColumnInfo(name = "class")
    private String mClassName;
    @ColumnInfo(name = "race")
    private String mRaceName;
    @ColumnInfo(name = "image_id")
    private String mImageResourceTag;

    @Ignore
    private boolean hasImage;
    @Ignore
    private boolean checked;


    @Ignore
    public Character(String characterName, String className, String raceName) {
        mCharacterName = characterName;
        mClassName = className;
        mRaceName = raceName;

        mImageResourceTag = "";
        hasImage = false;
    }

    public Character(String characterName, String className, String raceName, String imageResourceTag) {
        mCharacterName = characterName;
        mClassName = className;
        mRaceName = raceName;

        mImageResourceTag = imageResourceTag;
        if (imageResourceTag.isEmpty()) {
            hasImage = false;
        } else {
            hasImage = true;
        }
    }

    public String getCharacterName() {
        return mCharacterName;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getRaceName() {
        return mRaceName;
    }

    public String getImageResourceTag() {
        return mImageResourceTag;
    }

    public int getImageResourceId() {
        switch (mImageResourceTag) {
            case ImageContract.Character.COWLED:
                return ImageContract.Character.COWLED_ID;
            case ImageContract.Character.CULTIST:
                return ImageContract.Character.CULTIST_ID;
            case ImageContract.Character.VIKING:
                return ImageContract.Character.VIKING_ID;
            case ImageContract.Character.WIZARD:
                return ImageContract.Character.WIZARD_ID;
            case ImageContract.Character.VISORED:
                return ImageContract.Character.VISORED_ID;
            case ImageContract.Character.KENAKU:
                return ImageContract.Character.KENAKU_ID;
            case ImageContract.Character.ALIEN:
                return ImageContract.Character.ALIEN_ID;
            case ImageContract.Character.BANDIT:
                return ImageContract.Character.BANDIT_ID;
            case ImageContract.Character.DWARF:
                return ImageContract.Character.DWARF_ID;
            case ImageContract.Character.HOOD:
                return ImageContract.Character.HOOD_ID;
            case ImageContract.Character.MEDUSA:
                return ImageContract.Character.MEDUSA_ID;
            case ImageContract.Character.ORC:
                return ImageContract.Character.ORC_ID;
            case ImageContract.Character.OVERLORD:
                return ImageContract.Character.OVERLORD_ID;
            case ImageContract.Character.BESTIAL_FANGS:
                return ImageContract.Character.BESTIAL_FANGS_ID;
            case ImageContract.Character.BOAR_TUSKS:
                return ImageContract.Character.BOAR_TUSKS_ID;
            case ImageContract.Character.BURNING_SKULL:
                return ImageContract.Character.BURNING_SKULL_ID;
            case ImageContract.Character.HORNED_REPTILE:
                return ImageContract.Character.HORNED_REPTILE_ID;


            default:
                return ImageContract.Character.COWLED_ID;
        }
    }

    public boolean hasImage() {
        return hasImage;
    }

    public void setChecked(boolean check) {
        checked = check;
    }

    public boolean isChecked() {
        return checked;
    }
}
