package com.mycompany;

import com.mycompany.atm.ATM;
import com.mycompany.memento.Caretaker;

import java.util.Map;

public class AtmDepartmentImpl implements AtmDepartment {

    /**
     * Stores ATMs with their initial states.
     */
    private final Map<ATM, Caretaker> atmStateMap;

    AtmDepartmentImpl(Map<ATM, Caretaker> atmStateMap) {
        this.atmStateMap = atmStateMap;
    }

    @Override
    public void addATM(ATM atm) {
        atmStateMap.putIfAbsent(atm, new Caretaker().setMemento(atm.saveInitialState()));
    }

    @Override
    public void removeATM(ATM atm) {
        atmStateMap.remove(atm, atmStateMap.get(atm));
    }

    @Override
    public void restoreInitialStates() {
        atmStateMap.forEach((atm, caretaker) -> atm.restoreInitialState(caretaker.getMemento()));
    }

    @Override
    public long getRemainders() {
        return atmStateMap.keySet().stream().mapToLong(ATM::getBalance).sum();
    }
}
