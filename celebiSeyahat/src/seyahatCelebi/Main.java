package seyahatCelebi;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // GridPane oluştur
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // Başlık oluştur
        Text title = new Text("ÇELEBİ SEYAHAT ACENTESİNE HOŞGELDİNİZ");
        title.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        StackPane titlePane = new StackPane(title);
        titlePane.setAlignment(Pos.CENTER);
        grid.add(titlePane, 0, 0, 2, 1);

        // Tatil butonunu oluştur
        Button tatilButton = new Button("TATİL");
        tatilButton.setPrefSize(200, 400);
        tatilButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        tatilButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 0);");
        HBox hbTatilButton = new HBox(tatilButton);
        hbTatilButton.setAlignment(Pos.CENTER_LEFT);
        grid.add(hbTatilButton, 0, 1);
        
        tatilButton.setOnAction(e -> {
            tatilSecimEkrani tatilSecimEkrani = new tatilSecimEkrani();
            tatilSecimEkrani.showAndWait();
        });

        // Bakiye İşlemleri butonunu oluştur
        Button bakiyeButton = new Button("BAKİYE İŞLEMLERİ");
        bakiyeButton.setPrefWidth(200);
        bakiyeButton.setPrefHeight(50);
        bakiyeButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        bakiyeButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 0);");
        HBox hbBakiyeButton = new HBox(bakiyeButton);
        hbBakiyeButton.setAlignment(Pos.CENTER);
        hbBakiyeButton.setPadding(new Insets(50, 0, 0, 0));
        hbBakiyeButton.setSpacing(10);
        grid.add(hbBakiyeButton, 0, 2, 2, 1);

        bakiyeButton.setOnAction(e -> {
            bakiyeIslemleriSayfasi bakiyeIslemleriSayfasi = new bakiyeIslemleriSayfasi();
            bakiyeIslemleriSayfasi.showAndWait();
        });
    
    
        // Seyahat butonunu oluştur
        Button seyahatButton = new Button("SEYAHAT");
        seyahatButton.setPrefSize(200, 400);
        seyahatButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        seyahatButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 0);");
        HBox hbSeyahatButton = new HBox(seyahatButton);
        hbSeyahatButton.setAlignment(Pos.CENTER_RIGHT);
        grid.add(hbSeyahatButton, 1, 1);

        seyahatButton.setOnAction(e -> {
            ulasimSecimEkrani ulasimSecimEkrani = new ulasimSecimEkrani();
            ulasimSecimEkrani.showAndWait();
        });

        // Scene'i oluştur
        Scene scene = new Scene(grid, 600, 800);
        
        // Stage'i ayarla ve göster
        primaryStage.setTitle("ÇELEBİ SEYAHAT ACENTESİ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
