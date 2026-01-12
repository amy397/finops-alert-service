package com.finops.alert.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record SlackMessage(
        String channel,
        String username,
        String icon_emoji,
        List<Attachment> attachments
) {
    @Builder
    public record Attachment(
            String color,
            String title,
            String text,
            List<Field> fields,
            String footer,
            Long ts
    ) {}

    @Builder
    public record Field(
            String title,
            String value,
            boolean shortField
    ) {}
}
