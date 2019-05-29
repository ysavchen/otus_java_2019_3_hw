package com.mycompany;

import java.util.ArrayDeque;
import java.util.Deque;

class Cell {

    /**
     * Banknotes to store
     */
    private final Deque<Banknote> banknotes = new ArrayDeque<>();

    boolean putBanknote(Banknote banknote) {
        return banknotes.add(banknote);
    }

    Banknote getBanknote() {
        return banknotes.getFirst();
    }

    int numAvailableBanknotes() {
        return banknotes.size();
    }
}
