package seyahatCelebi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class tatilRezervasyonSayfasi {

    public tatilRezervasyonSayfasi(String konum, LocalDate baslangicTarihi, LocalDate bitisTarihi) {
        ObservableList<otel> otelList = FXCollections.observableArrayList();
    
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        BorderPane root = new BorderPane();

        TableView<otel> otelTable = new TableView<>();

        // Otel tablosunu oluştur
        TableColumn<otel, Integer> adColumn = new TableColumn<>("Otel Adı");
        adColumn.setCellValueFactory(new PropertyValueFactory<otel, Integer>("otelAdi"));

        TableColumn<otel, String> yildizColumn = new TableColumn<>("Yıldız Sayısı");
        yildizColumn.setCellValueFactory(new PropertyValueFactory<otel, String>("otelinYildizSayisi"));
        
        TableColumn<otel, String> konumColumn = new TableColumn<>("Konum");
        konumColumn.setCellValueFactory(new PropertyValueFactory<otel, String>("Konum"));

        TableColumn<otel, String> baslangicColumn = new TableColumn<>("Başlangıç Tarihi");
        baslangicColumn.setCellValueFactory(new PropertyValueFactory<otel, String>("baslangicTarihi"));

        TableColumn<otel, String> bitisColumn = new TableColumn<>("Bitiş Tarihi");
        bitisColumn.setCellValueFactory(new PropertyValueFactory<otel, String>("bitisTarihi"));
        
        TableColumn<otel, String> gunColumn = new TableColumn<>("Gün Sayısı");
        gunColumn.setCellValueFactory(new PropertyValueFactory<otel, String>("gunSayisi"));
        
        TableColumn<otel, String> kisiColumn = new TableColumn<>("Kişi Sayısı");
        kisiColumn.setCellValueFactory(new PropertyValueFactory<otel, String>("kisiSayisi"));
        
        TableColumn<otel, String> fiyatColumn = new TableColumn<>("Fiyat");
        fiyatColumn.setCellValueFactory(new PropertyValueFactory<otel, String>("fiyat"));
        
        otelTable.getColumns().add(adColumn);
        otelTable.getColumns().add(yildizColumn);
        otelTable.getColumns().add(konumColumn);
        otelTable.getColumns().add(baslangicColumn);
        otelTable.getColumns().add(bitisColumn);
        otelTable.getColumns().add(gunColumn);
        otelTable.getColumns().add(kisiColumn);
        otelTable.getColumns().add(fiyatColumn);

        String baslangicTarihiStr = baslangicTarihi.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String bitisTarihiStr = bitisTarihi.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Tabloyu veritabanından doldur
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/otel.db")) {
            String sql = "SELECT * FROM otel WHERE konum=? AND baslangicTarihi >= ? AND bitisTarihi <= ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, konum);
            pstmt.setString(2, baslangicTarihiStr);
            pstmt.setString(3, bitisTarihiStr);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                otelList.add(new otel(rs.getString("otelAdi"), rs.getString("otelinYildizSayisi"), rs.getString("Konum"), rs.getString("baslangicTarihi"), rs.getString("bitisTarihi"), rs.getString("gunSayisi"), rs.getString("kisiSayisi"), rs.getString("fiyat")));
            }
            otelTable.setItems(otelList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Button rezervasyonYapButton = new Button("Rezervasyon Yap");
        rezervasyonYapButton.setOnAction(e -> {
            otel selectedOtel = otelTable.getSelectionModel().getSelectedItem();
            if (selectedOtel != null) {
                String otelAdi = selectedOtel.getOtelAdi();
                String fiyat = selectedOtel.getFiyat();
                String gunSayisi = selectedOtel.getGunSayisi();

                tatilOdemeSayfasi tatilOdemeSayfasi = new tatilOdemeSayfasi(otelAdi, fiyat, gunSayisi);
                tatilOdemeSayfasi.initModality(Modality.APPLICATION_MODAL);
                tatilOdemeSayfasi.showAndWait();
            } else {
                // Hata durumunda kullanıcıya bildirim gösterilebilir veya gerekli işlemler yapılabilir.
                System.out.println("Hata: Bir otel seçilmedi!");
            }
        });

        Label baslikLabel = new Label("OTEL SEÇİM LİSTESİ");
        baslikLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(baslikLabel, otelTable, rezervasyonYapButton);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.showAndWait();

    }

    public void showAndWait() {
    }

}
