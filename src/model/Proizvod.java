package model;

public class Proizvod {
    private static int ID;
    private int id;
    private String ime;
    private double cena;
    private String opis;
    private Kategorija kategorija;

    public Proizvod(String ime, double cena, String opis, Kategorija kategorija) {
        this.id = ++ID;
        this.ime = ime;
        this.cena = cena;
        this.opis = opis;
        this.kategorija = kategorija;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }
}
