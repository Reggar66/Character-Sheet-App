package com.awkwardlydevelopedapps.unicharsheet.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.models.Item;
import com.awkwardlydevelopedapps.unicharsheet.adapters.BackpackAdapter;
import com.awkwardlydevelopedapps.unicharsheet.repositories.BackpackRepository;

import java.util.ArrayList;
import java.util.List;

public class BackpackViewModel extends ViewModel {

    private BackpackRepository backpackRepository;
    private LiveData<List<Item>> allItems;
    private List<Item> items;

    public BackpackViewModel(Application application, int charId) {
        backpackRepository = new BackpackRepository(application, charId);
        allItems = backpackRepository.getAllItems();
    }


    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void insert(Item item) {
        backpackRepository.insert(item);
    }

    public void delete(Item item) {
        backpackRepository.delete(item);
    }

    public void updateItem(String newName, String newValue, int charId, int id) {
        backpackRepository.updateItem(newName, newValue, charId, id);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void checkItemsToDelete(BackpackAdapter adapter) {
        List<Item> temp = new ArrayList<>(this.items);
        for (Item item : temp) {
            if (item.isChecked()) {
                backpackRepository.delete(item);
            }
        }
        adapter.setShowChecks();
    }


    /**
     * Factory
     */
    public static class BackpackViewModelFactory implements ViewModelProvider.Factory {

        Application application;
        int charId;

        public BackpackViewModelFactory(Application application, int charId) {
            this.application = application;
            this.charId = charId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new BackpackViewModel(application, charId);
        }
    }
}
