package com.mycompany.atm;

import com.mycompany.atm.exceptions.InsufficientFundsException;
import com.mycompany.atm.exceptions.NoSuchCellException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AtmTests {

    private Dispenser dispenser;
    private ATM atm;

    private Map<Banknote, Cell> noteCellMap = Map.of(
            Banknote.HUNDRED_RUB, new Cell(Banknote.HUNDRED_RUB),
            Banknote.FIVE_HUNDRED_RUB, new Cell(Banknote.FIVE_HUNDRED_RUB),
            Banknote.THOUSAND_RUB, new Cell(Banknote.THOUSAND_RUB)
    );

    @BeforeEach
    void prepareAtm() {
        dispenser = Mockito.mock(Dispenser.class);
        atm = new ATMImpl(dispenser, noteCellMap);
    }

    @Test
    void acceptSuccessfully() {
        final var banknotes = List.of(
                Banknote.FIVE_HUNDRED_RUB,
                Banknote.HUNDRED_RUB);
        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
    }

    @Test
    void checkBalance() {
        final var banknotes = List.of(
                Banknote.THOUSAND_RUB,
                Banknote.HUNDRED_RUB);

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertEquals(atm.getBalance(), 1100L,
                "Balance is not correct");
    }

    @Test
    void dispenseBanknotes() {
        final var banknotes = List.of(
                Banknote.FIVE_HUNDRED_RUB,
                Banknote.THOUSAND_RUB);
        when(dispenser.dispense(1500L, noteCellMap)).thenReturn(banknotes);

        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
        assertTrue(atm.dispenseBanknotes(1500L).containsAll(banknotes));
    }

    @Test
    void testNoSuchCellException() {
        assertThrows(NoSuchCellException.class,
                () -> atm.acceptBanknotes(Banknote.FIFTY_RUB));
    }

    @Test
    void testInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class,
                () -> atm.dispenseBanknotes(100L));
    }
}
