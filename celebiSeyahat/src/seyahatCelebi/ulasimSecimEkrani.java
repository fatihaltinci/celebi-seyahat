package seyahatCelebi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import seyahatCelebi.ulasimSecimEkrani;

public class ulasimSecimEkrani {
    private String kalkisYeri;
    private String varisYeri;
    
    public ulasimSecimEkrani() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        
        // GridPane oluştur
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // Kalkış Yeri için ChoiceBox oluştur
        ObservableList<String> kalkisYerleri = FXCollections.observableArrayList(
            "İstanbul", "Ankara", "İzmir", "Antalya", "Bodrum");
        ChoiceBox<String> kalkisYeriBox = new ChoiceBox<>(kalkisYerleri);
        kalkisYeriBox.setValue("İstanbul");
        grid.add(new Label("Kalkış Yeri:"), 0, 2);
        grid.add(kalkisYeriBox, 1, 2);

        // Varış Yeri için ChoiceBox oluştur
        ObservableList<String> varisYerleri = FXCollections.observableArrayList(
            "İstanbul", "Ankara", "İzmir", "Antalya", "Bodrum");
        ChoiceBox<String> varisYeriBox = new ChoiceBox<>(varisYerleri);
        varisYeriBox.setValue("Ankara");
        grid.add(new Label("Varış Yeri:"), 0, 3);
        grid.add(varisYeriBox, 1, 3);

        // Tarih için DatePicker oluştur
        DatePicker tarihPicker = new DatePicker();
        tarihPicker.setValue(LocalDate.now());
        grid.add(new Label("Tarih:"), 0, 4);
        grid.add(tarihPicker, 1, 4);

        // Ulaşım aracı için RadioButton oluştur
        ToggleGroup ulasimAraci = new ToggleGroup();
        RadioButton ucakButton = new RadioButton("Uçak");
        ucakButton.setToggleGroup(ulasimAraci);
        RadioButton trenButton = new RadioButton("Tren");
        trenButton.setToggleGroup(ulasimAraci);
        RadioButton otobusButton = new RadioButton("Otobüs");
        otobusButton.setToggleGroup(ulasimAraci);
        HBox hboxUlasimAraci = new HBox(10, ucakButton, trenButton, otobusButton);
        grid.add(new Label("Ulaşım Aracı:"), 0, 5);
        grid.add(hboxUlasimAraci, 1, 5);
        
        Button araButton = new Button("ARA");
        HBox hbAraButton = new HBox(10, araButton);
        hbAraButton.setAlignment(Pos.CENTER_RIGHT);
        grid.add(hbAraButton, 1, 7);


        // Ara butonuna tıklandığında
        araButton.setOnAction(e -> {
            String kalkisYeri = kalkisYeriBox.getValue();
            String varisYeri = varisYeriBox.getValue();
            LocalDate tarih = tarihPicker.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String tarihStr = tarih.format(formatter);
            
            Toggle toggle = ulasimAraci.getSelectedToggle();
            if (toggle == null) { // Ulaşım aracı seçilmedi
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText(null);
                alert.setContentText("Lütfen ulaşım aracı seçiniz.");
                alert.showAndWait();
                return;
            }
            String ulasim = ((RadioButton) toggle).getText();
    
            biletSecimEkrani biletSecimEkrani = new biletSecimEkrani(kalkisYeri, varisYeri, tarihStr, ulasim);
            biletSecimEkrani.showAndWait();
        });

        // Scene'i oluştur
        Scene scene = new Scene(grid, 400, 250);
        
        // Stage'i ayarla ve göster
        
        stage.setTitle("Kalkış - Varış Yeri Seçimi");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public String getKalkisYeri() {
        return kalkisYeri;
    }

    public String getVarisYeri() {
        return varisYeri;
    }

    public void showAndWait() {
    }
}

