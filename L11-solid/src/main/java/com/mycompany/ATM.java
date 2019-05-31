package com.mycompany;

import com.mycompany.exceptions.InsufficientFundsException;
import com.mycompany.exceptions.NoBanknotesException;

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

    private long amount;

    /**
     * Stores cells with different {@link Nominal}
     */
    private static Map<Nominal, Cell> nominalCellMap = Map.of(
            Nominal.FIFTY, new Cell(),
            Nominal.HUNDRED, new Cell(),
            Nominal.FIVE_HUNDRED, new Cell(),
            Nominal.THOUSAND, new Cell()
    );

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
        for (Banknote note : banknotes) {
            nominalCellMap.get(note.getNominal()).putBanknote(note);
        }

        amount += calculateBanknotesValue(banknotes);
        return true;
    }

    /**
     * Calculates the overall value of banknotes with different nominal.
     *
     * @param banknotes banknotes for value calculation
     * @return value
     */
    private long calculateBanknotesValue(Collection<Banknote> banknotes) {
        long banknotesValue = 0L;
        for (Banknote note : banknotes) {
            banknotesValue += note.getNominal().getValue();
        }
        return banknotesValue;
    }

    /**
     * Dispenses banknotes.<p>
     *
     * @param amountToDispense amount of money to be dispensed by ATM
     * @return list of banknotes
     */
    public List<Banknote> dispenseBanknotes(long amountToDispense) {
        if (this.amount < amountToDispense) {
            throw new InsufficientFundsException(
                    "Amount(" + this.amount + ") is less than requested amount - " + amountToDispense);
        }

        final List<Banknote> notesToDispense = new ArrayList<>();
        for (var entry : nominalCellMap.entrySet()) {
            List<Banknote> notes = getAvailableNotes(amountToDispense, entry.getKey());
            amountToDispense -= calculateBanknotesValue(notes);
            notesToDispense.addAll(notes);
        }

        if (notesToDispense.isEmpty())
            throw new NoBanknotesException();

        amount -= amountToDispense;
        return notesToDispense;
    }

    private List<Banknote> getAvailableNotes(long amountToDispense, Nominal nominal) {
        var cell = nominalCellMap.get(nominal);
        int numNeededNotes = Math.toIntExact(amountToDispense / nominal.getValue());
        if (cell.numNotes() > 0 && numNeededNotes > 0) {
            if (cell.numNotes() > numNeededNotes) {
                return cell.getBanknotes(numNeededNotes);
            }
        }
        return Collections.emptyList();
    }

    /**
     * Checks the balance in the account.
     *
     * @return balance
     */
    public long checkBalance() {
        return amount;
    }
}
