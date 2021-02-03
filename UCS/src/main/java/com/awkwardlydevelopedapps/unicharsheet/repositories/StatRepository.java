package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.models.Stat;

import java.util.List;

public class StatRepository {
    private final StatDao statDao;
    private int characterId;

    public StatRepository(Application application, int characterId) {
        statDao = DbSingleton.Instance(application).getStatDao();
        this.characterId = characterId;
    }

    public LiveData<List<Stat>> getAllStats() {
        return statDao.getLiveDataAllStats(characterId);
    }

    public LiveData<List<Stat>> getAllStatsOfPage(int page) {
        return statDao.getLiveDataAllPageStats(characterId, page);
    }

    public LiveData<List<Stat>> getAllStatsOfPageByNameAsc(int page) {
        return statDao.getLiveDataAllPageStatsByNameAsc(characterId, page);
    }

    public LiveData<List<Stat>> getAllStatsOfPageByNameDesc(int page) {
        return statDao.getLiveDataAllPageStatsByNameDesc(characterId, page);
    }

    public LiveData<List<Stat>> getAllStatsOfPageByValueAsc(int page) {
        return statDao.getLiveDataAllPageStatsByValueAsc(characterId, page);
    }

    public LiveData<List<Stat>> getAllStatsOfPageByValueDesc(int page) {
        return statDao.getLiveDataAllPageStatsByValueDesc(characterId, page);
    }

    public void insert(Stat stat) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                statDao.insert(stat);
            }
        });
    }

    public void delete(Stat stat) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                statDao.delete(stat);
            }
        });
    }

    public void updateStatValues(String statName, String newValue, int charId, int statId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                statDao.updateStatValue(statName, newValue, charId, statId);
            }
        });
    }


}
