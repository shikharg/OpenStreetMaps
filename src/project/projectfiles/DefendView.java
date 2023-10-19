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

public class DefendView extends MapObjectImpl implements MapPolygon, MapMarker {

 Coordinate coord;
 double radius;
 STYLE markerStyle;
 public Polygon defend;
 public int direction;
 private int xMax;
 private int xMin;
 private int yMax;
 private int yMin;
 
 public Polygon hitBox;
 
private int xCenter;
private int yCenter;

public int health;
public boolean isAlive;


 public DefendView(Layer layer, String name,Coordinate coord, double seed) {
     
     
     super(layer);
     
     
     
     int cx[] = {100, 50,   50, 100, 100,  60,  60, 100,  100};
     int cy[] = {100, 100, 170, 170, 160, 160, 110, 110,  100};
     
     for (int i=0;i<cx.length;i++){
         cx[i]= cx[i]+900;
     }
     
     
     int sx[] = {100, 50,   50,  90,   90,  50,   50,  100,  100,  60,  60, 100, 100};
     int sy[] = {100, 100, 135, 135,  160,  160,  170, 170,  125, 125, 110, 110, 100};
     
     for (int i=0;i<sx.length;i++){
         sx[i]=sx[i]+900;
         sy[i]=sy[i]+100;
     }
     
     int twox[] = { 50, 100, 100,  60,  60, 100, 100,   50,  50,  90,  90,  50,  50};
     int twoy[] = {100, 100, 135, 135, 160, 160, 170,  170, 125, 125, 110, 110, 100};
     
     for (int i=0;i<twox.length;i++){
         twox[i]=twox[i]+900;
         twoy[i]=twoy[i]+200;
     }
     
     int ox[] = {100, 50,   50, 100, 100,  60,  60, 100,  100, 90,   90,  90, 100,  100};
     int oy[] = {100, 100, 170, 170, 160, 160, 110, 110,  100, 100, 110, 160, 160,  110};
     
     for (int i=0;i<ox.length;i++){
         ox[i]=ox[i]+900;
         oy[i]=oy[i]+300;
     }
     
    
     
     
     
     
     int onex[] = {30,   50,  50, 60, 60,  30,   30,  40,  40,   30, 30};
     int oney[] = {100, 100, 160, 160, 170, 170, 160, 160, 110,  110,  100};
     
     for (int i=0;i<onex.length;i++){
         onex[i]=onex[i]+940;
         oney[i]=oney[i]+400;
     }
     //the coordinates draw the polygon in a clockwise fashion, 300, 300 is the reference point here
     
     
     int []xPoly=null;
     int[] yPoly=null;
     
     if (seed<.2)
     {
         xPoly=cx;
         yPoly=cy;
         
     }
     else if (seed<.4)
     {
         xPoly=ox;
         yPoly=oy;
         
     }
     else if (seed <.6)
     {
         xPoly=sx;
         yPoly=sy;
         
     }
     else if (seed <.8)
     {
         xPoly=twox;
         yPoly=twoy;
         
     }
     
     else 
     {
         xPoly=onex;
         yPoly=oney;
         
     }
     
     for (int x=0;x<xPoly.length;x++){
         xPoly[x]= (int) Math.ceil(xPoly[x]*(.70));
     }
     for (int y=0;y<yPoly.length;y++)
         yPoly[y]= (int) Math.ceil(yPoly[y]*(.70));
     
     
     
     
     
     xMin=650;
     xMax=750;
     yMin=yPoly[0];
     yMax=yPoly[0]+70;
     
     int[]hitboxX= {xMin, xMax, xMax, xMin};
     int[]hitboxY= {yMax, yMax, yMin, yMin};
     
     hitBox = new Polygon(hitboxX, hitboxY, 4);
     
     xCenter = xMin + (xMax-xMin)/2;
     yCenter= yMin + (yMax-yMin)/2;
     
     
     this.coord = new Coordinate(coord.getLat(),coord.getLon());
     this.direction=0;
     markerStyle= STYLE.VARIABLE;
     defend = new Polygon();
     defend.xpoints=xPoly;
     defend.ypoints=yPoly;
     isAlive=true;
     health=3;
     
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

     if (g instanceof Graphics2D && getBackColor()!=null) {
         Graphics2D g2 = (Graphics2D) g;
         Composite oldComposite = g2.getComposite();
         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
         g2.setPaint(getBackColor());
         g.fillOval(position.x - size_h, position.y - size_h, size, size);
         g2.setComposite(oldComposite);
     }
     ////PLANE PART *********************
     
     
     
     if (health==3)
         g.setColor(new Color(12,99,34));
     else if (health==2)
         g.setColor(new Color(176,162,0));
     else if (health==1)
         g.setColor(new Color(162,61,0));
     else if (health==0){
         isAlive=false;
     }
         
         
     if (isAlive){
         g.fillPolygon(defend.xpoints, defend.ypoints, defend.xpoints.length);
        
     }
     
     
     
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
    
    defend.xpoints=xPoly;
    defend.ypoints=yPoly;
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
    
    defend.xpoints=xPoly;
    defend.ypoints=yPoly;
}


}