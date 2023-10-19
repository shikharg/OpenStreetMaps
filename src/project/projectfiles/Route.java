package project.projectfiles;

import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

public class Route {
    public List<Coordinate> coords;
    public long realTime, perfectTime;
    public MapPolygon route;
    public double distance;
    public Route()
    {
        
    }
    public Route(List<Coordinate> a, double distance, long perfectTime)
    {
        this.coords = a;
        this.perfectTime = perfectTime;
        this.distance = distance;
        if(this.coords.size() < 3)
        {
            this.coords.add(this.coords.get(1));
        }
        this.route = new MapPolyLine(a);
    }
}
