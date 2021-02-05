package com.awkwardlydevelopedapps.unicharsheet.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.repositories.StatRepository;
import com.awkwardlydevelopedapps.unicharsheet.models.Stat;
import com.awkwardlydevelopedapps.unicharsheet.adapters.StatAdapter;

import java.util.ArrayList;
import java.util.List;

public class StatsViewModel extends ViewModel {

    private int page;

    private final StatRepository statRepository;
    private LiveData<List<Stat>> allStatsOfPage;
    private LiveData<List<Stat>> allStatsOfPageByNameAsc;
    private LiveData<List<Stat>> allStatsOfPageByNameDesc;
    private LiveData<List<Stat>> allStatsOfPageByValueAsc;
    private LiveData<List<Stat>> allStatsOfPageByValueDesc;

    private MediatorLiveData<List<Stat>> statsOfPage = new MediatorLiveData<>();

    public StatsViewModel(Application application, int charId, int page) {
        statRepository = new StatRepository(application, charId);
        this.page = page;
        initStatsLiveData();

        statsOfPage.addSource(getAllStatsOfPageByNameAsc(), stats -> statsOfPage.setValue(stats));
    }

    private void initStatsLiveData() {
        allStatsOfPage = statRepository.getAllStatsOfPage(page);
        allStatsOfPageByNameAsc = statRepository.getAllStatsOfPageByNameAsc(page);
        allStatsOfPageByNameDesc = statRepository.getAllStatsOfPageByNameDesc(page);
        allStatsOfPageByValueAsc = statRepository.getAllStatsOfPageByValueAsc(page);
        allStatsOfPageByValueDesc = statRepository.getAllStatsOfPageByValueDesc(page);
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

    public void updateStatValues(String statName, String newValue, int charId, int statId) {
        statRepository.updateStatValues(statName, newValue, charId, statId);
    }

    public MediatorLiveData<List<Stat>> getStatsOfPage() {
        return statsOfPage;
    }

    public void sortBy(int sortOrder) {

        statsOfPage.removeSource(getAllStatsOfPage());
        statsOfPage.removeSource(getAllStatsOfPageByNameAsc());
        statsOfPage.removeSource(getAllStatsOfPageByNameDesc());
        statsOfPage.removeSource(getAllStatsOfPageByValueAsc());
        statsOfPage.removeSource(getAllStatsOfPageByValueDesc());

        switch (sortOrder) {
            default:
            case Sort.BY_NAME_ASC:
                statsOfPage.addSource(getAllStatsOfPageByNameAsc(),
                        stats -> statsOfPage.setValue(stats));
                break;
            case Sort.BY_NAME_DESC:
                statsOfPage.addSource(getAllStatsOfPageByNameDesc(),
                        stats -> statsOfPage.setValue(stats));
                break;
            case Sort.BY_VALUE_ASC:
                statsOfPage.addSource(getAllStatsOfPageByValueAsc(),
                        stats -> statsOfPage.setValue(stats));
                break;
            case Sort.BY_VALUE_DESC:
                statsOfPage.addSource(getAllStatsOfPageByValueDesc(),
                        stats -> statsOfPage.setValue(stats));
                break;
        }
    }

    public void checkStatsAndDelete(StatAdapter adapter, List<Stat> stats) {
        List<Stat> temp = new ArrayList<>(stats);
        for (Stat stat : temp) {
            if (stat.isChecked()) {
                statRepository.delete(stat);
            }
        }
        adapter.setShowChecks();
    }

    public void insert(Stat stat) {
        statRepository.insert(stat);
    }


    /**
     * Factory
     */
    public static class CAViewModelFactory implements ViewModelProvider.Factory {

        Application application;
        int charId;
        int page;

        public CAViewModelFactory(Application application, int charId, int page) {
            this.application = application;
            this.charId = charId;
            this.page = page;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StatsViewModel(application, charId, page);
        }
    }
}
