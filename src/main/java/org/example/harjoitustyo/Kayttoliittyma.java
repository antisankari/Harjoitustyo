package org.example.harjoitustyo;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Kayttoliittyma-luokka
 * <br>
 * Kayttoliittyma luokka piirtää pelin grafiikan ja lähettää Pelilogiikka luokalle kutsun käynnistää pelilogiikka.
 * Käyttöliittymässä toteutetaan painikkeiden vilkkuminen painaessa sekä pelilogiikan sekvenssin mukaan.
 * Lisäksi näytölle piirretään ennätystuloksen haltijan nimimerkki ja pisteet.
 */
public class Kayttoliittyma extends Application {
    private Pelilogiikka peliLogiikka;
    private Pelaaja pelaaja;

    //yläosan peliruutu, johon asetetaan pelipainikkeet
    private Pane peliIkkuna;
    protected Circle punainen;
    protected Circle sininen;
    protected Circle vihrea;
    protected Circle keltainen;
    private Button lopetaPeli;

    //alaosa jossa on toiminnot ja ennätys
    private Line jakaja;
    private Button aloita;
    private Label ennatys;

    //aloituksen päällysruutu
    private Pane nimiKysely;
    private TextField tfNimiKentta;
    private Button btSyotaNimi;
    private Rectangle pohja;

    //suojaruutu
    private Pane suojaruutu;

    //loppuruutu
    private Pane loppuruutu;
    private Label peliohi;

    //välähdysten animointi
    protected SequentialTransition vilkutusJarjestys;

    //ajastimet
    final private int tauonKesto = 1000;
    final private int valaytysKesto = 250;

    /**
     * piirraPeli metodi piirtää peli-ikkunan ohjelman käynnistyessä.
     * <br>
     * Peli-ikkunan piirto pitää sisällään kaikki painikkeet, peliruudun jne.
     */
    private void piirraPeli() {
        peliIkkuna = new Pane();
        peliIkkuna.setStyle("-fx-background-color: #000000;");

        punainen = new Circle();
        punainen.setLayoutY(125);
        punainen.setLayoutX(125);
        punainen.setRadius(75);
        //punainen.setPrefSize(150,150);
        punainen.setFill(Color.rgb(180,0,0));

        sininen = new Circle();
        sininen.setLayoutY(125);
        sininen.setLayoutX(275);
        sininen.setRadius(75);
        sininen.setFill(Color.rgb(0,0,180));

        vihrea = new Circle();
        vihrea.setLayoutY(275);
        vihrea.setLayoutX(125);
        vihrea.setRadius(75);
        vihrea.setFill(Color.rgb(0,180,0));

        keltainen = new Circle();
        keltainen.setLayoutY(275);
        keltainen.setLayoutX(275);
        keltainen.setRadius(75);
        keltainen.setFill(Color.rgb(180,180,0));

        Label pikaohje = new Label("Seuraa vilkkumista ja toista järjestys!");
        pikaohje.setTextFill(Color.WHITE);
        pikaohje.setLayoutX(105);
        pikaohje.setLayoutY(20);

        jakaja = new Line(0.0,370.0, 500.0, 370.0);
        jakaja.setStroke(Color.DARKGRAY);
        jakaja.setStrokeWidth(5);

        aloita = new Button();
        aloita.setText("Aloita peli");
        aloita.setPrefSize(100,25);
        aloita.setLayoutY(380);
        aloita.setLayoutX(150);

        lopetaPeli = new Button();
        lopetaPeli.setText("Sammuta");
        lopetaPeli.setLayoutY(470);
        lopetaPeli.setLayoutX(330);

        ennatys = new Label();
        ennatys.setTextFill(Color.WHITESMOKE);
        ennatys.setLayoutY(410);
        ennatys.setLayoutX(10);

        pohja = new Rectangle(400,500);
        pohja.setFill(Color.SLATEGRAY);

        btSyotaNimi = new Button("Ok");
        btSyotaNimi.setLayoutX(180);
        btSyotaNimi.setLayoutY(250);

        tfNimiKentta = new TextField();
        tfNimiKentta.setPromptText("Nimimerkkisi");
        tfNimiKentta.setLayoutX(120);
        tfNimiKentta.setLayoutY(220);
        nimiKysely = new Pane();

        Label peliohje = new Label("   Peli alkaa painamalla aloita peli.\nSeuraava taso alkaa automaattisesti.");
        peliohje.setTextFill(Color.WHITE);
        peliohje.setFont(new Font(18));
        peliohje.setLayoutX(60);
        peliohje.setLayoutY(150);

        nimiKysely.setPrefSize(400,500);
        nimiKysely.getChildren().addAll(pohja, btSyotaNimi, tfNimiKentta, peliohje);

        suojaruutu = new Pane();
        suojaruutu.setPrefSize(310,310);
        suojaruutu.setLayoutY(45);
        suojaruutu.setLayoutX(45);

        loppuruutu = new Pane();
        loppuruutu.setPrefSize(310,310);
        loppuruutu.setLayoutY(45);
        loppuruutu.setLayoutX(45);
        loppuruutu.setStyle("-fx-background-color: #000000;");

        peliohi = new Label();
        peliohi.setText("Muistit väärin. Peli ohi!\n\nAloita uusi peli alta!");
        peliohi.setTextAlignment(TextAlignment.CENTER);
        peliohi.setLayoutY(120);
        peliohi.setLayoutX(100);
        peliohi.setTextFill(Color.WHITE);

        loppuruutu.getChildren().add(peliohi);

        peliIkkuna.getChildren().addAll(
                punainen,sininen,vihrea,keltainen,
                jakaja,aloita,lopetaPeli,ennatys,pikaohje,suojaruutu,nimiKysely);
        suojaruutu.toBack();
    }

    /**
     * naytaLoppuruutu metodi tuo näkyviin loppuruudun, mikäli tehdään väärä arvaus.
     */
    private void naytaLoppuruutu() {
        peliIkkuna.getChildren().add(loppuruutu);
        loppuruutu.toFront();
    }

    /**
     * piilotaLoppuruutu metodi piilottaa loppuruudun, mikäli loppuruutu on piirretty.
     */
    private void piilotaLoppuruutu() {
        peliIkkuna.getChildren().remove(loppuruutu);
    }

    /**
     * Metodin kutsuminen muuttaa sen värisen painikkeen väriä, jolta kutsu tulee.
     * PauseTransitionin lopussa palauttaa alkuperäisen värin takaisin napille, niin näyttää välähdykseltä.
     * @param painike Painike jota on painettu tai joka on aktivoitunut.
     * @param vari teksti sen mukaan minkä väriseltä napilta kutsu tulee
     */
    protected void valaytaNappia(Circle painike, String vari) {
        Paint alkuperainenVari = painike.getFill();
        switch (vari) {
            case "punainen":
                painike.setFill(Color.rgb(255,0,0));
                break;
            case "sininen":
                painike.setFill(Color.rgb(0,0,255));
                break;
            case "vihrea":
                painike.setFill(Color.rgb(0,255,0));
                break;
            case "keltainen":
                painike.setFill(Color.rgb(255,255,0));
                break;
                //muistetaan ne breakit siellä...
        }

        PauseTransition varinVaihto = new PauseTransition();
        varinVaihto.setDuration(Duration.millis(valaytysKesto)); //testaa sopiva aika
        varinVaihto.setOnFinished(event -> painike.setFill(alkuperainenVari));
        varinVaihto.play();
    }

    /**
     * nappienTapahtumat metodi luo väripainikkeille toiminnallisuudet.
     * Napin painaminen lähettää kutsun väläyttää painetun väristä nappia ja
     * lähettää Pelilogiikka oliolle kutsun tarkastaa onko painettu nappi
     * sama kuin haluttu napinpainallus.
     */
    private void nappienTapahtumat() {
        punainen.setOnMousePressed(event -> {
            valaytaNappia(punainen,"punainen");
            peliLogiikka.tarkastaPelaajanSyote('R');
        });

        sininen.setOnMousePressed(event -> {
            valaytaNappia(sininen,"sininen");
            peliLogiikka.tarkastaPelaajanSyote('B');
        });

        vihrea.setOnMousePressed(event -> {
            valaytaNappia(vihrea,"vihrea");
            peliLogiikka.tarkastaPelaajanSyote('G');
        });

        keltainen.setOnMousePressed(event -> {
            valaytaNappia(keltainen,"keltainen");
            peliLogiikka.tarkastaPelaajanSyote('Y');
        });
    }

    /**
     * sekvenssinEsitys näyttää grafiikalla halutun painelusekvenssin nappien vilkutuksena.
     * Metodia kutsutaan aina, kun Pelilogiikka luokan sekvenssi lista päivittyy.
     * @param sekvenssi sisältää sekvenssi listan, joka päivittyy kun Pelilogiikka luokan olion sekvenssilista päivittyy
     */
    public void sekvenssinEsitys(ArrayList<Character> sekvenssi) {
        suojaruutu.toFront();
        vilkutusJarjestys = new SequentialTransition();
        for (int indeksi = 0; indeksi < sekvenssi.size(); indeksi++) { //jokaista sekvenssissä olevaa merkkiä kohti

            PauseTransition tauko = new PauseTransition();
            tauko.setDuration(Duration.millis(tauonKesto)); //tauon pituus, testaa sopiva

            if (sekvenssi.get(indeksi).equals('R')) {
                tauko.setOnFinished(event -> valaytaNappia(punainen,"punainen"));
                //tauko suoritetaan ensin, jonka jälkeen suoritetaan valaytaNappia metodin PauseTransitio
            } else if (sekvenssi.get(indeksi).equals('B')) {
                tauko.setOnFinished(event -> valaytaNappia(sininen, "sininen"));

            } else if (sekvenssi.get(indeksi).equals('G')) {
                tauko.setOnFinished(event -> valaytaNappia(vihrea,"vihrea"));

            } else if (sekvenssi.get(indeksi).equals('Y')) {
                tauko.setOnFinished(event -> valaytaNappia(keltainen, "keltainen"));
            }
            vilkutusJarjestys.getChildren().add(tauko);

        }
        vilkutusJarjestys.play();
        vilkutusJarjestys.setOnFinished(event -> suojaruutu.toBack());
    }

    /**
     * Graafisen käyttöliittymän ja pelilogiikan käynnistys.
     * Luo painikkeille tapahtumayhteydet.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        pelaaja = new Pelaaja();
        peliLogiikka = new Pelilogiikka(pelaaja);

        peliLogiikka.setSekvenssiKuuntelija(sekvenssi -> sekvenssinEsitys(sekvenssi));
        peliLogiikka.setPeliKaynnissaKuuntelija(peliKaynnissa -> {
            if (peliKaynnissa == false) {
                naytaLoppuruutu();
                ennatys.setText(pelaaja.lueTiedostosta().toString());
            } else {
                piilotaLoppuruutu();
            }
        });
        piirraPeli();
        nappienTapahtumat();

        //haetaan ennätyksen tiedot
        if (pelaaja.getEnnatysPelaaja() != null) {
            ennatys.setText(pelaaja.getEnnatysPelaaja().toString());
        } else {
            ennatys.setText("Ei olemassa olevaa ennätystä.");
        }

        Scene scene = new Scene(peliIkkuna,400,500);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Muistipeli");
        primaryStage.show();

        //annetun nimen välitys Pelaaja oliolle
        btSyotaNimi.setOnAction(event -> {
            pelaaja.setPelaajaNimi(tfNimiKentta.getText());
            peliIkkuna.getChildren().remove(nimiKysely);
            suojaruutu.toFront();
        });

        //aloita napin painaminen käynnistää ensimmäisen sekvenssin
        aloita.setOnAction(event -> {
            suojaruutu.toFront();
            peliLogiikka.setUusiTaso(0);
            if (this.vilkutusJarjestys != null) {
                vilkutusJarjestys.stop();
            }
            peliLogiikka.luoSekvenssi(peliLogiikka.getLahtoTaso());
        });

        //lopeta napin painaminen sammuttaa pelin ja sulkee ikkunan
        lopetaPeli.setOnAction(event -> {
            peliLogiikka.lopetaPeli();
            Platform.exit();
        });


    }
}
