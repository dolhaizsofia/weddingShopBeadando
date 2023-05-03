package com.example.beadandoo;

public class Termekek {
    private String neve;
    private String info;
    private String ar;
    private float csillagokSzama;
    private int imageResource;

    public Termekek(String neve, String info, String ar, float csillagokSzama, int imageResource) {
        this.neve = neve;
        this.info = info;
        this.ar = ar;
        this.csillagokSzama = csillagokSzama;
        this.imageResource = imageResource;
    }

    public String getNeve() {
        return neve;
    }

    public String getInfo() {
        return info;
    }

    public String getAr() {
        return ar;
    }

    public float getCsillagokSzama() {
        return csillagokSzama;
    }

    public int getImageResource() {
        return imageResource;
    }
}
