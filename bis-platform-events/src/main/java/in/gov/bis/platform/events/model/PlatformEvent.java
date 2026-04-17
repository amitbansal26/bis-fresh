package in.gov.bis.platform.events.model;

import java.time.Instant;
import java.util.Map;

public record PlatformEvent(
        String eventType,
        String aggregateId,
        Instant occurredAt,
        Map<String, Object> payload
) {
}
