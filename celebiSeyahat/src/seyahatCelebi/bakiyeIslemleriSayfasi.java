package seyahatCelebi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class bakiyeIslemleriSayfasi {

    public void showAndWait() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        int mevcutBakiye = getMevcutBakiye();
        int mevcutPuan = getMevcutPuan();

        // GridPane oluştur
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));

        // Sütunlar için ColumnConstraints oluştur
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        
        // Başlık oluştur
        Text title = new Text("BAKİYE İŞLEMLERİ");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(title, 0, 0, 2, 1);
        
        // Boş bir satır ekle
        Text emptyText = new Text();
        grid.add(emptyText, 0, 1);
        
        // Mevcut bakiyeyi gösteren yazıyı ekle
        Text bakiyeText = new Text("Mevcut Bakiye: " + mevcutBakiye);
        bakiyeText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        StackPane bakiyePane = new StackPane(bakiyeText);
        bakiyePane.setAlignment(Pos.CENTER);
        grid.add(bakiyeText, 0, 2, 2, 1);
        
        // Mevcut puanı gösteren yazıyı ekle
        Text puanText = new Text("Mevcut Puan:" + mevcutPuan);
        puanText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        StackPane puanPane = new StackPane(puanText);
        puanPane.setAlignment(Pos.CENTER);
        grid.add(puanText, 0, 3, 2, 1);

        // Boş bir satır ekle
        Text emptyText2 = new Text();
        grid.add(emptyText2, 0, 4);
        
        // Para ekleme seçeneklerini oluştur
        Button nakitButton = new Button("Nakit");
        Button krediKartiButton = new Button("Kredi Kartı");
        Button cekButton = new Button("Çek");
        Button cuzdanaEkleButton = new Button("Puanı Cüzdana Ekle");
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(nakitButton, krediKartiButton, cekButton, cuzdanaEkleButton);
        grid.add(buttonContainer, 0, 5, 2, 1);
        
        // İşlem butonlarının olaylarını tanımla
        nakitButton.setOnAction(e -> {
            nakitParaEkle(bakiyeText);
        });
        
        krediKartiButton.setOnAction(e -> {
            krediKartiEkle(bakiyeText);
        });
        
        cekButton.setOnAction(e -> {
            cekIslemleri(bakiyeText);
        });

        cuzdanaEkleButton.setOnAction(e -> {
            // Mevcut bakiyeye puanı ekle
            int mevcutBakiye2 = getMevcutBakiye();
            int mevcutPuan2 = getMevcutPuan();
            
            mevcutBakiye2 += mevcutPuan2;

            // Veritabanında bakiye ve puanı güncelle
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bakiye.db")) {
                // Bakiye güncelleme sorgusu oluştur
                String bakiyeSql = "UPDATE bakiye SET miktar = ? WHERE id = 0";
                PreparedStatement bakiyeStatement = connection.prepareStatement(bakiyeSql);
                bakiyeStatement.setDouble(1, mevcutBakiye2);
                bakiyeStatement.executeUpdate();

            } catch (SQLException e2) {
                System.out.println("Veritabanı hatası: " + e2.getMessage());
            }
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/puan.db")) {
                // Puanı sıfırlama sorgusu oluştur
                String puanSql = "UPDATE puan SET miktar = 0";
                PreparedStatement puanStatement = connection.prepareStatement(puanSql);
                puanStatement.executeUpdate();

            } catch (SQLException e3) {
                System.out.println("Veritabanı hatası: " + e3.getMessage());
            }
            bakiyeText.setText("Mevcut Bakiye: " + mevcutBakiye2);
            int mevcutPuan3 = getMevcutPuan();
            puanText.setText("Mevcut Puan: " + mevcutPuan3);
        });

        // Scene'i oluştur
        Scene scene = new Scene(grid);
        
        // Stage'i ayarla ve göster
        stage.setTitle("Bakiye İşlemleri");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public int getMevcutBakiye() {
        int mevcutBakiye = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            veritabaniBakiyeBaglantisi connector = new veritabaniBakiyeBaglantisi();
            connection = connector.connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT miktar FROM bakiye WHERE id = 0");
            if (resultSet.next()) {
                mevcutBakiye = resultSet.getInt("miktar");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return mevcutBakiye;
    }

    public int getMevcutPuan() {
        int mevcutPuan = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            veritabaniPuanBaglantisi connector = new veritabaniPuanBaglantisi();
            connection = connector.connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT miktar FROM puan WHERE id = 0");
            if (resultSet.next()) {
                mevcutPuan = resultSet.getInt("miktar");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return mevcutPuan;
    }

    private void nakitParaEkle(Text bakiyeText) {
        // Yeni bir pencere oluştur
        Stage nakitParaEkleStage = new Stage();
        nakitParaEkleStage.initModality(Modality.WINDOW_MODAL);
    
        // GridPane oluştur
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));
    
        // Tutar giriş alanını oluştur
        TextField tutarField = new TextField();
        tutarField.setPromptText("Eklenecek Tutar");
    
        // Ekle butonunu oluştur
        Button ekleButton = new Button("Ekle");
    
        // Ekle butonuna basıldığında tutarı bakiyeye ekle
        ekleButton.setOnAction(event -> {
            String tutarStr = tutarField.getText();
            double tutar = Double.parseDouble(tutarStr);
    
            // Bakiyeyi güncelle
            double mevcutBakiye = getMevcutBakiye();
            double yeniBakiye = mevcutBakiye + tutar;

            // Veritabanı bağlantısı oluştur
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bakiye.db")) {
                // Güncelleme sorgusu oluştur
                String sql = "UPDATE bakiye SET miktar = ? WHERE id = 0";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setDouble(1, yeniBakiye);

                // Güncelleme işlemini gerçekleştir
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Bakiye güncellendi. Yeni bakiye: " + yeniBakiye);
                    bakiyeText.setText("Mevcut Bakiye: " + yeniBakiye);
                } else {
                    System.out.println("Bakiye güncellenemedi.");
                }
            } catch (SQLException e) {
                System.out.println("Veritabanı hatası: " + e.getMessage());
            }
            // Pencereyi kapat
            nakitParaEkleStage.close();
        });
    
        // GridPane'e bileşenleri ekle
        grid.add(tutarField, 0, 0);
        grid.add(ekleButton, 0, 1);
    
        // Scene'i oluştur
        Scene scene = new Scene(grid);
    
        // Stage'i ayarla ve göster
        nakitParaEkleStage.setTitle("Nakit Para Ekle");
        nakitParaEkleStage.setScene(scene);
        nakitParaEkleStage.showAndWait();
    }

    private void krediKartiEkle(Text bakiyeText) {
        // Yeni bir pencere oluştur
        Stage krediKartiEkleStage = new Stage();
        krediKartiEkleStage.initModality(Modality.WINDOW_MODAL);
    
        // GridPane oluştur
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));
    
        // Kredi Kartı No giriş alanını oluştur
        TextField kartNoField = new TextField();
        kartNoField.setPromptText("Kredi Kartı No");
    
        // Son Kullanma Tarihi giriş alanını oluştur
        TextField tarihField = new TextField();
        tarihField.setPromptText("Son Kullanma Tarihi (AA/YY)");
    
        // CVV giriş alanını oluştur
        TextField cvvField = new TextField();
        cvvField.setPromptText("CVV");

        // Tutar giriş alanını oluştur
        TextField tutarField = new TextField();
        tutarField.setPromptText("Tutar");
    
        // Ekle butonunu oluştur
        Button ekleButton = new Button("Ekle");
    
        // Ekle butonuna basıldığında kredi kartı bilgilerini kaydet
        ekleButton.setOnAction(event -> {
            String kartNo = kartNoField.getText();
            String tarih = tarihField.getText();
            String cvv = cvvField.getText();
            double tutar = Double.parseDouble(tutarField.getText());

            // Girişlerin doğruluğunu kontrol et
            if (kartNo.length() != 16 || !tarih.matches("\\d{2}/\\d{2}") || cvv.length() != 3) {
                // Hatalı giriş uyarısı ver
                System.out.println("Hatalı giriş!");
                return;
            }

            double mevcutBakiye = getMevcutBakiye();
            double yeniBakiye = mevcutBakiye + tutar;
            bakiyeText.setText(String.valueOf(yeniBakiye));

            // Veritabanı bağlantısı oluştur
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bakiye.db")) {
                // Güncelleme sorgusu oluştur
                String sql = "UPDATE bakiye SET miktar = ? WHERE id = 0";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setDouble(1, yeniBakiye);

                // Güncelleme işlemini gerçekleştir
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Bakiye güncellendi. Yeni bakiye: " + yeniBakiye);
                    bakiyeText.setText("Mevcut Bakiye: " + yeniBakiye);
                } else {
                    System.out.println("Bakiye güncellenemedi.");
                }
            } catch (SQLException e) {
                System.out.println("Veritabanı hatası: " + e.getMessage());
            }

            // Pencereyi kapat
            krediKartiEkleStage.close();
        });
    
        // GridPane'e öğeleri ekle
        grid.add(kartNoField, 0, 0);
        grid.add(tarihField, 0, 1);
        grid.add(cvvField, 0, 2);
        grid.add(tutarField, 0, 3);
        grid.add(ekleButton, 0, 4);
        
    
        // Yeni bir Scene oluştur ve Stage'e ekle
        Scene scene = new Scene(grid);
        krediKartiEkleStage.setScene(scene);
        krediKartiEkleStage.show();
    }
    
    private void cekIslemleri(Text bakiyeText) {
        // Yeni bir pencere oluştur
        Stage cekIslemleriStage = new Stage();
        cekIslemleriStage.initModality(Modality.WINDOW_MODAL);
    
        // GridPane oluştur
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));
    
        // Çekleri listeleyen TableView oluştur
        TableView<cek> cekTableView = new TableView<>();
        
        // Çekler tablosu için sütunları tanımla
        TableColumn<cek, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<cek, Integer>("id"));
        
        TableColumn<cek, Integer> miktarColumn = new TableColumn<>("Miktar");
        miktarColumn.setCellValueFactory(new PropertyValueFactory<cek, Integer>("miktar"));
        
        // Tabloya sütunları ekle
        cekTableView.getColumns().add(idColumn);
        cekTableView.getColumns().add(miktarColumn);
    
        // Veritabanı bağlantısı oluştur
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/cek.db")) {
            // Sorguyu hazırla ve çekleri al
            String sql = "SELECT id, miktar FROM cek";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
    
            // Her bir çeki döngüyle al ve listeye ekle
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int miktar = resultSet.getInt("miktar");
                cekTableView.getItems().add(new cek(id, miktar));
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        }
    
        // Bozdur butonunu oluştur
        Button bozdurButton = new Button("Bozdur");
    
        // Bozdur butonuna basıldığında seçilen çeki cüzdana ekle
        bozdurButton.setOnAction(event -> {
            cek selectedCek = cekTableView.getSelectionModel().getSelectedItem();
            if (selectedCek != null) {
                double tutar = selectedCek.getMiktar();
                
                // Çeki bozdur ve cüzdana ekleme işlemlerini burada gerçekleştirin
                double mevcutBakiye = getMevcutBakiye();
                double yeniBakiye = mevcutBakiye + tutar;
                bakiyeText.setText(String.valueOf(yeniBakiye));

                // Veritabanı bağlantısı oluştur
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bakiye.db")) {
                    // Güncelleme sorgusu oluştur
                    String sql = "UPDATE bakiye SET miktar = ? WHERE id = 0";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setDouble(1, yeniBakiye);

                    // Güncelleme işlemini gerçekleştir
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows > 0) {
                        // System.out.println("Bakiye güncellendi. Yeni bakiye: " + yeniBakiye);
                        bakiyeText.setText("Mevcut Bakiye: " + yeniBakiye);
                    } else {
                        System.out.println("Bakiye güncellenemedi.");
                    }
                } catch (SQLException e) {
                    System.out.println("Veritabanı hatası: " + e.getMessage());
                }

                // Veritabanı bağlantısı oluştur
                try (Connection connection2 = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/cek.db")) {
                    // Güncelleme sorgusu oluştur
                    String cekSql = "UPDATE cek SET miktar = 0 WHERE id = ?";
                    PreparedStatement cekStatement = connection2.prepareStatement(cekSql);
                    cekStatement.setInt(1, selectedCek.getId());

                    // Güncelleme işlemini gerçekleştir
                    int affectedRows = cekStatement.executeUpdate();
                    if (affectedRows > 0) {
                        // System.out.println("Bakiye güncellendi. Yeni bakiye: " + yeniBakiye);
                        bakiyeText.setText("Mevcut Bakiye: " + yeniBakiye);
                    } else {
                        System.out.println("Bakiye güncellenemedi.");
                    }
                } catch (SQLException e) {
                    System.out.println("Veritabanı hatası: " + e.getMessage());
                }
                
                // Pencereyi kapat
                cekIslemleriStage.close();
            }
        });
    
        // GridPane'e öğeleri ekle
        grid.add(cekTableView, 0, 0);
        grid.add(bozdurButton, 0, 1);
    
        // Yeni bir Scene oluştur ve Stage'e ekle
        Scene scene = new Scene(grid);
        cekIslemleriStage.setScene(scene);
        cekIslemleriStage.show();
    }
    
    
}
