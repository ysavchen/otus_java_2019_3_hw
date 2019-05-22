package com.mycompany;

public class Banknote {

    private final Nominal nominal;

    public Banknote(Nominal nominal) {
        this.nominal = nominal;
    }

    public enum Nominal {
        FIFTY, HUNDRED, FIVE_HUNDRED, THOUSAND
    }

    public Nominal getNominal() {
        return nominal;
    }
}
