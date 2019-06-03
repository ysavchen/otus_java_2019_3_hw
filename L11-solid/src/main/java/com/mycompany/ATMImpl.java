package com.mycompany;

import com.mycompany.exceptions.InsufficientFundsException;

import java.util.Collection;
import java.util.List;

public class ATMImpl implements ATM {

    private final Dispenser dispenser;

    ATMImpl(Dispenser dispenser) {

        this.dispenser = dispenser;
    }

    public boolean acceptBanknotes(Banknote banknote) {

        return acceptBanknotes(List.of(banknote));
    }

    public boolean acceptBanknotes(Collection<Banknote> banknotes) {
        var noteCellMap = dispenser.getStorage();
        banknotes.forEach(note -> noteCellMap.get(note).putBanknote());
        return true;
    }

    public List<Banknote> dispenseBanknotes(long neededAmount) {
        long balance = getBalance();
        if (balance < neededAmount) {
            throw new InsufficientFundsException(
                    "Amount(" + balance + ") is less than requested amount - " + neededAmount);
        }

        return dispenser.dispense(neededAmount);
    }

    public long getBalance() {
        var noteCellMap = dispenser.getStorage();

        long balance = 0;
        for (var entry : noteCellMap.entrySet()) {
            balance += (entry.getKey().getValue() * entry.getValue().numAvailableNotes());
        }
        return balance;
    }
}
