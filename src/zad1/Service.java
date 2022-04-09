/**
 *
 *  @author Banulski Piotr S21735
 *
 */

package zad1;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service {
    String miasto;
    String kraj;
    Map<String, String> countries;

    Service (String kraj) {
        Locale.setDefault(Locale.ENGLISH);
        this.kraj=kraj;
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("",iso);
            countries.put(l.getDisplayCountry(), iso);
        }
        this.countries=countries;
    }


public String getWeather(String city){
this.miasto=city;
    StringBuilder json= new StringBuilder();
    try {
        URL url = new URL ("http://api.openweathermap.org/data/2.5/weather?q="+city+","+countries.get(kraj)+"&appid=someId");

    try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
        String line;
        while((line = in.readLine())!= null)
            json.append(line);

    } catch (IOException ex) {
        System.out.println("ex1");
    }
    } catch (MalformedURLException e) {
        System.out.println("ex2");
    }

    return json.toString();
}

public Double getRateFor(String kod_waluty) {

        String currencyCodeService = Currency.getInstance(new Locale("",countries.get(kraj))).getCurrencyCode();

    StringBuilder json= new StringBuilder();
    try {
        URL url = new URL ("https://api.exchangerate.host/latest?base="+kod_waluty+   "&symbols="+currencyCodeService);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while((line = in.readLine())!= null)
                json.append(line);

        } catch (IOException ex) {
            System.out.println("ex1");
        }
    } catch (MalformedURLException e) {
        System.out.println("ex2");
    }

    Gson gson = new Gson();
    CurrencyCodeGsonWrapper ccode = gson.fromJson(json.toString(), CurrencyCodeGsonWrapper.class);

return ccode.rates.get(currencyCodeService);
}

public Double getNBPRate() {

    String currencyCodeService = Currency.getInstance(new Locale("",countries.get(kraj))).getCurrencyCode();

    if(currencyCodeService.equals("PLN"))
        return 1.0;

    String json= "";
    try {
        URL url = new URL ("http://api.nbp.pl/api/exchangerates/rates/a/"+currencyCodeService+"/?format=json");

        try  {
            json=ConnectWithNBPApi(url);
            Gson gson = new Gson();
            NBPGsonWrapper NBPResponse = gson.fromJson(json.toString(), NBPGsonWrapper.class);
            return Double.parseDouble(NBPResponse.rates.get(0).get("mid"));
        } catch (IOException ioException) {
            System.err.println("Something went wrong, could not connect with NBP api table a");

        }

    } catch (IOException ex) {

        try {
            URL url = new URL ("http://api.nbp.pl/api/exchangerates/rates/b/"+currencyCodeService+"/?format=json");
            json=ConnectWithNBPApi(url);
            Gson gson = new Gson();
            NBPGsonWrapper NBPResponse = gson.fromJson(json.toString(), NBPGsonWrapper.class);
            return Double.parseDouble(NBPResponse.rates.get(0).get("mid"));

        } catch (IOException e) {
            System.err.println("Something went wrong, could not connect with NBP api table b");

        }

    }
    return 0.0;
}

private String ConnectWithNBPApi(URL url) throws IOException {
    StringBuilder json = new StringBuilder();

    try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
        String line;
        while ((line = in.readLine()) != null)
            json.append(line);
        return json.toString();
        }
  }

}
