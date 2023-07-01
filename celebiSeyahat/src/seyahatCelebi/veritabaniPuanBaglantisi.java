package seyahatCelebi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class veritabaniPuanBaglantisi {

    public Connection connect() {
        Connection connection = null;
        try {
            String url = "jdbc:sqlite:celebiSeyahat/src/seyahatCelebi/db/puan.db"; // SQLite veritabanı dosyasının yolunu belirtin
            connection = DriverManager.getConnection(url);
            System.out.println("Veritabanına bağlantı başarılı.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
