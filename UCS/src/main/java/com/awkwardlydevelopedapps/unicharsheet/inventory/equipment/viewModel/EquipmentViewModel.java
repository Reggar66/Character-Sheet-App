package com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.model.Equipment;
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.repository.EquipmentRepository;

import java.util.List;

public class EquipmentViewModel extends ViewModel {

    private EquipmentRepository equipmentRepository;
    private LiveData<List<Equipment>> allEquipment;
    private List<Equipment> equipmentList;

    public EquipmentViewModel(Application application, int charId) {
        // Repository specific for given character (charID)
        equipmentRepository = new EquipmentRepository(application, charId);

        allEquipment = equipmentRepository.getAllEquipment();
    }


    public LiveData<List<Equipment>> getAllEquipment() {
        return allEquipment;
    }

    public void insertEq(Equipment equipment) {
        equipmentRepository.insertEquipment(equipment);
    }

    public void updateEquipment(String name, String type, String value, String additionalEffects, String slot, int charId) {
        equipmentRepository.updateEquipment(name, type, value, additionalEffects, slot, charId);
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }


    /**
     * Factory
     */
    public static class EquipmentViewModelFactory implements ViewModelProvider.Factory {

        Application application;
        int charId;

        public EquipmentViewModelFactory(Application application, int charId) {
            this.application = application;
            this.charId = charId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new EquipmentViewModel(application, charId);
        }
    }
}
