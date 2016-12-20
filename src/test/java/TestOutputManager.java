import program.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.TestCase.assertEquals;

/**
 * Integration test
 */
public class TestOutputManager
{
    private OutputManager oManager;
    private DBManager dbManager;
    private String properties;
    private String existinTableName;
    private DBService dbService;
    private String existingTableFile;

    @Before
    public void setUp() throws IOException, CloneNotSupportedException
    {
        properties="src/main/resources/databaseService.properties";

        existingTableFile="src/main/java/inputDataStructure/exampleFile";
        oManager=new OutputManager();
        dbManager=new DBManager(properties);
        dbService= new DBService(new InputManager(),oManager, dbManager, new Table());
        existinTableName="testTable";
        dbService.copyFile(existingTableFile, existinTableName);



    }

    @After
    public void tearDown()
    {
        dbManager.dropTable(existinTableName);
        oManager = null;
    }


    @Test
    public void fillDataSetWithDataFromDataBase() throws SQLException
    {
        //Arrange
        ResultSet rs = dbManager.getAllFromTable(existinTableName);

        //Act
        Table dataSetFilled = oManager.fillTable(rs, new Table());

        //Assert
        assertEquals(dataSetFilled.getName(), existinTableName.toLowerCase());
    }
}
