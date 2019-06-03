package com.mycompany;

import com.mycompany.exceptions.NoBanknotesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DispenserTests {

    private Dispenser dispenser;

    private final Cell cell_50 = new Cell(Banknote.FIFTY_RUB);
    private final Cell cell_100 = new Cell(Banknote.HUNDRED_RUB);
    private final Cell cell_500 = new Cell(Banknote.FIVE_HUNDRED_RUB);
    private final Cell cell_1000 = new Cell(Banknote.THOUSAND_RUB);

    private Map<Banknote, Cell> noteCellMap = Map.of(
            Banknote.FIVE_HUNDRED_RUB, cell_500,
            Banknote.FIFTY_RUB, cell_50,
            Banknote.THOUSAND_RUB, cell_1000,
            Banknote.HUNDRED_RUB, cell_100
    );

    @BeforeEach
    void prepareDispenser() {
        dispenser = new MinBanknotesDispenser(noteCellMap);
    }

    @Test
    void dispenseFullNeededAmount() {
        cell_1000.putBanknote();
        cell_100.putBanknote();
        assertEquals(2, dispenser.dispense(1500).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void dispensePartNeededAmount() {
        for (int i = 0; i < 3; i++) cell_100.putBanknote();
        assertEquals(2, dispenser.dispense(250).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void checkMinimalNumBanknotesDispensed() {
        for (int i = 0; i < 5; i++) cell_100.putBanknote();
        cell_500.putBanknote();
        assertEquals(1, dispenser.dispense(500).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void testNoBanknotesException() {
        cell_1000.putBanknote();
        assertThrows(NoBanknotesException.class,
                () -> dispenser.dispense(100));
    }

    @Test
    void testSomeErrors() {
        assertTrue(dispenser.dispense(0L).isEmpty(),
                "Banknotes for 0 amount is not empty");
        assertTrue(dispenser.dispense(-5L).isEmpty(),
                "Banknotes for -5 amount is not empty");
    }
}
