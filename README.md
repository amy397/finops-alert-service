# FinOps Alert Service

비용 임계치 초과 시 Slack 알림을 발송하는 서비스

## 기능

- 일일 비용 증가율 모니터링
- 월간 예산 초과 감지
- Slack Webhook을 통한 알림 발송

## 알림 조건

- 전일 대비 비용 20% 이상 증가
- 월간 예산 초과

## 기술 스택

- Java 17
- Spring Boot 3.2
- Spring WebFlux (비동기 HTTP 클라이언트)
- PostgreSQL

## 환경 변수

| 변수명 | 설명 |
|--------|------|
| SLACK_WEBHOOK_URL | Slack Incoming Webhook URL |
| SLACK_CHANNEL | 알림을 받을 채널 |

## 로컬 실행

```bash
./gradlew bootRun
```
