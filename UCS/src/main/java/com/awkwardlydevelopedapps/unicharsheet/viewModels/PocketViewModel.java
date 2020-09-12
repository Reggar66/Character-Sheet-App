package com.awkwardlydevelopedapps.unicharsheet.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.models.Currency;
import com.awkwardlydevelopedapps.unicharsheet.models.Experience;
import com.awkwardlydevelopedapps.unicharsheet.models.Level;
import com.awkwardlydevelopedapps.unicharsheet.repositories.CurrencyRepository;
import com.awkwardlydevelopedapps.unicharsheet.repositories.ExperienceRepository;
import com.awkwardlydevelopedapps.unicharsheet.repositories.LevelRepository;

import java.util.List;

public class PocketViewModel extends ViewModel {

    private CurrencyRepository currencyRepository;
    private LiveData<List<Currency>> allCurrency;
    private List<Currency> currencies;
    private Currency gold;
    private Currency silver;
    private Currency copper;

    private ExperienceRepository experienceRepository;
    private LiveData<Experience> experienceLiveData;
    private Experience experience;

    private LevelRepository levelRepository;
    private LiveData<Level> levelLiveData;
    private Level level;

    public PocketViewModel(Application application, int charId) {
        currencyRepository = new CurrencyRepository(application, charId);
        allCurrency = currencyRepository.getAllCurrency();

        experienceRepository = new ExperienceRepository(application, charId);
        experienceLiveData = experienceRepository.getExperience();

        levelRepository = new LevelRepository(application, charId);
        levelLiveData = levelRepository.getLevel();
    }

    // Currency
    public LiveData<List<Currency>> getAllCurrency() {
        return allCurrency;
    }

    public void insert(Currency currency) {
        currencyRepository.insert(currency);
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public void updateCurrency(String value, int charId, String type) {
        currencyRepository.updateCurrency(value, charId, type);
    }

    public void updateCurrencyWithMaxValue(String value, String maxValue, int charId, String type) {
        currencyRepository.updateCurrencyWithMaxValue(value, maxValue, charId, type);
    }

    public Currency getCopper() {
        return copper;
    }

    public void setCopper(Currency copper) {
        this.copper = copper;
    }

    public Currency getGold() {
        return gold;
    }

    public void setGold(Currency gold) {
        this.gold = gold;
    }

    public Currency getSilver() {
        return silver;
    }

    public void setSilver(Currency silver) {
        this.silver = silver;
    }

    // Experience
    public LiveData<Experience> getExperienceLiveData() {
        return experienceLiveData;
    }

    public void insert(Experience experience) {
        experienceRepository.insert(experience);
    }

    public void delete(Experience experience) {
        experienceRepository.delete(experience);
    }

    public void updateExperience(int value, int charId) {
        experienceRepository.update(value, charId);
    }

    public void updateExperienceWithMaxValue(int value, int maxValue, int charId) {
        experienceRepository.updateWithMaxValue(value, maxValue, charId);
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    // Level
    public LiveData<Level> getLevelLiveData() {
        return levelLiveData;
    }

    public void insert(Level level) {
        levelRepository.insert(level);
    }

    public void delete(Level level) {
        levelRepository.delete(level);
    }

    public void updateLevel(int value, int charId) {
        levelRepository.update(value, charId);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Factory
     */

    public static class PocketViewModelFactory implements ViewModelProvider.Factory {

        Application application;
        int charId;

        public PocketViewModelFactory(Application application, int charId) {
            this.application = application;
            this.charId = charId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PocketViewModel(application, charId);
        }
    }
}
