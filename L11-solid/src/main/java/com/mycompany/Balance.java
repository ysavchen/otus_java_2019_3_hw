package com.mycompany;

class Balance {

    private final long amount;

    Balance(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public Balance addToAmount(long amountToAdd) {
        return new Balance(amountToAdd + amount);
    }
}
