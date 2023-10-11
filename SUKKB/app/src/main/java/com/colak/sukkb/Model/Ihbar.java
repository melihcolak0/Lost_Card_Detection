package com.colak.sukkb.Model;

public class Ihbar {

    private String AdSoyad, OgrNo, Birim, ResimURL, uId;

    public Ihbar(String AdSoyad, String OgrNo, String Birim, String ResimURL, String uId) {
        this.AdSoyad = AdSoyad;
        this.OgrNo = OgrNo;
        this.Birim = Birim;
        this.ResimURL = ResimURL;
        this.uId = uId;
    }

    public Ihbar() {
    }

    public String getAdSoyad() {
        return AdSoyad;
    }

    public void setAdSoyad(String AdSoyad) {
        this.AdSoyad = AdSoyad;
    }

    public String getOgrNo() {
        return OgrNo;
    }

    public void setOgrNo(String OgrNo) {
        this.OgrNo = OgrNo;
    }

    public String getBirim() {
        return Birim;
    }

    public void setBirim(String Birim) {
        this.Birim = Birim;
    }

    public String getResimURL() {
        return ResimURL;
    }

    public void setResimURL(String ResimURL) {
        this.ResimURL = ResimURL;
    }

    public String getuId() { return uId; }

    public void setuId(String uId) { this.uId = uId; }
}
