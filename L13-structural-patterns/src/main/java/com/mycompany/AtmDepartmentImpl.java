package com.mycompany;

import com.mycompany.atm.ATM;
import com.mycompany.atm.memento.CellsStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AtmDepartmentImpl implements AtmDepartment {

    /**
     * Stores ATMs
     */
    private final Set<ATM> machines = new HashSet<>();

    /**
     * Stores initial states for each ATM
     */
    private final Map<ATM, CellsStorage> atmCellsStorage = new HashMap<>();

    @Override
    public boolean addATM(ATM atm) {
        boolean isAtmAdded = machines.add(atm);
        boolean isCellsStored = atmCellsStorage.put(atm, atm.saveInitialCells()) == null;
        return isAtmAdded && isCellsStored;
    }

    @Override
    public boolean removeATM(ATM atm) {
        boolean isAtmRemoved = machines.remove(atm);
        boolean isCellsRemoved = atmCellsStorage.remove(atm, atm.saveInitialCells());
        return isAtmRemoved && isCellsRemoved;
    }

    @Override
    public void restoreInitialStates() {

        machines.forEach(atm -> atm.restoreInitialCells(atmCellsStorage.get(atm)));
    }

    @Override
    public long getRemainders() {

        return machines.stream().mapToLong(ATM::getBalance).sum();
    }
}
