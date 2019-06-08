package com.mycompany.command;

import java.util.Deque;
import java.util.LinkedList;

public class Switch {

    private final Deque<Command> history = new LinkedList<>();

    public void storeAndExecute(Command cmd) {
        this.history.add(cmd);
        cmd.execute();
    }

    public void undo() {
        if (history.size() > 1) {
            history.removeLast();
            history.getLast().execute();
        }
    }
}
