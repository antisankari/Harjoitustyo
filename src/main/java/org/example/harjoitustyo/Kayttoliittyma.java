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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
    protected Button punainen;
    protected Button sininen;
    protected Button vihrea;
    protected Button keltainen;
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
    private Label aloitauusi;

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

        punainen = new Button();
        punainen.setLayoutY(50);
        punainen.setLayoutX(50);
        punainen.setPrefSize(150,150);
        punainen.setStyle("-fx-background-color: #c80000;");

        sininen = new Button();
        sininen.setLayoutY(50);
        sininen.setLayoutX(200);
        sininen.setPrefSize(150,150);
        sininen.setStyle("-fx-background-color: #0000c8;");

        vihrea = new Button();
        vihrea.setLayoutY(200);
        vihrea.setLayoutX(50);
        vihrea.setPrefSize(150,150);
        vihrea.setStyle("-fx-background-color: #00c800;");

        keltainen = new Button();
        keltainen.setLayoutY(200);
        keltainen.setLayoutX(200);
        keltainen.setPrefSize(150,150);
        keltainen.setStyle("-fx-background-color: #c8c800;");

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

        nimiKysely.setPrefSize(400,500);
        nimiKysely.getChildren().addAll(pohja, btSyotaNimi, tfNimiKentta);

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
                jakaja,aloita,lopetaPeli,ennatys,suojaruutu,nimiKysely);
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
    protected void valaytaNappia(Button painike, String vari) {
        String alkuperainenVari = painike.getStyle();
        switch (vari) {
            case "punainen":
                painike.setStyle("-fx-background-color: #ff0000");
                break;
            case "sininen":
                painike.setStyle("-fx-background-color: #0000ff");
                break;
            case "vihrea":
                painike.setStyle("-fx-background-color: #00ff00");
                break;
            case "keltainen":
                painike.setStyle("-fx-background-color: #ffff00");
                break;
                //muistetaan ne breakit siellä...
        }

        PauseTransition varinVaihto = new PauseTransition();
        varinVaihto.setDuration(Duration.millis(valaytysKesto)); //testaa sopiva aika
        varinVaihto.setOnFinished(event -> painike.setStyle(alkuperainenVari));
        varinVaihto.play();
    }

    /**
     * nappienTapahtumat metodi luo väripainikkeille toiminnallisuudet.
     * Napin painaminen lähettää kutsun väläyttää painetun väristä nappia ja
     * lähettää Pelilogiikka oliolle kutsun tarkastaa onko painettu nappi
     * sama kuin haluttu napinpainallus.
     */
    private void nappienTapahtumat() {
        punainen.setOnAction(event -> {
            valaytaNappia(punainen,"punainen");
            peliLogiikka.tarkastaPelaajanSyote('R');
        });

        sininen.setOnAction(event -> {
            valaytaNappia(sininen,"sininen");
            peliLogiikka.tarkastaPelaajanSyote('B');
        });

        vihrea.setOnAction(event -> {
            valaytaNappia(vihrea,"vihrea");
            peliLogiikka.tarkastaPelaajanSyote('G');
        });

        keltainen.setOnAction(event -> {
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

    public static void main(String[] args) {
        launch(args);
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
            ennatys.setText("Ei olemassa olevaa ennätystä");
        }

        Scene scene = new Scene(peliIkkuna,400,500);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Muistipeli");
        primaryStage.show();

        //annetun nimen välitys Pelaaja oliolle
        btSyotaNimi.setOnAction(event -> {
            pelaaja.setPelaajaNimi(tfNimiKentta.getText());
            nimiKysely.getChildren().removeAll(pohja, tfNimiKentta, btSyotaNimi);
            peliIkkuna.getChildren().remove(nimiKysely);
        });

        //aloita napin painaminen käynnistää ensimmäisen sekvenssin
        aloita.setOnAction(event -> {
            suojaruutu.toFront();
            peliLogiikka.luoSekvenssi(peliLogiikka.getLahtoTaso());
        });

        //lopeta napin painaminen sammuttaa pelin ja sulkee ikkunan
        lopetaPeli.setOnAction(event -> {
            peliLogiikka.lopetaPeli();
            Platform.exit();
        });
    }
}
