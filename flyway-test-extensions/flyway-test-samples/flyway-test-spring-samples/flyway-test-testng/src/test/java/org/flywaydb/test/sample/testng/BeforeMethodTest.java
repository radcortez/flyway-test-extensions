package org.flywaydb.test.sample.testng;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.testng.AssertJUnit.assertEquals;


@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@Test
public class BeforeMethodTest extends AbstractTestNGSpringContextTests {

    @Autowired
    protected ApplicationContext context;

    private BaseDbHelper baseDbHelper;

    /**
     * Open a connection to database for test execution statements
     *
     * @throws Exception
     */
    @BeforeMethod
    @FlywayTest(locationsForMigrate = {"loadmsql"})
    public void beforeMethod() throws Exception {
        baseDbHelper = new BaseDbHelper(context);
        baseDbHelper.beforeMethod();
    }

    /**
     * Close the connection
     *
     * @throws Exception
     */
    @AfterMethod
    public void afterMethod() throws Exception {
        baseDbHelper.afterMethod();
    }

    @Test
    public void simpleCountWithoutAny() throws Exception {
        int res = countCustomer();

        assertEquals("Customer count musst be ", 2, res);

        addCustomer("simpleCountWithoutAny");

        res = countCustomer();

        assertEquals("Customer count musst be ", 3, res);
    }

    @Test
    public void additionalCountWithoutAny() throws Exception {
        int res = countCustomer();

        assertEquals("Customer count musst be ", 2, res);

        addCustomer("additionalCountWithoutAny");

        res = countCustomer();

        assertEquals("Customer count musst be ", 3, res);
    }

    /**
     * Simple counter query to have simple test inside test methods.
     *
     * @return number of customer in database
     * @throws Exception
     */
    private int countCustomer() throws Exception {
        return baseDbHelper.countCustomer();
    }

    private void addCustomer(String name) throws SQLException {
        baseDbHelper.addCustomer(name);
    }
}
