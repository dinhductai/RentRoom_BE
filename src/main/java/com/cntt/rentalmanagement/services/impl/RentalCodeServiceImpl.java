package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.models.RentalCode;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.response.RentalCodeResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.RentalCodeRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.services.RentalCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RentalCodeServiceImpl implements RentalCodeService {

    private final RentalCodeRepository rentalCodeRepository;
    private final UserRepository userRepository;

    @Override
    public RentalCodeResponse generateRentalCode() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException("User không tồn tại"));

        // Tạo mã mới mỗi lần gọi (không kiểm tra mã cũ)
        String code = generateUniqueCode();
        RentalCode rentalCode = new RentalCode();
        rentalCode.setCode(code);
        rentalCode.setUser(user);
        rentalCode.setIsUsed(false);
        rentalCode.setExpiredAt(LocalDateTime.now().plusMinutes(5)); // Hết hạn sau 5 phút

        RentalCode saved = rentalCodeRepository.save(rentalCode);
        return mapToResponse(saved);
    }

    @Override
    public RentalCodeResponse getRentalCodeByCode(String code) {
        RentalCode rentalCode = rentalCodeRepository.findByCode(code)
                .orElseThrow(() -> new BadRequestException("Mã thuê không tồn tại hoặc đã hết hạn"));

        if (rentalCode.getIsUsed()) {
            throw new BadRequestException("Mã thuê đã được sử dụng");
        }

        if (rentalCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Mã thuê đã hết hạn");
        }

        return mapToResponse(rentalCode);
    }

    @Override
    public RentalCodeResponse getCurrentUserRentalCode() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException("User không tồn tại"));

        return rentalCodeRepository.findByUserAndIsUsedFalse(user)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    public void markCodeAsUsed(String code) {
        RentalCode rentalCode = rentalCodeRepository.findByCode(code)
                .orElseThrow(() -> new BadRequestException("Mã thuê không tồn tại"));

        rentalCode.setIsUsed(true);
        rentalCode.setUsedAt(LocalDateTime.now());
        rentalCodeRepository.save(rentalCode);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (rentalCodeRepository.findByCode(code).isPresent());
        return code;
    }

    private String generateCode() {
        // Tạo mã gồm 5 chữ cái và 5 số
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        // 5 chữ cái
        for (int i = 0; i < 5; i++) {
            code.append(letters.charAt(random.nextInt(letters.length())));
        }

        // 5 số
        for (int i = 0; i < 5; i++) {
            code.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        return code.toString();
    }

    private RentalCodeResponse mapToResponse(RentalCode rentalCode) {
        RentalCodeResponse response = new RentalCodeResponse();
        response.setCode(rentalCode.getCode());
        response.setUserName(rentalCode.getUser().getName());
        response.setUserEmail(rentalCode.getUser().getEmail());
        response.setUserPhone(rentalCode.getUser().getPhone());
        response.setUserAddress(rentalCode.getUser().getAddress());
        response.setIsUsed(rentalCode.getIsUsed());
        response.setExpiredAt(rentalCode.getExpiredAt());
        response.setCreatedAt(rentalCode.getCreatedAt());
        return response;
    }
}
