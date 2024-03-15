package org.example.harjoitustyo;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Pelaaja
 * <br>
 * Pelaaja luokka pitää kirjaa pelaajan tiedoista kuten nimestä, pisteistä ja päivämäärästä, jolloin tulos on
 * pelattu.
 * Luokassa hoidetaan myös tietojen tallennus ja lukeminen tiedostosta.
 * @author Anssi Sankari
 */
public class Pelaaja implements Serializable {
    //pelidata
    private int taso = 1;
    private int pisteet = 0;
    private String pelaajaNimi = "";
    private Date gameDate = new Date();
    final private SimpleDateFormat paivaysMuotoilu = new SimpleDateFormat("dd.MM.yyyy");
    private Pelaaja ennatysPelaaja;

    //tiedosto
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
                System.out.println("Tiedosto on jo olemassa.");
            }
        }
        if (dataTiedosto.length() > 0) {
            ennatysPelaaja = lueTiedostosta();
        } else {
            ennatysPelaaja = null;
        }
    }

    public void setTaso(int level) {
        this.taso = level;
    }

    public void setPisteet(int pisteet) {
        this.pisteet += pisteet;
    }
    public int getPisteet() {
        return pisteet;
    }

    public void setPelaajaNimi(String pelaajaNimi) {
        this.pelaajaNimi = pelaajaNimi;
    }

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
            System.out.println("Virhe kirjoittamisessa tallentaessa");
        }
        //tiedoston sulun virhe
        try {
            tiedostoStream.close();
        } catch (IOException e) {
            System.out.println("Virhe sulussa tallentaessa");
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
            throw new RuntimeException(e); //en ymmärrä miksi IntelliJ vaatii tämän, muuten ei toimi, autogeneroitu
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

    @Override
    public String toString() {
        return "Nimimerkki: " + pelaajaNimi + "\n" +
                "Taso: " + taso + "\n" +
                "Pisteet: " + pisteet + "\n" +
                "Päivämäärä: " + paivaysMuotoilu.format(gameDate);
    }

    public static void main(String[] args) {
        //testausta
        //Pelaaja testData = new Pelaaja(1,2,3,0,"Peluri");
        //testData.lueTiedostosta();
        //testData.tallennaTiedostoon();
        //Pelaaja tulostus = new Pelaaja();
        //tulostus.setTaso(10);
        //tulostus.setPisteet(500);
        //tulostus.setPelaajaNimi("testiukkeli");
        //tulostus.tallennaTiedostoon();
        /*
        tulostus.lueTiedostosta();
        Pelaaja ennatysPelaaja = tulostus.lueTiedostosta();
        if (ennatysPelaaja != null) {
            System.out.println(ennatysPelaaja);
        } else {
            System.out.println("asdasd");
        }

         */
    }
}
