package com.awkwardlydevelopedapps.unicharsheet.stats.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.stats.adapters.StatAdapter;
import com.awkwardlydevelopedapps.unicharsheet.stats.model.Stat;
import com.awkwardlydevelopedapps.unicharsheet.stats.repository.StatRepository;

import java.util.ArrayList;
import java.util.List;

public class StatsViewModel extends ViewModel {

    private final int page;
    private final StatRepository statRepository;

    private final MediatorLiveData<List<Stat>> statsOfPage = new MediatorLiveData<>();

    public StatsViewModel(Application application, int charId, int page) {
        statRepository = new StatRepository(application, charId, page);
        this.page = page;

        statsOfPage.addSource(getAllStatsOfPageByNameAsc(), stats -> statsOfPage.setValue(stats));
    }

    public LiveData<List<Stat>> getAllStatsOfPage() {
        return statRepository.getAllStatsOfPage();
    }

    public LiveData<List<Stat>> getAllStatsOfPageByNameAsc() {
        return statRepository.getAllStatsOfPageByNameAsc();
    }

    public LiveData<List<Stat>> getAllStatsOfPageByNameDesc() {
        return statRepository.getAllStatsOfPageByNameDesc();
    }

    public LiveData<List<Stat>> getAllStatsOfPageByValueAsc() {
        return statRepository.getAllStatsOfPageByValueAsc();
    }

    public LiveData<List<Stat>> getAllStatsOfPageByValueDesc() {
        return statRepository.getAllStatsOfPageByValueDesc();
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
