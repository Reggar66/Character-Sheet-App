package com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.common.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.dao.ItemDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.model.Item;

import java.util.List;

public class BackpackRepository {

    private final ItemDao itemDao;
    private final LiveData<List<Item>> allItems;
    private final LiveData<List<Item>> allItemsByNameAsc;
    private final LiveData<List<Item>> allItemsByNameDesc;
    private final LiveData<List<Item>> allItemsByValueAsc;
    private final LiveData<List<Item>> allItemsByValueDesc;

    public BackpackRepository(Application application, int charId) {
        itemDao = DbSingleton.Instance(application).getItemDao();
        allItems = itemDao.getLiveDataAllItems(charId);
        allItemsByNameAsc = itemDao.getLiveDataAllItemsByNameAsc(charId);
        allItemsByNameDesc = itemDao.getLiveDataAllItemsByNameDesc(charId);
        allItemsByValueAsc = itemDao.getLiveDataAllItemsByValueAsc(charId);
        allItemsByValueDesc = itemDao.getLiveDataAllItemsByValueDesc(charId);
    }

    public void insert(Item item) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                itemDao.insert(item);
            }
        });
    }

    public void delete(Item item) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                itemDao.delete(item);
            }
        });
    }

    public void updateItem(String newName, String newQuantity, int charId, int id) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                itemDao.updateItem(newName, newQuantity, charId, id);
            }
        });
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public LiveData<List<Item>> getAllItemsByNameAsc() {
        return allItemsByNameAsc;
    }

    public LiveData<List<Item>> getAllItemsByNameDesc() {
        return allItemsByNameDesc;
    }

    public LiveData<List<Item>> getAllItemsByValueAsc() {
        return allItemsByValueAsc;
    }

    public LiveData<List<Item>> getAllItemsByValueDesc() {
        return allItemsByValueDesc;
    }
}
