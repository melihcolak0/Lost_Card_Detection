package com.colak.sukkb.Model;

public class Kullanici {
    private String kullaniciAdSoyad, kullaniciEposta, kullaniciId, kullaniciFakulte, kullaniciBolum;

    public Kullanici(String kullaniciAdSoyad, String kullaniciEposta, String kullaniciId, String kullaniciFakulte, String kullaniciBolum) {
        this.kullaniciAdSoyad = kullaniciAdSoyad;
        this.kullaniciEposta = kullaniciEposta;
        this.kullaniciId = kullaniciId;
        this.kullaniciFakulte = kullaniciFakulte;
        this.kullaniciBolum = kullaniciBolum;
    }

    public Kullanici() {
    }

    public String getKullaniciAdSoyad() {
        return kullaniciAdSoyad;
    }

    public void setKullaniciAdSoyad(String kullaniciAdSoyad) { this.kullaniciAdSoyad = kullaniciAdSoyad; }

    public String getKullaniciEposta() {
        return kullaniciEposta;
    }

    public void setKullaniciEposta(String kullaniciEposta) { this.kullaniciEposta = kullaniciEposta; }

    public String getKullaniciId() { return kullaniciId; }

    public void setKullaniciId(String kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciFakulte() { return kullaniciFakulte; }

    public void setKullaniciFakulte(String kullaniciFakulte) { this.kullaniciFakulte = kullaniciFakulte; }

    public String getKullaniciBolum() { return kullaniciBolum; }

    public void setKullaniciBolum(String kullaniciBolum) { this.kullaniciBolum = kullaniciBolum; }
}
