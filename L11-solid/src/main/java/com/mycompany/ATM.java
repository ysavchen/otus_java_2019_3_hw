package com.mycompany;

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

    private long amount;

    /**
     * Stores cells with different {@link Nominal}
     */
    private static Map<Nominal, Cell> map = Map.of(
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
            map.get(note.getNominal()).putBanknote(note);
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
    long calculateBanknotesValue(Collection<Banknote> banknotes) {
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

        //todo: state pattern?
        long tempAmount = amount;

        long numThousands = tempAmount / 1000L;
        long numThousandsDispense = amountToDispense / 1000L;
        if (tempAmount > 0 && numThousands > 0 && numThousandsDispense > 0) {
            tempAmount -= (numThousandsDispense * 1000L);
            amountToDispense -= (numThousandsDispense * 1000L);
        }

        long numFiveHundreds = tempAmount / 500L;
        long numFiveHundredsDispense = amountToDispense / 500L;
        if (tempAmount > 0 && numFiveHundreds > 0 && numFiveHundredsDispense > 0) {
            tempAmount -= (numFiveHundredsDispense * 500L);
            amountToDispense -= (numFiveHundredsDispense * 500L);
        }

        long numHundreds = tempAmount / 100L;
        long numHundredsDispense = amountToDispense / 100L;
        if (tempAmount > 0 && numHundreds > 0 && amountToDispense > 0) {
            tempAmount -= (numHundredsDispense * 100L);
            amountToDispense -= (numHundredsDispense * 100L);
        }

        long numFifties = tempAmount / 50L;
        long numFiftiesDispense = amountToDispense / 50L;
        if (tempAmount > 0 && numFifties > 0 && numFiftiesDispense > 0) {
            tempAmount -= (numFiftiesDispense * 50L);
            amountToDispense -= (numFiftiesDispense * 50L);
        }
        amount -= tempAmount;

        final List<Banknote> notes = new ArrayList<>();
        if (numThousandsDispense > 0) {
            for (int i = 0; i < numThousandsDispense; i++) {
                notes.add(map.get(Nominal.THOUSAND).getBanknote());
            }
        }

        if (numFiveHundredsDispense > 0) {
            for (int i = 0; i < numFiveHundredsDispense; i++) {
                notes.add(map.get(Nominal.FIVE_HUNDRED).getBanknote());
            }
        }
        if (numHundredsDispense > 0) {
            for (int i = 0; i < numHundredsDispense; i++) {
                notes.add(map.get(Nominal.HUNDRED).getBanknote());
            }
        }

        if (numFiftiesDispense > 0) {
            for (int i = 0; i < numFiftiesDispense; i++) {
                notes.add(map.get(Nominal.FIFTY).getBanknote());
            }
        }

        return notes;
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
