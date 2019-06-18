package com.mycompany.atm;

import com.mycompany.atm.exceptions.NoBanknotesException;

import java.util.*;

/**
 * Dispenses the needed amount in a min number of banknotes.
 */
class MinBanknotesDispenser implements Dispenser {

    @Override
    public List<Banknote> dispense(long neededAmount, Map<Banknote, Cell> noteCellMap) {
        if (neededAmount <= 0) {
            return Collections.emptyList();
        }

        final List<Banknote> notesToDispense = new ArrayList<>();
        final SortedMap<Banknote, Cell> sortedMap = new TreeMap<>(Collections.reverseOrder());
        sortedMap.putAll(noteCellMap);

        for (var entry : sortedMap.entrySet()) {
            Banknote note = entry.getKey();
            Cell cell = noteCellMap.get(note);
            int numNeededNotes = Math.toIntExact(neededAmount / note.getValue());

            final List<Banknote> notes = getNotesFromCell(numNeededNotes, cell);
            neededAmount -= calculateBanknotesValue(notes);
            notesToDispense.addAll(notes);
        }

        if (neededAmount != 0) {
            for (Banknote note : notesToDispense) {
                noteCellMap.get(note).putBanknote();
            }
            throw new NoBanknotesException();
        }
        return notesToDispense;
    }

    /**
     * Calculates and returns a number of banknotes depending on the needed amount.
     * If a cell doesn't store any, or neededAmount is less than the nominal, then an emptyList is returned.
     *
     * @param numNeededNotes
     * @param cell
     * @return retrieved banknotes
     */
    private List<Banknote> getNotesFromCell(int numNeededNotes, Cell cell) {
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
