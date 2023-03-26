package com.gpmrks.testebackendestagiov3.equipment_state.service.impl;

import com.gpmrks.testebackendestagiov3.equipment_state.dto.EquipmentStateForm;
import com.gpmrks.testebackendestagiov3.equipment_state.entity.EquipmentState;
import com.gpmrks.testebackendestagiov3.equipment_state.exception.CannotDeleteEquipmentStateException;
import com.gpmrks.testebackendestagiov3.equipment_state.exception.EquipmentStateNotFoundException;
import com.gpmrks.testebackendestagiov3.equipment_state.repository.EquipmentStateRepository;
import com.gpmrks.testebackendestagiov3.equipment_state.service.EquipmentStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EquipmentStateServiceImpl implements EquipmentStateService {

    private EquipmentStateRepository equipmentStateRepository;

    @Autowired
    public EquipmentStateServiceImpl(EquipmentStateRepository equipmentStateRepository) {
        this.equipmentStateRepository = equipmentStateRepository;
    }

    @Override
    public List<EquipmentState> getAllEquipmentsStates() {
        return equipmentStateRepository.findAll();
    }

    @Override
    public EquipmentState getEquipmentStateById(UUID id) {
        return checkIfEquipmentStateExists(id);
    }

    @Override
    public EquipmentState createEquipmentState(EquipmentStateForm equipmentStateToCreate) {
        EquipmentState equipmentState = new EquipmentState();
        equipmentState.setName(equipmentStateToCreate.getName());
        equipmentState.setColor(equipmentStateToCreate.getColor());
        equipmentStateRepository.save(equipmentState);
        return equipmentState;
    }

    @Override
    public EquipmentState updateEquipmentState(UUID id, EquipmentStateForm updatedEquipmentState) {
        EquipmentState equipmentState = checkIfEquipmentStateExists(id);
        equipmentState.setName(updatedEquipmentState.getName());
        equipmentState.setColor(updatedEquipmentState.getColor());
        equipmentStateRepository.save(equipmentState);
        return equipmentState;
    }

    @Override
    public void deleteEquipmentState(UUID id) {
        try {
            checkIfEquipmentStateExists(id);
            equipmentStateRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new CannotDeleteEquipmentStateException(id);
        }
    }

    private EquipmentState checkIfEquipmentStateExists(UUID id) {

        Optional<EquipmentState> optionalEquipmentState = equipmentStateRepository.findById(id);
        final EquipmentState equipmentState;

        if (optionalEquipmentState.isPresent()) {
            equipmentState = optionalEquipmentState.get();
        } else {
            throw new EquipmentStateNotFoundException(id);
        }

        return equipmentState;
    }
}