package com.mycompany;

import com.mycompany.exceptions.InsufficientFundsException;
import com.mycompany.exceptions.NoBanknotesException;

import java.util.*;

class DispenserImpl implements Dispenser {

    private long neededAmount;

    private final List<Banknote> notesToDispense = new ArrayList<>();

    /**
     * Stores cells with banknotes having different nominal
     */
    private final Map<Banknote, Cell> noteCellMap;

    DispenserImpl(Map<Banknote, Cell> noteCellMap) {
        this.noteCellMap = noteCellMap;
    }

    /**
     * Distributes banknotes to a specific cell depending on their nominal.
     *
     * @param banknotes
     * @return true for success, otherwise false
     */
    public boolean putBanknotes(Collection<Banknote> banknotes) {
        banknotes.forEach(note -> noteCellMap.get(note).putBanknote());
        return true;
    }

    /**
     * Checks the dispenser has enough funds to give the needed amount.
     *
     * @param neededAmount amount requester by a client
     * @return dispenser
     * @throws InsufficientFundsException if the stored amount < needed amount
     */
    public DispenserImpl checkFunds(long neededAmount) {
        long atmAmount = getAtmAmount();
        if (atmAmount < neededAmount) {
            throw new InsufficientFundsException(
                    "Amount(" + atmAmount + ") is less than requested amount - " + neededAmount);
        }

        this.neededAmount = neededAmount;
        return this;
    }

    /**
     * Prepares banknotes for dispensing.
     *
     * @param nominal nominal of banknotes
     * @return dispenser
     */
    public DispenserImpl getBanknotes(Banknote nominal) {
        final List<Banknote> notes = getNotesFromCell(nominal);
        long notesValue = calculateBanknotesValue(notes);
        neededAmount -= notesValue;
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
                .mapToLong(Banknote::getValue)
                .sum();
    }

    /**
     * Calculates and returns a number of banknotes depending on the needed amount.
     * If a cell doesn't store any, or neededAmount is less than the nominal, then an emptyList is returned.
     *
     * @param note
     * @return retrieved banknotes
     */
    private List<Banknote> getNotesFromCell(Banknote note) {
        Cell cell = noteCellMap.get(note);
        int numNeededNotes = Math.toIntExact(neededAmount / note.getValue());

        if (cell.numAvailableNotes() > 0 && numNeededNotes > 0) {
            return cell.retrieveBanknotes(numNeededNotes);
        }
        return Collections.emptyList();
    }

    /**
     * Dispenses banknotes if there is any
     *
     * @return list of banknotes
     */
    public List<Banknote> dispense() {
        if (notesToDispense.isEmpty())
            throw new NoBanknotesException();
        return notesToDispense;
    }

    public long getAtmAmount() {
        long atmAmount = 0;
        for (var entry : noteCellMap.entrySet()) {
            atmAmount += (entry.getKey().getValue() * entry.getValue().numAvailableNotes());
        }
        return atmAmount;
    }
}
