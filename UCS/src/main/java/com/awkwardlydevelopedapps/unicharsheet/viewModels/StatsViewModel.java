package com.awkwardlydevelopedapps.unicharsheet.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.repositories.StatRepository;
import com.awkwardlydevelopedapps.unicharsheet.models.Stat;
import com.awkwardlydevelopedapps.unicharsheet.adapters.StatAdapter;

import java.util.ArrayList;
import java.util.List;

public class StatsViewModel extends ViewModel {

    private StatRepository statRepository;

    private LiveData<List<Stat>> allStatsOfPage;

    public StatsViewModel(Application application, int charId) {
        statRepository = new StatRepository(application, charId);
    }

    public LiveData<List<Stat>> getAllStatsOfPage(int id, int page) {
        allStatsOfPage = statRepository.getAllStatsOfPage(id, page);
        return allStatsOfPage;
    }

    public void updateStatValue(String statName, String newValue, int charId, int statId) {
        statRepository.updateStatValue(statName, newValue, charId, statId);
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

        public CAViewModelFactory(Application application, int charId) {
            this.application = application;
            this.charId = charId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StatsViewModel(application, charId);
        }
    }
}
