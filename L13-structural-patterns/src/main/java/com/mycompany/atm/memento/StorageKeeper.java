package com.mycompany.atm.memento;

public interface StorageKeeper {

    void saveInitialCells();

    void restoreInitialCells();
}
