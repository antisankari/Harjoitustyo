package org.example.harjoitustyo;

import java.util.ArrayList;

public interface SekvenssiKuuntelija {
    void onkoSekvenssiPaivitetty(ArrayList<Character> sekvenssi);
    // tämä toteutustapa on lainattu internetistä, en muistanut ottaa talteen sivustoa mistä löysin
    //Observer toteutus, jossa on sisäänrakennettu päivitystiedon lähetys ominaisuus
    //edit. löytyi sama lähde
    //https://naveen-metta.medium.com/mastering-the-observer-pattern-in-java-a-comprehensive-guide-dda8df350687
}
