package seyahatCelebi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class testKrediKartiOdeme {
    public static void main(String[] args) {
        testKrediKartiylaOdeme();
    }
    
    public static void testKrediKartiylaOdeme() {
        // Test için gerekli başlangıç değerleri oluşturulur
        String paymentMethod = "Kredi Kartı";
        String cardNumber = "1234567890123456";
        String expiryDate = "12/24";
        String cvv = "123";
        String otelAdi = "Örnek Otel";
        double otelFiyati = 300.0;
        double beklenenOtelFiyati = 300.0;
        
        // Kredi kartı bilgileri girilir
        if (paymentMethod.equals("Kredi Kartı") && cardNumber.matches("\\d{16}")
                && expiryDate.matches("\\d{2}/\\d{2}") && cvv.matches("\\d{3}")) {
            // Ödeme tamamlandı, otel fiyatı alındı
            System.out.println("Ödeme tamamlandı.");
            try {
                // Veritabanı bağlantısı oluşturma
                Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/otel.db");

                // Otel adına göre fiyat sorgulama
                String query = "SELECT fiyat FROM otel WHERE otelAdi = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, otelAdi);
                ResultSet rs = ps.executeQuery();

                // Bilet fiyatını al
                if (rs.next()) {
                    otelFiyati = rs.getDouble("fiyat");
                }
                // Bağlantıyı kapat
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Beklenen otel fiyatıyla gerçek otel fiyatı karşılaştırılır
            if (otelFiyati == beklenenOtelFiyati) {
                System.out.println("Test Başarılı"); // Test başarılı
            } else {
                System.out.println("Test Başarısız"); // Test başarısız
            }
        } else {
            System.out.println("Test Başarısız"); // Test başarısız
        }
    }
}
