package com.uz.shop.animal.world.validators;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Predicate;


//Walidator sprawdzający, czy captcha przesłana z frontendu jest poprawna.
@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class RecaptchaValidator implements Predicate<String> {
    private static final String URL = "https://www.google.com/recaptcha/api/siteverify";

    //Sekretny token Google
    @Value("${recaptcha.secret}")
    private String SECRET_KEY;

    //Test recaptchy, budowany jest request do serwerów google, a następnie sprawdzany jest response czy jest success
    @Override
    public boolean test(String recaptchaToken) {
        try {
            URL url = new URL(URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            StringBuilder params = new StringBuilder();
            params.append("secret=").append(SECRET_KEY).append("&response=").append(recaptchaToken);
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(params.toString());
            outputStream.flush();
            outputStream.close();

            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject json = new JSONObject(new JSONTokener(response.toString()));

            return json.getBoolean("success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
