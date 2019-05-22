package com.mycompany;

import com.mycompany.Banknote.Nominal;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptBanknotesTests {

    @Test
    void acceptSuccessfully() {
        Collection<Banknote> banknotes = List.of(
                new Banknote(Nominal.FIFTY),
                new Banknote(Nominal.HUNDRED),
                new Banknote(Nominal.FIVE_HUNDRED),
                new Banknote(Nominal.THOUSAND));
        assertTrue(new ATM().acceptBanknotes(banknotes));
    }
}
