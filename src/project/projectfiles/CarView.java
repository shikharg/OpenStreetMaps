// License: GPL. For details, see Readme.txt file.
package project.projectfiles;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapObjectImpl;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import java.awt.event.MouseListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import org.openstreetmap.gui.jmapviewer.interfaces.MapRectangle;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker.STYLE;

public class CarView extends MapObjectImpl implements MapMarker {

    Coordinate coord;
    double radius;
    STYLE markerStyle;
    CarUnit car;

    public CarView(CarUnit c, Coordinate coord, double radius) {
        this(c, null, null, coord, radius);
    }
    public CarView(CarUnit c, String name, Coordinate coord, double radius) {
        this(c,null, name, coord, radius);
    }
    public CarView(CarUnit c, Layer layer, Coordinate coord, double radius) {
        this(c,layer, null, coord, radius);
    }
    public CarView(CarUnit c, double lat, double lon, double radius) {
        this(c,null, null, new Coordinate(lat,lon), radius);
    }
    public CarView(CarUnit c, Layer layer, double lat, double lon, double radius) {
        this(c,layer, null, new Coordinate(lat,lon), radius);
    }
    public CarView(CarUnit c, Layer layer, String name, Coordinate coord, double radius) {
        this(c,layer, name, coord, radius, STYLE.VARIABLE, getDefaultStyle());
    }
    public CarView(CarUnit c, Layer layer, String name, Coordinate coord, double radius, STYLE markerStyle, Style style) {
        super(layer, name, style);
        this.markerStyle = markerStyle;
        this.coord = coord;
        this.radius = radius;
        this.car = c;
    }
    public CarUnit getCar()
    {
        return this.car;
    }
    public Coordinate getCoordinate(){
        return coord;
    }
    public double getLat() {
        return coord.getLat();
    }

    public double getLon() {
        return coord.getLon();
    }

    public double getRadius() {
        return radius;
    }

    public STYLE getMarkerStyle() {
        return markerStyle;
    }

    public void paint(Graphics g, Point position, int radio) {
        int size_h = radio;
        int size = size_h * 2;

        if (g instanceof Graphics2D && getBackColor()!=null) {
            Graphics2D g2 = (Graphics2D) g;
            Composite oldComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2.setPaint(getBackColor());
            g.fillOval(position.x - size_h, position.y - size_h, size, size);
            g2.setComposite(oldComposite);
        }
        g.setColor(getColor());
        g.drawOval(position.x - size_h, position.y - size_h, size, size);

        if(getLayer()==null||getLayer().isVisibleTexts()) paintText(g, position);
    }

    public static Style getDefaultStyle(){
        return new Style(Color.ORANGE, new Color(200,200,200,200), null, getDefaultFont());
    }
    @Override
    public String toString() {
        return "Car at " + getLat() + " " + getLon();
    }
    @Override
    public void setLat(double lat) {
        if(coord==null) coord = new Coordinate(lat,0);
        else coord.setLat(lat);
    }
    @Override
    public void setLon(double lon) {
        if(coord==null) coord = new Coordinate(0,lon);
        else coord.setLon(lon);
    }
    public boolean contains(Coordinate a)
    {
        return (a.getLat() <= this.getLat()+this.radius && a.getLat() >= this.getLat() - this.radius  && a.getLon() <= this.getLon() + this.radius && a.getLon() >= this.getLon() - this.radius);
    }
    public void render()
    {
        this.coord = this.car.model.getPosition();
    }
}