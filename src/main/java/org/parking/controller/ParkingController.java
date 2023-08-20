package org.parking.controller;

import org.parking.model.Car;
import org.parking.model.ParkingRecord;
import org.parking.model.TariffTable;
import org.parking.utils.LicensePlateValidator;
import org.parking.view.ParkingView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ParkingController {

    private ParkingView view;
    private TariffTable tariffTable;
    private Map<String, ParkingRecord> parkingRecords;
    private final ParkingDB parkingDB;

    public ParkingController() {
        view = new ParkingView();
        tariffTable = new TariffTable();
        parkingRecords = new HashMap<>();
        this.parkingDB = new ParkingDB();
    }

    public void run() { //Вывод на консоли
        Scanner scanner = new Scanner(System.in);

        while (true) {
            view.showOptions();
            try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Считывание символа новой строки после считывания числа

            switch (choice) {
                case 1:
                    enterCar();
                    break;
                case 2:
                    exitCar();
                    break;
                case 3:
                    viewCarsOnParking();
                    break;
                case 4:
                    view.stopProgram();
                    scanner.close();
                    return;
                default:
                    view.wrongChoiceMessage();
            }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
                scanner.nextLine(); // Считывание символа новой строки после ошибки
            }
        }
    }

    private void enterCar() { //Въезд машины
        Scanner scanner = new Scanner(System.in);
        view.enterLicensePlate();
        String licensePlate = scanner.nextLine();

        if (!LicensePlateValidator.isValidLicensePlate(licensePlate)) {
            view.wrongLicensePlate();
            enterCar(); // Вызываем метод снова для повторного ввода
            return;
        }
        try {
            if (parkingDB.isCarAlreadyParked(licensePlate)) {
                view.alreadyEnterMessage(licensePlate);
            } else {
                Car car = new Car(licensePlate);
                LocalDateTime entryTime = LocalDateTime.now();
                ParkingRecord parkingRecord = new ParkingRecord(car, entryTime);

                parkingDB.saveParkingRecord(parkingRecord); // Сохранение записи о парковке в базе данных
                parkingRecords.put(licensePlate, parkingRecord);
                view.enterCarMessage(licensePlate);
            }
        } catch (Exception e) {
            view.displayMessage("Произошла ошибка: " + e.getMessage());
        }
    }

    private void exitCar() { //Выезд машины
        Scanner scanner = new Scanner(System.in);

        view.enterLicensePlate();
        String licensePlate = scanner.nextLine();

        if (!LicensePlateValidator.isValidLicensePlate(licensePlate)) {
            view.wrongLicensePlate();
            exitCar(); // Вызываем метод снова для повторного ввода
        }
        try {
        if (!parkingDB.isCarAlreadyParked(licensePlate)) {
            view.notFoundMessage(licensePlate);
            return;
        }

        ParkingRecord parkingRecord = parkingDB.getParkingRecordByLicensePlate(licensePlate);

        LocalDateTime exitTime = LocalDateTime.now();
        parkingRecord.setExitTime(exitTime);
        parkingRecord.calculateCost(tariffTable);
        view.displayInvoice(parkingRecord);

        parkingDB.updateParkingRecord(parkingRecord);
        } catch (Exception e) {
            view.displayMessage("Произошла ошибка: " + e.getMessage());
        }
    }

    private void viewCarsOnParking() { // Просмотр машин на парковке
        try {
            List<ParkingRecord> parkedCars = parkingDB.getParkedCars();

            if (parkedCars.isEmpty()) {
                view.notFoundMessage();
            } else {
                view.parkedCarsMessage();
                for (ParkingRecord record : parkedCars) {
                    view.parkedCars(record.getCar().getLicensePlate(), record.getEntryTime());
                }
            }
        } catch (Exception e) {
            view.displayMessage("Произошла ошибка: " + e.getMessage());
        }
    }


}
