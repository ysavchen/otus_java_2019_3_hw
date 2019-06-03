package com.mycompany;

import java.util.List;
import java.util.Map;

abstract class Dispenser {

    /**
     * Stores banknotes in their own cells
     */
    private final Map<Banknote, Cell> noteCellMap;

    Dispenser(Map<Banknote, Cell> noteCellMap) {
        this.noteCellMap = noteCellMap;
    }

    /**
     * Dispenses neededAmount from cells.
     *
     * @param neededAmount amount needed for a client
     * @return banknotes for dispensation
     */
    abstract List<Banknote> dispense(long neededAmount);

    Map<Banknote, Cell> getStorage() {
        return noteCellMap;
    }

}
