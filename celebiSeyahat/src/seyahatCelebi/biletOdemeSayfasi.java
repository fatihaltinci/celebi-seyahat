package seyahatCelebi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class biletOdemeSayfasi extends Application {
    private Integer pnr;
    private Stage stage;
    private double biletFiyati;
    private Label bakiyeLabel = new Label();
    private Statement stmt;
    private String ulasimAraci;

    public String getUlasimAraci() {
        return ulasimAraci;
    }

    public void setUlasimAraci(String ulasimAraci) {
        this.ulasimAraci = ulasimAraci;
    }

    public biletOdemeSayfasi(Integer pnr, String ulasimAraci) {
        this.pnr = pnr;
        this.ulasimAraci = ulasimAraci;
    }

    public void start(Stage primaryStage) {
    // Veritabanı bağlantısı oluşturma
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bakiye.db");
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT miktar FROM bakiye")) {

    // ResultSet nesnesinde bir sonraki satıra geçiyoruz
        if (rs.next()) {
            // Sorgu sonucunda elde edilen bakiyeyi alıyoruz
            double bakiye = rs.getDouble("miktar");

            // Bakiyeyi balanceLabel'da gösteriyoruz
            bakiyeLabel.setText("Mevcut Bakiye: " + bakiye);
        }

        } catch (SQLException e) {
            System.err.println("Veritabanına bağlanırken hata oluştu: " + e.getMessage());
        }

        stage = primaryStage;
        stage.setTitle("Ödeme Sayfası");

        // ChoiceBox ile ödeme yöntemlerini seçebiliriz
        ChoiceBox<String> paymentMethod = new ChoiceBox<>();
        paymentMethod.getItems().addAll("Cüzdan", "Kredi Kartı", "Çek");
        paymentMethod.setValue("Cüzdan"); // default seçenek

        // Label ve Button nesneleri
        Label infoLabel = new Label("Ödeme yöntemi seçiniz:");
        Button confirmButton = new Button("Onayla");
        Button cancelButton = new Button("İptal");

        // Button nesnelerinin tıklama olayları
        confirmButton.setOnAction(e -> onConfirmClicked(paymentMethod.getValue()));
        cancelButton.setOnAction(e -> onCancelClicked());

        // HBox nesnesi ile butonları yatayda yan yana yerleştirebiliriz
        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // GridPane nesnesi ile ödeme sayfasını düzenleyebiliriz
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.add(infoLabel, 0, 0);
        layout.add(paymentMethod, 1, 0);
        layout.add(bakiyeLabel, 1, 1);
        layout.add(buttonBox, 1, 2);

        Scene scene = new Scene(layout, 300, 150);
        stage.setScene(scene);
        stage.showAndWait();
    }

    // Onayla butonuna tıklandığında çalışacak metot
    private void onConfirmClicked(String paymentMethod) {
        System.out.println("Ödeme yöntemi: " + paymentMethod);

        if (paymentMethod.equals("Cüzdan")) 
        {
                    try {
                    // Veritabanı bağlantısı oluşturma
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bilet.db");
            
                    // PNR numarasına göre bilet fiyatını sorgulama
                    String query = "SELECT fiyat FROM bilet WHERE pnr = ?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setInt(1, pnr);
                    ResultSet rs = ps.executeQuery();
            
                    // Bilet fiyatını al
                    if (rs.next()) {
                        biletFiyati = rs.getDouble("fiyat");
                    }
                    // Bağlantıyı kapat
                    rs.close();
                    ps.close();
                    conn.close();
            
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    // Veritabanı bağlantısı oluşturma
                    Connection conn2 = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bakiye.db");
            
                    // Cüzdaki bakiyeyi sorgulama
                    String query2 = "SELECT miktar FROM bakiye";
                    PreparedStatement st2 = conn2.prepareStatement(query2);
                    ResultSet rs2 = st2.executeQuery();
                        // Cüzdaki bakiyeyi al
                        double cüzdanBakiyesi = 0;
                        if (rs2.next()) {
                            cüzdanBakiyesi = rs2.getDouble("miktar");
                        }
        
                        // Bağlantıyı kapat
                        rs2.close();
        
                        // Bilet fiyatını cüzdaki bakiyeyle karşılaştır
                        if (biletFiyati <= cüzdanBakiyesi) {
                            // Ödeme işlemi tamamlandı, bilet satın alındı
                            // System.out.println("Bilet satın alındı.");
                            cüzdanBakiyesi -= biletFiyati;
        
                            // Cüzdaki bakiyeyi güncelle
                            String updateQuery = "UPDATE bakiye SET miktar = ?";
                            PreparedStatement ps2 = conn2.prepareStatement(updateQuery);

                            // Yeni bakiyeyi göster
                            String yeniBakiye = String.format("%.2f TL", cüzdanBakiyesi);
                            Label bakiyeLabel = new Label("Cüzdaki Bakiye: " + yeniBakiye);
                            
                            BorderPane root = new BorderPane();
                            root.setPadding(new Insets(10));
                            root.setCenter(bakiyeLabel);
                            root.setTop(new Label("Bilet satın alındı."));
                            Scene scene = new Scene(root, 300, 150);
                            stage.setScene(scene);
                            stage.show();

                            ps2.setDouble(1, cüzdanBakiyesi);
                            ps2.executeUpdate();
                            ps2.close();

                            if (ulasimAraci == "Uçak"){
                                double ucaktanPuan = biletFiyati * 0.01;
                                Connection conn3;
                                try {
                                    conn3 = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/puan.db");
                                    // Mevcut puanı sorgulama
                                    String query3 = "SELECT miktar FROM puan";
                                    PreparedStatement st3 = conn3.prepareStatement(query3);
                                    ResultSet rs3 = st3.executeQuery();
                                        // Mevcut puanı al
                                        double puan = 0;
                                        if (rs3.next()) {
                                            puan = rs3.getDouble("miktar");
                                        }
                                        puan += ucaktanPuan;
                                        System.out.println("Uçaktan kazanılan puan: " + ucaktanPuan);
                                        System.out.println("Toplam puan: " + puan);
                                        String guncelle = "UPDATE puan SET miktar = ?";
                                        PreparedStatement ps3 = conn3.prepareStatement(guncelle);
                                        ps3.setDouble(1, puan);
                                        ps3.executeUpdate();
                                        ps3.close();
                                        rs3.close();
                                        st3.close();
                                        conn3.close();
                                } catch (SQLException e) {
                                    
                                    e.printStackTrace();
                                }
            
                            }
        
                        } else {
                            // Yeterli bakiye yok uyarısı ver
                            System.out.println("Yeterli bakiye yok.");
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Hata");
                            alert.setHeaderText("Ödeme işlemi başarısız oldu");
                            alert.setContentText("Yeterli bakiye yok.");
                            alert.showAndWait();
                        }
            
                    // Bağlantıyı kapat
                    conn2.close();
            
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }


         else if (paymentMethod.equals("Kredi Kartı"))
         {
            // Kart bilgileri için pencereyi aç
            Stage cardStage = new Stage();
            cardStage.setTitle("Kart Bilgileri");

            Label cardNumberLabel = new Label("Kart Numarası:");
            TextField cardNumberField = new TextField();
            Label expiryDateLabel = new Label("Son Kullanma Tarihi (MM/YY):");
            TextField expiryDateField = new TextField();
            Label cvvLabel = new Label("CVV:");
            TextField cvvField = new TextField();

            Button confirmButton = new Button("Onayla");
            confirmButton.setOnAction(event -> {
                String cardNumber = cardNumberField.getText();
                String expiryDate = expiryDateField.getText();
                String cvv = cvvField.getText();

                // Kart numarası, son kullanma tarihi ve CVV doğru formatta mı diye kontrol et
                if (cardNumber.matches("\\d{16}") && expiryDate.matches("\\d{2}/\\d{2}") && cvv.matches("\\d{3}")) {
                    // ödeme tamamlandı ekranı gösteriliyor
                    StackPane root = new StackPane();
                    root.setPadding(new Insets(10));
                    root.getChildren().add(new Label("Ödeme tamamlandı."));
                    StackPane.setAlignment(root.getChildren().get(0), Pos.CENTER);
                    Scene scene = new Scene(root, 300, 150);
                    stage.setScene(scene);
                    stage.show();

                    if (ulasimAraci == "Uçak"){
                        // Veritabanı bağlantısı oluşturma
                        Connection conn;
                        try {
                            conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bilet.db");
                            // PNR numarasına göre bilet fiyatını sorgulama
                            String query = "SELECT fiyat FROM bilet WHERE pnr = ?";
                            PreparedStatement ps = conn.prepareStatement(query);
                            ps.setInt(1, pnr);
                            ResultSet rs = ps.executeQuery();
                    
                            // Bilet fiyatını al
                            if (rs.next()) {
                                biletFiyati = rs.getDouble("fiyat");
                            }
                            // Bağlantıyı kapat
                            rs.close();
                            ps.close();
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        double ucaktanPuan = biletFiyati * 0.01;
                        Connection conn3;
                        try {
                            conn3 = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/puan.db");
                            // Mevcut puanı sorgulama
                            String query3 = "SELECT miktar FROM puan";
                            PreparedStatement st3 = conn3.prepareStatement(query3);
                            ResultSet rs3 = st3.executeQuery();
                                // Mevcut puanı al
                                double puan = 0;
                                if (rs3.next()) {
                                    puan = rs3.getDouble("miktar");
                                }
                                puan += ucaktanPuan;
                                System.out.println("Uçaktan kazanılan puan: " + ucaktanPuan);
                                System.out.println("Toplam puan: " + puan);
                                String updateQuery = "UPDATE puan SET miktar = ?";
                                PreparedStatement ps3 = conn3.prepareStatement(updateQuery);
                                ps3.setDouble(1, puan);
                                ps3.executeUpdate();
                                ps3.close();
                                rs3.close();
                                st3.close();
                                conn3.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
    
                    }

                } else {
                    // Hatalı bilgi uyarısı ver
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hata");
                    alert.setHeaderText("Bilgiler hatalı");
                    alert.setContentText("Lütfen kart bilgilerini doğru formatta girin.");
                    alert.showAndWait();
                }

            });

            VBox cardBox = new VBox(10);
            cardBox.setPadding(new Insets(10));
            cardBox.getChildren().addAll(cardNumberLabel, cardNumberField, expiryDateLabel, expiryDateField, cvvLabel, cvvField, confirmButton);
            Scene cardScene = new Scene(cardBox, 300, 250);
            cardStage.setScene(cardScene);
            cardStage.show();
            
        } 
        else if (paymentMethod.equals("Çek"))
        {
            try {
                // Veritabanı bağlantısı oluşturma
                Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bilet.db");
        
                // PNR numarasına göre bilet fiyatını sorgulama
                String query = "SELECT fiyat FROM bilet WHERE pnr = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, pnr);
                ResultSet rs = ps.executeQuery();
        
                // Bilet fiyatını al
                if (rs.next()) {
                    biletFiyati = rs.getInt("fiyat");
                }
                // Bağlantıyı kapat
                rs.close();
                ps.close();
                conn.close();
        
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            try (Connection conn2 = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/cek.db")) {
                stmt = conn2.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id, miktar FROM cek");
    
                // Tabloyu oluşturmak için TableView kullanıyoruz
                TableView<cek> table = new TableView<>();
    
                // Sütunları tanımlıyoruz
                TableColumn<cek, Integer> idColumn = new TableColumn<>("Çek Numarası");
                idColumn.setCellValueFactory(new PropertyValueFactory<cek, Integer>("id"));
    
                TableColumn<cek, Integer> miktarColumn = new TableColumn<>("Miktar");
                miktarColumn.setCellValueFactory(new PropertyValueFactory<>("miktar"));
    
                table.getColumns().add(idColumn);
                table.getColumns().add(miktarColumn);
    
                // Verileri tabloya ekliyoruz
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int miktar = rs.getInt("miktar");
                    table.getItems().add(new cek(id, miktar));
                }
    
                // Tabloyu ekrana yazdırıyoruz
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10));
    
            Label label = new Label("Ödeme yapmak istediğiniz çeki seçin:");
            Button onaylaButton = new Button("Onayla");
    
            onaylaButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    cek secilenCek = table.getSelectionModel().getSelectedItem();
                    if (secilenCek == null) {
                        Alert alert = new Alert(AlertType.ERROR, "Lütfen bir çek seçin.");
                        alert.showAndWait();
                    } else {
                        double biletMiktar = biletFiyati;
                        System.out.println(biletMiktar);
                        System.out.println(biletFiyati);
                        int cekMiktar = secilenCek.getMiktar();
                        if (cekMiktar >= biletMiktar) {
                            cekMiktar -= biletMiktar;
                            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/cek.db")) {
                                String update = "UPDATE cek SET miktar = ? WHERE id = ?";
                                PreparedStatement pstmt = conn.prepareStatement(update);
                                pstmt.setInt(1, cekMiktar);
                                pstmt.setInt(2, secilenCek.getId());
                                pstmt.executeUpdate();
                                Alert alert = new Alert(AlertType.INFORMATION, "Ödeme yapıldı.");
                                alert.showAndWait();
                                Stage stage = (Stage) onaylaButton.getScene().getWindow();
                                stage.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alert = new Alert(AlertType.ERROR, "Çek yeterli tutarda değil.");
                            alert.showAndWait();
                            Stage stage = (Stage) onaylaButton.getScene().getWindow();
                            stage.close();
                        }
                        if (ulasimAraci == "Uçak"){
                            // Veritabanı bağlantısı oluşturma
                            Connection conn;
                            try {
                                conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bilet.db");
                                // PNR numarasına göre bilet fiyatını sorgulama
                                String query = "SELECT fiyat FROM bilet WHERE pnr = ?";
                                PreparedStatement ps = conn.prepareStatement(query);
                                ps.setInt(1, pnr);
                                ResultSet rs = ps.executeQuery();
                        
                                // Bilet fiyatını al
                                if (rs.next()) {
                                    biletFiyati = rs.getDouble("fiyat");
                                }
                                // Bağlantıyı kapat
                                rs.close();
                                ps.close();
                                conn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            double ucaktanPuan = biletFiyati * 0.01;
                            Connection conn3;
                            try {
                                conn3 = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/puan.db");
                                // Mevcut puanı sorgulama
                                String query3 = "SELECT miktar FROM puan";
                                PreparedStatement st3 = conn3.prepareStatement(query3);
                                ResultSet rs3 = st3.executeQuery();
                                    // Mevcut puanı al
                                    double puan = 0;
                                    if (rs3.next()) {
                                        puan = rs3.getDouble("miktar");
                                    }
                                    puan += ucaktanPuan;
                                    System.out.println("Uçaktan kazanılan puan: " + ucaktanPuan);
                                    System.out.println("Toplam puan: " + puan);
                                    String updateQuery = "UPDATE puan SET miktar = ?";
                                    PreparedStatement ps3 = conn3.prepareStatement(updateQuery);
                                    ps3.setDouble(1, puan);
                                    ps3.executeUpdate();
                                    ps3.close();
                                    rs3.close();
                                    st3.close();
                                    conn3.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
        
                        }
                    }
                }
            });
    
            VBox vbox = new VBox(label, table, onaylaButton);
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(10);
    
            root.setCenter(vbox);
            Scene scene = new Scene(root, 300, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   }

    // İptal butonuna tıklandığında çalışacak metot
    private void onCancelClicked() {
        stage.close(); // ödeme sayfasını kapat
    }

    public Integer getPnr() {
        return pnr;
    }

    public void setPnr(Integer pnr) {
        this.pnr = pnr;
    }

    public void showAndWait() {
        start (new Stage());
    }
}


