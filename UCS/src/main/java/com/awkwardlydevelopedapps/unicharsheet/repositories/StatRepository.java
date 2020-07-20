package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.stat.Stat;

import java.util.List;

public class StatRepository {
    private StatDao statDao;
    private LiveData<List<Stat>> allStatsPage1;
    private LiveData<List<Stat>> allStatsPage2;
    private LiveData<List<Stat>> allStatsPage3;

    private LiveData<List<Stat>> allStats;

    private LiveData<List<Stat>> allStatsOfPage;

    public StatRepository(Application application, int id) {
        statDao = DbSingleton.Instance(application).getStatDao();

        allStatsPage1 = statDao.getLiveDataAllPageStats(id, 1);
        allStatsPage2 = statDao.getLiveDataAllPageStats(id, 2);
        allStatsPage3 = statDao.getLiveDataAllPageStats(id, 3);

        allStats = statDao.getLiveDataAllStats(id);
    }

    public LiveData<List<Stat>> getAllStatsPage1() {
        return allStatsPage1;
    }

    public LiveData<List<Stat>> getAllStatsPage2() {
        return allStatsPage2;
    }

    public LiveData<List<Stat>> getAllStatsPage3() {
        return allStatsPage3;
    }

    public LiveData<List<Stat>> getAllStats() {
        return allStats;
    }

    public LiveData<List<Stat>> getAllStatsOfPage(int id, int page) {
        allStatsOfPage = statDao.getLiveDataAllPageStats(id, page);
        return allStatsOfPage;
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

    public void updateStatValue(String statName, String newValue, int charId, int statId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                statDao.updateStatValue(statName, newValue, charId, statId);
            }
        });
    }


}
