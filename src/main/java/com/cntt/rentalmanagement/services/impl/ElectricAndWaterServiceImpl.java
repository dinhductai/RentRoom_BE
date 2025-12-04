package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.models.ElectricAndWater;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.domain.payload.response.ElectricAndWaterResponse;
import com.cntt.rentalmanagement.repository.ElectricAndWaterRepository;
import com.cntt.rentalmanagement.services.ElectricAndWaterService;
import com.cntt.rentalmanagement.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ElectricAndWaterServiceImpl implements ElectricAndWaterService {
    @Autowired
    private ElectricAndWaterRepository electricAndWaterRepository;
    @Autowired
    private RoomService roomService;

    @Override
    @Transactional
    public ElectricAndWater saveElectric(ElectricAndWater electricAndWater) {
        electricAndWater.setPaid(false);
        int deviatedBlock = electricAndWater.getThisMonthBlockOfWater() - electricAndWater.getLastMonthBlockOfWater();
        int deviatedNumber = electricAndWater.getThisMonthNumberOfElectric() - electricAndWater.getLastMonthNumberOfElectric();
        BigDecimal totalMoneyOfWater = deviatedBlock > 0 ? electricAndWater.getMoneyEachBlockOfWater().multiply(BigDecimal.valueOf(deviatedBlock)) : BigDecimal.ZERO;
        BigDecimal totalMoneyOfElectric = deviatedNumber > 0 ? electricAndWater.getMoneyEachNumberOfElectric().multiply(BigDecimal.valueOf(deviatedNumber)) : BigDecimal.ZERO;
        electricAndWater.setTotalMoneyOfWater(totalMoneyOfWater);
        electricAndWater.setTotalMoneyOfElectric(totalMoneyOfElectric);

        Room room = electricAndWater.getRoom();
        room.setPublicElectricCost(totalMoneyOfElectric);
        room.setWaterCost(totalMoneyOfWater);
        roomService.updateRoom(room, room.getId());
        return electricAndWaterRepository.save(electricAndWater);
    }

    @Override
    @Transactional
    public ElectricAndWater updateElectric(ElectricAndWater electricAndWater, Long id) {
        return electricAndWaterRepository.findById(id)
            .map(electricAndWater1 -> {
                int deviatedBlock = electricAndWater.getThisMonthBlockOfWater() - electricAndWater.getLastMonthBlockOfWater();
                int deviatedNumber = electricAndWater.getThisMonthNumberOfElectric() - electricAndWater.getLastMonthNumberOfElectric();
                BigDecimal totalMoneyOfWater = deviatedBlock > 0 ? electricAndWater.getMoneyEachBlockOfWater().multiply(BigDecimal.valueOf(deviatedBlock)) : BigDecimal.ZERO;
                BigDecimal totalMoneyOfElectric = deviatedNumber > 0 ? electricAndWater.getMoneyEachNumberOfElectric().multiply(BigDecimal.valueOf(deviatedNumber)) : BigDecimal.ZERO;

                electricAndWater1.setRoom(electricAndWater.getRoom());
                electricAndWater1.setMonth(electricAndWater.getMonth());
                electricAndWater1.setName(electricAndWater.getName());

                electricAndWater1.setLastMonthBlockOfWater(electricAndWater.getLastMonthBlockOfWater());
                electricAndWater1.setThisMonthBlockOfWater(electricAndWater.getThisMonthBlockOfWater());
                electricAndWater1.setMoneyEachBlockOfWater(electricAndWater.getMoneyEachBlockOfWater());
                electricAndWater1.setTotalMoneyOfWater(totalMoneyOfWater);

                electricAndWater1.setLastMonthNumberOfElectric(electricAndWater.getLastMonthNumberOfElectric());
                electricAndWater1.setThisMonthNumberOfElectric(electricAndWater.getThisMonthNumberOfElectric());
                electricAndWater1.setMoneyEachNumberOfElectric(electricAndWater.getMoneyEachNumberOfElectric());
                electricAndWater1.setTotalMoneyOfElectric(totalMoneyOfElectric);
                electricAndWater1.setPaid(electricAndWater.isPaid());

                Room room = electricAndWater.getRoom();
                room.setPublicElectricCost(totalMoneyOfElectric);
                room.setWaterCost(totalMoneyOfWater);
                roomService.updateRoom(room, room.getId());
                return electricAndWaterRepository.save(electricAndWater1);
            })
            .orElseThrow(() -> new RuntimeException("Electric not found with id " + id));
    }

    @Override
    public List<ElectricAndWaterResponse> getElectricByRoom(Long id) {
        return electricAndWaterRepository.findByRoomId(id).stream().map(electricAndWater -> {
            ElectricAndWaterResponse electricAndWaterResponse = new ElectricAndWaterResponse();
            electricAndWaterResponse.setId(electricAndWater.getId());
            electricAndWaterResponse.setName(electricAndWater.getName());
            electricAndWaterResponse.setMonth(electricAndWater.getMonth());
            electricAndWaterResponse.setLastMonthBlockOfWater(electricAndWater.getLastMonthBlockOfWater());
            electricAndWaterResponse.setThisMonthBlockOfWater(electricAndWater.getThisMonthBlockOfWater());
            electricAndWaterResponse.setMoneyEachBlockOfWater(electricAndWater.getMoneyEachBlockOfWater());
            electricAndWaterResponse.setTotalMoneyOfWater(electricAndWater.getTotalMoneyOfWater());

            electricAndWaterResponse.setLastMonthNumberOfElectric(electricAndWater.getLastMonthNumberOfElectric());
            electricAndWaterResponse.setThisMonthNumberOfElectric(electricAndWater.getThisMonthNumberOfElectric());
            electricAndWaterResponse.setMoneyEachNumberOfElectric(electricAndWater.getMoneyEachNumberOfElectric());
            electricAndWaterResponse.setTotalMoneyOfElectric(electricAndWater.getTotalMoneyOfElectric());

            electricAndWaterResponse.setRoom(roomService.getRoomById(electricAndWater.getRoom().getId()));
            electricAndWaterResponse.setPaid(electricAndWater.isPaid());

            return electricAndWaterResponse;
        }).toList();
    }

    @Override
    public ElectricAndWaterResponse getElectricAndWater(Long id) {
        return electricAndWaterRepository.findById(id)
            .map(electricAndWater -> {
                ElectricAndWaterResponse electricAndWaterResponse = new ElectricAndWaterResponse();
                electricAndWaterResponse.setId(electricAndWater.getId());
                electricAndWaterResponse.setName(electricAndWater.getName());
                electricAndWaterResponse.setMonth(electricAndWater.getMonth());
                electricAndWaterResponse.setLastMonthBlockOfWater(electricAndWater.getLastMonthBlockOfWater());
                electricAndWaterResponse.setThisMonthBlockOfWater(electricAndWater.getThisMonthBlockOfWater());
                electricAndWaterResponse.setMoneyEachBlockOfWater(electricAndWater.getMoneyEachBlockOfWater());
                electricAndWaterResponse.setTotalMoneyOfWater(electricAndWater.getTotalMoneyOfWater());

                electricAndWaterResponse.setLastMonthNumberOfElectric(electricAndWater.getLastMonthNumberOfElectric());
                electricAndWaterResponse.setThisMonthNumberOfElectric(electricAndWater.getThisMonthNumberOfElectric());
                electricAndWaterResponse.setMoneyEachNumberOfElectric(electricAndWater.getMoneyEachNumberOfElectric());
                electricAndWaterResponse.setTotalMoneyOfElectric(electricAndWater.getTotalMoneyOfElectric());

                electricAndWaterResponse.setRoom(roomService.getRoomById(electricAndWater.getRoom().getId()));
                electricAndWaterResponse.setPaid(electricAndWater.isPaid());
                return electricAndWaterResponse;
            })
            .orElseThrow(() -> new RuntimeException("Electric not found with id " + id));
    }
}
