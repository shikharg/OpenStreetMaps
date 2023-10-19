package project.projectfiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

public class RouteGenerator {
    private JMapViewer map;
    private JSONObject jsonObject;
    public RouteGenerator(JMapViewer map)
    {
        this.map = map;
    }
    public void findRoute(Coordinate a, Coordinate b)
    {
        String aLat = Double.toString(a.getLat());
        String aLon = Double.toString(a.getLon());
        String bLat = Double.toString(b.getLat());
        String bLon = Double.toString(b.getLon());
        String fromString = aLat + "," + aLon;
        String toString = bLat + "," + bLon;
        String urlstring="http://www.mapquestapi.com/directions/v2/route?key=Fmjtd%7Cluur2qu7nq%2Cag%3Do5-9a2wg6&from="+ fromString +
        "&to=" + toString + "&highwayEfficiency=235&doReverseGeocode=false&fullShape=true";
        JSONReader jsonReader = new JSONReader(urlstring, null);
        try {
           jsonReader.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.jsonObject = jsonReader.getJSONObject();
    }
    public Route generateRoute(Coordinate a, Coordinate b)
    {
        findRoute(a,b);
        List<Coordinate> reiseRoute = new ArrayList<Coordinate>();
        double distance = 0.0;
        long perfectTime = 0;
        if(this.jsonObject!= null)
        {
            JSONObject shapeData = (JSONObject)((JSONObject)this.jsonObject.get("route")).get("shape");
            Object dist = ((JSONObject)this.jsonObject.get("route")).get("distance");
            Object ti = ((JSONObject)this.jsonObject.get("route")).get("time");
            if(dist instanceof Number)
            distance = (Double)dist;
            if(ti instanceof Number)
            perfectTime = (Long)ti;  
           
            JSONArray points = (JSONArray) shapeData.get("shapePoints");
            for(int i = 0; i < points.size()/2; i++)
            {
              if(points.get(2*i) instanceof Double && points.get(2*i+1) instanceof Double)
              {
                  double coordLat = (Double)(points.get(2*i));
              double coordLon = (Double)(points.get(2*i+1));
              System.out.println(coordLat);
              System.out.println(coordLon);
              Coordinate coord = new Coordinate(coordLat, coordLon);
              reiseRoute.add(coord);
              }
            }
        }
        return new Route(reiseRoute, distance, perfectTime);
    }
}
