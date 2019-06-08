package com.mycompany.atm;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        AtmTests.class,
        CellTests.class,
        DispenserTests.class,
        AtmDispenserTests.class})
public class AllTestsSuite {
}
