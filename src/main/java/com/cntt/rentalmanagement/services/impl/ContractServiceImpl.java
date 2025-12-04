package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.enums.LockedStatus;
import com.cntt.rentalmanagement.domain.enums.RoomStatus;
import com.cntt.rentalmanagement.domain.models.Contract;
import com.cntt.rentalmanagement.domain.models.RentalCode;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.response.ContractResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.ContractRepository;
import com.cntt.rentalmanagement.repository.RentalCodeRepository;
import com.cntt.rentalmanagement.repository.RoomRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.ContractService;
import com.cntt.rentalmanagement.services.FileStorageService;
import com.cntt.rentalmanagement.services.RentalCodeService;
import com.cntt.rentalmanagement.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl extends BaseService implements ContractService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;
    private final FileStorageService fileStorageService;
    private final MapperUtils mapperUtils;
    private final RentalCodeRepository rentalCodeRepository;
    private final RentalCodeService rentalCodeService;
    private final UserRepository userRepository;

    @Override
    public MessageResponse addContract(String name, Long roomId, String nameRentHome, Long numOfPeople, String phone,
            String deadline, List<MultipartFile> files) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException("Phòng đã không tồn tại"));
        if (room.getIsLocked().equals(LockedStatus.DISABLE)) {
            throw new BadRequestException("Phòng đã bị khóa");
        }

        String fileName = fileStorageService.storeFile(files.get(0));
        Contract contract = new Contract(name, fileName, nameRentHome, deadline, getUsername(), getUsername(), room);
        contract.setPhone(phone);
        contract.setNumOfPeople(numOfPeople);

        // Tìm user theo phone và liên kết với contract
        userRepository.findByPhone(phone).ifPresent(contract::setRenterUser);

        contractRepository.save(contract);

        room.setStatus(RoomStatus.HIRED);
        roomRepository.save(room);
        return MessageResponse.builder().message("Thêm hợp đồng mới thành công").build();
    }

    @Override
    public MessageResponse addContractWithRentalCode(String name, Long roomId, String rentalCode, String deadline,
            List<MultipartFile> files) {
        // Verify rental code
        RentalCode code = rentalCodeRepository.findByCode(rentalCode)
                .orElseThrow(() -> new BadRequestException("Mã thuê không tồn tại"));

        if (code.getIsUsed()) {
            throw new BadRequestException("Mã thuê đã được sử dụng");
        }

        if (code.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Mã thuê đã hết hạn");
        }

        User renterUser = code.getUser();

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException("Phòng đã không tồn tại"));
        if (room.getIsLocked().equals(LockedStatus.DISABLE)) {
            throw new BadRequestException("Phòng đã bị khóa");
        }

        String fileName = fileStorageService.storeFile(files.get(0));
        Contract contract = new Contract();
        contract.setName(name);
        contract.setFiles(fileName);
        contract.setNameOfRent(renterUser.getName());
        contract.setPhone(renterUser.getPhone());
        contract.setDeadlineContract(LocalDateTime.parse(deadline));
        contract.setCreatedBy(getUsername());
        contract.setUpdatedBy(getUsername());
        contract.setRoom(room);
        contract.setRentalCode(rentalCode);
        contract.setRenterUser(renterUser);
        contractRepository.save(contract);

        // Mark rental code as used
        code.setIsUsed(true);
        code.setUsedAt(LocalDateTime.now());
        rentalCodeRepository.save(code);

        room.setStatus(RoomStatus.HIRED);
        roomRepository.save(room);
        return MessageResponse.builder().message("Thêm hợp đồng mới thành công").build();
    }

    @Override
    public Page<ContractResponse> getAllContractOfRentaler(String name, String phone, Integer pageNo,
            Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(
                contractRepository.searchingContact(name, phone, getUserId(), pageable), ContractResponse.class,
                pageable);
    }

    @Override
    public ContractResponse getContractById(Long id) {
        return mapperUtils.convertToResponse(
                contractRepository.findById(id).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!")),
                ContractResponse.class);
    }

    @Override
    public MessageResponse editContractInfo(Long id, String name, Long roomId, String nameOfRent, Long numOfPeople,
            String phone, String deadlineContract, List<MultipartFile> files) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException("Phòng đã không tồn tại"));
        if (room.getIsLocked().equals(LockedStatus.DISABLE)) {
            throw new BadRequestException("Phòng đã bị khóa");
        }

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
        contract.setDeadlineContract(LocalDateTime.parse(deadlineContract));
        contract.setRoom(room);
        contract.setName(name);
        contract.setPhone(phone);
        contract.setNumOfPeople(numOfPeople);
        if (Objects.nonNull(files.get(0))) {
            String fileName = fileStorageService.storeFile(files.get(0));
            contract.setFiles(fileName);
        }
        contract.setNameOfRent(nameOfRent);

        // Tìm user theo phone và cập nhật liên kết với contract
        userRepository.findByPhone(phone).ifPresent(contract::setRenterUser);

        contractRepository.save(contract);
        return MessageResponse.builder().message("Cập nhật hợp đồng thành công.").build();
    }

    @Override
    public Page<ContractResponse> getAllContractOfCustomer(String phone, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Contract> contracts = contractRepository.searchingContact(phone, pageable);

        // Xử lý fallback phone: nếu contract không có phone, lấy từ renterUser
        contracts.getContent().forEach(contract -> {
            if (contract.getPhone() == null || contract.getPhone().isEmpty()) {
                // Query phone từ user table dựa trên renter_user_id
                // Lấy renter_user_id từ contract (có thể từ getRenterUser().getId() hoặc từ
                // database)
                if (contract.getRenterUser() != null && contract.getRenterUser().getId() != null) {
                    userRepository.findById(contract.getRenterUser().getId()).ifPresent(user -> {
                        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                            contract.setPhone(user.getPhone());
                        }
                    });
                } else {
                    // Nếu renterUser chưa được load, query trực tiếp từ database
                    // Tìm contract trong database để lấy renter_user_id
                    contractRepository.findById(contract.getId()).ifPresent(dbContract -> {
                        if (dbContract.getRenterUser() != null) {
                            userRepository.findById(dbContract.getRenterUser().getId()).ifPresent(user -> {
                                if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                                    contract.setPhone(user.getPhone());
                                }
                            });
                        }
                    });
                }
            }
        });

        return mapperUtils.convertToResponsePage(contracts, ContractResponse.class, pageable);
    }

}
