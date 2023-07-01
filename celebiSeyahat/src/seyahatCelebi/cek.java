package seyahatCelebi;

public class cek {

    private Integer id;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer miktar;
    public Integer getMiktar() {
        return miktar;
    }

    public void setMiktar(Integer miktar) {
        this.miktar = miktar;
    }

    public cek(Integer id, Integer miktar) {  
        
        this.id = id;
        this.miktar = miktar;

    }
    
}
