package com.mycompany.atm.memento;

public interface StorageKeeper {

    CellsStorage saveInitialCells();

    void restoreInitialCells(CellsStorage cellsStorage);
}
