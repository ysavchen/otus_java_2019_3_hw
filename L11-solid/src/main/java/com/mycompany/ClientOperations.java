package com.mycompany;

import java.util.Collection;
import java.util.List;

public interface ClientOperations {

    boolean acceptBanknotes(Collection<Banknote> banknotes);

    List<Banknote> dispenseBanknotes(long amount);

    long checkBalance();
}
