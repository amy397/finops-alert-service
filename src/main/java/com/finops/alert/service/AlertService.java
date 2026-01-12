package com.finops.alert.service;

import com.finops.alert.client.CostApiClient;
import com.finops.alert.config.AlertConfig;
import com.finops.alert.config.SlackConfig;
import com.finops.alert.dto.CostSummaryDto;
import com.finops.alert.dto.SlackMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final CostApiClient costApiClient;
    private final SlackNotificationService slackNotificationService;
    private final AlertConfig alertConfig;
    private final SlackConfig slackConfig;

    public void checkAndSendAlerts() {
        log.info("비용 알림 체크 시작");
        try {
            CostSummaryDto summary = costApiClient.getCostSummary();
            List<SlackMessage.Attachment> alerts = new ArrayList<>();

            checkDailyIncrease(summary, alerts);
            checkMonthlyBudget(summary, alerts);

            if (!alerts.isEmpty()) {
                sendAlerts(alerts);
            }
        } catch (Exception e) {
            log.error("알림 체크 중 오류", e);
        }
    }

    private void checkDailyIncrease(CostSummaryDto summary, List<SlackMessage.Attachment> alerts) {
        if (summary.yesterdayCost() == null || summary.yesterdayCost().compareTo(BigDecimal.ZERO) == 0) return;

        BigDecimal increase = summary.todayCost().subtract(summary.yesterdayCost())
                .multiply(BigDecimal.valueOf(100))
                .divide(summary.yesterdayCost(), 2, RoundingMode.HALF_UP);

        if (increase.compareTo(alertConfig.getDailyIncreasePercent()) > 0) {
            alerts.add(SlackMessage.Attachment.builder()
                    .color("warning")
                    .title(":warning: 일일 비용 급증")
                    .text(String.format("전일 대비 %.2f%% 증가", increase))
                    .fields(List.of(
                            new SlackMessage.Field("어제", String.format("$%.2f", summary.yesterdayCost()), true),
                            new SlackMessage.Field("오늘", String.format("$%.2f", summary.todayCost()), true)))
                    .ts(Instant.now().getEpochSecond())
                    .build());
        }
    }

    private void checkMonthlyBudget(CostSummaryDto summary, List<SlackMessage.Attachment> alerts) {
        if (summary.currentMonthCost().compareTo(alertConfig.getMonthlyBudget()) > 0) {
            alerts.add(SlackMessage.Attachment.builder()
                    .color("danger")
                    .title(":rotating_light: 월간 예산 초과")
                    .text(String.format("예산 $%.2f 초과!", alertConfig.getMonthlyBudget()))
                    .fields(List.of(
                            new SlackMessage.Field("현재 비용", String.format("$%.2f", summary.currentMonthCost()), true)))
                    .ts(Instant.now().getEpochSecond())
                    .build());
        }
    }

    private void sendAlerts(List<SlackMessage.Attachment> alerts) {
        slackNotificationService.sendMessage(SlackMessage.builder()
                .channel(slackConfig.getChannel())
                .username(slackConfig.getUsername())
                .icon_emoji(slackConfig.getIconEmoji())
                .attachments(alerts)
                .build());
    }

    public void sendDailyReport() {
        try {
            CostSummaryDto summary = costApiClient.getCostSummary();
            var attachment = SlackMessage.Attachment.builder()
                    .color("good")
                    .title(":chart_with_upwards_trend: 일일 비용 리포트")
                    .text(LocalDate.now().toString())
                    .fields(List.of(
                            new SlackMessage.Field("이번 달", String.format("$%.2f", summary.currentMonthCost()), true),
                            new SlackMessage.Field("전월 대비", String.format("%+.1f%%", summary.changePercent()), true)))
                    .ts(Instant.now().getEpochSecond())
                    .build();

            slackNotificationService.sendMessage(SlackMessage.builder()
                    .channel(slackConfig.getChannel())
                    .username(slackConfig.getUsername())
                    .icon_emoji(":bar_chart:")
                    .attachments(List.of(attachment))
                    .build());
        } catch (Exception e) {
            log.error("일일 리포트 생성 실패", e);
        }
    }
}
