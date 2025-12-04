package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.models.ElectricAndWater;
import com.cntt.rentalmanagement.domain.payload.response.ElectricAndWaterResponse;

import java.util.List;

public interface ElectricAndWaterService {
    public ElectricAndWater saveElectric(ElectricAndWater electricAndWater);
    public ElectricAndWater updateElectric(ElectricAndWater electricAndWater, Long id);
    public List<ElectricAndWaterResponse> getElectricByRoom(Long id);
    public ElectricAndWaterResponse getElectricAndWater(Long id);
}
