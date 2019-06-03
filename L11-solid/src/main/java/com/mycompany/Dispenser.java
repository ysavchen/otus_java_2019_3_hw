package com.mycompany;

import java.util.Collection;
import java.util.List;

//todo: analyze and update api
public interface Dispenser {

    boolean putBanknotes(Collection<Banknote> banknotes);

    DispenserImpl checkFunds(long neededAmount);

    long getAtmAmount();

    Dispenser getBanknotes(Banknote banknote);

    List<Banknote> dispense();
}
