package com.mycompany;

import com.mycompany.exceptions.InsufficientFundsException;
import com.mycompany.exceptions.NoBanknotesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DispenserTests {

    private Dispenser dispenser;
    private ATM atm;

    @BeforeEach
    void prepareAtm() {
        dispenser = Mockito.mock(Dispenser.class);
        atm = new ATMImpl(dispenser);
    }

    private Map<Banknote, Cell> noteCellMap = Map.of(
            Banknote.FIFTY_RUB, new Cell(),
            Banknote.HUNDRED_RUB, new Cell(),
            Banknote.FIVE_HUNDRED_RUB, new Cell(),
            Banknote.THOUSAND_RUB, new Cell()
    );

    @Test
    void testBrokenDispense() {
        final Dispenser dispenser = new DispenserImpl(noteCellMap);
        dispenser.putBanknotes(List.of(Banknote.FIVE_HUNDRED_RUB, Banknote.THOUSAND_RUB));
        assertThrows(NullPointerException.class, () -> dispenser.checkFunds(1500).getBanknotes(Banknote.THOUSAND_RUB).getBanknotes(null).dispense());
        assertDoesNotThrow(
                () -> dispenser.checkFunds(1500)
                        .getBanknotes(Banknote.THOUSAND_RUB)
                        .getBanknotes(Banknote.THOUSAND_RUB)
                        .dispense()
        );
    }

    @Test
    void dispenseNotesPositive() {
        final var banknotes = List.of(
                Banknote.THOUSAND_RUB,
                Banknote.THOUSAND_RUB,
                Banknote.THOUSAND_RUB);

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");

        assertEquals(1, atm.dispenseBanknotes(1500).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void testInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class,
                () -> atm.dispenseBanknotes(100));
    }

    @Test
    void testNoBanknotesException() {
        assertTrue(atm.acceptBanknotes(Banknote.THOUSAND_RUB),
                "Banknotes are not accepted");
        assertThrows(NoBanknotesException.class,
                () -> atm.dispenseBanknotes(100));
    }

    @Test
    void checkNotAllNeededAmountDispensed() {
        final var banknotes = List.of(
                Banknote.HUNDRED_RUB,
                Banknote.HUNDRED_RUB);

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertEquals(1, atm.dispenseBanknotes(150).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void checkMinimalNumBanknotesDispensed() {
        final var banknotes = List.of(
                Banknote.HUNDRED_RUB,
                Banknote.HUNDRED_RUB,
                Banknote.HUNDRED_RUB,
                Banknote.HUNDRED_RUB,
                Banknote.HUNDRED_RUB,
                Banknote.FIVE_HUNDRED_RUB);
        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertEquals(1, atm.dispenseBanknotes(500).size(),
                "Invalid number of dispensed banknotes");
    }
}
