package com.skillbox.cryptobot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DataExtractor {
    public static Double extractPrice(String subscribeValue){
        String[] valueSplit = subscribeValue.trim().split(" ");
        return Double.parseDouble(valueSplit[1].replaceAll(",", "."));
    }
}
