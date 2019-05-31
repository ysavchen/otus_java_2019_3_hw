package com.mycompany;

import com.mycompany.exceptions.InsufficientFundsException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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

        List<Banknote> thousands = getAvailableNotes(amountToDispense, Nominal.THOUSAND);
        amountToDispense -= calculateBanknotesValue(thousands);
        List<Banknote> fiveHundreds = getAvailableNotes(amountToDispense, Nominal.FIVE_HUNDRED);
        amountToDispense -= calculateBanknotesValue(fiveHundreds);
        List<Banknote> hundreds = getAvailableNotes(amountToDispense, Nominal.HUNDRED);
        amountToDispense -= calculateBanknotesValue(hundreds);
        List<Banknote> fifties = getAvailableNotes(amountToDispense, Nominal.FIFTY);
        amountToDispense -= calculateBanknotesValue(fifties);
        amount -= amountToDispense;

        return Stream.of(thousands, fiveHundreds, hundreds, fifties)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<Banknote> getAvailableNotes(long amountToDispense, Nominal nominal) {
        var cell = map.get(nominal);
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
