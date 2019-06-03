package com.mycompany;

import com.mycompany.exceptions.NoBanknotesException;

import java.util.*;

/**
 * Dispenses the needed amount in a min number of banknotes.
 */
class MinBanknotesDispenser extends Dispenser {

    MinBanknotesDispenser(Map<Banknote, Cell> noteCellMap) {
        super(noteCellMap);
    }

    @Override
    List<Banknote> dispense(long neededAmount) {
        if (neededAmount <= 0) {
            return Collections.emptyList();
        }

        final List<Banknote> notesToDispense = new ArrayList<>();
        final SortedMap<Banknote, Cell> sortedMap = new TreeMap<>(Collections.reverseOrder());
        sortedMap.putAll(getStorage());

        for (var entry : sortedMap.entrySet()) {
            final List<Banknote> notes = getNotesFromCell(entry.getKey(), neededAmount);
            neededAmount -= calculateBanknotesValue(notes);
            notesToDispense.addAll(notes);
        }

        if (notesToDispense.isEmpty())
            throw new NoBanknotesException();
        return notesToDispense;
    }

    /**
     * Calculates and returns a number of banknotes depending on the needed amount.
     * If a cell doesn't store any, or neededAmount is less than the nominal, then an emptyList is returned.
     *
     * @param note
     * @param neededAmount
     * @return retrieved banknotes
     */
    private List<Banknote> getNotesFromCell(Banknote note, long neededAmount) {
        Cell cell = getStorage().get(note);
        int numNeededNotes = Math.toIntExact(neededAmount / note.getValue());

        if (cell.numAvailableNotes() > 0 && numNeededNotes > 0) {
            return cell.retrieveBanknotes(numNeededNotes);
        }
        return Collections.emptyList();
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
}
