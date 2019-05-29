package com.mycompany;

class Account {

    private boolean isActive;

    private Balance balance;

    boolean isActive() {
        return isActive;
    }

    Account setActive(boolean active) {
        isActive = active;
        return this;
    }

    Balance getBalance() {
        return balance;
    }

    Account setBalance(Balance balance) {
        this.balance = balance;
        return this;
    }
}
