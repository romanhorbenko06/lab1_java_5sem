package ua.kpi.comsys.dop;

import java.math.BigDecimal;

public record Paid(String paymentId, BigDecimal amount) implements OrderStatus {

    public Paid {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId must not be null or blank");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        }
    }
}
