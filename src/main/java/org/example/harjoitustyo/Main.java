package org.example.harjoitustyo;

import javafx.application.Application;

/**
 * Main luokka
 * Luokka sisältää main metodin pelin käynnistämiseen.
 * @author Anssi Sankari
 */
public class Main {
    /**
     * Main metodi käynnistämiseen
     * @param args välitetään komentoriviargumentteja?
     */
    public static void main(String[] args) {
        Application.launch(Kayttoliittyma.class, args);
    }
}
