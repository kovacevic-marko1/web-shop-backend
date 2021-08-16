package model;

import java.util.ArrayList;

public class Kategorija {
    private static int ID = 0;
    private int id;
    private String ime;


    public Kategorija(String ime) {
        this.id = ++ID;
        this.ime = ime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
