package com.renthome.renthome_backend.repository;

import com.renthome.renthome_backend.entity.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    long countByEventType(String eventType);

    List<Analytics> findTop50ByOrderByCreatedAtDesc();

    @Query("""
        SELECT a.propertyId, COUNT(a)
        FROM Analytics a
        WHERE a.eventType = 'VIEW'
        GROUP BY a.propertyId
        ORDER BY COUNT(a) DESC
    """)
    List<Object[]> mostViewedProperty();
}