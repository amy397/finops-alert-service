package com.finops.alert.service;

import com.finops.alert.config.SlackConfig;
import com.finops.alert.dto.SlackMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackNotificationService {

    private final SlackConfig slackConfig;
    private final WebClient.Builder webClientBuilder;

    public void sendMessage(SlackMessage message) {
        if (slackConfig.getWebhookUrl() == null || slackConfig.getWebhookUrl().isBlank()) {
            log.warn("Slack Webhook URL 미설정 - 로그 출력");
            message.attachments().forEach(att ->
                log.info("[SLACK] {}: {}", att.title(), att.text()));
            return;
        }

        webClientBuilder.build()
                .post()
                .uri(slackConfig.getWebhookUrl())
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(r -> log.info("Slack 알림 전송 성공"))
                .doOnError(e -> log.error("Slack 알림 전송 실패", e))
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }
}
