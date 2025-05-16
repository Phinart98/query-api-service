package com.assessment.queryapi.controller;

import com.assessment.queryapi.dto.CallDetailRecordDto;
import com.assessment.queryapi.dto.QueryRequestDto;
import com.assessment.queryapi.service.QueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/query")
@RequiredArgsConstructor
public class QueryController {

    private final QueryService queryService;

    @PostMapping
    public ResponseEntity<List<CallDetailRecordDto>> queryRecords(
            @Valid @RequestBody QueryRequestDto request) {

        List<CallDetailRecordDto> results = queryService.queryRecords(request);
        return ResponseEntity.ok(results);
    }
}
