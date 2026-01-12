package com.finops.alert.scheduler;

import com.finops.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertScheduler {

    private final AlertService alertService;

    @Scheduled(cron = "${alert.schedule.check-cron:0 0 * * * *}")
    public void checkAlerts() {
        log.info("스케줄 - 비용 알림 체크");
        alertService.checkAndSendAlerts();
    }

    @Scheduled(cron = "${alert.schedule.report-cron:0 0 10 * * *}")
    public void sendDailyReport() {
        log.info("스케줄 - 일일 리포트");
        alertService.sendDailyReport();
    }

    @Scheduled(initialDelay = 30000, fixedDelay = Long.MAX_VALUE)
    public void initialCheck() {
        log.info("초기 알림 체크");
        alertService.checkAndSendAlerts();
    }
}
