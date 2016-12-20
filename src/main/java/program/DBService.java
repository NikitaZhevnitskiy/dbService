package program;

import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Class database service
 * That class provides interactions with application
 *
 * @author Nikita Zhevnitskiy
 * @version 1.0
 */
public class DBService
{
    private DBManager dbManager;
    private InputManager iManager;
    private OutputManager oManager;
    private Table table;

    /**
     * Constructor
     * All dependency injections in constructor
     */
    public DBService(InputManager iManager, OutputManager oManager, DBManager dbManager, Table table)
    {
        this.iManager = iManager;
        this.oManager = oManager;
        this.dbManager = dbManager;
        this.table = table;
    }

    /**
     * Method provides: creation and fill table, uses params
     */
    public void copyFile(String fileName, String tableName) throws CloneNotSupportedException, FileNotFoundException
    {
        if (dbManager.isTableExist(tableName)) dbManager.dropTable(tableName);
        // use clone of table as a emptyTemplate

        Table table = iManager.fillTable(fileName, tableName, this.table.clone());
        dbManager.createTable(table);
        dbManager.insertData(table);
        System.out.println();
    }

    /**
     * Method provides: all data from table in console, from database
     */
    public void showTable(String tableName) throws CloneNotSupportedException, SQLException
    {
        if (dbManager.isTableExist(tableName))
        {
            // use clone of table as a emptyTemplate
            Table table = oManager.fillTable(dbManager.getAllFromTable(tableName), this.table.clone());
            System.out.println((char) 27 + "[34m### showTable " + table.getName() + (char) 27 + "[0m");
            System.out.println(table.toStringDataOnly());
            System.out.println();
        } else
        {
            System.out.println((char) 27 + "[34m" + "### Table with name " + tableName + " NOT EXISTS" + (char) 27 + "[0m");
        }

    }

    /**
     * Expand method
     * Method provides: all data from table in console, from database
     */
    public Table getTable(String tableName) throws CloneNotSupportedException, SQLException
    {
        Table tableTable = null;
        if (dbManager.isTableExist(tableName))
        {
            // use clone of table as a emptyTemplate
            tableTable = oManager.fillTable(dbManager.getAllFromTable(tableName), this.table.clone());
        } else
        {
            throw new NullPointerException("Table with name " + tableName + " is NOT EXISTS");
        }
        return tableTable;
    }

}