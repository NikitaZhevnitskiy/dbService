import program.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Class to start program
 * <p>
 * ps.
 * root-catalog is nik.pgr200.
 * check examples
 *
 * @author Nikita Zhevnitskiy
 * @version 1.0
 */
public class Application
{

    public static void main(String[] args) throws SQLException, CloneNotSupportedException, FileNotFoundException
    {
        String properties = "src/main/resources/databaseService.properties";

        DBService service = new DBService(
                new InputManager(),
                new OutputManager(),
                new DBManager(properties),
                new Table()
        );


        service.showTable("TELEFONLISTE");
        service.copyFile("src/main/java/inputDataStructure/exampleFile", "mynewtable");

        service.showTable("MYNEWTABLE");
        service.showTable("TELEFONLISTE");
        service.showTable("Persons");
        service.showTable("dasfsdgdfhdghf");

        // expand functionality
        Table table = service.getTable("mynewtable");
        System.out.println(table.toString());

    }
}