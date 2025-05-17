package com.assessment.queryapi.controller;

import com.assessment.queryapi.dto.CallDetailRecordDto;
import com.assessment.queryapi.dto.QueryRequestDto;
import com.assessment.queryapi.service.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryService queryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void queryRecords_shouldReturnResults() throws Exception {
        // Create test data
        LocalDateTime startDate = LocalDateTime.of(2023, 8, 18, 10, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 8, 18, 10, 1, 0);

        QueryRequestDto requestDto = new QueryRequestDto();
        requestDto.setRecordDateStart(startDate);
        requestDto.setRecordDateEnd(endDate);
        requestDto.setMsisdn("573228550000");
        requestDto.setImsi("732101647793504");

        // Create mock results
        CallDetailRecordDto dto1 = new CallDetailRecordDto();
        dto1.setRecordDate(LocalDateTime.of(2023, 8, 18, 10, 0, 30));
        dto1.setMsisdn("573228550000");
        dto1.setImsi("732101647793504");

        CallDetailRecordDto dto2 = new CallDetailRecordDto();
        dto2.setRecordDate(LocalDateTime.of(2023, 8, 18, 10, 0, 45));
        dto2.setMsisdn("573228550000");
        dto2.setImsi("732101647793504");

        List<CallDetailRecordDto> mockResults = Arrays.asList(dto1, dto2);

        // Mock service response
        when(queryService.queryRecords(any(QueryRequestDto.class))).thenReturn(mockResults);

        // Perform test
        mockMvc.perform(post("/api/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].msisdn").value("573228550000"))
                .andExpect(jsonPath("$[0].imsi").value("732101647793504"))
                .andExpect(jsonPath("$[1].msisdn").value("573228550000"))
                .andExpect(jsonPath("$[1].imsi").value("732101647793504"));
    }

    @Test
    void queryRecords_withMissingRequiredFields_shouldReturnBadRequest() throws Exception {
        // Create invalid request (missing required fields)
        QueryRequestDto invalidRequest = new QueryRequestDto();

        // Perform test
        mockMvc.perform(post("/api/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
