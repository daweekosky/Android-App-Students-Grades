package com.example.lab1czarnecki;

public class Przedmiot {
    private String nazwa;
    private int ocena;

    public Przedmiot(String nazwa) {
        this.nazwa = nazwa;
        this.ocena = 2;
    }

    public Przedmiot(String nazwa, int ocena) {
        this.nazwa = nazwa;
        this.ocena = ocena;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }
}
