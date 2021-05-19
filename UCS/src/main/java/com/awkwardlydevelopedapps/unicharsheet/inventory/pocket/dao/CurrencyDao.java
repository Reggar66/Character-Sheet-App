package com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Currency;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Insert
    void insert(Currency currency);

    @Delete
    void delete(Currency currency);

    @Query("DELETE FROM currency WHERE char_id = :id AND type = :typeName")
    void delete(String typeName, int id);

    @Query("SELECT * FROM currency WHERE char_id = :id AND type = :typeName")
    Currency getCurrency(String typeName, int id);

    @Query("SELECT * FROM currency WHERE char_id = :id")
    LiveData<List<Currency>> getLiveDataAllCurrency(int id);

    @Query("UPDATE currency SET value = :value WHERE char_id = :charId AND type = :type")
    void updateCurrency(String value, int charId, String type);

    @Query("UPDATE currency SET value = :value, max_value = :maxValue WHERE char_id = :charId AND type = :type")
    void updateCurrencyWithMaxValue(String value, String maxValue, int charId, String type);
}
