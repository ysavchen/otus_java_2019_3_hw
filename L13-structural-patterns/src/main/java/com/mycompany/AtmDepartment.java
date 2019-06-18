package com.mycompany;

import com.mycompany.atm.ATM;

/**
 * Департамент ATM
 * Написать приложение ATM Департамент:
 * 1) Департамент может содержать несколько ATM.
 * 2) Департамент может собирать сумму остатков со всех ATM.
 * 3) Департамент может инициировать событие – восстановить состояние всех
 * ATM до начального (начальные состояния у разных ATM могут быть
 * разными).
 * Это тренировочное задание на применение паттернов.
 * Попробуйте использовать как можно больше.
 */
public interface AtmDepartment {

    /**
     * Adds an ATM to the department.
     *
     * @param atm atm to be added
     * @return true for success, otherwise false
     */
    boolean addATM(ATM atm);

    /**
     * Removes an ATM from the department.
     *
     * @param atm atm to be removed
     * @return true for success, otherwise false
     */
    boolean removeATM(ATM atm);

    /**
     * Restores an initial state for all ATMs.
     */
    void restoreInitialStates();

    /**
     * Collects the overall sum of remainders from all ATMs in the department.
     *
     * @return sum of remainders
     */
    long getRemainders();

}
