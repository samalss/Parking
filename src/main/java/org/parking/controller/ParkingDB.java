package org.parking.controller;

import org.parking.model.Car;
import org.parking.model.ParkingRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingDB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/parking_db";
    private static final String DB_USER = "samal";
    private static final String DB_PASSWORD = "password";

    public void saveParkingRecord(ParkingRecord record) { // Въезд машины
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            String carQuery = "SELECT id FROM car WHERE license_plate = ?"; // проверяется наличие номера машины в таблице car, чтобы не было дублирования номера если машина ранее заезжала
            PreparedStatement selectStatement = connection.prepareStatement(carQuery);
            selectStatement.setString(1, record.getCar().getLicensePlate());
            int carId = -1;
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (!resultSet.next()) { // если номера в таблице car не нашлось
                    carQuery = "INSERT INTO car (license_plate) VALUES (?) ON DUPLICATE KEY UPDATE id=id";
                    try (PreparedStatement carStatement = connection.prepareStatement(carQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        carStatement.setString(1, record.getCar().getLicensePlate());
                        carStatement.executeUpdate();
                        try (var generatedKeys = carStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                carId = generatedKeys.getInt(1);
                            }
                        }
                    }
                }
                else carId = resultSet.getInt("id"); // если ранее заезжала берем существующий id
            }
            String insertParkingRecordQuery = "INSERT INTO parking_records (car_id, entry_time) VALUES (?, ?)";

            if (carId != -1) {
                try (PreparedStatement parkingRecordStatement = connection.prepareStatement(insertParkingRecordQuery)) {
                    parkingRecordStatement.setInt(1, carId);
                    parkingRecordStatement.setTimestamp(2, java.sql.Timestamp.valueOf(record.getEntryTime()));
                    parkingRecordStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateParkingRecord(ParkingRecord parkingRecord) { //Выезд машины
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE parking_records SET exit_time = ?, cost = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(parkingRecord.getExitTime()));
            preparedStatement.setDouble(2, parkingRecord.getCost());
            preparedStatement.setInt(3, parkingRecord.getParkingRecord_id());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isCarAlreadyParked(String licensePlate) { // Проверка припаркована ли машина
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT id FROM parking_records WHERE car_id IN (SELECT id FROM car WHERE license_plate = ?) AND exit_time IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, licensePlate);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Возвращает значение true, если автомобиль уже припаркован
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Возвращает значение false в случае ошибки или отсутствия совпадения
        }
    }

    public List<ParkingRecord> getParkedCars() { // Получить список припаркованных машин
        List<ParkingRecord> parkedCars = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT pr.id, pr.entry_time, c.license_plate FROM parking_records pr JOIN car c ON pr.car_id = c.id WHERE pr.exit_time IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int recordId = resultSet.getInt("id");
                    LocalDateTime entryTime = resultSet.getTimestamp("entry_time").toLocalDateTime();
                    String licensePlate = resultSet.getString("license_plate");

                    Car car = new Car(licensePlate);
                    ParkingRecord record = new ParkingRecord(car, entryTime);
                    record.setParkingRecord_id(recordId);

                    parkedCars.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parkedCars;
    }

    public ParkingRecord getParkingRecordByLicensePlate(String licensePlate) { // Получить информацию о парковку машины по номеру
        ParkingRecord parkingRecord = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT pr.id, pr.entry_time, pr.exit_time, pr.cost, c.id AS car_id " +
                    "FROM parking_records pr " +
                    "JOIN car c ON pr.car_id = c.id " +
                    "WHERE c.license_plate = ? AND pr.exit_time IS NULL";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, licensePlate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int recordId = resultSet.getInt("id");
                    LocalDateTime entryTime = resultSet.getTimestamp("entry_time").toLocalDateTime();
                    int carId = resultSet.getInt("car_id");

                    Car car = new Car(licensePlate);
                    parkingRecord = new ParkingRecord(car, entryTime);
                    parkingRecord.setParkingRecord_id(recordId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parkingRecord;
    }
}