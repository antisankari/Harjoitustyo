module org.example.harjoitustyo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.harjoitustyo to javafx.fxml;
    exports org.example.harjoitustyo;
}