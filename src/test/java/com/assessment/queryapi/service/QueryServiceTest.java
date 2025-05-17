package com.assessment.queryapi.service;

import com.assessment.queryapi.dto.CallDetailRecordDto;
import com.assessment.queryapi.dto.QueryRequestDto;
import com.assessment.queryapi.model.CallDetailRecord;
import com.assessment.queryapi.repository.CallDetailRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryServiceTest {

    @Mock
    private CallDetailRecordRepository repository;

    @InjectMocks
    private QueryService queryService;

    private QueryRequestDto requestDto;
    private List<CallDetailRecord> mockRecords;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Helper method to set fields via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Error setting field " + fieldName, e);
        }
    }

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2023, 8, 18, 10, 0, 0);
        endDate = LocalDateTime.of(2023, 8, 18, 10, 1, 0);

        requestDto = new QueryRequestDto();
        requestDto.setRecordDateStart(startDate);
        requestDto.setRecordDateEnd(endDate);
        requestDto.setMsisdn("573228550000");
        requestDto.setImsi("732101647793504");

        // Create mock records using reflection
        CallDetailRecord record1 = new CallDetailRecord();
        setField(record1, "id", 1L);
        setField(record1, "recordDate", LocalDateTime.of(2023, 8, 18, 10, 0, 30));
        setField(record1, "msisdn", "573228550000");
        setField(record1, "imsi", "732101647793504");

        CallDetailRecord record2 = new CallDetailRecord();
        setField(record2, "id", 2L);
        setField(record2, "recordDate", LocalDateTime.of(2023, 8, 18, 10, 0, 45));
        setField(record2, "msisdn", "573228550000");
        setField(record2, "imsi", "732101647793504");

        mockRecords = Arrays.asList(record1, record2);
    }

    @Test
    void queryRecords_shouldReturnMappedDtos() {
        // Given
        when(repository.findByDateRangeAndOptionalParams(
                eq(startDate),
                eq(endDate),
                eq("573228550000"),
                eq("732101647793504")
        )).thenReturn(mockRecords);

        // When
        List<CallDetailRecordDto> result = queryService.queryRecords(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first record mapping
        CallDetailRecordDto dto1 = result.get(0);
        assertEquals(LocalDateTime.of(2023, 8, 18, 10, 0, 30), dto1.getRecordDate());
        assertEquals("573228550000", dto1.getMsisdn());
        assertEquals("732101647793504", dto1.getImsi());

        // Verify second record mapping
        CallDetailRecordDto dto2 = result.get(1);
        assertEquals(LocalDateTime.of(2023, 8, 18, 10, 0, 45), dto2.getRecordDate());
        assertEquals("573228550000", dto2.getMsisdn());
        assertEquals("732101647793504", dto2.getImsi());

        // Verify repository was called with correct parameters
        verify(repository).findByDateRangeAndOptionalParams(
                eq(startDate),
                eq(endDate),
                eq("573228550000"),
                eq("732101647793504")
        );
    }

    @Test
    void queryRecords_withNullOptionalParams_shouldPassNullsToRepository() {
        // Given
        QueryRequestDto requestWithoutOptionals = new QueryRequestDto();
        requestWithoutOptionals.setRecordDateStart(startDate);
        requestWithoutOptionals.setRecordDateEnd(endDate);
        // msisdn and imsi are null

        when(repository.findByDateRangeAndOptionalParams(
                eq(startDate),
                eq(endDate),
                eq(null),
                eq(null)
        )).thenReturn(mockRecords);

        // When
        List<CallDetailRecordDto> result = queryService.queryRecords(requestWithoutOptionals);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify repository was called with correct parameters
        verify(repository).findByDateRangeAndOptionalParams(
                eq(startDate),
                eq(endDate),
                eq(null),
                eq(null)
        );
    }

    @Test
    void queryRecords_withEmptyResults_shouldReturnEmptyList() {
        // Given
        when(repository.findByDateRangeAndOptionalParams(
                any(), any(), any(), any()
        )).thenReturn(List.of());

        // When
        List<CallDetailRecordDto> result = queryService.queryRecords(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
