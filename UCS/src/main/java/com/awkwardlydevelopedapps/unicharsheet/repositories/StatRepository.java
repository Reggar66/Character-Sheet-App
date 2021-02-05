package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.models.Stat;

import java.util.List;

public class StatRepository {
    private final int characterId;
    private final int page;
    private final StatDao statDao;

    private final LiveData<List<Stat>> allStatsOfPage;
    private final LiveData<List<Stat>> allStatsOfPageByNameAsc;
    private final LiveData<List<Stat>> allStatsOfPageByNameDesc;
    private final LiveData<List<Stat>> allStatsOfPageByValueAsc;
    private final LiveData<List<Stat>> allStatsOfPageByValueDesc;

    public StatRepository(Application application, int characterId, int page) {
        statDao = DbSingleton.Instance(application).getStatDao();
        this.characterId = characterId;
        this.page = page;

        allStatsOfPage = statDao.getLiveDataAllPageStats(characterId, page);
        allStatsOfPageByNameAsc = statDao.getLiveDataAllPageStatsByNameAsc(characterId, page);
        allStatsOfPageByNameDesc = statDao.getLiveDataAllPageStatsByNameDesc(characterId, page);
        allStatsOfPageByValueAsc = statDao.getLiveDataAllPageStatsByValueAsc(characterId, page);
        allStatsOfPageByValueDesc = statDao.getLiveDataAllPageStatsByValueDesc(characterId, page);
    }

    public LiveData<List<Stat>> getAllStatsOfPage() {
        return allStatsOfPage;
    }

    public LiveData<List<Stat>> getAllStatsOfPageByNameAsc() {
        return allStatsOfPageByNameAsc;
    }

    public LiveData<List<Stat>> getAllStatsOfPageByNameDesc() {
        return allStatsOfPageByNameDesc;
    }

    public LiveData<List<Stat>> getAllStatsOfPageByValueAsc() {
        return allStatsOfPageByValueAsc;
    }

    public LiveData<List<Stat>> getAllStatsOfPageByValueDesc() {
        return allStatsOfPageByValueDesc;
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
