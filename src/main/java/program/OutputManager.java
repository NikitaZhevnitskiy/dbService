package program;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class OutputManager
 * Provide data from ResultSet,
 * Fill dataSet with that data
 *
 * @author Nikita Zhevnitskiy
 * @version 1.0
 */

public class OutputManager
{
    private ResultSet rs;

    public OutputManager() {}

    public Table fillTable(ResultSet rs, Table table) throws SQLException
    {
        //setSource(rs);
        ResultSetMetaData metaRS = rs.getMetaData();
        table.setName(metaRS.getTableName(1));

        //System.out.println(Arrays.asList(getColumnsName(metaRS)));
        table.setColumnsName(getColumnsName(metaRS));

        //System.out.println(Arrays.asList(getColumnsType(metaRS)));
        table.setColumnsType(getColumnsType(metaRS));

        //System.out.println(Arrays.asList(getColumnsCharLimit(metaRS)));
        table.setColumnsCharLimit(getColumnsCharLimit(metaRS));

        table.setOnlyData(getOnlyData(rs));

        resetSource();
        rs.close();
        return table;
    }

    private void setSource(ResultSet rs)
    {
        this.rs = rs;
    }

    public void resetSource()
    {
        this.rs = null;
    }

    public ResultSet getSource()
    {
        return rs;
    }

    public String[] getColumnsName(ResultSetMetaData meta) throws SQLException
    {
        String[] columnsName = new String[meta.getColumnCount()];
        for (int i = 1; i <= meta.getColumnCount(); i++)
        {
            String line = meta.getColumnName(i);
            columnsName[i - 1] = line;
        }
        return columnsName;
    }

    public String[] getColumnsType(ResultSetMetaData meta) throws SQLException
    {
        String[] columnsType = new String[meta.getColumnCount()];
        for (int i = 1; i <= meta.getColumnCount(); i++)
        {
            String line = meta.getColumnTypeName(i);
            columnsType[i - 1] = line;
        }
        return columnsType;
    }

    public String[] getColumnsCharLimit(ResultSetMetaData meta) throws SQLException
    {
        String[] columnsType = new String[meta.getColumnCount()];
        for (int i = 1; i <= meta.getColumnCount(); i++)
        {
            String line = meta.getColumnDisplaySize(i) + "";
            columnsType[i - 1] = line;
        }
        return columnsType;
    }

    public ArrayList<String[]> getOnlyData(ResultSet rs) throws SQLException
    {
        ArrayList<String[]> data = new ArrayList<>();
        while (rs.next())
        {
            String[] raw = new String[rs.getMetaData().getColumnCount()];
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
            {
                raw[i - 1] = rs.getString(i);
            }
            data.add(raw);
        }
        return data;
    }
}