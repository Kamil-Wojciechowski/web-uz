package com.uz.shop.animal.world.validators;


import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Serwis odpowiadający za walidacje maila
@Service
public class EmailValidator implements Predicate<String> {

    //Patern odnośnie maila
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //Usługa API, która sprawdza czy email należy do domen od 10 minutowych maili
    private static final String url = "https://disposable.debounce.io/";

    //Logger
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailValidator.class);


    //Test który sprawdza mail oraz czy jego pochodzenie jest poprawne
    @Override
    public boolean test(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find() & !checkDisposableEmail(email);
    }

    //Metoda wysyłająca zapytanie oraz sprawdzająca, czy email powinien zostać użyty
    private boolean checkDisposableEmail(String email){

        try {
            String query = String.format("email=%s", email);
            URL url = new URL(this.url + "?" + query);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            BufferedReader rd  = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = rd.readLine()) != null)
            {
                sb.append(line + '\n');
            }

            JSONObject jsonItem = new JSONObject(new JSONTokener(sb.toString()));
            Boolean disposable = jsonItem.getBoolean("disposable");

            return disposable;
        } catch (Exception e) {
            return false;
        }
    }
}
