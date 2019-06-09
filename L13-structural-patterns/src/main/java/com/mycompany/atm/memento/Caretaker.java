package com.mycompany.atm.memento;

public class Caretaker {

    private Memento memento;

    public Memento getMemento() {
        return memento;
    }

    public Caretaker setMemento(Memento memento) {
        this.memento = memento;
        return this;
    }
}
