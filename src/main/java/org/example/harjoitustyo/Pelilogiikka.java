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

    protected ArrayList<Character> sekvenssi = new ArrayList<Character>();
    private String variValinta = "RGBY";
    private int sekvenssinIndeksi;
    private int taso;
    private int uusiTaso = 0;
    final private int lahtoTaso = 0;
    private boolean peliKaynnissa;
    //tiedonsiirtoon rajapinta
    private SekvenssiKuuntelija sekvenssiKuuntelija;
    private PeliKaynnissaKuuntelija peliKaynnissaKuuntelija;

    private Pelaaja pelaaja;

    public int getLahtoTaso() {
        return lahtoTaso;
    }

    /**
     * Pelilogiikka konstruktori saa parametriksi Pelaaja luokan olion, jotta pelaajaluokan metodien käyttö
     * on mahdollista.
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

        for (int i = 0; i < (taso + 3); i++) {
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
            //System.out.println("Sekvenssin merkki on :" + sekvenssi.get(sekvenssinIndeksi) + " ja painamasi on " + syote);
            if (sekvenssi.size() == sekvenssinIndeksi) {
                uusiTaso++;
                pelaaja.setPisteet(sekvenssinIndeksi);
                pelaaja.setTaso(uusiTaso);
                sekvenssinIndeksi = 0; //jos ei aseteta 0, alkaa uusi sekvenssi väärästä kohdasta
                System.out.println("Saavutit tason " + uusiTaso);
                luoSekvenssi(uusiTaso);
            }
        } else {
            setPeliKaynnissa(false);
            System.out.println(peliKaynnissa);
            pelaaja.setPisteet(sekvenssinIndeksi - 1); //lisätään oikeiden klikkausten määrä pisteisiin
            sekvenssinIndeksi = 0; //jos ei aseteta 0, alkaa uusi sekvenssi väärästä kohdasta
            if (pelaaja.getEnnatysPelaaja() == null || pelaaja.getPisteet() > pelaaja.getEnnatysPelaaja().getPisteet())
            {
                pelaaja.tallennaTiedostoon();
            }
        }
    }

    //lopetus
    protected void lopetaPeli() {
        if (pelaaja.getEnnatysPelaaja() == null || pelaaja.getPisteet() > pelaaja.getEnnatysPelaaja().getPisteet()) {
            pelaaja.tallennaTiedostoon();
        }
    }

    //kuuntelija sekvenssille
    /**
     * setSekvenssiKuuntelija toteuttaa oliolle kuuntelijan, joka seuraa sekvenssi listan muutoksia.
     * @param sekvenssiKuuntelija sekvenssiKuuntelijan ilmentymä
     */
    public void setSekvenssiKuuntelija(SekvenssiKuuntelija sekvenssiKuuntelija){
        this.sekvenssiKuuntelija = sekvenssiKuuntelija;
    }

    public void setPeliKaynnissaKuuntelija(PeliKaynnissaKuuntelija peliKaynnissaKuuntelija) {
        this.peliKaynnissaKuuntelija = peliKaynnissaKuuntelija;
    }

    public void setPeliKaynnissa(boolean peliKaynnissa) {
        this.peliKaynnissa = peliKaynnissa;
        if (peliKaynnissaKuuntelija != null) {
            peliKaynnissaKuuntelija.onkoPeliKaynnissa(peliKaynnissa);
        }
    }



    public static void main(String[] args) {
        //Pelilogiikka testi = new Pelilogiikka();
        //testi.luoSekvenssi(4);
        //System.out.println("|" + testi.sekvenssi.get(2) + "|");
    }

}
