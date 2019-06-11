package com.mycompany.department;

import com.google.common.collect.Sets;
import com.mycompany.AtmDepartment;
import com.mycompany.AtmDepartmentImpl;
import com.mycompany.atm.ATM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AtmDepartmentTests {

    private AtmDepartment atmDepartment;

    private ATM atmOne;
    private ATM atmTwo;

    @BeforeEach
    void prepare() {
        atmOne = Mockito.mock(ATM.class);
        atmTwo = Mockito.mock(ATM.class);
        atmDepartment = new AtmDepartmentImpl(Sets.newHashSet(atmOne));
    }

    @Test
    void addAtm() {
        assertTrue(atmDepartment.addATM(atmTwo),
                "New ATM is not added");
        assertFalse(atmDepartment.addATM(atmOne),
                "Already added ATM is added again");
    }

    @Test
    void removeATM() {
        assertTrue(atmDepartment.removeATM(atmOne),
                "Added ATM is not removed");
        assertFalse(atmDepartment.removeATM(Mockito.mock(ATM.class)),
                "Not added ATM is removed");
    }

    @Test
    void restoreInitialState() {
        atmDepartment.restoreInitialStates();
        verify(atmOne, Mockito.times(1)).restoreInitialCells();
    }

    @Test
    void checkRemaindersFromOneATM() {
        when(atmOne.getBalance()).thenReturn(500L);
        assertEquals(500L, atmDepartment.getRemainders(),
                "Incorrect remainders returned for one ATM");
    }

    @Test
    void checkRemaindersFromSeveralATMs() {
        when(atmOne.getBalance()).thenReturn(150L);
        when(atmTwo.getBalance()).thenReturn(200L);
        atmDepartment.addATM(atmTwo);
        assertEquals(350L, atmDepartment.getRemainders(),
                "Incorrect remainders returned for several ATMs");
    }
}
