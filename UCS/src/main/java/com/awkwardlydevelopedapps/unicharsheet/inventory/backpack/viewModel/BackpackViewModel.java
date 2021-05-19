package com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.adapters.BackpackAdapter;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.model.Item;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.repository.BackpackRepository;

import java.util.ArrayList;
import java.util.List;

public class BackpackViewModel extends ViewModel {

    private final BackpackRepository backpackRepository;
    private final MediatorLiveData<List<Item>> allItems = new MediatorLiveData<>();
    private List<Item> itemList;

    public BackpackViewModel(Application application, int charId) {
        backpackRepository = new BackpackRepository(application, charId);
        allItems.addSource(getAllItemsByNameAsc(), items -> allItems.setValue(items));
    }


    public MediatorLiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public LiveData<List<Item>> getAllItemsByNameAsc() {
        return backpackRepository.getAllItemsByNameAsc();
    }

    public LiveData<List<Item>> getAllItemsByNameDesc() {
        return backpackRepository.getAllItemsByNameDesc();
    }

    public LiveData<List<Item>> getAllItemsByValueAsc() {
        return backpackRepository.getAllItemsByValueAsc();
    }

    public LiveData<List<Item>> getAllItemsByValueDesc() {
        return backpackRepository.getAllItemsByValueDesc();
    }

    public void orderBy(int sort) {
        allItems.removeSource(getAllItemsByNameAsc());
        allItems.removeSource(getAllItemsByNameDesc());
        allItems.removeSource(getAllItemsByValueAsc());
        allItems.removeSource(getAllItemsByValueDesc());

        switch (sort) {
            default:
            case Sort.BY_NAME_ASC:
                allItems.addSource(getAllItemsByNameAsc(), items -> allItems.setValue(items));
                break;
            case Sort.BY_NAME_DESC:
                allItems.addSource(getAllItemsByNameDesc(), items -> allItems.setValue(items));
                break;
            case Sort.BY_VALUE_ASC:
                allItems.addSource(getAllItemsByValueAsc(), items -> allItems.setValue(items));
                break;
            case Sort.BY_VALUE_DESC:
                allItems.addSource(getAllItemsByValueDesc(), items -> allItems.setValue(items));
                break;
        }
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

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void checkItemsToDelete(BackpackAdapter adapter) {
        List<Item> temp = new ArrayList<>(this.itemList);
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
