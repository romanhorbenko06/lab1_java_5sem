package ua.kpi.comsys.dop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderStatusValidationTest {

    @Nested
    class PaidRecord {

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\t"})
        @DisplayName("порожній paymentId не приймається")
        void rejectsBlankPaymentId(String paymentId) {
            assertThrows(IllegalArgumentException.class,
                    () -> new Paid(paymentId, BigDecimal.TEN));
        }

        @Test
        void rejectsNullAmount() {
            assertThrows(IllegalArgumentException.class, () -> new Paid("PAY-1", null));
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "0.00", "-0.01", "-500"})
        @DisplayName("нуль і від'ємна сума не приймаються")
        void rejectsNonPositiveAmount(String amount) {
            assertThrows(IllegalArgumentException.class,
                    () -> new Paid("PAY-1", new BigDecimal(amount)));
        }

        @Test
        void keepsValidValues() {
            Paid paid = new Paid("PAY-1", new BigDecimal("0.01"));

            assertEquals("PAY-1", paid.paymentId());
            assertEquals(new BigDecimal("0.01"), paid.amount());
        }
    }

    @Nested
    class ShippedRecord {

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "   "})
        void rejectsBlankTrackingCode(String trackingCode) {
            assertThrows(IllegalArgumentException.class,
                    () -> new Shipped(trackingCode, LocalDate.now()));
        }

        @Test
        void rejectsNullDispatchDate() {
            assertThrows(IllegalArgumentException.class, () -> new Shipped("NP-1", null));
        }

        @Test
        void keepsValidValues() {
            LocalDate date = LocalDate.of(2026, 1, 15);
            Shipped shipped = new Shipped("NP-1", date);

            assertEquals("NP-1", shipped.trackingCode());
            assertEquals(date, shipped.dispatchDate());
        }
    }

    @Nested
    class CancelledRecord {

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "\n"})
        void rejectsBlankReason(String reason) {
            assertThrows(IllegalArgumentException.class, () -> new Cancelled(reason));
        }

        @Test
        void keepsValidValues() {
            assertEquals("Duplicate order", new Cancelled("Duplicate order").reason());
        }
    }

    @Test
    @DisplayName("Pending створюється без аргументів, два екземпляри рівні")
    void pendingHasValueSemantics() {
        assertDoesNotThrow(Pending::new);
        assertEquals(new Pending(), new Pending());
    }
}
