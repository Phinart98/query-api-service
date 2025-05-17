package com.assessment.queryapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "call_detail_records")
@Data
public class TestCallDetailRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_date", nullable = false)
    private LocalDateTime recordDate;

    @Column(name = "msisdn", length = 18)
    private String msisdn;

    @Column(name = "imsi", length = 100)
    private String imsi;
}