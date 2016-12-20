import program.*;
import org.junit.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Integration tests
 * (in memory)
 * */
public class IntegrationTest
{
    String properties = "src/main/resources/databaseService.properties";
    DBService service;
    String exampleFile;
    String tableName = "TEST_table";
    DBManager dbManager;

    //String fileName="src/main/java/databaseService/inputDataStructure/_testFile";

    @Before
    public void setUp(){
        properties="src/main/resources/databaseService.properties";
        service = new DBService(
            new InputManager(),
            new OutputManager(),
            dbManager,
            new Table()
        );

        exampleFile = "src/main/java/databaseService/inputDataStructure/exampleFile";

        //createTable();
    }
    @After
    public void tearDown()
    {
        deleteTable(tableName);
    }

    @Test
    public void isTableCreated() throws SQLException
    {
        createTable(tableName);
        assertTrue(isTableExist(tableName));
    }

    @Test
    public void isTableAlreadyCreated() throws SQLException
    {
        // Arrange
        deleteTable(tableName);
        createTable(tableName);

        boolean createdAlready = isTableExist(tableName);

        // Assert
        assertTrue(createdAlready);

    }



    private boolean createTable(String name) throws SQLException
    {
        // Arrange
        DBManager dbManager = new DBManager(properties);

        String query = "CREATE TABLE IF NOT EXISTS "+name+" (\n" +
                "ID int(10) NOT NULL AUTO_INCREMENT,\n" +
                "TEXT varchar(100) NOT NULL,\n" +
                "PRIMARY KEY (ID));";
        int warnings = 0;
        // Act
        try (Connection connection = dbManager.getConnection();
             PreparedStatement createTable = connection.prepareStatement(
                     query)
        )
        {
            warnings = createTable.executeUpdate();
        }
        System.out.println(warnings);
        //System.out.println(warnings);
        return warnings<1?true:false;
    }

    private boolean deleteTable(String name)
    {
        // Arrange
        DBManager dbManager = new DBManager(properties);

        String query = "DROP TABLE IF EXISTS "+name;
        int warnings =0;
        // Act
        try (Connection connection = dbManager.getConnection();
             PreparedStatement createTable = connection.prepareStatement(
                     query)
        )
        {
            warnings = createTable.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        System.out.println(warnings);
        return warnings<1?true:false;
    }

    private boolean isTableExist(String name) throws SQLException
    {
        // Arrange
        boolean exists=false;
        DBManager dbManager = new DBManager(properties);
        java.sql.DatabaseMetaData dbm = dbManager.getConnection().getMetaData();

        // check if "employee" table is there
        ResultSet tables = dbm.getTables(null, null, name, null);
        if (tables.next()) {
            exists=true;
        }

        return exists;
    }
}
