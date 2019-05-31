package com.mycompany;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckBalanceTests {

    @Test
    void checkBalancePositive() {
        final ATM atm = new ATM();
        assertTrue(atm.acceptBanknotes(new Banknote(Nominal.HUNDRED)),
                "Banknotes are not accepted");

        assertEquals(atm.checkBalance(), 100L,
                "Balance is not correct");
    }
}
