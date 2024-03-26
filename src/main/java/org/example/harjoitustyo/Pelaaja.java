package org.example.harjoitustyo;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Pelaaja-luokka
 * <br>
 * Pelaaja luokka pitää kirjaa pelaajan tiedoista kuten nimestä, pisteistä ja päivämäärästä, jolloin tulos on
 * pelattu.
 * Luokassa hoidetaan myös tietojen tallennus ja lukeminen tiedostosta.
 * @author Anssi Sankari
 */
public class Pelaaja implements Serializable {
    //pelidata
    /**
     * Pelaajan taso. Taso nousee kun sekvenssi on läpäisty.
     */
    private int taso = 1;
    /**
     * Pelaajan keräämät pisteet. Päivitetään sekvenssin päivittyessä tai pelin loppuessa.
     */
    private int pisteet = 0;
    /**
     * Pelaajan antama nimimerkki.
     */
    private String pelaajaNimi = "";
    /**
     * Pelatun pelin päivämäärä. Kerää tiedon pelin päivämäärästä ennätystä varten.
     */
    private Date peliPaiva = new Date();
    final private SimpleDateFormat paivaysMuotoilu = new SimpleDateFormat("dd.MM.yyyy");
    /**
     * Pelaaja-olion luonti ennätyspelaajaksi, joka päivitetään tiedostosta ja tallennetaan tiedostoon
     * kun ennätys päivittyy.
     */
    private Pelaaja ennatysPelaaja;

    /**
     * Tiedosto, johon ennätyspelaaja olio tallennetaan ja josta ennätyspelaaja luetaan.
     */
    final private File dataTiedosto = new File("datafile.dat");

    /**
     * Pelaaja olion konstruktori luomaan uusi pelaaja olio sekä hakemaan ennätystiedot
     * tiedostosta toiseen pelaaja olioon.
     */
    public Pelaaja() {
        if (!dataTiedosto.exists()) {
            try {
                dataTiedosto.createNewFile();
            } catch (IOException e) {
                System.out.println("Tiedosto on jo olemassa. " + e.getMessage());
            }
        }
        if (dataTiedosto.length() > 0) {
            ennatysPelaaja = lueTiedostosta();
        } else {
            ennatysPelaaja = null;
        }
    }

    /**
     * setTaso metodilla asetetaan Taso muuttujalle uusi arvo.
     * @param taso uusi taso, jolla pelaaja on
     */
    public void setTaso(int taso) {
        this.taso = taso;
    }

    /**
     * setPisteet lisää olemassa oleviin pisteisiin tasosta saadut pisteet.
     * @param pisteet saadut pisteet.
     */
    public void setPisteet(int pisteet) {
        this.pisteet += pisteet;
    }

    /**
     * getPisteet hakee olion pisteet tallennettavaksi tiedostoon-
     * @return saavutetut pisteet int muodossa.
     */
    public int getPisteet() {
        return pisteet;
    }

    /**
     * setPelaajaNimi asettaa pelaajaoliolle pelaajan valitseman nimimerkin.
     * @param pelaajaNimi pelaajan antama nimimerkki.
     */
    public void setPelaajaNimi(String pelaajaNimi) {
        this.pelaajaNimi = pelaajaNimi;
    }

    /**
     * getEnnatysPelaaja hakee ennatysPelaaja olion tietoineen.
     * @return palauttaa ennatysPelaaja olion.
     */
    public Pelaaja getEnnatysPelaaja(){
        return ennatysPelaaja;
    }

    /**
     * tallennaTiedostoon metodi tallentaa tiedostoon pelikerran tiedot, jotta ennätys voidaan näyttää.
     */
    protected void tallennaTiedostoon() {
        FileOutputStream tiedostoStream = null;
        ObjectOutputStream pelaajaStream = null;
        try {
            tiedostoStream = new FileOutputStream(dataTiedosto);
            pelaajaStream = new ObjectOutputStream(tiedostoStream);
            //kirjotetaan tiedot tiedostoon
            pelaajaStream.writeObject(this);
        } catch (IOException e) {
            System.out.println("Virhe kirjoittamisessa tallentaessa" + " " + e.getMessage());
        }
        //tiedoston sulun virhe
        try {
            tiedostoStream.close();
        } catch (IOException e) {
            System.out.println("Virhe sulussa tallentaessa" + " " + e.getMessage());
        }
    }

    /**
     * lueTiedostosta metodi hoitaa talletettujen tietojen lukemisen tiedostosta ja palauttaa
     * String muotoisen tiedon, jossa on luetut tiedot.
     * @return palauttaa String muotoisena tiedostosta luetut tiedot.
     */
    protected Pelaaja lueTiedostosta() {
        FileInputStream tiedostoStream = null;
        ObjectInputStream pelaajaStream = null;
        Pelaaja ennatysPelaaja = null;
        try {
            tiedostoStream = new FileInputStream(dataTiedosto);
            pelaajaStream = new ObjectInputStream(tiedostoStream);
            ennatysPelaaja = (Pelaaja) pelaajaStream.readObject();
        } catch (IOException e) {
            System.out.println("Virhe lukemisessa lukiessa." + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); //en keksi miksi IntelliJ vaatii tämän, muuten ei toimi, autogeneroitu
        } finally {
            if (tiedostoStream != null) {
                try {
                    tiedostoStream.close();
                } catch (IOException e) {
                    System.out.println("Virhe sulussa lukiessa" + e.getMessage());
                }
            }
        }
        return ennatysPelaaja;
    }

    /**
     * toString muodostaa luokan tiedoista tekstin.
     * @return palauttaa String muotoisena tiedot tulostettavaksi.
     */
    @Override
    public String toString() {
        return "Ennätys\n" +
                "Nimimerkki: " + pelaajaNimi + "\n" +
                "Taso: " + taso + "\n" +
                "Pisteet: " + pisteet + "\n" +
                "Päivämäärä: " + paivaysMuotoilu.format(peliPaiva);
    }
}
