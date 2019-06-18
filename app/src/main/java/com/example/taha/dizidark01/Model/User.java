package com.example.taha.dizidark01.Model;

public class User {
    private String id;
    private String isim;
    private String soyad;
    private String kullaniciadi;
    private String ProfilePhoto;
    private String email;
    private String sifre;
    private String yetki;


    private User() {
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private User(String isim, String soyad, String kullaniciadi, String email, String sifre, String yetki){
        this.isim = isim;
        this.soyad = soyad;
        this.kullaniciadi = kullaniciadi;
        this.email = email;
        this.sifre = sifre;
        this.yetki = yetki;
    }


    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getKullaniciadi() {
        return kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getYetki() {
        return yetki;
    }

    public void setYetki(String yetki) {
        this.yetki = yetki;
    }
}
