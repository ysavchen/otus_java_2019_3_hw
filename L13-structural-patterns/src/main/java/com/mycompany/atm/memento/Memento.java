package com.mycompany.atm.memento;

import com.mycompany.atm.State;

/**
 * Stores the initial state of ATM
 */
public class Memento {

    private final State state;

    public Memento(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
