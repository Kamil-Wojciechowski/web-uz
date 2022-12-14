package com.uz.shop.animal.world.utils;
import static com.uz.shop.animal.world.utils.Dictionary.*;
import static com.uz.shop.animal.world.utils.Dictionary.Inputs.*;

//Klasa mająca na celu przetłumaczenie kluczy przy błedzie
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
            case "amount":
                sb = new StringBuilder(AMOUNT);
                break;
            case "productTag":
                sb = new StringBuilder(PRODUCTTAG);
                break;
            case "description":
                sb = new StringBuilder(DESCRIPTION);
                break;
            case "priceUnit":
                sb = new StringBuilder(PRICEUNIT);
                break;
            case "imageBase":
                sb = new StringBuilder(IMAGEBASE);
                break;
            case "city":
                sb = new StringBuilder(CITY);
                break;
            case "mobileNumber":
                sb = new StringBuilder(MOBILENUMBER);
                break;
            case "street":
                sb = new StringBuilder(STREET);
                break;
            case "postalCode":
                sb = new StringBuilder(POSTALCODE);
                break;
            default:
                sb = new StringBuilder(text);
                break;
        }

        return sb.toString();
    }
}
