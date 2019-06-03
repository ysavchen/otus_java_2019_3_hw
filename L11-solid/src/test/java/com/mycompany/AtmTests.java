package com.mycompany;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class AtmTests {

    private Dispenser dispenser;
    private ATM atm;

    private Map<Banknote, Cell> noteCellMap = Map.of(
            Banknote.FIFTY_RUB, new Cell(Banknote.FIFTY_RUB),
            Banknote.HUNDRED_RUB, new Cell(Banknote.HUNDRED_RUB),
            Banknote.FIVE_HUNDRED_RUB, new Cell(Banknote.FIVE_HUNDRED_RUB),
            Banknote.THOUSAND_RUB, new Cell(Banknote.THOUSAND_RUB)
    );

    @BeforeEach
    void prepareAtm() {
        dispenser = Mockito.mock(Dispenser.class);
        atm = new ATMImpl(dispenser);
    }

    @Test
    void acceptSuccessfully() {
        final var banknotes = List.of(
                Banknote.FIFTY_RUB,
                Banknote.HUNDRED_RUB);
        when(dispenser.getStorage()).thenReturn(noteCellMap);
        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
    }

    @Test
    void checkBalance() {
        final var banknotes = List.of(
                Banknote.FIFTY_RUB,
                Banknote.HUNDRED_RUB);
        when(dispenser.getStorage()).thenReturn(noteCellMap);

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertEquals(atm.getBalance(), 150L,
                "Balance is not correct");
    }

    @Test
    void dispenseBanknotes() {
        final var banknotes = List.of(
                Banknote.FIVE_HUNDRED_RUB,
                Banknote.THOUSAND_RUB);
        when(dispenser.getStorage()).thenReturn(noteCellMap);
        when(dispenser.dispense(1500L)).thenReturn(banknotes);

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertTrue(atm.dispenseBanknotes(1500L).containsAll(banknotes));
    }
}
