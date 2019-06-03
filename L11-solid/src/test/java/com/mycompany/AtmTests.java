package com.mycompany;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AtmTests {

    private Dispenser dispenser;
    private ATM atm;

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
        when(dispenser.putBanknotes(banknotes)).thenReturn(true);
        assertTrue(atm.acceptBanknotes(banknotes),
                "Banknotes are not accepted");
    }

    @Test
    void acceptUnsuccessfully() {
        final var banknotes = List.of(
                Banknote.FIVE_HUNDRED_RUB,
                Banknote.THOUSAND_RUB);
        when(dispenser.putBanknotes(banknotes)).thenReturn(false);
        assertFalse(atm.acceptBanknotes(banknotes),
                "Banknotes are accepted");
    }

    @Test
    void checkBalance() {
        when(dispenser.getAtmAmount()).thenReturn(100L);
        assertEquals(atm.checkBalance(), 100L,
                "Balance is not correct");
    }

    //todo: update api and fix the test
    @Test
    @Disabled
    void dispenseBanknotes() {
        final var banknotes = List.of(
                Banknote.FIVE_HUNDRED_RUB,
                Banknote.THOUSAND_RUB);
//        when(dispenser.checkFunds(1500L)).thenCallRealMethod();
//        when(dispenser.getBanknotes(Banknote.THOUSAND_RUB)).thenCallRealMethod();
//        when(dispenser.getBanknotes(Banknote.FIVE_HUNDRED_RUB)).thenCallRealMethod();
//        when(dispenser.getBanknotes(Banknote.HUNDRED_RUB)).thenCallRealMethod();
//        when(dispenser.getBanknotes(Banknote.FIFTY_RUB)).thenCallRealMethod();
        when(dispenser.dispense()).thenReturn(banknotes);
        assertTrue(atm.dispenseBanknotes(1500L).containsAll(banknotes));
    }
}
