package com.mycompany;

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
        return dispenser.putBanknotes(banknotes);
    }

    public List<Banknote> dispenseBanknotes(long neededAmount) {
        return dispenser
                .checkFunds(neededAmount)
                .getBanknotes(Banknote.THOUSAND_RUB)
                .getBanknotes(Banknote.FIVE_HUNDRED_RUB)
                .getBanknotes(Banknote.HUNDRED_RUB)
                .getBanknotes(Banknote.FIFTY_RUB)
                .dispense();
    }

    public long checkBalance() {
        return dispenser.getAtmAmount();
    }
}
