//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc;

import com.inthergroup.webpoc.services.NativeQueryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author gvandenbekerom
 * @since 15-Oct-18
 *
 * This test class uses the H2 in memory database to execute tests on
 * this has also been tested with a local PostgreSQL database and the results were the same
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NativeQueryServiceTest {
    @Autowired
    private NativeQueryService service;

    @Autowired
    @Qualifier("data")
    private JdbcTemplate db;

    @Before
    public void beforeTest() {
        db.execute("CREATE TABLE test ( id int, name varchar(255) )");
        db.execute("INSERT INTO test VALUES(1, 'test')");
    }

    @After
    public void afterTest() {
        db.execute("DROP TABLE test");
    }

    @Test
    public void testIsValidSelectSyntax_withDropDatabaseQuery_expectFailure() {
        boolean valid1 = service.isValidSelectSyntax("DROP DATABASE dummy");
        boolean valid2 = service.isValidSelectSyntax("; DROP DATABASE dummy");
        boolean valid3 = service.isValidSelectSyntax("'; DROP DATABASE dummy");
        boolean valid4 = service.isValidSelectSyntax("; DROP DATABASE dummy;");
        assertFalse(valid1);
        assertFalse(valid2);
        assertFalse(valid3);
        assertFalse(valid4);
    }

    @Test
    public void testIsValidSelectSyntax_withDropTableQuery_expectFailure() {
        boolean valid1 = service.isValidSelectSyntax("DROP TABLE test");
        boolean valid2 = service.isValidSelectSyntax("; DROP TABLE test");
        boolean valid3 = service.isValidSelectSyntax("'; DROP TABLE test");
        boolean valid4 = service.isValidSelectSyntax("; DROP TABLE test;");
        assertFalse(valid1);
        assertFalse(valid2);
        assertFalse(valid3);
        assertFalse(valid4);
    }

    @Test
    public void testIsValidSelectSyntax_withCreateTableQuery_expectFailure() {
        boolean valid1 = service.isValidSelectSyntax("CREATE TABLE TEST(ID INT PRIMARY KEY NOT NULL, DEPT CHAR(50) NOT NULL, EMP_ID INT NOT NULL)");
        boolean valid2 = service.isValidSelectSyntax("; CREATE TABLE TEST(ID INT PRIMARY KEY NOT NULL, DEPT CHAR(50) NOT NULL, EMP_ID INT NOT NULL)");
        boolean valid3 = service.isValidSelectSyntax("'; CREATE TABLE TEST(ID INT PRIMARY KEY NOT NULL, DEPT CHAR(50) NOT NULL, EMP_ID INT NOT NULL)");
        boolean valid4 = service.isValidSelectSyntax("; CREATE TABLE TEST(ID INT PRIMARY KEY NOT NULL, DEPT CHAR(50) NOT NULL, EMP_ID INT NOT NULL);");
        assertFalse(valid1);
        assertFalse(valid2);
        assertFalse(valid3);
        assertFalse(valid4);
    }

    @Test
    public void testIsValidSelectSyntax_withSelectUnknownTableQuery_expectFailure() {
        boolean valid = service.isValidSelectSyntax("SELECT * FROM asdfasdf");
        assertFalse(valid);
    }

    @Test
    public void testIsValidSelectSyntax_withValidSelectQuery_expectSuccess() {
        boolean valid = service.isValidSelectSyntax("SELECT * FROM test");
        assertTrue(valid);
    }
}
