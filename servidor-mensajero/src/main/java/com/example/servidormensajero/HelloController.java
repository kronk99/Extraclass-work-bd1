package com.example.servidormensajero;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button BtnEnviar;
    @FXML
    private TextField Txtescribe;
    @FXML
    private ScrollPane barra_texto;
    @FXML
    private VBox Vbox_mensajes;

    private Server server;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            server = new Server(new ServerSocket(9090));//crea el socket, si no tira un mensaje de error

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al crear servidor");
        }
        Vbox_mensajes.heightProperty().addListener(new  ChangeListener<Number>() { //añade un puerto de escucha
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                barra_texto.setVvalue((Double) t1); //posible error a futuro , deberia de haber un newvalue pero hay t1, quiza se llamo otro metodo
//el codigo de arriba lo que hace es que los valores de la barra texto observan cambios en el Vbox, cuando este se llena manda
// un metodo que cambia el valor en el que esta la barra de texto al mensaje mas proximo, lo grande que es el vbox , el barra texto se mueve a esa posicion


            }
        });
        server.receiveMessageFromClient(Vbox_mensajes);//va  a recibir el mensaje del cliente en el Vbox, es un metodo de server
        //para evitar usar tantos hilos, se mantienen 2 por separado, un listener y un sender,



        BtnEnviar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                String MensajeEnCola = Txtescribe.getText();

                if(!MensajeEnCola.isEmpty()){ // Si la varibale no esta en blanco ...
                    HBox hBox = new HBox(); // crea un horizontal box(burbujitas de texto del whatsapp)
                    hBox.setAlignment(Pos.CENTER_RIGHT); //hace que la burbuja este en el centro a la derecha del Vbox, puesto que es un mensaje que se "envia"
                    hBox.setPadding(new Insets(5,5,5,10)); //investigar mas, creo que hace el recorte

                    Text text = new Text(MensajeEnCola); //crea el objeto text que contiene el mensaje que se va a enviar
                    TextFlow textFlow = new TextFlow(text); //basicamente si el texto es muy grande , lo baja una linea al envolverlo
                    //QUEDE EN EL MINUTO 18 DEL VIDEO
                }
            }
        });


    }
}
