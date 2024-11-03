package me.udnek.itemscoreu.util;

import java.text.DecimalFormat;

public class Utils {
    private Utils(){}


    public static String roundToTwoDigits(double value){
        if (value % 1 == 0){
            return String.valueOf(((int) value));
        }
        return new DecimalFormat("#.##").format(value);
    }


}
