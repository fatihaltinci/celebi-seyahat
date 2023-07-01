package seyahatCelebi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class biletSecimEkrani{

    private Connection conn;
    
    public biletSecimEkrani(String kalkisYeri, String varisYeri, String tarihStr, String ulasimAraci) {
        ObservableList<bilet> biletList = FXCollections.observableArrayList();
    
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        BorderPane root = new BorderPane();

        TableView<bilet> biletTable = new TableView<>();

        // Bilet tablosunu oluştur
        TableColumn<bilet, Integer> pnrColumn = new TableColumn<>("PNR");
        pnrColumn.setCellValueFactory(new PropertyValueFactory<bilet, Integer>("pnr"));

        TableColumn<bilet, String> kalkisYeriColumn = new TableColumn<>("Kalkış Yeri");
        kalkisYeriColumn.setCellValueFactory(new PropertyValueFactory<bilet, String>("kalkisYeri"));
        
        TableColumn<bilet, String> varisYeriColumn = new TableColumn<>("Varış Yeri");
        varisYeriColumn.setCellValueFactory(new PropertyValueFactory<bilet, String>("varisYeri"));
        
        TableColumn<bilet, String> tarihColumn = new TableColumn<>("Tarih");
        tarihColumn.setCellValueFactory(new PropertyValueFactory<bilet, String>("tarih"));
        
        TableColumn<bilet, String> ulasimAraciColumn = new TableColumn<>("Ulaşım Aracı");
        ulasimAraciColumn.setCellValueFactory(new PropertyValueFactory<bilet, String>("ulasimAraci"));
        
        TableColumn<bilet, String> fiyatColumn = new TableColumn<>("Fiyat");
        fiyatColumn.setCellValueFactory(new PropertyValueFactory<bilet, String>("fiyat"));
        
        biletTable.getColumns().add(pnrColumn);
        biletTable.getColumns().add(kalkisYeriColumn);
        biletTable.getColumns().add(varisYeriColumn);
        biletTable.getColumns().add(tarihColumn);
        biletTable.getColumns().add(ulasimAraciColumn);
        biletTable.getColumns().add(fiyatColumn);
        
        // Tabloyu veritabanından doldur
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bilet.db")) {
            String sql = "SELECT * FROM bilet WHERE kalkis_yeri=? AND varis_yeri=? AND tarih=? AND ulasim_araci=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kalkisYeri);
            pstmt.setString(2, varisYeri);
            pstmt.setString(3, tarihStr);
            pstmt.setString(4, ulasimAraci);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                biletList.add(new bilet(rs.getInt("pnr"), rs.getString("kalkis_yeri"), rs.getString("varis_yeri"), rs.getString("tarih"), rs.getString("ulasim_araci"), rs.getString("fiyat")));
            }
            biletTable.setItems(biletList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Bilet seçim mesajı
        Label biletSecimLabel = new Label("Lütfen seçtiğiniz biletin PNR numarasını giriniz:");

        // PNR numarası giriş kutusu
        TextField pnrTextField = new TextField();
        pnrTextField.setMaxWidth(120);

        // Bilet seç butonu
        Button biletSecButton = new Button("Bilet Seç");
        biletSecButton.setOnAction(e -> {

            // PNR numarası girilmediyse hata ver
            if (pnrTextField.getText().isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText("PNR Numarası Girilmedi");
                alert.setContentText("Lütfen PNR numarasını giriniz.");
                alert.showAndWait();
                return;
            }

            // PNR numarası girildiyse

            Integer pnr = Integer.parseInt(pnrTextField.getText());
            // PNR numarasına göre veritabanından bilet bilgilerini getir
            try {
                // Biletlerin PNR numaralarını bir HashSet içinde topla
                Set<Integer> pnrSet = new HashSet<>();
                for (bilet b : biletList) {
                    pnrSet.add(b.getPnr());
                }
                if (pnrSet.contains(pnr)) {
                    biletOdemeSayfasi biletOdemeSayfasi = new biletOdemeSayfasi(pnr, ulasimAraci);
                    biletOdemeSayfasi.showAndWait();
                } else {
                    // Girilen PNR numarası, tabloda yer alan biletlerin PNR numaraları arasında yer almıyorsa, hata göster
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hata");
                    alert.setHeaderText("Bilet Bulunamadı");
                    alert.setContentText("Girdiğiniz PNR numarasına sahip bir bilet bulunamadı.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                // PNR numarası sayısal bir değer değilse hata göster
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText("PNR Numarası Geçersiz");
                alert.setContentText("Lütfen geçerli bir PNR numarası giriniz.");
                alert.showAndWait();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        // Bilet seçim kutusu
        HBox biletSecimHBox = new HBox(10);
        biletSecimHBox.setAlignment(Pos.CENTER);
        biletSecimHBox.setPadding(new Insets(10, 10, 10, 10));
        biletSecimHBox.getChildren().addAll(biletSecimLabel, pnrTextField, biletSecButton);

        // Bilet seçim ekranını oluştur
        root.setCenter(biletTable);
        root.setBottom(biletSecimHBox);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Bilet Seçim Ekranı");
        stage.showAndWait();

    }

    public void setVisible(boolean b) {
        
    }

    public void showAndWait() {

    }

    public void show() {
    }

}
