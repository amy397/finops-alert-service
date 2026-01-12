package com.finops.alert.controller;

import com.finops.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/check")
    public ResponseEntity<String> triggerCheck() {
        alertService.checkAndSendAlerts();
        return ResponseEntity.ok("Alert check triggered");
    }

    @PostMapping("/report")
    public ResponseEntity<String> triggerReport() {
        alertService.sendDailyReport();
        return ResponseEntity.ok("Daily report triggered");
    }
}
