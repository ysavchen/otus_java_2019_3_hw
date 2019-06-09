package com.mycompany;

import com.mycompany.atm.ATM;
import com.mycompany.atm.StateListener;

import java.util.Set;

public class AtmDepartmentImpl implements AtmDepartment {

    /**
     * Stores ATMs
     */
    private final Set<ATM> machines;

    public AtmDepartmentImpl(Set<ATM> machines) {
        this.machines = machines;
    }

    @Override
    public boolean addATM(ATM atm) {
        return machines.add(atm);
    }

    @Override
    public boolean removeATM(ATM atm) {

        return machines.remove(atm);
    }

    @Override
    public void restoreInitialStates() {
        machines.forEach(StateListener::restoreInitialState);
    }

    @Override
    public long getRemainders() {

        return machines.stream().mapToLong(ATM::getBalance).sum();
    }
}
