package com.uz.shop.animal.world.utils;

//Polski słownik, który jest wykorzystywany w aplikacji
public class Dictionary {
    public static final String RECOVERY_EMAIL = "Sprawdź swoją skrzynkę mailową.";
    public static final String TOKEN_NOT_FOUND = "Nie znaleziono tokenu.";
    public static final String TOKEN_EXPIRED = "Token wygasł.";
    public static final String PASSWORD_ARE_NOT_THE_SAME = "Hasła nie są takie same.";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Hasło zmienione pomyślnie.";
    public static final String WRONG_FORMAT_EMAIL = "Email ma zły format.";
    public static final String EMAIL_SEND_CONFIRM = "Sprawdź swoją skrzynkę mailową.";
    public static final String EMAIL_ALREADY_TAKEN = "Konto o takim emailu już istnieje.";
    public final static String USER_NOT_FOUND = "Nie znaleziono takiego konta.";
    public final static String INVALID_RECAPTCHA = "Chyba jesteś botem!";
    public final static String INVALID_INPUT = "Pole wypełnione nieprawidłowo";
    public final static String ALREADY_EXISTS = "Taki element już istnieje";
    public final static String PRODUCT_PARENT_NOT_FOUND = "parentId : Produkt nie istnieje";
    public final static String TAG_NOT_FOUND = "Tag : Taki tag nie istnieje";
    public final static String ITEM_NOT_FOUND = "Nie znaleziono elementu o podanym Id";

    public final static String ITEM_COUNT_NOT_ENOUGH = "Nie można zakupić produktu, ponieważ jest go za mało";
    public final static String ITEM_NOT_AVALIABLE = "Produkt nie jest dostępny";

    public final static String UNAUTHORIZED = "Brak dostępu";

    class Inputs {
        public final static String FIRSTNAME = "Imię";
        public final static String LASTNAME = "Nazwisko";
        public final static String PASSWORD = "Hasło";
        public final static String NAME = "Nazwa";
        public final static String AMOUNT = "Ilość";
        public final static String PRODUCTTAG = "Produkt Tag";
        public final static String DESCRIPTION = "Opis";
        public final static String PRICEUNIT = "Cena jednostki";
        public final static String IMAGEBASE = "Obraz";
        public final static String CITY = "Miejscowość";
        public final static String MOBILENUMBER = "Numer telefonu";
        public final static String STREET = "Ulica";
        public final static String POSTALCODE = "Kod pocztowy";

    }

    public static class Company {
        public static final String NAME = "Świat zwierząt";
        public static final String STREET = "Ulica 0A";
        public static final String CITY = "00-000 Miasto";
    }

    public static class Document {
        public final static String INVOICE = "invoice";
        public final static String LABEL = "label";
    }
}
