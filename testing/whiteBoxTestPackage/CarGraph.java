package project.projectfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class CarGraph {
    private Object mapData[][];
    private XMLReader XMLReader;
    public CarGraph()
    {
        this.mapData = null;
        this.XMLReader = new XMLReader();
    }
    public CarGraph(String onOffRamp, String direction, String freeway)
    {
        this.mapData = null;
        
    }
    public List<Coordinate> generateRoute(String fileName)
    {
        this.XMLReader.setFile(fileName);
        Map<Integer, ArrayList<String>> parsedFile = this.XMLReader.parseByTagsIntegral("rtept", "lat", "lon");
        List<Coordinate> mapCoordinates = new ArrayList<Coordinate>();
        
        for(int i = 0; i < parsedFile.size(); i++)
        {
            ArrayList<String> LAT_LONG = parsedFile.get(i);
            try{
            double latitude = Double.parseDouble(LAT_LONG.get(0));
            double longitude = Double.parseDouble(LAT_LONG.get(1));
          
            Coordinate a = new Coordinate(latitude, longitude);
            mapCoordinates.add(a);
            System.out.println("latitude is" + latitude); //test cases
            System.out.println("longitude is" + longitude);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
        return mapCoordinates;
    }

}
