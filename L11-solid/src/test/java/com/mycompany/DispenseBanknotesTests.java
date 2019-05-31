package com.mycompany;

import com.mycompany.exceptions.InsufficientFundsException;
import com.mycompany.exceptions.NoBanknotesException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DispenseBanknotesTests {

    @Test
    void dispenseNotesPositive() {
        final ATM atm = new ATM();
        final var banknotes = List.of(
                Banknote.with(Nominal.THOUSAND),
                Banknote.with(Nominal.THOUSAND),
                Banknote.with(Nominal.THOUSAND));
        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");

        assertEquals(1, atm.dispenseBanknotes(1500).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void testInsufficientFundsException() {
        final ATM atm = new ATM();
        assertThrows(InsufficientFundsException.class,
                () -> atm.dispenseBanknotes(100));
    }

    @Test
    void testNoBanknotesException() {
        final ATM atm = new ATM();
        assertTrue(atm.acceptBanknotes(Banknote.with(Nominal.THOUSAND)),
                "Banknotes are not accepted");
        assertThrows(NoBanknotesException.class,
                () -> atm.dispenseBanknotes(100));
    }

    @Test
    void checkNotAllNeededAmountDispensed() {
        final ATM atm = new ATM();
        final var banknotes = List.of(
                Banknote.with(Nominal.HUNDRED),
                Banknote.with(Nominal.HUNDRED));

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertEquals(1, atm.dispenseBanknotes(150).size(),
                "Invalid number of dispensed banknotes");
    }

    @Test
    void checkMinimalNumBanknotesDispensed() {
        final ATM atm = new ATM();
        final var banknotes = List.of(
                Banknote.with(Nominal.HUNDRED),
                Banknote.with(Nominal.HUNDRED),
                Banknote.with(Nominal.HUNDRED),
                Banknote.with(Nominal.HUNDRED),
                Banknote.with(Nominal.HUNDRED),
                Banknote.with(Nominal.FIVE_HUNDRED));
        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertEquals(1, atm.dispenseBanknotes(500).size(),
                "Invalid number of dispensed banknotes");
    }
}
