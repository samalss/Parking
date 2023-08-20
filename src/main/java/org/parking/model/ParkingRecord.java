package org.parking.model;


import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingRecord {
    private int ParkingRecord_id;
    private Car car;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double cost;

    public ParkingRecord(Car car, LocalDateTime entryTime) {
        this.car = car;
        this.entryTime = entryTime;
    }

    public int getParkingRecord_id() {
        return ParkingRecord_id;
    }

    public void setParkingRecord_id(int parkingRecord_id) {
        ParkingRecord_id = parkingRecord_id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void calculateCost(TariffTable tariffTable) {
        if (exitTime == null) {
            throw new IllegalStateException("Exit time has not been set.");
        }

        Duration parkingDuration = Duration.between(entryTime, exitTime);
        cost = tariffTable.calculateCost(parkingDuration);
    }
}
