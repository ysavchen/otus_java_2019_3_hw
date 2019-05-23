package com.mycompany;

class Account {

    private boolean isActive;

    private Balance balance;

    public boolean isActive() {
        return isActive;
    }

    public Account setActive(boolean active) {
        isActive = active;
        return this;
    }

    public Balance getBalance() {
        return balance;
    }

    public Account setBalance(Balance balance) {
        this.balance = balance;
        return this;
    }
}
