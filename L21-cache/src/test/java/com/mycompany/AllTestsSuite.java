package com.mycompany;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        HibernateHwTests.class,
        CacheTests.class,
        DbServiceImplCacheTests.class})
public class AllTestsSuite {
}
