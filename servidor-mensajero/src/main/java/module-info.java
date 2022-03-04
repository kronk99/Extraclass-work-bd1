module com.example.servidormensajero {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.servidormensajero to javafx.fxml;
    exports com.example.servidormensajero;
}