package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.CurrencyDao;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.EquipmentDao;
import com.awkwardlydevelopedapps.unicharsheet.data.ItemDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.Currency;
import com.awkwardlydevelopedapps.unicharsheet.inventory.Equipment;
import com.awkwardlydevelopedapps.unicharsheet.inventory.Item;

import java.util.List;

public class EquipmentRepository {
    private EquipmentDao equipmentDao;
    private LiveData<List<Equipment>> allEquipment;

    public EquipmentRepository(Application application, int charId) {
        equipmentDao = DbSingleton.Instance(application).getEquipmentDao();

        allEquipment = equipmentDao.getLiveDataAllEquipment(charId);
    }

    public LiveData<List<Equipment>> getAllEquipment() {
        return allEquipment;
    }

    public void insertEquipment(Equipment equipment) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                equipmentDao.insertEq(equipment);
            }
        });
    }

    public void updateEquipment(String name, String type, String value, String additionalEffects, String slot, int charId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                equipmentDao.updateEquipment(name, type, value, additionalEffects, slot, charId);
            }
        });
    }
}
