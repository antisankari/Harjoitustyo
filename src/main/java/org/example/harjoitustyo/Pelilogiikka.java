package org.example.harjoitustyo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Pelilogiikka
 * Luokka toteuttaa pelilogiikan sekvenssimuistipelille.
 * <br>
 * Luokka sisältää toiminnallisuuden sekvenssi listan luontiin, uuden pelaajan luontiin ja pelaajan painaman
 * painikkeen tarkastamiseen ja vertaamisen olevassa olevaan sekvenssiin.
 * @author AntiSankari
 */
public class Pelilogiikka {

    /**
     * ArrayList merkkilista tallentaa halutun merkkisekvenssin.
     */
    protected ArrayList<Character> sekvenssi = new ArrayList<Character>();
    /**
     * variValinta merkkijono toimii pohjana, josta arvotaan seuraava merkki lisättäväksi ArrayListiin
     */
    private String variValinta = "RGBY";
    /**
     * sekvenssinIndeksi tallettaa tiedon, missä kohdassa sekvenssiä tarkastus menee.
     */
    private int sekvenssinIndeksi;
    /**
     * taso tallentaa tiedon, tämänhetkisestä tasosta pelissä.
     */
    private int taso;
    /**
     * uusiTaso tallettaa tiedon mille tasolle siirrytään.
     */
    private int uusiTaso = 0;
    /**
     * lahtoTaso tallettaa tiedon tasosta, josta peli aina aloitetaan.
     */
    final private int lahtoTaso = 0;
    /**
     * peliKaynnissa boolean muuttuja tallentaa tiedon onko peli käynnissä.
     */
    private boolean peliKaynnissa;
    //tiedonsiirtoon rajapinta
    /**
     * SekvenssiKuuntelija rajapinta seuraa sekvenssi listan tilannetta. Kun sekvenssilista päivittyy, tieto näkyy
     * Kayttoliittyma luokalle.
     */
    private SekvenssiKuuntelija sekvenssiKuuntelija;
    /**
     * PeliKaynnissaKuuntelija rajapinta seuraa peliKaynnissa muuttujaa ja tilan muuttuessa tieto näkyy Kayttoliittyma
     * luokalle.
     */
    private PeliKaynnissaKuuntelija peliKaynnissaKuuntelija;

    /**
     * Alustetaan Pelaaja olio, joka toimii pelaajan tietovarastona.
     */
    private Pelaaja pelaaja;

    /**
     * setUusiTaso asettaa uusiTaso muuttujan arvoksi 0, jolla varmistetaan uuden pelin alkaminen
     * tasolta 0.
     * @param uusiTaso pelin uusi käynnistystaso
     */
    public void setUusiTaso(int uusiTaso) {
        this.uusiTaso = uusiTaso;
    }

    public int getLahtoTaso() {
        return lahtoTaso;
    }

    /**
     * Pelilogiikka konstruktori saa parametriksi Pelaaja luokan olion, jotta pelaajaluokan metodien käyttö
     * on mahdollista. Korvaa alustetun määrittelemättömän pelaaja olion.
     */
    public Pelilogiikka(Pelaaja pelaaja) {
        this.pelaaja = pelaaja;
    }

    //sekvenssin luonti
    /**
     * Metodi luo satunnaisen sekvenssin, joka käyttäjän täytyy toistaa painikkeilla
     * @param tasoNumero kertoo metodille montako lisämerkkiä lisätään listaan
     */
    protected void luoSekvenssi(int tasoNumero) {
        setPeliKaynnissa(true);
        sekvenssi.clear();
        this.taso = tasoNumero;
        Random rand = new Random();
        int ylaraja = 4;
        int alaraja = 0;

        for (int i = 0; i < (this.taso + 3); i++) {
            int indeksi = rand.nextInt(ylaraja - alaraja) + alaraja;
            sekvenssi.add(variValinta.charAt(indeksi));
        }
        System.out.println(sekvenssi);
        //kuuntelija päivittyy tyhjästä jolloin tieto saadaan siirrettyä
        if (sekvenssiKuuntelija != null) {
            sekvenssiKuuntelija.onkoSekvenssiPaivitetty(sekvenssi);
        }
    }

    /**
     * Metodi tarkastaa pelaajan painamasta painikkeesta syötteen ja vertaa sitä listassa indeksiin,
     * joka kertoo monesko painallus on menossa. Jos tarkastuu trueksi odotetaan seuraavaa syötettä, jos falseksi
     * niin päätetään peli.
     * @param syote grafiikalta saatu painallustieto, jolla tarkastetaan mitä on painettu
     */
    //pelaajan syote
    protected void tarkastaPelaajanSyote(Character syote) {
        if (syote.equals(sekvenssi.get(sekvenssinIndeksi))) {
            sekvenssinIndeksi++;
            if (sekvenssi.size() == sekvenssinIndeksi) {
                uusiTaso++;
                pelaaja.setPisteet(sekvenssinIndeksi);
                pelaaja.setTaso(uusiTaso);
                sekvenssinIndeksi = 0; //jos ei aseteta 0, alkaa uusi sekvenssi väärästä kohdasta
                luoSekvenssi(uusiTaso);
            }
        } else {
            pelaaja.setPisteet(sekvenssinIndeksi - 1); //lisätään oikeiden klikkausten määrä pisteisiin
            sekvenssinIndeksi = 0; //jos ei aseteta 0, alkaa uusi sekvenssi väärästä kohdasta
            if (pelaaja.getEnnatysPelaaja() == null || pelaaja.getPisteet() > pelaaja.getEnnatysPelaaja().getPisteet())
            {
                pelaaja.tallennaTiedostoon();
            }
            setPeliKaynnissa(false);
        }
    }

    /**
     * lopetaPeli metodi varmistaa, että mahdollinen ennätys tallennetaan myös jos peli lopetetaan kesken.
     */
    protected void lopetaPeli() {
        if (pelaaja.getEnnatysPelaaja() == null || pelaaja.getPisteet() > pelaaja.getEnnatysPelaaja().getPisteet()) {
            pelaaja.tallennaTiedostoon();
        }
    }

    //kuuntelija sekvenssille
    /**
     * setSekvenssiKuuntelija toteuttaa olion sekvenssi muuttujalle kuuntelijan, joka seuraa sekvenssi listan muutoksia.
     * @param sekvenssiKuuntelija sekvenssiKuuntelijan ilmentymä
     */
    public void setSekvenssiKuuntelija(SekvenssiKuuntelija sekvenssiKuuntelija){
        this.sekvenssiKuuntelija = sekvenssiKuuntelija;
    }

    /**
     * setPeliKaynnissaKuuntelija toteuttaa olion peliKaynnissa muuttujalle kuuntelijan.
     * @param peliKaynnissaKuuntelija PeliKaynnissaKuuntelijan ilmentymä
     */
    public void setPeliKaynnissaKuuntelija(PeliKaynnissaKuuntelija peliKaynnissaKuuntelija) {
        this.peliKaynnissaKuuntelija = peliKaynnissaKuuntelija;
    }

    /**
     * setPeliKaynnissa metodi asettaa peliKaynnissa muuttujalle arvon ja jokainen muutos
     * kulkeutuu Kayttoliittyma-luokalle tiedoksi.
     * @param peliKaynnissa peliKaynnissa boolean arvo
     */
    public void setPeliKaynnissa(boolean peliKaynnissa) {
        this.peliKaynnissa = peliKaynnissa;
        if (peliKaynnissaKuuntelija != null) {
            peliKaynnissaKuuntelija.onkoPeliKaynnissa(peliKaynnissa);
        }
    }

    /*
    public static void main(String[] args) {

        //Pelilogiikka testi = new Pelilogiikka();
        //testi.luoSekvenssi(4);
        //System.out.println("|" + testi.sekvenssi.get(2) + "|");
    }

     */

}
