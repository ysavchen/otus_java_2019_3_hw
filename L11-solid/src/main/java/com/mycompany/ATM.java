package com.mycompany;

import com.mycompany.exceptions.InactiveAccountException;
import com.mycompany.exceptions.InsufficientFundsException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    private final Account clientAccount;

    private final Account atmAccount = new Account();

    private static Map<Nominal, Cell> map = Map.of(
            Nominal.FIFTY, new Cell(),
            Nominal.HUNDRED, new Cell(),
            Nominal.FIVE_HUNDRED, new Cell(),
            Nominal.THOUSAND, new Cell()
    );

    public ATM(Account clientAccount) {

        this.clientAccount = clientAccount;
        atmAccount.setActive(true);
    }

    /**
     * Accepts banknotes.
     *
     * @param banknotes banknotes to be accepted by ATM
     * @return {@code true} for success, otherwise {@code false}
     */
    public boolean acceptBanknotes(Collection<Banknote> banknotes) {
        for (Banknote note : banknotes) {
            if (!map.get(note.getNominal()).putBanknote(note)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Dispenses banknotes.<p>
     *
     * @param amount amount of money to be dispensed by ATM
     * @return list of banknotes
     */
    public List<Banknote> dispenseBanknotes(long amount) {
        long clientBalance = clientAccount.getBalance().getAmount();
        long atmBalance = atmAccount.getBalance().getAmount();
        if (clientBalance < amount) {
            throw new InsufficientFundsException(
                    "Client balance(amount: " + clientBalance + ") is less than requested amount - " + amount);
        }
        if (atmBalance < amount) {
            throw new InsufficientFundsException(
                    "ATM balance(amount: " + atmBalance + ") is less than requested amount - " + amount);
        }


        return new ArrayList<>();
    }

    /**
     * Checks the balance in the account.
     *
     * @return balance
     */
    public long checkBalance() {
        if (!clientAccount.isActive()) {
            throw new InactiveAccountException("Client account is not active");
        }
        return clientAccount.getBalance().getAmount();
    }
}
