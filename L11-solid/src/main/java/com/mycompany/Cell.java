package com.mycompany;

import java.util.ArrayDeque;
import java.util.Deque;

class Cell {

    /**
     * Nominal of banknotes to be stored
     */
    private final Nominal nominal;

    /**
     * Banknotes to store
     */
    private final Deque<Banknote> banknotes = new ArrayDeque<>();

    Cell(Nominal nominal) {
        this.nominal = nominal;
    }

    boolean putBanknote(Banknote banknote) {
        if (banknote.getNominal() == nominal) {
            return banknotes.add(banknote);
        }
        System.out.println("Banknote nominal is not correct");
        return false;
    }

    Banknote getBanknote() {
        return banknotes.getFirst();
    }
}
