package com.uz.shop.animal.world.utils;
import static com.uz.shop.animal.world.utils.Dictionary.*;

public class Translator {
    public static String translate(String text) {
        StringBuilder sb = new StringBuilder();
        switch(text) {
            case "firstname":
                sb = new StringBuilder(FIRSTNAME);
                break;
            case "lastname":
                sb = new StringBuilder(LASTNAME);
                break;
            case "password":
                sb = new StringBuilder(PASSWORD);
                break;
            case "name":
                sb = new StringBuilder(NAME);
                break;
            default:
                sb = new StringBuilder(text);
                break;
        }

        return sb.toString();
    }
}
