package seyahatCelebi;

import java.sql.*;

public class testCuzdanOdeme {

    public static void main(String[] args) {
        testCuzdanlaOdeme();
    }

    public static void testCuzdanlaOdeme() {
        try {
            // Örnek veritabanı bağlantısı oluşturma
            Connection conn = DriverManager.getConnection("jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/bakiye.db");

            // Test için örnek otel adı ve fiyatı
            int otelFiyati = 100;

            // Örnek cüzdan bakiyesi
            int cuzdanBakiyesi = 200;

            // Beklenen değer
            int beklenenBakiye = 100;

            // Test öncesinde cüzdan bakiyesini veritabanına ekleyin
            String insertQuery = "INSERT INTO bakiye (miktar) VALUES (?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setInt(1, cuzdanBakiyesi);
            insertStatement.executeUpdate();

            // Ödeme işlemi için test edilecek kod parçacığı
            if (otelFiyati <= cuzdanBakiyesi) {
                cuzdanBakiyesi -= otelFiyati;

                // Cüzdan bakiyesini güncelle
                String updateQuery = "UPDATE bakiye SET miktar = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                updateStatement.setInt(1, cuzdanBakiyesi);
                updateStatement.executeUpdate();

                if(beklenenBakiye == cuzdanBakiyesi) {
                    System.out.println("Beklenen bakiye ile güncellenen bakiye aynı.");
                    // Test başarılı, beklenen sonuçlar elde edildi
                    System.out.println("Test başarılı.");
                } else {
                    System.out.println("Beklenen bakiye ile güncellenen bakiye farklı.");
                    // Test başarısız, beklenen sonuçlar elde edilemedi
                    System.out.println("Test başarısız.");
                }

                // Ödeme işlemi başarılı olduğunda yapılacak işlemler
                System.out.println("Mevcut Bakiye:" + cuzdanBakiyesi);
                System.out.println("Ödeme işlemi tamamlandı. Kalan bakiye: " + cuzdanBakiyesi);

                // Puan hesaplama ve güncelleme işlemlerini de buraya ekleyebilirsiniz


            } else {
                // Yeterli bakiye yok uyarısı ver
                System.out.println("Yeterli bakiye yok.");

                // Test başarısız, beklenen sonuçlar elde edilemedi
                System.out.println("Test başarısız.");
            }

            // Veritabanı bağlantısını kapat
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // Test başarısız, bir hata oluştu
            System.out.println("Test başarısız.");
        }
    }
}

