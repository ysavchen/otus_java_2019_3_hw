package com.mycompany;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptBanknotesTests {

    @Test
    void acceptSuccessfully() {
        final Account account = new Account()
                .setActive(true)
                .setBalance(new Balance().setAmount(100L));

        Collection<Banknote> banknotes = List.of(
                new Banknote(Nominal.FIFTY),
                new Banknote(Nominal.HUNDRED),
                new Banknote(Nominal.FIVE_HUNDRED),
                new Banknote(Nominal.THOUSAND));
        assertTrue(new ATM(account).acceptBanknotes(banknotes));
    }
}
