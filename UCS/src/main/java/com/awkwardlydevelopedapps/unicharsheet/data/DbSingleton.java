package com.awkwardlydevelopedapps.unicharsheet.data;

import android.content.Context;

import androidx.room.Room;

public class DbSingleton {

    private static DbSingleton instance = null;

    private AppDatabase database;

    private DbSingleton(Context context) {

        database = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "characters.db")
                //.allowMainThreadQueries()
                //.fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_3_4,
                        AppDatabase.MIGRATION_4_5,
                        AppDatabase.MIGRATION_5_6,
                        AppDatabase.MIGRATION_6_7,
                        AppDatabase.MIGRATION_7_8)
                .build();
    }

    public static DbSingleton Instance(Context context) {
        if (instance == null) {
            synchronized (DbSingleton.class) {
                instance = new DbSingleton(context);
            }
        }

        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public CharacterDao getCharacterDao() {
        return database.characterDao();
    }

    public CurrencyDao getCurrencyDao() {
        return database.currencyDao();
    }

    public EquipmentDao getEquipmentDao() {
        return database.equipmentDao();
    }

    public ItemDao getItemDao() {
        return database.itemDao();
    }

    public PresetDao getPresetDao() {
        return database.presetDao();
    }

    public SpellDao getSpellDao() {
        return database.spellDao();
    }

    public StatDao getStatDao() {
        return database.statDao();
    }

    public ExperienceDao getExperienceDao() {
        return database.experienceDao();
    }

    public LevelDao getLevelDao() {
        return database.levelDao();
    }
}
