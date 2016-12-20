package program;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class InputManager
 * Provide data from file,
 * Validate data,
 * Fill dataSet with that data
 *
 * @author Nikita Zhevnitskiy
 * @version 1.0
 */
public class InputManager
{
    private Scanner source;

    public InputManager() {}

    public Table fillTable(String fileName, String tableName, Table table) throws FileNotFoundException
    {
        setSource(fileName);
        ArrayList<String> allData = getAllFromFile();
        table.setName(tableName);
        dataValidation(allData);
        table.setColumnsName(allData.get(0).split("/"));
        table.setColumnsType(typeParsing(allData.get(1).split("/")));
        table.setColumnsCharLimit(allData.get(2).split("/"));
        table.setOnlyData(getOnlyData(allData));
        resetSource();
        return table;
    }

    private void setSource(String fileName) throws FileNotFoundException
    {
        //System.out.println(fileName);
        File file = new File(fileName);

        source = new Scanner(file, "utf-8");
    }

    private void resetSource() {this.source = null;}


    // we exactly know structure of data in file. First 3 lines are metaData and rest of lines are data
    public ArrayList<String> getAllFromFile()
    {
        ArrayList<String> allData = new ArrayList<>();

        while (source.hasNext())
        {
            allData.add(source.nextLine());
        }
        return allData;
    }

    private ArrayList<String[]> getOnlyData(ArrayList<String> allData)
    {
        ArrayList<String[]> onlyData = new ArrayList<>();
        for (int i = 3; i < allData.size(); i++)
        {
            String[] raw = allData.get(i).split("/");
            //String[] raw = dataParsing(rawTemp);
            onlyData.add(raw);
            //System.out.println(Arrays.asList(raw).toString());
        }
        return onlyData;
    }

    // STRING type is not exist in SQL syntax
    private String[] typeParsing(String[] columnsType)
    {
        for (int i = 0; i < columnsType.length; i++)
        {
            if (columnsType[i].equalsIgnoreCase("STRING")) columnsType[i] = "VARCHAR";
        }
        return columnsType;
    }

    private void dataValidation(ArrayList<String> list)
    {
        if (!isDataValid(list)) throw new IndexOutOfBoundsException("Data in file is not valid or file is empty");
    }

    private boolean isDataValid(ArrayList<String> list)
    {
        if (list.isEmpty()) return false;
        String[] firstRaw = list.get(0).split("/");
        int rawLength = firstRaw.length;
        for (int i = 0; i < list.size(); i++)
            if (rawLength != list.get(i).split("/").length)
                return false;

        return true;
    }
}