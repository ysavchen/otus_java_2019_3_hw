package com.mycompany;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AcceptBanknotesTests {

    @Test
    void acceptSuccessfully() {
        final ATM atm = new ATM();
        final var banknotes = List.of(
                Banknote.with(Nominal.FIFTY),
                Banknote.with(Nominal.HUNDRED),
                Banknote.with(Nominal.FIVE_HUNDRED),
                Banknote.with(Nominal.THOUSAND));

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
    }
}
