package com.mycompany;

import com.mycompany.atm.AtmDispenserTests;
import com.mycompany.atm.AtmTests;
import com.mycompany.atm.CellTests;
import com.mycompany.atm.DispenserTests;
import com.mycompany.department.AtmDepartmentTests;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        AtmTests.class,
        CellTests.class,
        DispenserTests.class,
        AtmDispenserTests.class,
        AtmDepartmentTests.class})
public class AllTestsSuite {
}
