package com.assessment.queryapi.service;

import com.assessment.queryapi.dto.CallDetailRecordDto;
import com.assessment.queryapi.dto.QueryRequestDto;
import com.assessment.queryapi.model.CallDetailRecord;
import com.assessment.queryapi.repository.CallDetailRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryService {

    private final CallDetailRecordRepository repository;

    public List<CallDetailRecordDto> queryRecords(QueryRequestDto request) {
        List<CallDetailRecord> records = repository.findByDateRangeAndOptionalParams(
                request.getRecordDateStart(),
                request.getRecordDateEnd(),
                request.getMsisdn(),
                request.getImsi()
        );

        return records.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CallDetailRecordDto convertToDto(CallDetailRecord record) {
        CallDetailRecordDto dto = new CallDetailRecordDto();
        dto.setRecordDate(record.getRecordDate());
        dto.setMsisdn(record.getMsisdn());
        dto.setImsi(record.getImsi());
        return dto;
    }
}
