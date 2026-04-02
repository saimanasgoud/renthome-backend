package com.renthome.renthome_backend.service;

import com.renthome.renthome_backend.entity.Analytics;
import com.renthome.renthome_backend.repository.AnalyticsRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final AnalyticsRepository repo;

    public AnalyticsService(AnalyticsRepository repo) {
        this.repo = repo;
    }

    // Record property page view
    public void recordView(Long propertyId) {

        Analytics analytics = new Analytics();
        analytics.setPropertyId(propertyId);
        analytics.setEventType("VIEW");

        repo.save(analytics);
    }

    // Record QR scan
    public void recordQrScan(Long propertyId) {

        Analytics analytics = new Analytics();
        analytics.setPropertyId(propertyId);
        analytics.setEventType("QR_SCAN");

        repo.save(analytics);
    }

    // Get analytics stats
    public Map<String, Object> getStats() {

        long totalViews = repo.countByEventType("VIEW");
        long totalQrScans = repo.countByEventType("QR_SCAN");

        List<Object[]> mostViewed = repo.mostViewedProperty();

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalViews", totalViews);
        stats.put("totalQrScans", totalQrScans);

        if (!mostViewed.isEmpty()) {
            stats.put("mostViewedPropertyId", mostViewed.get(0)[0]);
            stats.put("mostViewedCount", mostViewed.get(0)[1]);
        }

        return stats;
    }

    public List<Analytics> getHistory() {
        return repo.findTop50ByOrderByCreatedAtDesc();
    }
}