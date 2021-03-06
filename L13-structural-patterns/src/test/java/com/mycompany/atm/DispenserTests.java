package com.mycompany.atm;

import com.mycompany.atm.exceptions.NoBanknotesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DispenserTests {

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
        dispenser = new MinBanknotesDispenser();
    }

    @Test
    void dispenseFullNeededAmount() {
        cell_1000.putBanknote();
        cell_100.putBanknote();
        assertEquals(2, dispenser.dispense(1100, noteCellMap).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void dispensePartNeededAmount() {
        for (int i = 0; i < 3; i++) {
            cell_100.putBanknote();
        }
        assertThrows(NoBanknotesException.class,
                () -> dispenser.dispense(250, noteCellMap).size(),
                "Invalid number of dispensed banknotes");
        assertEquals(3, dispenser.dispense(300, noteCellMap).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void checkMinimalNumBanknotesDispensed() {
        for (int i = 0; i < 5; i++) {
            cell_100.putBanknote();
        }
        cell_500.putBanknote();
        assertEquals(1, dispenser.dispense(500, noteCellMap).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void testNoBanknotesException() {
        cell_1000.putBanknote();
        assertThrows(NoBanknotesException.class,
                () -> dispenser.dispense(100, noteCellMap));
    }

    @Test
    void testSomeErrors() {
        assertTrue(dispenser.dispense(0L, noteCellMap).isEmpty(),
                "Banknotes for 0 amount is not empty");
        assertTrue(dispenser.dispense(-5L, noteCellMap).isEmpty(),
                "Banknotes for -5 amount is not empty");
    }
}
