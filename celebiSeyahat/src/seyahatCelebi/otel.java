package seyahatCelebi;

public class otel {

    private String otelAdi;
    public String getOtelAdi() {
        return otelAdi;
    }

    public void setOtelAdi(String otelAdi) {
        this.otelAdi = otelAdi;
    }

    private String otelinYildizSayisi;
    public String getOtelinYildizSayisi() {
        return otelinYildizSayisi;
    }

    public void setOtelinYildizSayisi(String otelinYildizSayisi) {
        this.otelinYildizSayisi = otelinYildizSayisi;
    }

    private String konum;
    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    private String baslangicTarihi;
    public String getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public void setBaslangicTarihi(String baslangicTarihi) {
        this.baslangicTarihi = baslangicTarihi;
    }

    private String bitisTarihi;
    public String getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(String bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    private String gunSayisi;
    public String getGunSayisi() {
        return gunSayisi;
    }

    public void setGunSayisi(String gunSayisi) {
        this.gunSayisi = gunSayisi;
    }

    private String kisiSayisi;
    public String getKisiSayisi() {
        return kisiSayisi;
    }

    public void setKisiSayisi(String kisiSayisi) {
        this.kisiSayisi = kisiSayisi;
    }

    private String fiyat;

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public otel(String otelAdi, String otelinYildizSayisi, String konum, String baslangictarihi, String bitisTarihi, String gunSayisi,
            String kisiSayisi, String fiyat) {
        this.otelAdi = otelAdi;
        this.otelinYildizSayisi = otelinYildizSayisi;
        this.konum = konum;
        this.baslangicTarihi = baslangictarihi;
        this.bitisTarihi = bitisTarihi;
        this.gunSayisi = gunSayisi;
        this.kisiSayisi = kisiSayisi;
        this.fiyat = fiyat;
    }

}
