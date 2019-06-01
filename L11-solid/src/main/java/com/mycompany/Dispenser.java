package com.mycompany;

import com.mycompany.exceptions.InsufficientFundsException;
import com.mycompany.exceptions.NoBanknotesException;

import java.util.*;

public class Dispenser {

    private long atmAmount;
    private long neededAmount;

    private final List<Banknote> notesToDispense = new ArrayList<>();

    /**
     * Stores cells with different {@link Nominal}
     */
    private Map<Nominal, Cell> nominalCellMap = Map.of(
            Nominal.FIFTY, new Cell(),
            Nominal.HUNDRED, new Cell(),
            Nominal.FIVE_HUNDRED, new Cell(),
            Nominal.THOUSAND, new Cell()
    );

    Dispenser() {
    }

    boolean putBanknotes(Collection<Banknote> banknotes) {
        for (Banknote note : banknotes) {
            nominalCellMap.get(note.getNominal()).putBanknote(note);
        }

        atmAmount += calculateBanknotesValue(banknotes);
        return true;
    }

    Dispenser checkFunds(long neededAmount) {
        if (atmAmount < neededAmount) {
            throw new InsufficientFundsException(
                    "Amount(" + atmAmount + ") is less than requested amount - " + neededAmount);
        }

        this.neededAmount = neededAmount;
        return this;
    }

    Dispenser getBanknotes(Nominal nominal) {
        List<Banknote> notes = getNotesFromCell(neededAmount, nominal);
        long calcAmount = calculateBanknotesValue(notes);
        neededAmount -= calcAmount;
        atmAmount -= calcAmount;
        notesToDispense.addAll(notes);
        return this;
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

    List<Banknote> dispense() {
        if (notesToDispense.isEmpty())
            throw new NoBanknotesException();
        return notesToDispense;
    }

    long getAtmAmount() {
        return atmAmount;
    }
}
