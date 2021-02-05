package com.awkwardlydevelopedapps.unicharsheet.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.models.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    long insert(Item item);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM items WHERE char_id = :charId")
    List<Item> getAllItems(int charId);

    @Query("SELECT * FROM items WHERE char_id = :charId")
    LiveData<List<Item>> getLiveDataAllItems(int charId);

    @Query("SELECT * FROM items WHERE char_id = :charId ORDER BY name ASC")
    LiveData<List<Item>> getLiveDataAllItemsByNameAsc(int charId);

    @Query("SELECT * FROM items WHERE char_id = :charId ORDER BY name DESC")
    LiveData<List<Item>> getLiveDataAllItemsByNameDesc(int charId);

    @Query("SELECT * FROM items WHERE char_id = :charId ORDER BY quantity ASC")
    LiveData<List<Item>> getLiveDataAllItemsByValueAsc(int charId);

    @Query("SELECT * FROM items WHERE char_id = :charId ORDER BY quantity DESC")
    LiveData<List<Item>> getLiveDataAllItemsByValueDesc(int charId);

    @Query("UPDATE items SET name = :newName, quantity = :newQuantity WHERE char_id = :charId AND id = :itemId")
    void updateItem(String newName, String newQuantity, int charId, int itemId);
}
