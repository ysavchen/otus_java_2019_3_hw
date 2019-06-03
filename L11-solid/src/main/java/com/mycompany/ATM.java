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
public interface ATM {

    boolean acceptBanknotes(Banknote banknote);

    /**
     * Accepts banknotes.
     *
     * @param banknotes banknotes to be accepted by ATM
     * @return {@code true} for success, otherwise {@code false}
     */
    boolean acceptBanknotes(Collection<Banknote> banknotes);

    /**
     * Dispenses banknotes.
     *
     * @param neededAmount amount of money needed for a client
     * @return list of banknotes
     */
    List<Banknote> dispenseBanknotes(long neededAmount);

    /**
     * Checks the balance.
     *
     * @return balance
     */
    long checkBalance();
}
