package com.mycompany.atm.memento;

import com.mycompany.atm.Banknote;
import com.mycompany.atm.Cell;

import java.util.Map;

/**
 * Stores the initially loaded cells of ATM
 */
public class CellsStorageImpl implements CellsStorage {

    private final Map<Banknote, Cell> noteCellMap;

    public CellsStorageImpl(Map<Banknote, Cell> noteCellMap) {
        this.noteCellMap = noteCellMap;
    }

    public Map<Banknote, Cell> getInitialCells() {
        return noteCellMap;
    }
}
