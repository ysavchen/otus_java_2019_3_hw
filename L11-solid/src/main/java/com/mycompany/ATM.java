package com.mycompany;

import java.util.Collection;
import java.util.List;

/**
 * Написать эмулятор АТМ (банкомата).
 * <p>
 * Объект класса АТМ должен уметь:<p>
 * - принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)<p>
 * - выдавать запрошенную сумму минимальным количеством банкнот или ошибку если сумму нельзя выдать<p>
 * Это задание не на алгоритмы, а на проектирование.<p>
 * Поэтому оптимизировать выдачу не надо.<p>
 * - выдавать сумму остатка денежных средств<p>
 */
public class ATM {

    private final Dispenser dispenser = new Dispenser();

    public boolean acceptBanknotes(Banknote banknote) {
        return acceptBanknotes(List.of(banknote));
    }

    /**
     * Accepts banknotes.
     *
     * @param banknotes banknotes to be accepted by ATM
     * @return {@code true} for success, otherwise {@code false}
     */
    public boolean acceptBanknotes(Collection<Banknote> banknotes) {
        return dispenser.putBanknotes(banknotes);
    }

    /**
     * Dispenses banknotes.<p>
     *
     * @param neededAmount amount of money needed for a user
     * @return list of banknotes
     */
    public List<Banknote> dispenseBanknotes(long neededAmount) {
        return dispenser
                .checkFunds(neededAmount)
                .getBanknotes(Nominal.THOUSAND)
                .getBanknotes(Nominal.FIVE_HUNDRED)
                .getBanknotes(Nominal.HUNDRED)
                .getBanknotes(Nominal.FIFTY)
                .dispense();
    }

    /**
     * Checks the balance in the account.
     *
     * @return balance
     */
    public long checkBalance() {
        return dispenser.getAtmAmount();
    }
}
