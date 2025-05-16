package com.assessment.queryapi.repository;

import com.assessment.queryapi.model.CallDetailRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CallDetailRecordRepository extends JpaRepository<CallDetailRecord, Long> {

    // Custom query to handle all filter combinations in one method
    @Query("SELECT c FROM CallDetailRecord c WHERE c.recordDate BETWEEN :startDate AND :endDate " +
            "AND (:msisdn IS NULL OR c.msisdn = :msisdn) " +
            "AND (:imsi IS NULL OR c.imsi = :imsi)")
    List<CallDetailRecord> findByDateRangeAndOptionalParams(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("msisdn") String msisdn,
            @Param("imsi") String imsi);
}
