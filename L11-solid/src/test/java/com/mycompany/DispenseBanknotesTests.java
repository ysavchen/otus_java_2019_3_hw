package com.mycompany;

import com.mycompany.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DispenseBanknotesTests {

    @Test
    void dispenseBanknotesPositive() {
        final ATM atm = new ATM();
        Collection<Banknote> banknotes = List.of(
                new Banknote(Nominal.THOUSAND),
                new Banknote(Nominal.THOUSAND),
                new Banknote(Nominal.THOUSAND));
        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");

        assertEquals(1, atm.dispenseBanknotes(1500).size());
    }

    @Test
    void dispenseBanknotesInsufficientFunds() {
        final ATM atm = new ATM();
        assertThrows(InsufficientFundsException.class,
                () -> atm.dispenseBanknotes(100));
    }
}
