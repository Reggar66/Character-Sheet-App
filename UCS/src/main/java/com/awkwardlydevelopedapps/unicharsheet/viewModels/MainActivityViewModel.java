package com.awkwardlydevelopedapps.unicharsheet.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.character.Character;
import com.awkwardlydevelopedapps.unicharsheet.character.CharacterListAdapter;
import com.awkwardlydevelopedapps.unicharsheet.repositories.CharacterRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private CharacterRepository characterRepository;
    private LiveData<List<Character>> allCharacters;
    private List<Character> characterList;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        characterRepository = new CharacterRepository(application);
        allCharacters = characterRepository.getAllCharacters();
    }


    public LiveData<List<Character>> getAllCharacters() {
        return allCharacters;
    }

    public void insert(Character character) {
        characterRepository.insert(character);
    }

    public void checkCharsAndDelete(CharacterListAdapter adapter) {
        Character character;
        for (int i = 0; i < characterList.size(); i++) {
            character = characterList.get(i);
            if (character.isChecked()) {
                characterRepository.deleteCharacter(character);
                characterRepository.deleteStats(character.id);
                characterRepository.deleteSpells(character.id);
                characterRepository.deleteEquipment(character.id);
                characterRepository.deleteItems(character.id);
                characterRepository.deleteCurrencies(character.id);
                characterRepository.deleteExperience(character.id);
            }
        }
        adapter.setShowChecks();
    }

    public void setCharacterList(List<Character> characterList) {
        this.characterList = characterList;
    }

    public List<Character> getCharacterList() {
        return characterList;
    }
}
