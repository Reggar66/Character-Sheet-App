package com.awkwardlydevelopedapps.unicharsheet.data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.awkwardlydevelopedapps.unicharsheet.models.Character;
import com.awkwardlydevelopedapps.unicharsheet.models.Currency;
import com.awkwardlydevelopedapps.unicharsheet.models.Equipment;
import com.awkwardlydevelopedapps.unicharsheet.models.Experience;
import com.awkwardlydevelopedapps.unicharsheet.models.Item;
import com.awkwardlydevelopedapps.unicharsheet.models.Level;
import com.awkwardlydevelopedapps.unicharsheet.models.Preset;
import com.awkwardlydevelopedapps.unicharsheet.models.PresetList;
import com.awkwardlydevelopedapps.unicharsheet.models.Spell;
import com.awkwardlydevelopedapps.unicharsheet.models.Stat;

@Database(entities = {
        Character.class,
        Stat.class,
        Spell.class,
        Item.class,
        Equipment.class,
        Currency.class,
        PresetList.class,
        Preset.class,
        Experience.class,
        Level.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CharacterDao characterDao();

    public abstract StatDao statDao();

    public abstract SpellDao spellDao();

    public abstract EquipmentDao equipmentDao();

    public abstract ItemDao itemDao();

    public abstract CurrencyDao currencyDao();

    public abstract PresetDao presetDao();

    public abstract ExperienceDao experienceDao();

    public abstract LevelDao levelDao();

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE equipment " +
                    "ADD COLUMN type TEXT ");

            database.execSQL("ALTER TABLE equipment " +
                    "ADD COLUMN add_effect TEXT");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'experience'('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'value' INTEGER NOT NULL, 'max_value' INTEGER NOT NULL, 'char_id' INTEGER NOT NULL)");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE currency ADD COLUMN max_value TEXT");
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'level'('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'value' INTEGER NOT NULL, 'char_id' INTEGER NOT NULL)");
        }
    };
}
