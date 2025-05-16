package com.assessment.queryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "call_detail_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallDetailRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RECORD_DATE", nullable = false)
    private LocalDateTime recordDate;

    @Column(name = "MSISDN", length = 18)
    private String msisdn;

    @Column(name = "IMSI", length = 100)
    private String imsi;

    // I only included the fields we need for the query API
    // The rest of the fields are omitted for simplicity
}
