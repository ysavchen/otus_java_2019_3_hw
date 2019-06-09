package com.mycompany.atm;

import com.mycompany.atm.exceptions.InsufficientFundsException;
import com.mycompany.atm.exceptions.NoSuchCellException;
import com.mycompany.atm.memento.Caretaker;
import com.mycompany.atm.memento.Memento;

import java.util.Collection;
import java.util.List;

public class ATMImpl implements ATM {

    private final Dispenser dispenser;
    private State state;
    private final Caretaker caretaker = new Caretaker();

    ATMImpl(Dispenser dispenser, State initialState) {

        this.dispenser = dispenser;
        this.state = initialState;
        caretaker.setMemento(saveInitialState());
    }

    public boolean acceptBanknotes(Banknote banknote) {

        return acceptBanknotes(List.of(banknote));
    }

    @Override
    public boolean acceptBanknotes(Collection<Banknote> banknotes) {
        var noteCellMap = dispenser.getStorage();

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

        return dispenser.dispense(neededAmount);
    }

    @Override
    public long getBalance() {
        var noteCellMap = dispenser.getStorage();

        long balance = 0;
        for (var entry : noteCellMap.entrySet()) {
            balance += (entry.getKey().getValue() * entry.getValue().numAvailableNotes());
        }
        return balance;
    }

    public Memento saveInitialState() {
        return new Memento(state);
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void restoreInitialState() {

        this.state = caretaker.getMemento().getState();
    }
}
