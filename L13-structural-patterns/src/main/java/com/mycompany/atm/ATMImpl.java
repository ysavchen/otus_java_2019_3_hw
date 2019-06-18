package com.mycompany.atm;

import com.mycompany.atm.exceptions.InsufficientFundsException;
import com.mycompany.atm.exceptions.NoSuchCellException;
import com.mycompany.atm.memento.CellsStorage;
import com.mycompany.atm.memento.CellsStorageImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMImpl implements ATM {

    private final Dispenser dispenser;
    private Map<Banknote, Cell> noteCellMap;

    ATMImpl(Dispenser dispenser, Map<Banknote, Cell> noteCellMap) {

        this.dispenser = dispenser;
        this.noteCellMap = noteCellMap;
        saveInitialCells();
    }

    @Override
    public boolean acceptBanknotes(Banknote banknote) {

        return acceptBanknotes(List.of(banknote));
    }

    @Override
    public boolean acceptBanknotes(Collection<Banknote> banknotes) {
        for (Banknote note : banknotes) {
            Cell cell = noteCellMap.get(note);
            if (cell == null) {
                throw new NoSuchCellException("No cell for a banknote with value: " + note.getValue());
            }
            cell.putBanknote();
        }
        return true;
    }

    @Override
    public List<Banknote> dispenseBanknotes(long neededAmount) {
        long balance = getBalance();
        if (balance < neededAmount) {
            throw new InsufficientFundsException(
                    "Amount(" + balance + ") is less than requested amount - " + neededAmount);
        }

        return dispenser.dispense(neededAmount, noteCellMap);
    }

    @Override
    public long getBalance() {
        long balance = 0;
        for (var entry : noteCellMap.entrySet()) {
            balance += (entry.getKey().getValue() * entry.getValue().numAvailableNotes());
        }
        return balance;
    }

    @Override
    public CellsStorage saveInitialCells() {
        return new CellsStorageImpl(new HashMap<>(noteCellMap));
    }

    @Override
    public void restoreInitialCells(CellsStorage cellsStorage) {

        this.noteCellMap = cellsStorage.getInitialCells();
    }
}
