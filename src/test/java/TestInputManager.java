import org.junit.*;
import program.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;


import org.junit.Test;


public class TestInputManager
{

    private InputManager iManager;
    private String fileNameExist= "src/main/java/inputDataStructure/exampleFile";
    private String fileNameNotExist= "notExistFile";
    private String fileNameEmpty="src/main/java/inputDataStructure/__testFileEmpty";
    private String fileNameErrorStructure="src/main/java/inputDataStructure/__testFileErrorStructure";


    @Before
    public void setUp() throws IOException
    {

        iManager=new InputManager();
        File file = new File(fileNameEmpty);
        file.createNewFile();
        File file1 = new File(fileNameErrorStructure);
        file1.createNewFile();
        FileWriter fileWriter = new FileWriter(file1);
        fileWriter.write("col1/col2\ncol1");
        fileWriter.close();
    }

    @After
    public void tearDown()
    {
        iManager = null;
        File file = new File(fileNameEmpty);
        file.delete();
        File file1 = new File(fileNameErrorStructure);
        file1.delete();
    }

    @Test
    public void fillTableFileExist()throws FileNotFoundException
    {
        Table table = iManager.fillTable(fileNameExist, anyString(), new Table());
        //Table table = new Table();
        assertEquals(table.getName(), anyString());
    }

    @Test(expected = FileNotFoundException.class)
    public void fillTableFileNotExist() throws FileNotFoundException
    {
        iManager.fillTable(fileNameNotExist, anyString(), new Table());
        //iManager.fillTable(fileNameExist, anyString(), new Table());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void fillTableFileExistButEmty() throws FileNotFoundException
    {
        iManager.fillTable(fileNameEmpty, anyString(), new Table());
        //iManager.fillTable(fileNameExist, anyString(), new Table());

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void fillTableFileExistButErrorStructure() throws FileNotFoundException
    {
        iManager.fillTable(fileNameErrorStructure, anyString(), new Table());
        //iManager.fillTable(fileNameExist, anyString(), new Table());
    }
}
