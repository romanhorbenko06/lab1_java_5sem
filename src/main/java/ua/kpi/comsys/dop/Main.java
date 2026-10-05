package ua.kpi.comsys.dop;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        OrderProcessor processor = new OrderProcessor();

        OrderStatus[] statuses = {
                new Pending(),
                new Paid("PAY-1024", new BigDecimal("349.99")),
                new Shipped("NP-77341", LocalDate.of(2026, 10, 5)),
                new Cancelled("Customer changed their mind")
        };

        for (OrderStatus status : statuses) {
            System.out.print(processor.process(status));
            System.out.println("---");
        }
    }
}
