package com.mycompany;

class Account {

    private final boolean isActive;

    private final Balance balance;

    Account(boolean isActive, Balance balance) {
        this.isActive = isActive;
        this.balance = balance;
    }

    boolean isActive() {
        return isActive;
    }

    Account setActive(boolean newActive) {
        return new Account(newActive, balance);
    }

    Balance getBalance() {
        return balance;
    }

    Account setBalance(Balance newBalance) {
        return new Account(isActive, newBalance);
    }
}
