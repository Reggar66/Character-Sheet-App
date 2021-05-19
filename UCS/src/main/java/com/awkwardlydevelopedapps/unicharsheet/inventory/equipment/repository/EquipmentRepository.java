package com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.common.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.dao.EquipmentDao;
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.model.Equipment;

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
