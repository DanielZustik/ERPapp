package com.zustik.erpapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;

public class Main extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        //zde pozdeji dam nacist uvodni obrazovku... ostatni moduly budou mit vlastni FXML a bude se nastovavat na na scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("issuedInvoices/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 640); //v fmxl je pritom nastaveny nejaky root obejct ktery je root scene grafu pujde asi o gridpane. ten root pritom musi byt potomkem Node. To se hodi pritom, kdy si chci napriklad udelat vlastni obalovaci classu do ktere bych vlozil nejaky kod naprilkad k tableview aby se vse zprehlednilo a nebylo v jedne classe mainWindow, kde bych dal vsech kod pro vytvoreni UI, musel bych potom dedit z Node, abych mohl plugnout tu clasu do Scene viz https://www.youtube.com/watch?v=b-0QMxCB2zI
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/appIcon.png")));
        stage.setTitle("ERP application Zustik");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public static Stage getStage() {
        return stage;
    }
}