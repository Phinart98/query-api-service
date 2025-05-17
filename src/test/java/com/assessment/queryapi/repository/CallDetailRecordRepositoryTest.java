package com.assessment.queryapi.repository;

import com.assessment.queryapi.model.CallDetailRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class CallDetailRecordRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CallDetailRecordRepository repository;

    @Test
    void testFindByDateRangeAndOptionalParams() {
        // Create test data with minimal required fields
        LocalDateTime now = LocalDateTime.now();

        CallDetailRecord record1 = new CallDetailRecord();
        record1.setRecordDate(now);
        record1.setMsisdn("123456789");
        record1.setImsi("987654321");
        entityManager.persist(record1);

        CallDetailRecord record2 = new CallDetailRecord();
        record2.setRecordDate(now.plusHours(1));
        record2.setMsisdn("123456789");
        record2.setImsi("111111111");
        entityManager.persist(record2);

        CallDetailRecord record3 = new CallDetailRecord();
        record3.setRecordDate(now.plusHours(2));
        record3.setMsisdn("999999999");
        record3.setImsi("987654321");
        entityManager.persist(record3);

        entityManager.flush();

        // Test with date range only
        List<CallDetailRecord> results1 = repository.findByDateRangeAndOptionalParams(
                now.minusHours(1),
                now.plusHours(3),
                null,
                null
        );
        assertEquals(3, results1.size());

        // Test with msisdn filter
        List<CallDetailRecord> results2 = repository.findByDateRangeAndOptionalParams(
                now.minusHours(1),
                now.plusHours(3),
                "123456789",
                null
        );
        assertEquals(2, results2.size());

        // Test with imsi filter
        List<CallDetailRecord> results3 = repository.findByDateRangeAndOptionalParams(
                now.minusHours(1),
                now.plusHours(3),
                null,
                "987654321"
        );
        assertEquals(2, results3.size());

        // Test with both filters
        List<CallDetailRecord> results4 = repository.findByDateRangeAndOptionalParams(
                now.minusHours(1),
                now.plusHours(3),
                "123456789",
                "987654321"
        );
        assertEquals(1, results4.size());
    }
}
