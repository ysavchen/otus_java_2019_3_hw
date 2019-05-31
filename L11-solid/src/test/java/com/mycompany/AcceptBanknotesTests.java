package com.mycompany;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AcceptBanknotesTests {

    @Test
    void acceptSuccessfully() {
        final ATM atm = new ATM();
        Collection<Banknote> banknotes = List.of(
                new Banknote(Nominal.FIFTY),
                new Banknote(Nominal.HUNDRED),
                new Banknote(Nominal.FIVE_HUNDRED),
                new Banknote(Nominal.THOUSAND));

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
    }

    @Test
    void amountIsIncreasedForClientAccount() {
        final ATM atm = new ATM();
        assertTrue(atm.acceptBanknotes(new Banknote(Nominal.HUNDRED)));
        assertEquals(atm.checkBalance(), 100L);
    }
}
