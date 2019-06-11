package com.mycompany.atm.memento;

import com.mycompany.atm.Banknote;
import com.mycompany.atm.Cell;

import java.util.Map;

public interface CellsStorage {

    Map<Banknote, Cell> getInitialCells();
}
