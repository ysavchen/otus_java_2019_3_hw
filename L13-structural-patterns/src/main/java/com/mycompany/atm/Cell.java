package com.mycompany.atm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Cell {

    /**
     * Banknote type stored by a cell
     */
    private final Banknote banknote;

    /**
     * Number of banknotes stored by a cell
     */
    private long numAvailableNotes;

    Cell(Banknote banknote) {
        Objects.requireNonNull(banknote);
        this.banknote = banknote;
    }

    void putBanknote() {
        numAvailableNotes++;
    }

    /**
     * Retrieves banknotes from a cell.<p>
     * If numNotes < maxNumNotes, then all available banknotes are retrieved.
     *
     * @param maxNumNotes number of notes
     * @return list of banknotes
     */
    List<Banknote> retrieveBanknotes(int maxNumNotes) {
        final List<Banknote> banknotes = new ArrayList<>();
        long notesToRetrieve = (maxNumNotes < numAvailableNotes) ? maxNumNotes : numAvailableNotes;

        for (int i = 0; i < notesToRetrieve; i++) {
            banknotes.add(banknote);
        }
        numAvailableNotes -= notesToRetrieve;
        return banknotes;
    }

    long numAvailableNotes() {
        return numAvailableNotes;
    }
}
