package com.awkwardlydevelopedapps.unicharsheet.characterList.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.characterList.dao.CharacterDao;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Character;
import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.common.data.DbSingleton;

import java.util.List;

public class CharacterRepository {
    private final CharacterDao characterDao;
    private final LiveData<List<Character>> allCharacters;
    private final LiveData<List<Character>> allCharactersByNameAsc;
    private final LiveData<List<Character>> allCharactersByNameDesc;

    public CharacterRepository(Application application) {
        characterDao = DbSingleton.Instance(application).getCharacterDao();
        allCharacters = characterDao.getLiveDataAllCharacters();
        allCharactersByNameAsc = characterDao.getLiveDataAllCharactersByNameAsc();
        allCharactersByNameDesc = characterDao.getLiveDataAllCharactersByNameDesc();
    }

    public LiveData<List<Character>> getAllCharacters() {
        return allCharacters;
    }

    public LiveData<List<Character>> getAllCharactersByNameAsc() {
        return allCharactersByNameAsc;
    }

    public LiveData<List<Character>> getAllCharactersByNameDesc() {
        return allCharactersByNameDesc;
    }

    public void insert(Character character) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                characterDao.insert(character);
            }
        });
    }

    public void deleteCharacter(Character character) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                characterDao.delete(character);
            }
        });
    }

    public void deleteStats(int id) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                characterDao.deleteStats(id);
            }
        });
    }

    public void deleteSpells(int id) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                characterDao.deleteSpells(id);
            }
        });
    }

    public void deleteEquipment(int id) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                characterDao.deleteEquipment(id);
            }
        });
    }

    public void deleteItems(int id) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                characterDao.deleteItems(id);
            }
        });
    }

    public void deleteCurrencies(int id) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                characterDao.deleteCurrencies(id);
            }
        });
    }

    public void deleteExperience(int id) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                characterDao.deleteExperience(id);
            }
        });
    }
}
