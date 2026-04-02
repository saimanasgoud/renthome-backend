package com.renthome.renthome_backend.controller;

import com.renthome.renthome_backend.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;
import com.renthome.renthome_backend.entity.Analytics;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin
public class AnalyticsController {

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    // Record property page view
    @PostMapping("/view/{propertyId}")
    public void recordView(@PathVariable Long propertyId) {
        service.recordView(propertyId);
    }

    // Record QR scan
    @PostMapping("/scan/{propertyId}")
    public void recordScan(@PathVariable Long propertyId) {
        service.recordQrScan(propertyId);
    }

    // Get analytics stats
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return service.getStats();
    }

    
    @GetMapping("/history")
    public List<Analytics> getHistory() {
        return service.getHistory();
    }
    
}