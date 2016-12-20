package program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class Table
 * implement Cloneable to make empty as a template
 * Provides DAO mechanism
 * source: https://en.wikipedia.org/wiki/Data_access_object
 * <p>
 * All setters have modifier: default - access only in package
 * <p>
 * toString method based on streaming JAVA8 !!!
 *
 * @author Nikita Zhevnitskiy
 * @version 1.0
 */
public class Table implements Cloneable
{
    public Table() {}

    private String name;
    private String[] columnsName;
    private String[] columnsType;
    private String[] columnsCharLimit;
    private ArrayList<String[]> onlyData;

    public void setName(String name)
    {
        this.name = name;
    }

    void setColumnsName(String[] columnsName)
    {
        this.columnsName = columnsName;
    }

    void setColumnsType(String[] columnsType)
    {
        this.columnsType = columnsType;
    }

    void setColumnsCharLimit(String[] columnsCharLimit)
    {
        this.columnsCharLimit = columnsCharLimit;
    }

    void setOnlyData(ArrayList<String[]> onlyData)
    {
        this.onlyData = onlyData;
    }

    public String toStringDataOnly()
    {
        ArrayList<String[]> data = getOnlyData();
        //System.out.println(Arrays.asList(data.get(0)).toString());
        StringBuilder line = new StringBuilder();

        for (String[] raw : data)
        {
            String temp = Arrays.asList(raw).toString();
            line.append(temp + "\n");
        }
        return line.toString();
    }

    @Override
    protected Table clone() throws CloneNotSupportedException
    {
        return (Table) super.clone();
    }

    public String getName()
    {
        return name;
    }

    public String[] getColumnsName() {return columnsName;}

    public String[] getColumnsType() {return columnsType;}

    public String[] getColumnsCharLimit() {return columnsCharLimit;}

    public ArrayList<String[]> getOnlyData() {return onlyData;}

    /**
     * Java-8 streaming (super cool thing)
     */
    public String toString()
    {
        ArrayList<String[]> data = getOnlyData();
        String line = (char) 27 + "[34m### showTable (toString method) " + this.getName() + (char) 27 + "[0m" + "\n";
        return line + data.stream()
                .map(s -> Arrays.asList(s).toString())
                .collect(Collectors.joining("\n"));
    }

}