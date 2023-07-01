package seyahatCelebi;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class tatilSecimEkrani {
    private Stage stage;
    private Label konumLabel;
    private Label tarihLabel;

    public tatilSecimEkrani() {
        stage = new Stage();
        stage.setTitle("Tatil Seçimi");
        // Tatil seçim arayüzünü oluştur ve içeriğini doldur
        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        // Konum girişi için TextField
        // TextField konumField = new TextField();
        // konumField.setPromptText("Konum");

        // Konum seçimi için ComboBox
        ComboBox<String> konumComboBox = new ComboBox<>();
        konumComboBox.setPromptText("Konum");
        // Veritabanından konumları ComboBox'a ekle
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/otel.db")) {
            String sql = "SELECT DISTINCT konum FROM otel";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                konumComboBox.getItems().add(rs.getString("konum"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        // Tarih aralığı seçimi için başlangıç ve bitiş DatePicker'ları
        DatePicker baslangicPicker = new DatePicker();
        DatePicker bitisPicker = new DatePicker();
        
        // Seçimleri onaylamak için Button
        Button onayButton = new Button("Onayla");
        onayButton.setOnAction(e -> {
            // Kullanıcının girdiği konum ve tarih aralığını al
            //String konum = konumField.getText();
            String konum = konumComboBox.getValue();
            LocalDate baslangicTarihi = baslangicPicker.getValue();
            LocalDate bitisTarihi = bitisPicker.getValue();
            
            konumLabel.setText("Konum: " + konum);
            tarihLabel.setText("Tarih Aralığı: " + baslangicTarihi.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                    " - " + bitisTarihi.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

            tatilRezervasyonSayfasi tatilRezervasyonSayfasi = new tatilRezervasyonSayfasi(konum, baslangicTarihi, bitisTarihi);
            tatilRezervasyonSayfasi.showAndWait();
        });
        
        // Metinleri görüntülemek için Label'lar
        Label konumBaslikLabel = new Label("Konum:");
        konumLabel = new Label();
        Label tarihBaslikLabel = new Label("Tarih Aralığı:");
        tarihLabel = new Label();
        
        // İçeriği düzenle
        content.getChildren().addAll(konumBaslikLabel, konumComboBox, tarihBaslikLabel, baslangicPicker, bitisPicker, onayButton, konumLabel, tarihLabel);
        
        // Scene'i oluştur
        Scene scene = new Scene(content, 400, 250);
        
        // Stage'i ayarla ve göster
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}
