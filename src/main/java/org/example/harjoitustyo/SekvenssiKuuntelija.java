package org.example.harjoitustyo;

import java.util.ArrayList;

/**
 * SekvenssiKuuntelija rajapinta
 * <br>
 * Mahdollistaa rajapinnan listamuuttujan muutoksien seuraamiseen.
 */
public interface SekvenssiKuuntelija {
    /**
     * onkoSekvenssiPaivitetty seuraa listan päivittymistä.
     * @param sekvenssi Character lista, jossa on päivitetty sekvenssi.
     */
    void onkoSekvenssiPaivitetty(ArrayList<Character> sekvenssi);
    // tämä toteutustapa on lainattu internetistä, en muistanut ottaa talteen sivustoa mistä löysin
    //Observer toteutus, jossa on sisäänrakennettu päivitystiedon lähetys ominaisuus
    //edit. löytyi sama lähde
    //https://naveen-metta.medium.com/mastering-the-observer-pattern-in-java-a-comprehensive-guide-dda8df350687
}
