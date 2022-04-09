package zad1;
import com.google.gson.Gson;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

public class Gui {
    public static BrowserPane bbb1;

    Gui(String weatherJson, Double rate1, Double rate2, Service s) {

        DecimalFormat numberFormat = new DecimalFormat("#.0");

        Gson gson = new Gson();
        WeatherGsonWrapper weather = gson.fromJson(weatherJson, WeatherGsonWrapper.class);

        JFrame frame = new JFrame();
        frame.setSize(600,600);
        frame.setTitle("super fajne zadanko, wcale nie nienawidze gui");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());


        JLabel label = new JLabel();
        label.setText("Pogoda: siła wiatru "+numberFormat.format(weather.wind.speed*3.6)+"km/h" +
                ", temperatura "+ (numberFormat.format(weather.main.temp-273.15)) +
                " stopni celcjusza, temperatura odczuwalna "+numberFormat.format((weather.main.feels_like-273.15)) +
               " kierunek wiatru: "+weather.convertDegreeToCardinalDirection(weather.wind.deg)
        );
        JLabel label2 = new JLabel();
        JLabel label3 = new JLabel();
        label2.setText("kurs wymiany 1 "+ Currency.getInstance(new Locale("",s.countries.get(s.kraj))).getCurrencyCode()+" to "+rate1+" USD");
        label3.setText("kurs wymiany "+Currency.getInstance(new Locale("",s.countries.get(s.kraj))).getCurrencyCode() +" na PLN to "+ rate2);


        frame.add(label);
        frame.add(label2);
        frame.add(label3);

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(250,40));
        frame.add(textField);

         bbb1 = new BrowserPane("https://pl.wikipedia.org/wiki/"+s.miasto);




        JButton button = new JButton("nowe dane");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText(textField.getText());
                String dane[] = textField.getText().split(",");
            Service s2 = new Service(dane[0]);
            Gson gson2 = new Gson();
                WeatherGsonWrapper weather2 = gson2.fromJson(s2.getWeather(dane[1]), WeatherGsonWrapper.class);
                label.setText("Pogoda: siła wiatru "+numberFormat.format(weather2.wind.speed*3.6)+"km/h" +
                        ", temperatura "+ (numberFormat.format(weather2.main.temp-273.15)) +
                        " stopni celcjusza, temperatura odczuwalna "+numberFormat.format((weather2.main.feels_like-273.15)) +
                        " kierunek wiatru: "+weather2.convertDegreeToCardinalDirection(weather.wind.deg)
                );
                label2.setText("kurs wymiany 1 "+ Currency.getInstance(new Locale("",s2.countries.get(s2.kraj))).getCurrencyCode()+" to "+s2.getRateFor(dane[2])+dane[2]);
                label3.setText("kurs wymiany "+Currency.getInstance(new Locale("",s2.countries.get(s2.kraj))).getCurrencyCode() +" na PLN to "+ s2.getNBPRate());

                frame.remove(bbb1);
                bbb1 =new BrowserPane("https://pl.wikipedia.org/wiki/"+dane[1]);
                frame.add(bbb1);
            }
        });

        frame.add(bbb1);




        frame.add(button);
        frame.pack();
        frame.setVisible(true);


    }
}


