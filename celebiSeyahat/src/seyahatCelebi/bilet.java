package seyahatCelebi;

public class bilet {
    private Integer pnr;
    private String kalkisYeri;
    private String varisYeri;
    private String tarihStr;
    private String ulasimAraci;
    private String fiyat;

    public bilet(Integer pnr, String kalkisYeri, String varisYeri, String tarihStr, String ulasimAraci, String fiyat) {  
        
        this.pnr = pnr;
        this.kalkisYeri = kalkisYeri;
        this.varisYeri = varisYeri;
        this.tarihStr = tarihStr;
        this.ulasimAraci = ulasimAraci;
        this.fiyat = fiyat;

    }

    public Integer getPnr() {
        return pnr;
    }
    public void setPnr(Integer pnr) {
        this.pnr = pnr;
    }
    public String getKalkisYeri() {
        return kalkisYeri;
    }
    public void setKalkisYeri(String kalkisYeri) {
        this.kalkisYeri = kalkisYeri;
    }
    public String getVarisYeri() {
        return varisYeri;
    }
    public void setVarisYeri(String varisYeri) {
        this.varisYeri = varisYeri;
    }
    public String getTarih() {
        return tarihStr;
    }
    public void setTarih(String tarihStr) {
        this.tarihStr = tarihStr;
    }
    public String getUlasimAraci() {
        return ulasimAraci;
    }
    public void setUlasimAraci(String ulasimAraci) {
        this.ulasimAraci = ulasimAraci;
    }

    public String getFiyat() {
        return fiyat;
    }
    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public void setPropertyValueFactory(String string) {
    }

    public void setCellValueFactory(String string) {
    }

    public void setCellValueFactory(String string, String string2) {
    }

    public void getPropertyValueFactory(String string) {
    }

    public void getCellValueFactory(String string) {
    }

    public void getCellValueFactory(String string, String string2) {
    }
    public Object getPnrProperty() {
        return null;
    }

}
