package com.eps.meterservice.service;

import com.eps.meterservice.dto.MeterAccountRequestDTO;
import com.eps.meterservice.dto.MeterAccountResponseDTO;
import com.eps.meterservice.dto.MeterReadingRequestDTO;
import com.eps.meterservice.dto.MeterReadingResponseDTO;
import com.eps.meterservice.exception.InvalidMeterReadingException;
import com.eps.meterservice.exception.MeterAccountNotFoundException;
import com.eps.meterservice.exception.MeterReadingNotFoundException;
import com.eps.meterservice.exception.MeterSerialNumberAlreadyExistsException;
import com.eps.meterservice.mapper.MeterMapper;
import com.eps.meterservice.model.MeterAccount;
import com.eps.meterservice.model.MeterReading;
import com.eps.meterservice.repository.MeterAccountRepository;
import com.eps.meterservice.repository.MeterReadingRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeterService {

    private final MeterAccountRepository meterAccountRepository;
    private final MeterReadingRepository meterReadingRepository;

    public MeterService(MeterAccountRepository meterAccountRepository,
                        MeterReadingRepository meterReadingRepository) {
        this.meterAccountRepository = meterAccountRepository;
        this.meterReadingRepository = meterReadingRepository;
    }

    public List<MeterAccountResponseDTO> getMeterAccounts() {
        return meterAccountRepository.findAll().stream().map(MeterMapper::toDTO).toList();
    }

    public List<MeterAccountResponseDTO> getMeterAccountsByConnectionId(Long connectionId) {
        return meterAccountRepository.findByConnectionId(connectionId).stream().map(MeterMapper::toDTO).toList();
    }

    public MeterAccountResponseDTO createMeterAccount(MeterAccountRequestDTO request) {
        if (meterAccountRepository.existsByMeterSerialNumber(request.getMeterSerialNumber())) {
            throw new MeterSerialNumberAlreadyExistsException(
                "A meter with serial number " + request.getMeterSerialNumber() + " already exists");
        }

        MeterAccount newMeterAccount = meterAccountRepository.save(MeterMapper.toModel(request));
        return MeterMapper.toDTO(newMeterAccount);
    }

    public MeterAccountResponseDTO getMeterAccountById(Long id) {
        return MeterMapper.toDTO(findMeterAccount(id));
    }

    public MeterAccountResponseDTO updateMeterAccount(Long id, MeterAccountRequestDTO request) {
        MeterAccount meterAccount = findMeterAccount(id);

        if (meterAccountRepository.existsByMeterSerialNumberAndIdNot(request.getMeterSerialNumber(), id)) {
            throw new MeterSerialNumberAlreadyExistsException(
                "A meter with serial number " + request.getMeterSerialNumber() + " already exists");
        }

        meterAccount.setConnectionId(request.getConnectionId());
        meterAccount.setMeterSerialNumber(request.getMeterSerialNumber());
        meterAccount.setMeterType(request.getMeterType());
        meterAccount.setInstallationDate(request.getInstallationDate() == null
            ? LocalDate.now() : request.getInstallationDate());
        meterAccount.setStatus(request.getStatus() == null ? meterAccount.getStatus() : request.getStatus());

        return MeterMapper.toDTO(meterAccountRepository.save(meterAccount));
    }

    @Transactional
    public void deleteMeterAccount(Long id) {
        if (!meterAccountRepository.existsById(id)) {
            throw new MeterAccountNotFoundException("Meter account not found with ID: " + id);
        }
        meterReadingRepository.deleteByMeterAccountId(id);
        meterAccountRepository.deleteById(id);
    }

    public List<MeterReadingResponseDTO> getReadings(Long meterAccountId) {
        findMeterAccount(meterAccountId);
        return meterReadingRepository.findByMeterAccountIdOrderByReadingDateDesc(meterAccountId)
            .stream()
            .map(MeterMapper::toDTO)
            .toList();
    }

    public MeterReadingResponseDTO getLatestReading(Long meterAccountId) {
        findMeterAccount(meterAccountId);
        MeterReading reading = meterReadingRepository.findTopByMeterAccountIdOrderByReadingDateDesc(meterAccountId)
            .orElseThrow(() -> new MeterReadingNotFoundException(
                "No meter reading found for meter account ID: " + meterAccountId));
        return MeterMapper.toDTO(reading);
    }

    public MeterReadingResponseDTO recordMeterReading(Long meterAccountId, MeterReadingRequestDTO request) {
        findMeterAccount(meterAccountId);
        Double previousReading = resolvePreviousReading(meterAccountId, request.getPreviousReading());

        if (request.getCurrentReading() < previousReading) {
            throw new InvalidMeterReadingException("Current reading cannot be lower than previous reading");
        }

        MeterReading meterReading = new MeterReading(
            meterAccountId,
            request.getCurrentReading(),
            previousReading,
            request.getReadingDate()
        );

        return MeterMapper.toDTO(meterReadingRepository.save(meterReading));
    }

    private MeterAccount findMeterAccount(Long id) {
        return meterAccountRepository.findById(id)
            .orElseThrow(() -> new MeterAccountNotFoundException("Meter account not found with ID: " + id));
    }

    private Double resolvePreviousReading(Long meterAccountId, Double requestedPreviousReading) {
        if (requestedPreviousReading != null) {
            return requestedPreviousReading;
        }
        return meterReadingRepository.findTopByMeterAccountIdOrderByReadingDateDesc(meterAccountId)
            .map(MeterReading::getCurrentReading)
            .orElse(0.0);
    }
}
