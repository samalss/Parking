package org.parking.view;


import org.parking.model.ParkingRecord;

import java.time.LocalDateTime;

public class ParkingView {
    public void showOptions() {
        System.out.println("\nВыберите опцию:");
        System.out.println("1. Въезд машины");
        System.out.println("2. Выезд машины");
        System.out.println("3. Просмотр автомобилей на парковке");
        System.out.println("4. Выход\n");
        System.out.print("Ваш выбор: ");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }
    public void enterCarMessage(String licensePlate) {
        System.out.println("\nМашина с номером " + licensePlate + " заехала на парковку.");
    }

    public void alreadyEnterMessage(String licensePlate) {
        System.out.println("\nМашина с номером  " + licensePlate + " уже припаркована.");
    }
    public void parkedCars(String licensePlate, LocalDateTime entry_time) {
        System.out.println(licensePlate + " - время въезда " + entry_time);
    }
    public void notFoundMessage(String licensePlate) {
        System.out.println("\nМашина с номером " + licensePlate + " не найдена на парковке.");
    }
    public void notFoundMessage() {
        System.out.println("\nНа парковке нет машин.");
    }
    public void wrongLicensePlate(){
        System.out.println("\nНекорректный номер машины.");
    }
    public void wrongChoiceMessage(){
        System.out.println("\nНекорректный выбор. Пожалуйста, выберите 1, 2, 3 или 4.");
    }
    public void parkedCarsMessage(){
        System.out.println("\nМашины на парковке: ");
    }
    public void stopProgram(){
        System.out.println("\nПрограмма завершена.\n");
    }
    public void enterLicensePlate(){
        System.out.println("\nВведите номер машины: ");
    }

    public void displayInvoice(ParkingRecord parkingRecord) {
        System.out.println("\nСчет:");
        System.out.println("Номер машины: " + parkingRecord.getCar().getLicensePlate());
        System.out.println("Время въезда: " + parkingRecord.getEntryTime());
        System.out.println("Время выезда: " + parkingRecord.getExitTime());
        System.out.println("Сумма: " + parkingRecord.getCost() + " KZT\n");
    }
}
