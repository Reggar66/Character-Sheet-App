package com.awkwardlydevelopedapps.unicharsheet.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.models.Equipment;

import java.util.List;

@Dao
public interface EquipmentDao {

    @Insert
    void insertEq(Equipment equipment);

    @Query("SELECT * FROM equipment WHERE char_id = :id")
    LiveData<List<Equipment>> getLiveDataAllEquipment(int id);

    @Query("SELECT * FROM equipment WHERE slot = :slotName AND char_id = :id")
    Equipment getEquipment(String slotName, int id);

    @Query("DELETE FROM equipment WHERE slot = :slotName AND char_id = :id")
    void deleteSingleEquipment(String slotName, int id);

    @Query("UPDATE equipment SET name = :name, type = :type, value = :value, add_effect = :addEffect WHERE slot = :slot AND char_id = :charId")
    void updateEquipment(String name, String type, String value, String addEffect, String slot, int charId);
}
