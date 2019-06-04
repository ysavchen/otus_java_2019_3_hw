package com.mycompany;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AtmDispenserTests {

    private Dispenser dispenser;
    private ATM atm;

    private Map<Banknote, Cell> noteCellMap = Map.of(
            Banknote.FIVE_HUNDRED_RUB, new Cell(Banknote.FIVE_HUNDRED_RUB),
            Banknote.THOUSAND_RUB, new Cell(Banknote.THOUSAND_RUB)
    );

    @BeforeEach
    void prepareAtm() {
        dispenser = new MinBanknotesDispenser(noteCellMap);
        atm = new ATMImpl(dispenser);
    }

    @Test
    void checkBalanceAfterDispense() {
        final var banknotes = List.of(
                Banknote.THOUSAND_RUB,
                Banknote.FIVE_HUNDRED_RUB);

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertEquals(atm.getBalance(), 1500L,
                "Balance is not correct");
        atm.dispenseBanknotes(500L);
        assertEquals(atm.getBalance(), 1000L,
                "Balance is not correct");
    }
}
