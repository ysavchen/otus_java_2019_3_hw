package com.mycompany;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckBalanceTests {

    @Test
    void checkBalanceAfterAccept() {
        final ATM atm = new ATM();
        assertTrue(atm.acceptBanknotes(Banknote.with(Nominal.HUNDRED)),
                "Banknotes are not accepted");

        assertEquals(atm.checkBalance(), 100L,
                "Balance is not correct");
    }

    @Test
    void checkBalanceAfterDispense() {
        final ATM atm = new ATM();
        final var banknotes = List.of(
                Banknote.with(Nominal.THOUSAND),
                Banknote.with(Nominal.FIVE_HUNDRED),
                Banknote.with(Nominal.HUNDRED),
                Banknote.with(Nominal.HUNDRED));

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertEquals(2, atm.dispenseBanknotes(600).size(),
                "Invalid number of dispensed banknotes");
        assertEquals(1100L, atm.checkBalance(),
                "Balance is not correct");
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
        assertEquals(100L, atm.checkBalance(),
                "Balance is not correct");
    }
}
