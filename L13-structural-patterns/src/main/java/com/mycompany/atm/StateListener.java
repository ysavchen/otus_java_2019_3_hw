package com.mycompany.atm;

import com.mycompany.atm.memento.Memento;

public interface StateListener {

    void setState(State state);

    Memento saveInitialState();

    void restoreInitialState();

}
