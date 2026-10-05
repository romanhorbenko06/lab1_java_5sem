package ua.kpi.comsys.dop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderProcessorTest {

    private final OrderProcessor processor = new OrderProcessor();

    @Test
    @DisplayName("Pending -> текст без додаткових даних")
    void pendingIsFormatted() {
        String expected = """
                Order Status: PENDING
                Details: Order is currently pending processing.
                """;

        assertEquals(expected, processor.process(new Pending()));
    }

    @Test
    @DisplayName("Paid -> у рядку є ID платежу і сума")
    void paidIsFormatted() {
        String expected = """
                Order Status: PAID
                Details: Payment ID: PAY-1024, Amount: $349.99
                """;

        assertEquals(expected, processor.process(new Paid("PAY-1024", new BigDecimal("349.99"))));
    }

    @Test
    @DisplayName("Paid -> дробова частина суми не обрізається")
    void paidKeepsScaleOfAmount() {
        String result = processor.process(new Paid("PAY-7", new BigDecimal("100.00")));

        assertEquals("""
                Order Status: PAID
                Details: Payment ID: PAY-7, Amount: $100.00
                """, result);
    }

    @Test
    @DisplayName("Shipped -> дата виводиться в ISO-форматі")
    void shippedIsFormatted() {
        String expected = """
                Order Status: SHIPPED
                Details: Tracking Code: NP-77341, Dispatch Date: 2026-10-05
                """;

        assertEquals(expected, processor.process(new Shipped("NP-77341", LocalDate.of(2026, 10, 5))));
    }

    @Test
    @DisplayName("Cancelled -> причина потрапляє в звіт як є")
    void cancelledIsFormatted() {
        String expected = """
                Order Status: CANCELLED
                Details: Reason: Out of stock
                """;

        assertEquals(expected, processor.process(new Cancelled("Out of stock")));
    }

    @Test
    @DisplayName("null замість статусу -> IllegalArgumentException з текстом зі специфікації")
    void nullStatusIsRejected() {
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, () -> processor.process(null));

        assertEquals("Status cannot be null", thrown.getMessage());
    }
}
