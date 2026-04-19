package org.example.util;

import java.time.LocalDate;
import java.time.Period;

/**
 * Isikukoodi töötlemise klass
 */
public class PersonalCodeUtil {

    public static LocalDate extractBirthDate(String personalCode) {
        if (personalCode == null || personalCode.length() != 11) {
            throw new IllegalArgumentException("Invalid personal code");
        }
        int genderDigit = Character.getNumericValue(personalCode.charAt(0));
        int year = Integer.parseInt(personalCode.substring(1, 3));
        int month = Integer.parseInt(personalCode.substring(3, 5));
        int day = Integer.parseInt(personalCode.substring(5, 7));

        int century = switch (genderDigit){
            case 1,2 -> 1800;
            case 3,4 -> 1900;
            case 5,6 -> 2000;
            default -> throw new IllegalArgumentException("Invalid gender digit");
        };

        return LocalDate.of(century + year, month, day);
    }
    public static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    public static boolean mod11check(String personalCode) {
        if (personalCode == null || personalCode.length() != 11) {
            throw new IllegalArgumentException("Invalid personal code");
        }
        int[] weights1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
        int[] weights2 = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(personalCode.charAt(i)) * weights1[i];
        }

        int remainder = sum % 11;
        if (remainder == 10) {
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(personalCode.charAt(i)) * weights2[i];
            }
            remainder = sum % 11;
            if (remainder == 10) remainder = 0;
        }

        return Character.getNumericValue(personalCode.charAt(10)) == remainder;
    }
}
