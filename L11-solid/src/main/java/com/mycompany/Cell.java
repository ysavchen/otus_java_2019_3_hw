package com.mycompany;

import java.util.ArrayList;
import java.util.List;

class Cell {

    /**
     * Banknotes to store
     */
    private final List<Banknote> banknotes = new ArrayList<>();

    boolean putBanknote(Banknote banknote) {
        return banknotes.add(banknote);
    }

    List<Banknote> retrieveBanknotes(int maxNumNotes) {
        if (maxNumNotes < banknotes.size()) {
            return banknotes.subList(0, maxNumNotes);
        }
        return banknotes;
    }

    int numAvailableNotes() {
        return banknotes.size();
    }
}
