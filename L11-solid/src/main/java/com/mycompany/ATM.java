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
    private Map<Nominal, Cell> nominalCellMap = Map.of(
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
     * @return overall value of banknotes
     */
    private long calculateBanknotesValue(Collection<Banknote> banknotes) {
        return banknotes
                .stream()
                .map(Banknote::getNominal)
                .mapToLong(Nominal::getValue)
                .sum();
    }

    /**
     * Dispenses banknotes.<p>
     *
     * @param neededAmount amount of money needed for a user
     * @return list of banknotes
     */
    public List<Banknote> dispenseBanknotes(long neededAmount) {
        if (amount < neededAmount) {
            throw new InsufficientFundsException(
                    "Amount(" + amount + ") is less than requested amount - " + neededAmount);
        }

        final List<Banknote> notesToDispense = new ArrayList<>();

        List<Banknote> notes = getNotesFromCell(neededAmount, Nominal.THOUSAND);
        long calcAmount = calculateBanknotesValue(notes);
        neededAmount -= calcAmount;
        amount -= calcAmount;
        notesToDispense.addAll(notes);

        notes = getNotesFromCell(neededAmount, Nominal.FIVE_HUNDRED);
        calcAmount = calculateBanknotesValue(notes);
        neededAmount -= calcAmount;
        amount -= calcAmount;
        notesToDispense.addAll(notes);

        notes = getNotesFromCell(neededAmount, Nominal.HUNDRED);
        calcAmount = calculateBanknotesValue(notes);
        neededAmount -= calcAmount;
        amount -= calcAmount;
        notesToDispense.addAll(notes);

        notes = getNotesFromCell(neededAmount, Nominal.FIFTY);
        calcAmount = calculateBanknotesValue(notes);
        neededAmount -= calcAmount;
        amount -= calcAmount;
        notesToDispense.addAll(notes);
        System.out.println("Non dispensed amount: " + neededAmount);

        if (notesToDispense.isEmpty())
            throw new NoBanknotesException();
        return notesToDispense;
    }

    /**
     * Calculates and returns a number of banknotes depending on the needed amount.
     * If a cell doesn't store any, or amountToDispense is less than the nominal, then an emptyList is returned.
     *
     * @param amountToDispense amount left to be dispensed as banknotes
     * @param nominal          nominal of banknote
     * @return retrieved banknotes
     */
    private List<Banknote> getNotesFromCell(long amountToDispense, Nominal nominal) {
        Cell cell = nominalCellMap.get(nominal);
        int numNeededNotes = Math.toIntExact(amountToDispense / nominal.getValue());

        if (cell.numAvailableNotes() > 0 && numNeededNotes > 0) {
            return cell.getBanknotes(numNeededNotes);
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
