package project.projectfiles;

//License: GPL. For details, see Readme.txt file.

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapObjectImpl;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
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

public class SerpentView extends MapObjectImpl implements MapPolygon, MapMarker {

 Coordinate coord;
 double radius;
 STYLE markerStyle;
 public Polygon snake;
 public int direction;
 private int xMax;
 private int xMin;
 private int yMax;
 private int yMin;
 
private int xCenter;
private int yCenter;

private int headX;
private int headY;
public  boolean isAlive;

int yDistToBase;
int xDistToBase;
int yDestination;

 public SerpentView(Layer layer, String name, Coordinate coord, double radius) {
     
     super(layer);
     
     int yPoly[] = {/*top tail*/ 110,  90, 100,  90, 100, 90, /*long neck and head*/  100,  85,  85,   95,  95, 110, /*bottom tail*/ 100, 110, 100, 110, 100, 100, 110};
     int xPoly[] = {            100, 120, 130, 140, 150, 160,                         170, 185, 195,  195, 185, 170,                 160, 150, 140, 130, 120, 110, 100};
     //the coordinates draw the polygon in a clockwise fashion, 300, 300 is the reference point here
     
     
     double getRandy = (Math.random()+.01);
     int yOffSet=(int) ((getRandy*550));
     
     int headX = xPoly[8];
     int headY= yPoly[8];
     
     yDestination = (int)((Math.random()+.01)*550);
     
     
     int yDistToBase=yDestination-headY;
     int xDistToBase=700-headX;
     
     for (int y=0;y<yPoly.length;y++)
         yPoly[y]= yPoly[y]+yOffSet;
     
     
     
     for (int x=0;x<xPoly.length;x++){
         xPoly[x]= (int) Math.ceil(xPoly[x]*(.70));
     }
     for (int y=0;y<yPoly.length;y++)
         yPoly[y]= (int) Math.ceil(yPoly[y]*(.70));
     
     for (int x=0;x<xPoly.length;x++){
         xPoly[x]= xPoly[x]+20;
     }
     
     
     
     
     
     
     xCenter = xMin + (xMax-xMin)/2;
     yCenter= yMin + (yMax-yMin)/2;
     
     isAlive=true;
     
     this.coord = new Coordinate(coord.getLat(),coord.getLon());
     this.direction=0;
     markerStyle= STYLE.VARIABLE;
     snake = new Polygon();
     snake.xpoints=xPoly;
     snake.ypoints=yPoly;
     
     setVisible(false);
     
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
 

 
 public void faceUp(int[] xPoly, int[] yPoly){
     
     if (direction==0){
         transpose(xPoly, yPoly);
     }
     if (direction==3){
         reflect(xPoly, yPoly);
     }
     if (direction==2){
         transpose(xPoly, yPoly);
         reflect(xPoly, yPoly);
     }
     
     
     
     this.direction=1;
    
     
 }
 
 public void faceDown(int[] xPoly, int[] yPoly){
     
    
     if (this.direction==2){
         transpose(xPoly, yPoly);
     }
     else if (this.direction==1){
         reflect(xPoly, yPoly);
     }
     else if (this.direction==0){
         transpose(xPoly, yPoly);
         
         reflect(xPoly, yPoly);
     }
     
   
     this.direction=3;
    
     
 }
 
 public void faceLeft(int[] xPoly, int[] yPoly){
     
     if (direction==1){
         transpose(xPoly, yPoly);
     }
     if (direction==2){
         reflect(xPoly, yPoly);
     }
     if (direction==3){
         transpose(xPoly, yPoly);
         reflect(xPoly, yPoly);
     }
     
     this.direction=0;
    
     
 }
 
public void faceRight(int[] xPoly, int[] yPoly){
     
    if (direction==3){
        transpose(xPoly, yPoly);
    }
    if (direction==0){
        reflect(xPoly, yPoly);
    }
    if (direction==1){
        transpose(xPoly, yPoly);
        reflect(xPoly, yPoly);
    }
    
    
     
     
     
     this.direction=2;
    
     
 }

 public void paint(Graphics g, Point position, int radio) {
     int size_h = radio;
     int size = size_h * 2;

     
     ////PLANE PART *********************
     
     
     
     
     g.setColor(new Color(53,78,64));
     
     headX = snake.xpoints[8];
     headY= snake.ypoints[8];
     
     
     yDistToBase=yDestination-headY;
     xDistToBase=700-headX;
     
     int yDistToTravel=0;
     int xDistToTravel=0;
     
     xDistToTravel=(int) Math.ceil((.01)*xDistToBase);
     
     if (Math.abs(yDistToBase)>2){
             
         
         
     if (headY<yDestination)
             yDistToTravel=1;
     else if (headY>yDestination)
             yDistToTravel=-1;
     }
     else{
         if (xDistToBase==0)
        isAlive=false;
     }
     
     
     for (int y=0;y<snake.xpoints.length;y++){
         snake.xpoints[y]=snake.xpoints[y]+xDistToTravel;
         
     }
     
     for (int y=0;y<snake.xpoints.length;y++){
         snake.ypoints[y]=snake.ypoints[y]+yDistToTravel;
         
     }
     if (isAlive)
     g.fillPolygon(snake.xpoints, snake.ypoints, snake.xpoints.length);
     
     
     
     if(getLayer()==null||getLayer().isVisibleTexts()) 
         paintText(g, position);
 }


 
 public void setLat(double lat) {
     if(coord==null) coord = new Coordinate(lat,0);
     else coord.setLat(lat);
 }
 
 public void setLon(double lon) {
     if(coord==null) coord = new Coordinate(0,lon);
     else coord.setLon(lon);
 }
 public boolean contains(Coordinate a)
 {
     return (a.getLat() <= this.getLat()+this.radius && a.getLat() >= this.getLat() - this.radius  && a.getLon() <= this.getLon() + this.radius && a.getLon() >= this.getLon() - this.radius);
 }




@Override
public List<? extends ICoordinate> getPoints() {
    // TODO Auto-generated method stub
    return null;
}




@Override
public void paint(Graphics g, List<Point> points) {
    // TODO Auto-generated method stub
    
}




@Override
public void paint(Graphics g, Polygon polygon) {
    // TODO Auto-generated method stub
    
}

public void transpose(int[] xPoly, int[]yPoly){
    
    xMin=xPoly[5];
    xMax=xPoly[22];
    yMin=yPoly[0];
    yMax=yPoly[14];
    
    xCenter = xMin + (xMax-xMin)/2;
    yCenter= yMin + (yMax-yMin)/2;
    
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= xPoly[i]-xCenter;
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= yPoly[i]-yCenter;
        
    }
    
    int[] dummy;
    dummy=xPoly;
    xPoly=yPoly;
    yPoly=dummy;
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= xPoly[i]+xCenter;
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= yPoly[i]+yCenter;
        
    }
    
    snake.xpoints=xPoly;
    snake.ypoints=yPoly;
}

public void reflect(int[] xPoly, int[]yPoly){
    
    xMin=xPoly[5];
    xMax=xPoly[22];
    yMin=yPoly[0];
    yMax=yPoly[14];
    
    xCenter = xMin + (xMax-xMin)/2;
    yCenter= yMin + (yMax-yMin)/2;
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= xPoly[i]-xCenter;
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= yPoly[i]-yCenter;
        
    }
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= -xPoly[i];
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= -yPoly[i];
        
    }
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= xPoly[i]+xCenter;
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= yPoly[i]+yCenter;
        
    }
    
    snake.xpoints=xPoly;
    snake.ypoints=yPoly;
}


}