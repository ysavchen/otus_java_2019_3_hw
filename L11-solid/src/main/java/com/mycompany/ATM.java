package com.mycompany;

import java.util.Collection;

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

    /**
     * Accepts banknotes.
     *
     * @param banknotes banknotes to be accepted by ATM
     * @return {@code true} for success, otherwise {@code false}
     */
    public boolean acceptBanknotes(Collection<Banknote> banknotes) {

        return true;
    }

    /**
     * Dispenses banknotes.<p>
     *
     * @param banknotes banknotes to be dispensed by ATM
     * @return {@code true} for success, otherwise {@code false}
     */
    public boolean dispenseBanknotes(Collection<Banknote> banknotes) {
        return true;
    }

    /**
     * Checks balance.
     *
     * @return balance
     */
    public long checkBalance() {
        return 1L;
    }
}
