package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.models.Character;
import com.awkwardlydevelopedapps.unicharsheet.data.CharacterDao;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;

import java.util.List;

public class CharacterRepository {
    private CharacterDao characterDao;
    private LiveData<List<Character>> allCharacters;

    public CharacterRepository(Application application) {
        characterDao = DbSingleton.Instance(application).getCharacterDao();
        allCharacters = characterDao.getLiveDataAllCharacters();
    }

    public LiveData<List<Character>> getAllCharacters() {
        return allCharacters;
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
