package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.response.RentalCodeResponse;

public interface RentalCodeService {
    RentalCodeResponse generateRentalCode();

    RentalCodeResponse getRentalCodeByCode(String code);

    RentalCodeResponse getCurrentUserRentalCode();

    void markCodeAsUsed(String code);
}
