package com.mycompany;

import com.mycompany.atm.State;
import com.mycompany.memento.Memento;

public interface StateListener {

    void setState(State state);

    Memento saveInitialState();

    void restoreInitialState(Memento memento);

}
