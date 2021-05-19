package com.awkwardlydevelopedapps.unicharsheet.common.data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.awkwardlydevelopedapps.unicharsheet.characterList.dao.CharacterDao;
import com.awkwardlydevelopedapps.unicharsheet.characterList.dao.PresetDao;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Character;
import com.awkwardlydevelopedapps.unicharsheet.notes.dao.NoteDao;
import com.awkwardlydevelopedapps.unicharsheet.abilities.dao.SpellDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.dao.CurrencyDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.dao.EquipmentDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.dao.ExperienceDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.dao.ItemDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.dao.LevelDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Currency;
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.model.Equipment;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Experience;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.model.Item;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Level;
import com.awkwardlydevelopedapps.unicharsheet.notes.model.Note;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Preset;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.PresetList;
import com.awkwardlydevelopedapps.unicharsheet.abilities.model.Spell;
import com.awkwardlydevelopedapps.unicharsheet.stats.model.Stat;
import com.awkwardlydevelopedapps.unicharsheet.stats.dao.StatDao;

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
        Level.class,
        Note.class}, version = 8, exportSchema = false)
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

    public abstract NoteDao noteDao();

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

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'notes'('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'title' TEXT, 'note' TEXT, 'char_id' INTEGER NOT NULL)");
        }
    };
}
