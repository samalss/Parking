package org.parking.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LicensePlateValidator {
    public static boolean isValidLicensePlate(String licensePlate) { //Проверка валидности номера машины
        String pattern = "^\\d{3}[A-Za-z]{3}(0[1-9]|1[0-9]|20)$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(licensePlate);
        return matcher.matches();
    }
}
