package com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.common.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.dao.LevelDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Level;

public class LevelRepository {

    private LevelDao levelDao;
    private LiveData<Level> level;

    public LevelRepository(Application application, int charId) {
        levelDao = DbSingleton.Instance(application).getLevelDao();
        level = levelDao.getLevel(charId);
    }

    public LiveData<Level> getLevel() {
        return level;
    }

    public void insert(Level level) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                levelDao.insert(level);
            }
        });
    }

    public void delete(Level level) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                levelDao.delete(level);
            }
        });
    }

    public void update(int value, int charId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                levelDao.update(value, charId);
            }
        });
    }
}
