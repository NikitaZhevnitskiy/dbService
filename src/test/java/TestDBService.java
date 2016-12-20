import program.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

/**
 * Created by NIK on 09/11/2016.
 */
public class TestDBService
{
    private DBService dbService;
    private String properties="src/main/resources/databaseService.properties";
    private InputManager inputMocked;
    private OutputManager outputMocked;
    private DBManager dbManagerMocked;
    private String existingTableName;
    private String notExistingTableName;

    @Before
    public void setUp() throws IOException, CloneNotSupportedException
    {
        existingTableName ="TELEFONLISTE11";
        //createTable(existingTableName);
        notExistingTableName="";
        inputMocked=mock(InputManager.class);
        outputMocked = mock(OutputManager.class);
        dbManagerMocked = mock(DBManager.class);
        Table table = new Table();
        dbService= new DBService(inputMocked, outputMocked, dbManagerMocked, table);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void getTable_TableExist() throws FileNotFoundException, SQLException, CloneNotSupportedException
    {
        //Arrange
        ResultSet rsMocked = mock(ResultSet.class);

        //Act
        when(dbManagerMocked.isTableExist(existingTableName)).thenReturn(true);
        when(dbManagerMocked.getAllFromTable(existingTableName)).thenReturn(rsMocked);

        dbService.getTable(existingTableName);

        // Assert
        verify(dbManagerMocked, atLeastOnce()).getAllFromTable(existingTableName);
    }

    @Test(expected = NullPointerException.class)
    public void getTable_TableIsNotExist() throws FileNotFoundException, SQLException, CloneNotSupportedException
    {
        //Arrange
        ResultSet rsMocked = mock(ResultSet.class);

        //Act
        when(dbManagerMocked.isTableExist(notExistingTableName)).thenReturn(false);
        when(dbManagerMocked.getAllFromTable(notExistingTableName)).thenReturn(rsMocked);

        dbService.getTable(notExistingTableName);

        // Assert
        verify(dbManagerMocked, atMost(0)).getAllFromTable(notExistingTableName);
    }




    private void createTable(String name) throws FileNotFoundException, CloneNotSupportedException
    {
        DBService dbSer = new DBService(
                new InputManager(),
                new OutputManager(),
                new DBManager(properties),
                new Table()
        );
        dbSer.copyFile("src/main/java/databaseService/inputDataStructure/myExampleFile",
                name);
    }
}
