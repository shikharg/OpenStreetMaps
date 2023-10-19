package unitTestPackage;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import project.projectfiles.SQLFluxCapacitor;


public class SQLFluxCapacitor_Test {

    static SQLFluxCapacitor reader;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        reader = new SQLFluxCapacitor();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        try {
            Map<String, Double[]> m = reader.retrieveData();
            
            Iterator<?> it = (Iterator<?>) m.entrySet().iterator();
            
            for(Entry<String, Double[]> entry : m.entrySet()) {
                Double i = entry.getValue()[0];
                
                //Test is meant only to verify integrety of database data via expect data; in this case 10 cars
                //Test result may depend on your database content this far. Adjust expected value accordingly.
                assertEquals("Result","10.0" , i.toString());
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
