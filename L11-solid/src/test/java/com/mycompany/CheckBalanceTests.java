package com.mycompany;

import com.mycompany.exceptions.InactiveAccountException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckBalanceTests {

    @Test
    void checkBalanceOfActiveAccount() {
        final Account account = new Account(true, new Balance(100L));
        assertEquals(ATM.loginWith(account).checkBalance(), 100L,
                "Balance is not correct");
    }

    @Test
    void checkBalanceOfInactiveAccount() {
        final Account account = new Account(false, new Balance(100L));
        assertThrows(InactiveAccountException.class,
                () -> ATM.loginWith(account).checkBalance(),
                "Exception is not thrown");
    }
}
