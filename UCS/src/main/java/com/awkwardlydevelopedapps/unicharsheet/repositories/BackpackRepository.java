package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.ItemDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.Item;

import java.util.List;

public class BackpackRepository {

    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;

    public BackpackRepository(Application application, int charId) {
        itemDao = DbSingleton.Instance(application).getItemDao();
        allItems = itemDao.getLiveDataAllItems(charId);
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
}
