package org.example.harjoitustyo;
/**
 * PeliKaynnissaKuuntelija rajapinta
 * <br>
 * Mahdollistaa rajapinnan totuusarvomuuttujan muutoksien seuraamiseen.
 */
public interface PeliKaynnissaKuuntelija {
    void onkoPeliKaynnissa(boolean peliKaynnissa);
}
