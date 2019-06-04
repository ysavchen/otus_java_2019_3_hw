package com.mycompany;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CellTests {

    @Test
    void putBanknotesSuccess() {
        Cell cell = new Cell(Banknote.HUNDRED_RUB);
        cell.putBanknote();
        assertEquals(1, cell.numAvailableNotes(),
                "Banknote was not put to a cell");
    }

    @Test
    void retrieveAllNeededNotes() {
        Cell cell = new Cell(Banknote.HUNDRED_RUB);
        for (int i = 0; i < 5; i++) {
            cell.putBanknote();
        }
        assertEquals(4, cell.retrieveBanknotes(4).size(),
                "Retrieved incorrect number of banknotes");
    }

    @Test
    void retrieveAllAvailableNotes() {
        Cell cell = new Cell(Banknote.HUNDRED_RUB);
        for (int i = 0; i < 5; i++) {
            cell.putBanknote();
        }
        assertEquals(5, cell.retrieveBanknotes(10).size(),
                "Retrieved incorrect number of banknotes");
    }

    @Test
    void cellWithNullBanknoteNotCreated() {
        assertThrows(NullPointerException.class,
                () -> new Cell(null));
    }
}
