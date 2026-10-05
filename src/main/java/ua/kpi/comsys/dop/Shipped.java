package ua.kpi.comsys.dop;

import java.time.LocalDate;

public record Shipped(String trackingCode, LocalDate dispatchDate) implements OrderStatus {

    public Shipped {
        if (trackingCode == null || trackingCode.isBlank()) {
            throw new IllegalArgumentException("trackingCode must not be null or blank");
        }
        if (dispatchDate == null) {
            throw new IllegalArgumentException("dispatchDate must not be null");
        }
    }
}
