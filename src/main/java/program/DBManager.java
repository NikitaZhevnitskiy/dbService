package program;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.sun.rowset.CachedRowSetImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Class DbMySQLManager
 * Interaction with Database
 * Contains connection-metadata (database, users, etc...)
 *
 * @author Nikita Zhevnitskiya
 * @version 1.0
 */
public class DBManager
{
    /**
     * Connection metadata
     */
    private static String hostName;
    private static String dbName;
    private static String userName;
    private static String password;
    private static int port;
    private Properties props;


    /**
     * Default Constructor
     */
    public DBManager(String properties)
    {

        try
        {
            props = new Properties();
            FileInputStream fileInputStream = new FileInputStream(properties);
            props.load(fileInputStream);
            userName = props.getProperty("userName");
            hostName = props.getProperty("hostName");
            dbName = props.getProperty("dbName");
            password = props.getProperty("password");
            port = Integer.parseInt(props.getProperty("port"));
            fileInputStream.close();

        } catch (IOException e)
        {

        }
    }

    /**
     * Get a connection to the database.
     *
     * @return Connection
     */
    public Connection getConnection()
    {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName(dbName);
        ds.setServerName(hostName);
        ds.setUser(userName);
        ds.setPassword(password);
        ds.setPort(port);
        Connection con = null;
        try
        {
            con = ds.getConnection();
            // System.out.println("### Connection success ###");
        } catch (SQLException e)
        {
            System.err.println("### Connection error ###");
            System.err.println(e.getMessage());
            //System.exit(0);
        }
        return con;

    }


    public void dropTable(String tableName)
    {
        String dropTableQuery = String.format("DROP TABLE IF EXISTS %s", tableName);
        try (Connection connection = getConnection();
             PreparedStatement dropTable = connection.prepareStatement(dropTableQuery)
        )
        {
            dropTable.executeUpdate();

            if (dropTable.getWarnings() == null)
            {
                // (char)27 + "[31m"+### showTable " + table.getName() + (char)27 + "[0m"
                System.out.println((char) 27 + "[34m" + "### TABLE " + tableName + " EXISTS" +
                        "\n### TABLE " + tableName + " DROPPED" + (char) 27 + "[0m");
            } else
            {
                System.out.println((char) 27 + "[34m" + "###  " + tableName + " NOT EXISTS" + (char) 27 + "[0m");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isTableExist(String tableName)
    {
        boolean isExist = false;
        try (Connection connection = getConnection();
             ResultSet rs = connection.getMetaData().getTables(connection.getCatalog(), connection.getSchema(), "%", null))

        {
            while (rs.next())
            {

                //System.out.println(rs.getString(3));
                if (rs.getString(3).equalsIgnoreCase(tableName))
                    isExist = true; //System.out.println("program.Table " + tableName + " exists");
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return isExist;
    }

    /**
     * Create table by using Table object
     * table contains meta and data
     */
    public void createTable(Table table)
    {
        String query = getQueryCreateTable(table);

        try (Connection connection = getConnection();
             PreparedStatement createQ = connection.prepareStatement(query))
        {
            createQ.executeUpdate();
            System.out.println((char) 27 + "[34m" + "### TABLE " + table.getName() + " CREATED" + (char) 27 + "[0m");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Create String query
     * based on table object
     * return filled query
     */
    private String getQueryCreateTable(Table table)
    {
        StringBuilder query = new StringBuilder("CREATE TABLE " + table.getName() + " (\n");
        for (int i = 0; i < table.getColumnsName().length; i++)
        {
            query.append(table.getColumnsName()[i] + " " + table.getColumnsType()[i] + "(" + table.getColumnsCharLimit()[i] + "),\n");
        }
        return query.toString().substring(0, query.length() - 2) + "\n)";
    }

    /**
     * Insert data in DB
     * based on table object
     */
    public void insertData(Table table)
    {
        // prepared stmt contains ?
        String query = getQueryInsertDataPreparedStmt(table);
        //System.out.println(query);
        try (Connection connection = getConnection();
             PreparedStatement insertQ = connection.prepareStatement(query))
        {
            // insert data instead ? symbols in prepared stmt
            int i = 1;
            for (int k = 0; k < table.getOnlyData().size(); k++)
            {
                for (int j = 0; j < table.getOnlyData().get(k).length; )
                    insertQ.setString(i++, table.getOnlyData().get(k)[j++]);
            }
            //System.out.println();
            //System.out.println(insertQ.toString());
            insertQ.executeUpdate();
            System.out.println((char) 27 + "[34m" + "### TABLE " + table.getName() + " ADDED DATA" + (char) 27 + "[0m");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Create String query - prepared stmt (with ?)
     * based on table object
     * return filled query
     */
    private String getQueryInsertDataPreparedStmt(Table table)
    {
        // Add meta data
        StringBuilder rawLine = new StringBuilder("INSERT INTO " + table.getName() + " (");
        for (int i = 0; i < table.getColumnsName().length - 1; i++)
        {
            rawLine.append(table.getColumnsName()[i] + ", ");
        }
        rawLine.append(table.getColumnsName()[table.getColumnsName().length - 1] + ")\nVALUES\n(");

        // Add data
        for (int i = 0; i < table.getOnlyData().size(); i++)
        {
            String[] raw = table.getOnlyData().get(i);
            for (int j = 0; j < raw.length - 1; j++)
            {
                rawLine.append("?" + ", ");
            }
            rawLine.append("?" + "),\n(");
        }

        return rawLine.toString().substring(0, rawLine.length() - 3);
    }

    /**
     * Create String query - to show all info
     * I use (SELECT *) only to get access to metaData of table
     * After create a new query to select each column name
     * return new query
     */
    private String getQueryShowTable(String tableName)
    {
        StringBuilder sb = new StringBuilder("SELECT ");
        // Test query to db to get meta
        try (Connection connection = getConnection();
             PreparedStatement selectAll = connection.prepareStatement(
                     "SELECT * FROM " + tableName, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = selectAll.executeQuery()
        )
        {
            // Build new query based on ResultSet to take all columns name
            ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
            for (int i = 1; i < metaData.getColumnCount(); i++)
            {
                sb.append(metaData.getColumnName(i) + ", ");
            }
            sb.append(metaData.getColumnName(metaData.getColumnCount()) + " FROM " + tableName);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }


    /**
     * Create ResultSet
     * Fill rs with all data from table
     * !!! return CachedRowSetImpl (safety return of rs) only read
     * source: http://stackoverflow.com/questions/14853508/returning-a-resultset
     * javaDocSource: https://docs.oracle.com/javase/7/docs/api/javax/sql/rowset/CachedRowSet.html#populate(java.sql.ResultSet)
     */
    public ResultSet getAllFromTable(String tableName)
    {
        String queryShowTable = getQueryShowTable(tableName);
        //System.out.println(queryShowTable);
        CachedRowSetImpl crs = null;

        try (Connection connection = getConnection();
             PreparedStatement selectAll = connection.prepareStatement(
                     queryShowTable, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = selectAll.executeQuery()
        )
        {
            crs = new CachedRowSetImpl();
            crs.populate(rs);
        } catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return crs;
    }
}