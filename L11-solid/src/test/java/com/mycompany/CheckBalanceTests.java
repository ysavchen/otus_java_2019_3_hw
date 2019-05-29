package com.mycompany;

import com.mycompany.exceptions.InactiveAccountException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckBalanceTests {

    @Test
    void checkBalanceOfActiveAccount() {
        final Account account = new Account()
                .setActive(true)
                .setBalance(new Balance().setAmount(100L));
        assertEquals(new ATM(account).checkBalance(), 100L);
    }

    @Test
    void checkBalanceOfInactiveAccount() {
        final Account account = new Account()
                .setActive(false)
                .setBalance(new Balance().setAmount(100L));
        assertThrows(InactiveAccountException.class,
                () -> new ATM(account).checkBalance());
    }
}
