package com.awkwardlydevelopedapps.unicharsheet.characterList.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.awkwardlydevelopedapps.unicharsheet.characterList.repository.CharacterRepository;
import com.awkwardlydevelopedapps.unicharsheet.characterList.adapters.CharacterListAdapter;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Character;
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;

import java.util.List;

public class CharacterListViewModel extends AndroidViewModel {

    private final CharacterRepository characterRepository;
    private final MediatorLiveData<List<Character>> allCharacters = new MediatorLiveData<>();
    private List<Character> characterList;

    public CharacterListViewModel(@NonNull Application application) {
        super(application);
        characterRepository = new CharacterRepository(application);

        allCharacters.addSource(getAllCharactersByNameAsc(),
                characters -> allCharacters.setValue(characters));
    }


    public MediatorLiveData<List<Character>> getAllCharacters() {
        return allCharacters;
    }

    public LiveData<List<Character>> getAllCharactersByNameAsc() {
        return characterRepository.getAllCharactersByNameAsc();
    }

    public LiveData<List<Character>> getAllCharactersByNameDesc() {
        return characterRepository.getAllCharactersByNameDesc();
    }

    public void orderBy(int sort) {
        allCharacters.removeSource(getAllCharacters());
        allCharacters.removeSource(getAllCharactersByNameAsc());
        allCharacters.removeSource(getAllCharactersByNameDesc());

        switch (sort) {
            default:
            case Sort.BY_NAME_ASC:
                allCharacters.addSource(getAllCharactersByNameAsc(),
                        characters -> allCharacters.setValue(characters));
                break;
            case Sort.BY_NAME_DESC:
                allCharacters.addSource(getAllCharactersByNameDesc(),
                        characters -> allCharacters.setValue(characters));
                break;
        }
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
