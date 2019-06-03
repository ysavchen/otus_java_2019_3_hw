package com.mycompany;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DispenseTests {

    private Dispenser dispenser;

    private Cell fiftyCell = new Cell(Banknote.FIFTY_RUB);
    private Cell hundredCell = new Cell(Banknote.HUNDRED_RUB);
    private Cell fiveHundredCell = new Cell(Banknote.FIVE_HUNDRED_RUB);
    private Cell thousandCell = new Cell(Banknote.THOUSAND_RUB);

    private Map<Banknote, Cell> noteCellMap = Map.of(
            Banknote.FIVE_HUNDRED_RUB, fiveHundredCell,
            Banknote.FIFTY_RUB, fiftyCell,
            Banknote.THOUSAND_RUB, thousandCell,
            Banknote.HUNDRED_RUB, hundredCell
    );

    @BeforeEach
    void prepareDispenser() {
        dispenser = new MinBanknotesDispenser(noteCellMap);
    }

    @Test
    void dispenseNotesPositive() {
        thousandCell.putBanknote();
        fiveHundredCell.putBanknote();
        assertEquals(2, dispenser.dispense(1500).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void checkMinimalNumBanknotesDispensed() {
        for (int i = 0; i < 5; i++) hundredCell.putBanknote();
        fiveHundredCell.putBanknote();
        assertEquals(1, dispenser.dispense(500).size(),
                "Invalid number of dispensed banknotes");
    }
}
