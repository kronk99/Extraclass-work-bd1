package com.example.servidormensajero;

import javafx.application.Platform;
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
import javafx.scene.paint.Color;
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
            public void handle(ActionEvent actionEvent) {//metodo que controla la accion del boton


                String MensajeEnCola = Txtescribe.getText();

                if(!MensajeEnCola.isEmpty()){ // Si la varibale NO(!) esta en blanco ...
                    HBox hBox = new HBox(); // crea un horizontal box(burbujitas de texto del whatsapp)
                    hBox.setAlignment(Pos.CENTER_RIGHT); //hace que la burbuja este en el centro a la derecha del Vbox, puesto que es un mensaje que se "envia"
                    hBox.setPadding(new Insets(5,5,5,10)); //investigar mas, creo que hace el recorte

                    Text text = new Text(MensajeEnCola); //crea el objeto text que contiene el mensaje que se va a enviar
                    TextFlow textFlow = new TextFlow(text); //basicamente si el texto es muy grande , lo baja una linea al envolverlo
                    textFlow.setStyle("-fx-color; rgb(239,242,245 " +
                            "-fx-background-color: rgb(15,125,242" +
                            "fx-background-radius: 20px"); //este de aca le da la forma redonda al chat
                    textFlow.setPadding(new Insets(5,10,5,10));//investigar sobre el comando
                    text.setFill(Color.color(0.934 , 0.945 , 0.996));//le da color al texflow
                    //se procede a añadir lo de arriba en el Hbox--------------------
                    hBox.getChildren().add(textFlow);//el hbox añade la variable textflow (investigar mas de esto)
                    Vbox_mensajes.getChildren().add(hBox); //el vbox agarra el hbox(investigar mas)
                    //despues de enviar esto al GUI del server, se envia al usuario
                    server.SendMessageToClient(messageToSend);
                    Txtescribe.clear();//luego de enviar el mensaje al cliente, el textbox se limpia.



                }
            }
        });


    }
    public static void addLabel(String mensageDeCliente ,VBox vbox ){
        HBox hBox = new HBox(); // crea un horizontal box(burbujitas de texto del whatsapp)
        hBox.setAlignment(Pos.CENTER_LEFT); //hace que la burbuja este en el centro a la izquierda del Vbox, puesto que es un mensaje que se "recibe"
        hBox.setPadding(new Insets(5,5,5,10)); //investigar mas, creo que hace el recorte

        Text text = new Text(mensageDeCliente); //crea el objeto text que contiene el mensaje que se va a recibir
        TextFlow textFlow = new TextFlow(text); //basicamente si el texto es muy grande , lo baja una linea al envolverlo
        textFlow.setStyle("-fx-background-color: rgb(244,244,235" + //REVISAR ESTA LINEA CON COLORES PUEDE GENERAR ERROR
                "fx-background-radius: 20px"); //este de aca le da la forma redonda al chat
        textFlow.setPadding(new Insets(5,10,5,10));//investigar sobre el comando
        text.setFill(Color.color(0.934 , 0.945 , 0.996));//le da color al texflow
        hBox.getChildren().add(textFlow);
        //se procede a añadir lo de arriba en el Hbox--------------------
        //Se supone que javafx no posee multithreading para cada widget , ya el "hilo Vbox esta siendo usado para enviar mensajes" ,entonces se debe usar
        //el siguiente metodo es para añadir el Vbox de este metodo, este metodo va a ser llamado en la clase server para colocarlo en un "thread distinto
        //y simular multithreading(VER EXPLICACION SOBRE RUNNABLES EN JAVA)
        Platform.runLater(new Runnable() { //platform runlater es una utilyti class , me sirve para "actualizar " el hilo de javafx de la aplicacion
            //los runnable son objetos que solo pueden ser usados por hilos ,el java fx solo se puede actualizar con el hilo de java fx GUI
            //este metodo va a ser llamado en server en un hilo diferente, es como pausar hilos y continuar otros
            @Override
            public void run() {
                vbox.getChildren().add(hBox);

            }
        });

    }
}
