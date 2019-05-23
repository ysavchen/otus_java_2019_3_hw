package com.mycompany;

import java.util.*;

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

    private final Account account;

    private final Cell cell_50 = new Cell(Nominal.FIFTY);
    private final Cell cell_100 = new Cell(Nominal.HUNDRED);
    private final Cell cell_500 = new Cell(Nominal.FIVE_HUNDRED);
    private final Cell cell_1000 = new Cell(Nominal.THOUSAND);

    //Map<Nominal, Cell> map = new HashMap<>();

    public ATM(Account account) {
        this.account = account;
    }

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
     * @param amount amount of money to be dispensed by ATM
     * @return list of banknotes
     */
    public List<Banknote> dispenseBanknotes(long amount) {
        return new ArrayList<>();
    }

    /**
     * Gets the balance in the account.
     *
     * @return balance
     */
    public long getBalance() {
        return 1L;
    }
}
