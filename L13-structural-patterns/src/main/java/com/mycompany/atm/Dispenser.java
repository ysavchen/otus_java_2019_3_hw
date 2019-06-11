package com.mycompany.atm;

import java.util.List;
import java.util.Map;

public interface Dispenser {

    /**
     * Dispenses neededAmount from cells.
     *
     * @param neededAmount amount needed for a client
     * @return banknotes for dispensation
     */
    List<Banknote> dispense(long neededAmount);

    /**
     * Gets storage of banknotes from the dispenser.
     *
     * @return storage
     */
    Map<Banknote, Cell> getStorage();

}
