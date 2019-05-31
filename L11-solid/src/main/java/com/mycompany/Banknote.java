package com.mycompany;

public class Banknote {

    private final Nominal nominal;

    private Banknote(Nominal nominal) {
        this.nominal = nominal;
    }

    public static Banknote with(Nominal nominal) {
        return new Banknote(nominal);
    }

    public Nominal getNominal() {
        return nominal;
    }
}
