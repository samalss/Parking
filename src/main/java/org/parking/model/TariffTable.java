package org.parking.model;

import java.time.Duration;

import java.util.HashMap;
import java.util.Map;
public class TariffTable {
    private Map<Duration, Double> rates;

    public TariffTable() {
        rates = new HashMap<>();
        rates.put(Duration.ofMinutes(15), 0.0);   // до 15 минут бесплатно
        rates.put(Duration.ofHours(1), 100.0);     // от 15 минут до 1 часа - 100 тг
        rates.put(Duration.ofHours(3), 500.0);     // от 1 часа до 3 часов - 500 тг
        rates.put(Duration.ofHours(6), 1000.0);    // от 3 часов до 6 часов - 1000 тг
        // За каждый последующий час - 100 тг
    }

    public double calculateCost(Duration duration) {
        double cost = 0.0;
        if (duration.toMinutes() <= 15) {
            return cost;  // До 15 минут бесплатно
        }

        for (Map.Entry<Duration, Double> entry : rates.entrySet()) {
            if (duration.compareTo(entry.getKey()) <= 0) {
                cost = entry.getValue();
                break;
            }
        }

        if (cost == 0.0) {
            long extraHours = duration.toHours() - 6;
            cost = rates.get(Duration.ofHours(6)) + (extraHours * 100);
        }

        return cost;
    }
}