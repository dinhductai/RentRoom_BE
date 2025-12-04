package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.request.TotalNumberRequest;
import com.cntt.rentalmanagement.domain.payload.response.CostResponse;
import com.cntt.rentalmanagement.domain.payload.response.RevenueResponse;
import com.cntt.rentalmanagement.domain.payload.response.TotalNumberResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StatisticalService {
    TotalNumberRequest getNumberOfRentalerForStatistical();

    TotalNumberResponse getStatisticalNumberOfAdmin();

    Page<RevenueResponse> getByMonth();

    Page<CostResponse> getByCost();
}
