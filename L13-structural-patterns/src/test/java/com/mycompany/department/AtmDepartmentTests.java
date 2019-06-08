package com.mycompany.department;

import com.mycompany.AtmDepartment;
import com.mycompany.AtmDepartmentImpl;
import com.mycompany.atm.ATM;
import com.mycompany.atm.State;
import com.mycompany.memento.Caretaker;
import com.mycompany.memento.Memento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Map;

import static org.mockito.Mockito.when;

public class AtmDepartmentTests {

    private AtmDepartment atmDepartment;
    private ATM atm;

//    private final Map<ATM, Caretaker> atmCaretakerMap;

    @BeforeEach
    void prepareAtm() {
        atm = Mockito.mock(ATM.class);
    }

    @Test
    void acceptSuccessfully() {
        when(atm.saveInitialState()).thenReturn(new Memento(State.IN_SERVICE));
        ArgumentCaptor<AtmDepartmentImpl> captor = ArgumentCaptor.forClass(AtmDepartmentImpl.class);
        atmDepartment.addATM(atm);
    }
}
