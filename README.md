# Лабораторна робота №1 - Java та Data-Oriented Programming

## Про що проєкт

Модель статусів замовлення, зроблена в стилі DOP: дані окремо, логіка окремо.

Дані - це `sealed interface OrderStatus` і чотири `record`, що його реалізують
(`Pending`, `Paid`, `Shipped`, `Cancelled`). Усі вони незмінні, а перевірка
вхідних значень живе в компактних конструкторах: порожній рядок або
невід'ємна сума одразу дають `IllegalArgumentException`.

Логіка - `OrderProcessor.process(OrderStatus)`. Метод розбирає статус одним
switch expression з pattern matching (record patterns, без `default`, бо його
замінює перевірка вичерпності від компілятора) і збирає звіт через text blocks.

Усе лежить у пакеті `ua.kpi.comsys.dop`.

## Розрахунок варіанта

Залікової книжки немає, тому беремо порядковий номер у списку групи: **N = 6**.

```
V = N mod 3 = 6 mod 3 = 0
```

**Варіант 0 - система управління замовленнями.**

## Приклад роботи

```
Order Status: SHIPPED
Details: Tracking Code: NP-77341, Dispatch Date: 2026-10-05
```

## Структура

```
src/main/java/ua/kpi/comsys/dop/
    OrderStatus.java      sealed interface
    Pending.java          порожній record
    Paid.java             paymentId + amount
    Shipped.java          trackingCode + dispatchDate
    Cancelled.java        reason
    OrderProcessor.java   switch expression + text blocks
    Main.java             демонстраційний запуск
src/test/java/ua/kpi/comsys/dop/
    OrderProcessorTest.java          усі гілки switch + null
    OrderStatusValidationTest.java   валідація в конструкторах
```

## Як запустити

Потрібні JDK 23 і Maven.

Тести:

```bash
mvn test
```

Демонстрація в консолі:

```bash
mvn compile exec:java -Dexec.mainClass=ua.kpi.comsys.dop.Main
```

З IntelliJ IDEA: правий клік на теці `src/test/java` -> Run All Tests.

## Тести

27 тестів на JUnit 5. Покривають кожну гілку switch, точний текст звіту,
`IllegalArgumentException` з повідомленням `Status cannot be null` і всі
випадки, коли конструктор record має відмовити (null, пробіли, нуль
і від'ємна сума).
