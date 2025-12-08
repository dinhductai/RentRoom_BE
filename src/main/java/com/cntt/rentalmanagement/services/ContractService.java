package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.response.ContractResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractService {
    MessageResponse addContract(String name, Long roomId, String nameRentHome, Long numOfPeople, String phone,
            String deadline, List<MultipartFile> files);

    MessageResponse addContractWithRentalCode(String name, Long roomId, String rentalCode, String deadline,
            List<MultipartFile> files);

    Page<ContractResponse> getAllContractOfRentaler(String name, String phone, Integer pageNo, Integer pageSize);

    ContractResponse getContractById(Long id);

    MessageResponse editContractInfo(Long id, String name, Long roomId, String nameOfRent, Long numOfPeople,
            String phone, String deadlineContract, List<MultipartFile> files);

    Page<ContractResponse> getAllContractOfCustomer(Integer pageNo, Integer pageSize);
}
