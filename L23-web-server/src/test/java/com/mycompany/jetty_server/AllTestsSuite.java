package com.mycompany.jetty_server;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        DbServiceUserTests.class,
        UserOperationsTests.class,
        UserStoreTests.class,
        UserDataTests.class,
        ServerDbIntegrationTests.class})
public class AllTestsSuite {
}
