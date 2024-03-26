package org.example.harjoitustyo;
/**
 * PeliKaynnissaKuuntelija rajapinta
 * <br>
 * Mahdollistaa rajapinnan totuusarvomuuttujan muutoksien seuraamiseen.
 */
public interface PeliKaynnissaKuuntelija {
    /**
     * onkoPeliKaynnissa seuraa totuusarvon päivittymistä.
     * @param peliKaynnissa sisältää totuusarvomuotoisen tiedon.
     */
    void onkoPeliKaynnissa(boolean peliKaynnissa);
}
